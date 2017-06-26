/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htwg.dog.model.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.htwg.dog.model.ISquare;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kev
 */
public class Board {

    @JsonProperty
    private List<ISquare> squares = new ArrayList<>();

    public Board(){}

    public Board(int squareCnt) {
        this.squares = new ArrayList<>();

        for (int i = 0; i < squareCnt; i++) {
            squares.add(new Square("S" + i));
        }
    }

    @JsonIgnore
    public List<ISquare> getSquares() {
        return squares;
    }
    
    public ISquare getSquareByName(String name) {
        for (ISquare square : squares) {
            if (square.getName().equals(name)) {
                return square;
            }
        }

        return null;
    }
}
