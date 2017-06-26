/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htwg.dog.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.htwg.dog.model.impl.Player;

import java.util.List;

/**
 *
 * @author kev
 */

@JsonDeserialize(as=Player.class)
public interface IPlayer {

    public List<ICard> getCards();

    public List<ISquare> getFinishSquares();

    public List<ISquare> getHomeSquares();

    public int getPlayerNumber();

    public ISquare getStartSquare();

    public ICard getCardByName(String name);
}
