/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htwg.dog.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.htwg.dog.model.impl.Player;

import java.util.List;

/**
 *
 * @author kev
 */

@JsonDeserialize(as=Player.class)
public interface IPlayer {

    List<ICard> getCards();

    List<ISquare> getFinishSquares();

    List<ISquare> getHomeSquares();

    int getPlayerNumber();

    ISquare getStartSquare();

    ICard getCardByName(String name);
}
