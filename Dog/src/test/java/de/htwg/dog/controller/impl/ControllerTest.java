/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htwg.dog.controller.impl;

import de.htwg.dog.model.impl.ModelFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author kev
 */
public class ControllerTest {
    
    Controller contr = new Controller(new ModelFactory(), null);
    
    public ControllerTest() {
    }
    
    @Before
    public void setUp() {
        contr.startGame();
    }

    @Test
    public void testAddUpdateListener() {
        System.out.println("addUpdateListener");
        contr.addUpdateListener(null);
    }

    @Test
    public void testGetCurrentCards() {
        System.out.println("getCurrentCards");
        assertEquals(contr.getCurrentCards().size(), 6);
    }

    @Test
    public void testGetCurrentPlayerNo() {
        System.out.println("getCurrentPlayerNo");
        assertEquals(contr.getCurrentPlayerNo(), 0);
        contr.discardCard();
        assertEquals(contr.getCurrentPlayerNo(), 1);
    }

    @Test
    public void testGetWinnerNo() {
        System.out.println("getWinnerNo");
        assertEquals(contr.getWinnerNo(), -1);
    }

    @Test
    public void testGetOccupiedSquares() {
        System.out.println("getOccupiedSquares");
        assertEquals(contr.getOccupiedSquares(0).size(), 4);
    }
    
}
