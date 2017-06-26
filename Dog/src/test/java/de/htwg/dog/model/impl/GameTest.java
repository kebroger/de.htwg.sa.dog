/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htwg.dog.model.impl;

import de.htwg.dog.model.ICard;
import de.htwg.dog.model.IPlayer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author kev
 */
public class GameTest {
    
    Game game = new Game();
    
    public GameTest() {
    }
    
    @Before
    public void setUp() {
        game.startGame();
    }

    @Test
    public void testNewRound() {
        System.out.println("newRound");
        assertEquals(game.getCurrentPlayer().getCards().size(), 6);
        game.newRound();
        assertEquals(game.getCurrentPlayer().getPlayerNumber(), 0);
        assertEquals(game.getCurrentPlayer().getCards().size(), 5);
        game.newRound();
        game.newRound();
        game.newRound();
        assertEquals(game.getCurrentPlayer().getCards().size(), 2);
        game.newRound();
        assertEquals(game.getCurrentPlayer().getCards().size(), 6);
    }

    @Test
    public void testGetWinner() {
        System.out.println("getWinner");
        assertEquals(game.getWinner(), null);
        IPlayer winner = game.getCurrentPlayer();
        winner.getFinishSquares().forEach(finishSquare ->
            finishSquare.setOccupation(winner.getPlayerNumber()));
        game.doTurn();
        assertEquals(game.getWinner(), winner);
    }

    @Test
    public void testGetCurrentPlayer() {
        System.out.println("getCurrentPlayer");
        assertEquals(game.getCurrentPlayer().getPlayerNumber(), 0);
        game.nextPlayer();
        assertEquals(game.getCurrentPlayer().getPlayerNumber(), 1);
    }

    @Test
    public void testGetSquare() {
        System.out.println("getSquare");
        assertEquals(game.getSquare("Unbekanntes Feld"), null);
        assertEquals(game.getSquare("S0").getName(), "S0");
    }

    @Test
    public void testGetCard() {
        System.out.println("getCard");
        ICard card = game.getCurrentPlayer().getCards().get(1);
        assertEquals(game.getCard(card.getName()).getName(), card.getName());
        assertEquals(game.getCard("Unbekannte Karte"), null);
    }

    @Test
    public void testHasMissingParameter() {
        System.out.println("hasMissingParameter");
    }

    @Test
    public void testHasErroneousParameter() {
        System.out.println("hasErroneousParameter");
        assertEquals(game.hasErroneousParameter(null, null, null), true);
        ICard card = game.getCurrentPlayer().getCards().get(0);
        /*ISquare occupiedSquare = game.getCurrentPlayer().getOccupiedSquares().get(0);
        ISquare square = game.getSquare("S0");
        assertEquals(game.hasErroneousParameter(square, null, card), true);
        assertEquals(game.hasErroneousParameter(null, square, card), true);
        assertEquals(game.hasErroneousParameter(occupiedSquare, square, card), false);
        assertEquals(game.hasErroneousParameter(square, occupiedSquare, card), true);
        assertEquals(game.hasErroneousParameter(square, occupiedSquare, null), true);*/
    }

    @Test
    public void testStartGame() {
        System.out.println("startGame");
        game.newRound();
        game.nextPlayer();
        assertEquals(game.getCurrentPlayer().getPlayerNumber(), 1);
        game.startGame();
        assertEquals(game.getCurrentPlayer().getPlayerNumber(), 0);
    }
    
    @Test
    public void testDoTurn() {
        System.out.println("startGame");
        

        
        game.startGame();
        
        /*game.getCurrentPlayer().getOccupiedSquares().add(game.getSquare("S0"));
        game.getSquare("S0").setOccupation(true);
        //game.getPlayer(1).getOccupiedSquares().add(game.getSquare("S10"));
        game.getSquare("S10").setOccupation(true);*/
        game.getCurrentPlayer().getCards().clear();
        game.getCurrentPlayer().getCards().add(new Card(ValueEnum.JACK, SuitEnum.SPADE));

        
        game.startGame();
        
        /*game.getCurrentPlayer().getOccupiedSquares().add(game.getSquare("S0"));
        game.getSquare("S0").setOccupation(true);*/
        /*game.getPlayer(1).getOccupiedSquares().clear();
        game.getPlayer(1).getHomeSquares().forEach(homeSquare -> homeSquare.setOccupation(false));
        game.getPlayer(1).getOccupiedSquares().add(game.getSquare("S10"));
        game.getSquare("S10").setOccupation(true);
        game.getCurrentPlayer().getCards().clear();
        game.getCurrentPlayer().getCards().add(new Card(ValueEnum.TEN, SuitEnum.SPADE));*/
    }
    
}
