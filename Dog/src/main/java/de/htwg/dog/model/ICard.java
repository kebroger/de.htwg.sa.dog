/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htwg.dog.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.htwg.dog.model.impl.Card;

/**
 *
 * @author kev
 */

@JsonDeserialize(as=Card.class)
public interface ICard {

    public String getName();
}
