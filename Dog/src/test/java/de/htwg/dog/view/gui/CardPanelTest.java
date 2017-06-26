/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htwg.dog.view.gui;

import de.htwg.dog.controller.impl.Controller;
import de.htwg.dog.model.impl.ModelFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 *
 * @author kev
 */
public class CardPanelTest {
    
    CardPanel cardPanel = new CardPanel(new Controller(new ModelFactory(), null));
    
    public CardPanelTest() {
    }
    
    @Before
    public void setUp() {
    }


    @Test
    public void testPaintComponent() {
        System.out.println("paintComponent");
    }

    /*@Test
    public void testGetSelectedCard() {
        System.out.println("getSelectedCard");
        assertEquals(cardPanel.getSelectedCard(), "");
    }*/

    @Test
    public void testSetCards() {
        System.out.println("setCards");
        cardPanel.setCards(null);
        cardPanel.setCards(new ArrayList<>());
    }
    
}
