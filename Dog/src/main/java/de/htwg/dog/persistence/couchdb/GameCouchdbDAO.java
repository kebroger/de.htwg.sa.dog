package de.htwg.dog.persistence.couchdb;

import de.htwg.dog.model.ICard;
import de.htwg.dog.model.IModel;
import de.htwg.dog.model.IPlayer;
import de.htwg.dog.model.ISquare;
import de.htwg.dog.model.impl.Deck;
import de.htwg.dog.model.impl.Game;
import de.htwg.dog.persistence.IGameDAO;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.ViewResult.Row;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class GameCouchdbDAO implements IGameDAO {

	private CouchDbConnector db = null;

	public GameCouchdbDAO() {
		HttpClient client = null;
		try {
			client = new StdHttpClient.Builder().url(
					"http://lenny2.in.htwg-konstanz.de:5984").build();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		CouchDbInstance dbInstance = new StdCouchDbInstance(client);
		db = dbInstance.createConnector("dog_db", true);
	}

	public void closeDb(){}

	private IModel copyGame(PersistentGame pgame) {
		if(pgame == null) {
			return null;
		}
		IModel game = new Game();
		game.setId(pgame.id);
		game.setName(pgame.name);
		game.configureGame(pgame.playerCnt, pgame.squareCnt, pgame.tokenCnt);
		game.startGame();

		game.setCardsPerHand(pgame.cardsPerHand);
		game.setInfo(pgame.info);
		game.setCurrentPlayer(pgame.currentPlayerNo);

		Deck deck = new Deck();

		for (PersistentPlayer pplayer : pgame.players) {
			IPlayer player = game.getPlayers().get(pplayer.playerNumber);
			player.getCards().clear();

            for (String squareName : pplayer.occupiedSquares) {
                game.getSquare(squareName).setOccupation(pplayer.playerNumber);
            }

			for (String cardName : pplayer.cards) {
				ICard card = deck.undealedCards.stream().filter(c -> c.getName().equals(cardName)).findFirst().get();
				player.getCards().add(card);

				deck.undealedCards.remove(card);
			}
		}

		return game;
	}

	private PersistentGame copyGame(IModel game) {
		if (game == null) {
			return null;
		}

		PersistentGame pgame;
		if (containsGame(game.getId())) {
			// The Object already exists within the session
            pgame = db.find(PersistentGame.class, game.getId());

            // Synchronize values
            for(PersistentPlayer p : pgame.players) {
                p.occupiedSquares.clear();
                p.cards.clear();
                for (ICard card : game.getPlayers().get(p.playerNumber).getCards()) {
                    p.cards.add(card.getName());
                }
            }

            for (ISquare s: game.getSquares()) {
                if(s.getToken() > -1)
                {
                    pgame.players.get(s.getToken()).occupiedSquares.add(s.getName());
                }
            }

            pgame.cardsPerHand = game.getCardsPerHand();
            pgame.currentPlayerNo = game.getCurrentPlayer().getPlayerNumber();
            pgame.info = game.getInfo();

        } else {
            // A new database entry
            pgame = new PersistentGame();
            pgame.tokenCnt = game.getTokenCnt();
            pgame.squareCnt = game.getSquareCnt();
            pgame.playerCnt = game.getPlayerCnt();

            List<PersistentPlayer> players = new ArrayList<>();

            for (IPlayer p : game.getPlayers()) {
                PersistentPlayer pp = new PersistentPlayer();
                pp.playerNumber = p.getPlayerNumber();

                for (ICard card : game.getPlayers().get(p.getPlayerNumber()).getCards()) {
                    pp.cards.add(card.getName());
                }

                players.add(pp);
            }

            for (ISquare s: game.getSquares()) {
                if(s.getToken() > -1)
                {
                    players.get(s.getToken()).occupiedSquares.add(s.getName());
                }
            }

            pgame.id = game.getId();
            pgame.name = game.getName();
            pgame.cardsPerHand = game.getCardsPerHand();
            pgame.currentPlayerNo = game.getCurrentPlayer().getPlayerNumber();
            pgame.info = game.getInfo();
            pgame.players.addAll(players);
        }

        return pgame;
    }

	@Override
	public void saveGame(IModel game) {
		if (containsGame(game.getId())) {
			db.update(copyGame(game));
		} else {
			db.create(game.getId(), copyGame(game));
		}
	}

	@Override
	public boolean containsGame(String id) {
		if (getGame(id) == null) {
			return false;
		}
		return true;
	}

	@Override
	public IModel getGame(String id) {
		PersistentGame g = db.find(PersistentGame.class, id);
		if (g == null) {
			return null;
		}
		g.id = id;
		return copyGame(g);
	}

	@Override
	public void deleteGame(String id) {
		db.delete(copyGame(getGame(id)));
	}

	@Override
	public List<IModel> getAllGames() {
		List<IModel> lst = new ArrayList<>();
		ViewQuery query = new ViewQuery().allDocs();
		ViewResult vr = db.queryView(query);

		for (Row r : vr.getRows()) {
			lst.add(getGame(r.getId()));
		}
		return lst;
	}
}
