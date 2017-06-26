/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htwg.dog.model;

import de.htwg.dog.model.impl.Deck;

import java.util.List;

/**
 *
 * @author kev
 */
public interface IModel {

    List<ISquare> getSquares();
    IPlayer getCurrentPlayer();
    void setCurrentPlayer(int playerNo);

    public ICard getCard(String name);
    public ISquare getSquare(String name);

    public String getName();
    public void setName(String name);


    public void discardCards();
    public boolean doTurn();

    public void configureGame(int playerCnt, int squareCnt, int tokenCnt);
    public void startGame();

    public String getInfo();
    public void setInfo(String info);

    public int getCardsPerHand();
    public void setCardsPerHand(int cardsPerHand);

    public IPlayer getWinner();

    public int getPlayerCnt();
    public int getSquareCnt();
    public int getTokenCnt();

    public List<IPlayer> getPlayers();

    public String toJson();

    public void setSelectedCard(String card);
    public ICard getSelectedCard();

    public Deck getDeck();
    public void setDeck(Deck deck);

    public String getId();
    public void setId(String id);

    public void setSelectedSquare(String clickedSquare);
    public ISquare getSelectedSquare(int i);
    public void resetSelectedSquares();

    public void makeAutoDraw() throws Exception;
}
