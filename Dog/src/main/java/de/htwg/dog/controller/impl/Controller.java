/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htwg.dog.controller.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.htwg.dog.controller.IController;
import de.htwg.dog.model.ICard;
import de.htwg.dog.model.IModel;
import de.htwg.dog.model.IModelFactory;
import de.htwg.dog.model.ISquare;
import de.htwg.dog.persistence.IGameDAO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kev
 */
@Singleton
public class Controller implements IController {

    private IModel model;
    List<ActionListener> updateListener;
    private IGameDAO gameDAO;

    @Inject
    public Controller(IModelFactory modelFactory, IGameDAO gameDAO) {

        this.gameDAO = gameDAO;
        this.model = modelFactory.create();
        updateListener = new ArrayList<>();
    }

    @Override
    public void loadFromDB(String id){
        this.model = gameDAO.getGame(id);
        update();
    }

    @Override
    public void saveToDB(){
        gameDAO.saveGame(model);
    }

    @Override
    public String[][] getAllNames() {
        List<IModel> games = gameDAO.getAllGames();
        String[][] data = new String[games.size()][2];

        for(int i = 0; i < games.size(); i++) {
            IModel g = games.get(i);
            data[i][0] = g.getName();
            data[i][1] = g.getId();
        }
        return data;
    }

    @Override
    public void addUpdateListener(ActionListener actionListener) {
        updateListener.add(actionListener);
    }

    private void update() {
        for (ActionListener l : this.updateListener) {
            l.actionPerformed(new ActionEvent(this, 0, "update"));
        }
    }

    @Override
    public void discardCard() {
        model.discardCards();
        model.resetSelectedSquares();
        update();
    }

    @Override
    public boolean doTurn() {
        boolean b = model.doTurn();
        if(b) resetSelectedSquares();
        update();
        return b;
    }

    @Override
    public List<String> getCurrentCards() {
        List<String> cards = new ArrayList();
        for (ICard c : model.getCurrentPlayer().getCards()) {
            cards.add(c.getName());
        }

        return cards;
    }

    @Override
    public int getCurrentPlayerNo() {
        return model.getCurrentPlayer().getPlayerNumber();
    }

    @Override
    public int getWinnerNo() {
        if (model.getWinner() == null)
            return -1;
        else
            return model.getWinner().getPlayerNumber();
    }

    @Override
    public String getInfo() {
        return model.getInfo();
    }

    @Override
    public void startGame() {
        model.startGame();
        update();
    }

    @Override
    public String gameToJson(){
        return model.toJson();
    }

    @Override
    public void configureGame(int playerCnt, int squareCnt, int tokenCnt) {
        model.configureGame(playerCnt, squareCnt, tokenCnt);
        update();
    }

    @Override
    public List<String> getOccupiedSquares(int playerNo) {
        List<String> squares = new ArrayList();
        for (ISquare s : model.getSquares()) {
            if(s.getToken() == playerNo)
                squares.add(s.getName());
        }

        return squares;
    }

    public String getGameName() {
        return model.getName();
    }

    public void setGameName(String name) {
        model.setName(name);
    }

    @Override
    public int getPlayerCnt() {
        return model.getPlayerCnt();
    }

    @Override
    public int getSquareCnt() {
        return model.getSquareCnt();
    }

    @Override
    public int getTokenCnt() {
        return model.getTokenCnt();
    }

    @Override
    public void resetSelectedSquares() {
        model.resetSelectedSquares();
        update();
    }

    @Override
    public void setSelectedCard(String card) {
        model.setSelectedCard(card);
        update();
    }

    @Override
    public String getSelectedCard() {
        if(model.getSelectedCard() == null)
            return "";

        return model.getSelectedCard().getName();
    }

    @Override
    public void setSelectedSquare(String selectedSquare) {
        model.setSelectedSquare(selectedSquare);
        update();
    }

    @Override
    public String getSelectedSquare(int i) {

        if(model.getSelectedSquare(i) == null)
            return "";

        return model.getSelectedSquare(i).getName();
    }

    @Override
    public void makeAutoDraw() {
        try {
            model.makeAutoDraw();
        }
        catch(Exception e){ System.out.println(e.getMessage());};
        update();
    }
}
