/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htwg.dog.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.htwg.dog.model.impl.Square;

/**
 *
 * @author kev
 */
@JsonDeserialize(as=Square.class)
public interface ISquare {

    public int getToken();

    public void setOccupation(int tokenName);

    public String getName();

    public int getNo();
}
