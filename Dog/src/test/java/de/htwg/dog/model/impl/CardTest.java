/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htwg.dog.model.impl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author kev
 */
public class CardTest {
    
    Card card;
    
    @Before
    public void setUp() {
        card = new Card(ValueEnum.ACE, SuitEnum.CLUB);
    }

    @Test
    public void testChangeCard() {
        card.changeCard(ValueEnum.EIGHT, SuitEnum.DIAMOND);
        assertEquals(card.getName(), "DIAMOND_8");
    }

    @Test
    public void testGetName() {
        assertEquals(card.getName(), SuitEnum.CLUB.getName()+ "_" + ValueEnum.ACE.getName());
    }
    
}
