package de.htwg.dog.persistence;

import de.htwg.dog.model.IModel;

import java.util.List;

public interface IGameDAO {

    public void saveGame(IModel id);

    public IModel getGame(String id);

    public void deleteGame(String id);

    public List<IModel> getAllGames();

    public boolean containsGame(String id);
}
