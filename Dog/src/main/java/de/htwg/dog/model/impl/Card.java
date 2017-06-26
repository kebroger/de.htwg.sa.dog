/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htwg.dog.model.impl;

import de.htwg.dog.model.ICard;

/**
 *
 * @author kev
 */
public class Card implements ICard {

    private ValueEnum value;
    private SuitEnum suit;
    private String name = "";

    public Card(){}

    public Card(ValueEnum value, SuitEnum suit) {
        this.value = value;
        this.suit = suit;

        name = suit.getName() + "_" + value.getName();
    }

    void changeCard(ValueEnum value, SuitEnum suit) {
        this.value = value;
        this.suit = suit;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
