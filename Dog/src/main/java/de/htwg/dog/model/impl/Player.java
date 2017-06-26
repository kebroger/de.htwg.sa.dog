/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htwg.dog.model.impl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.htwg.dog.model.ICard;
import de.htwg.dog.model.IPlayer;
import de.htwg.dog.model.ISquare;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kev
 */
@JsonDeserialize(as=Player.class)
public class Player implements IPlayer {

    private List<ICard> cards = new ArrayList<>();
    private int playerNumber = 0;
    private List<ISquare> homeSquares = new ArrayList<>();
    private List<ISquare> finishSquares = new ArrayList<>();
    private ISquare startSquare = null;

    public Player(){}

    public Player(ISquare startSquare, int number, int tokenCnt) {

        playerNumber = number;
        homeSquares = new ArrayList<>();
        finishSquares = new ArrayList<>();
        //occupiedSquares = new ArrayList<>();

        this.startSquare = startSquare;

        for (int i = 0; i < tokenCnt; i++) {
            homeSquares.add(new Square("H" + i + "P" + playerNumber));
            //occupiedSquares.add(homeSquares.get(i));
            finishSquares.add(new Square("F" + i + "P" + playerNumber));
        }

        homeSquares.forEach(s -> 
            s.setOccupation(playerNumber)
        );
    }
    
    @Override
    public List<ICard> getCards() {
        return cards;
    }

    @Override
    public List<ISquare> getFinishSquares() {
        return finishSquares;
    }

    @Override
    public List<ISquare> getHomeSquares() {
        return homeSquares;
    }

    @Override
    public int getPlayerNumber() {
        return playerNumber;
    }

    @Override
    public ISquare getStartSquare() {
        return startSquare;
    }

    @Override
    public ICard getCardByName(String name) {
        for (ICard card : cards) {
            if (card.getName().equals(name)) {
                return card;
            }
        }

        return null;
    }
}
