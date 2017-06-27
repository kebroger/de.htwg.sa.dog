package de.htwg.dog.persistence.db4o;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;
import de.htwg.dog.model.IModel;
import de.htwg.dog.persistence.IGameDAO;

import java.util.List;

public class GameDb4oDAO implements IGameDAO {

    private ObjectContainer db;

    public GameDb4oDAO() {
        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),"game.data");
    }

    @Override
    public void saveGame(final IModel game) {
        db.store(game);
    }

    @Override
    public void deleteGame(String id) {
        db.delete(getGame(id));
    }

    @Override
    public List<IModel> getAllGames() {
        return db.query(IModel.class);
    }

    public void closeDb() {
        db.close();
    }

    @Override
    public boolean containsGame(final String id) {
        List<IModel> games = db.query(new Predicate<IModel>() {

            private static final long serialVersionUID = 1L;

            public boolean match(IModel game) {
                return (game.getId().equals(id));
            }
        });

        if (games.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public IModel getGame(final String id) {
        List<IModel> games = db.query(new Predicate<IModel>() {

            public boolean match(IModel game) {
                return (game.getId().equals(id));
            }
        });

        if (games.size() > 0) {
            return games.get(0);
        }

        return null;
    }
}
