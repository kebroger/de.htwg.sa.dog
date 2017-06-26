/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htwg.dog.controller;

import java.awt.event.ActionListener;
import java.util.List;

/**
 *
 * @author kev
 */
public interface IController {
    
    public void addUpdateListener(ActionListener actionListener);     
    public void discardCard();
    public boolean doTurn();
    public List<String> getCurrentCards();
    public List<String> getOccupiedSquares(int playerNo);
    public int getCurrentPlayerNo();
    public int getWinnerNo();
    public String getInfo();

    public void configureGame(int playerCnt, int squareCnt, int tokenCnt);
    public void startGame();

    public int getPlayerCnt();
    public int getSquareCnt();
    public int getTokenCnt();

    public void setSelectedCard(String card);
    public String getSelectedCard();

    public void setSelectedSquare(String clickedSquare);
    public String getSelectedSquare(int i);
    public void resetSelectedSquares();

    public String gameToJson();

    public void loadFromDB(String name);
    public void saveToDB();
    public String[][] getAllNames();

    public String getGameName();
    public void setGameName(String name);

    public void makeAutoDraw();
}
