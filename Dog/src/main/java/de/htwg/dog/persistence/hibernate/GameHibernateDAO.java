package de.htwg.dog.persistence.hibernate;

import de.htwg.dog.model.ICard;
import de.htwg.dog.model.IModel;
import de.htwg.dog.model.IPlayer;
import de.htwg.dog.model.ISquare;
import de.htwg.dog.model.impl.Deck;
import de.htwg.dog.model.impl.Game;
import de.htwg.dog.persistence.IGameDAO;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class GameHibernateDAO implements IGameDAO {

	private IModel copyGame(PersistentGame pgame) {
        if(pgame == null) {
            return null;
        }
        IModel game = new Game();

        game.setName(pgame.name);
        game.configureGame(pgame.playerCnt, pgame.squareCnt, pgame.tokenCnt);
        game.startGame();
        game.setId(pgame.id);

        game.setCardsPerHand(pgame.cardsPerHand);
        game.setInfo(pgame.info);
        game.setCurrentPlayer(pgame.currentPlayerNo);

        Deck deck = new Deck();

        for (PersistentPlayer pplayer : pgame.players) {
            IPlayer player = game.getPlayers().get(pplayer.playerNumber);
            player.getCards().clear();


            for (String cardName : pplayer.cards) {
                ICard card = deck.undealedCards.stream().filter(c -> c.getName().equals(cardName)).findFirst().get();
                player.getCards().add(card);

                deck.undealedCards.remove(card);
            }

            for (String squareName : pplayer.occupiedSquares) {
                game.getSquare(squareName).setOccupation(pplayer.playerNumber);
            }
        }

        return game;
	}

    public void closeDb(){}

	private PersistentGame copyGame(IModel game) {
		if(game == null) {
			return null;
		}

		String id = game.getId();
		PersistentGame pgame;
		if(containsGame(id)) {
			// The Object already exists within the session
			Session session = HibernateUtil.getInstance().getCurrentSession();
			pgame = session.get(PersistentGame.class, id);

			// Synchronize values
			for(PersistentPlayer p : pgame.players) {
				p.cards.clear();
				p.occupiedSquares.clear();
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
                pp.game = pgame;

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
		Transaction tx = null;
		Session session;

		try {
			session = HibernateUtil.getInstance().getCurrentSession();
            tx = session.getTransaction();
            if(!tx.isActive())
                tx = session.beginTransaction();

			PersistentGame pgame = copyGame(game);

			session.saveOrUpdate(pgame);
			for (PersistentPlayer pplayer : pgame.players) {
				session.saveOrUpdate(pplayer);
			}

			tx.commit();
		} catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			throw new RuntimeException(ex.getMessage());

		}

	}

	@Override
	public boolean containsGame(String id) {
		if(getGame(id) != null) {
			return true;
		}
		return false;
	}


	@Override
	public IModel getGame(String id) {
		Session session = HibernateUtil.getInstance().getCurrentSession();
		if(!session.getTransaction().isActive())
		    session.beginTransaction();

		return copyGame(session.get(PersistentGame.class, id));
	}

    @Override
    public List<IModel> getAllGames() {
        Session session = HibernateUtil.getInstance().getCurrentSession();
        if(!session.getTransaction().isActive())
            session.beginTransaction();
        Criteria criteria = session.createCriteria(PersistentGame.class);

        @SuppressWarnings("unchecked")
        List<PersistentGame> results = criteria.list();

        List<IModel> games = new ArrayList<>();
        for (PersistentGame pgrid : results) {
            IModel grid = copyGame(pgrid);
            games.add(grid);
        }
        return games;
    }

    @Override
    public void deleteGame(String id) {
        Transaction tx = null;
        Session session;

        try {
            session = HibernateUtil.getInstance().getCurrentSession();
            tx = session.getTransaction();
            if(!tx.isActive())
                tx = session.beginTransaction();

            PersistentGame pgame = session.get(PersistentGame.class, id);

            for(PersistentPlayer p : pgame.players) {
                session.delete(p);
            }
            session.delete(pgame);

            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null)
                tx.rollback();
            throw new RuntimeException(ex.getMessage());

        }

    }
}
