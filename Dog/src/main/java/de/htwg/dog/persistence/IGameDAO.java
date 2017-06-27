package de.htwg.dog.persistence;

import de.htwg.dog.model.IModel;

import java.util.List;

public interface IGameDAO {

    void saveGame(IModel id);

    IModel getGame(String id);

    void deleteGame(String id);

    List<IModel> getAllGames();

    boolean containsGame(String id);

    void closeDb();
}
