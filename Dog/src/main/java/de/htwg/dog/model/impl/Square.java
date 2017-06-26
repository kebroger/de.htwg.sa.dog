/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htwg.dog.model.impl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.htwg.dog.model.ISquare;

/**
 *
 * @author kev
 */
@JsonDeserialize(as=Square.class)
public final class Square implements ISquare {

    private String name = "";
    private int token = -1;
    private int no = 0;

    public Square() { }

    public Square(String name) {
        setName(name);
    }
    
    @Override
    public int getToken() {
        return token;
    }

    @Override
    public void setOccupation(int tokenName) {
        token = tokenName;
    }

    private void setName(String name) {
        this.name = name;

        switch (name.charAt(0)) {
            case 'S':
                no = Integer.parseInt(name.substring(1));
                break;
            case 'F':
                no = Character.getNumericValue(name.charAt(1));
                break;
            case 'H':
                no = Character.getNumericValue(name.charAt(1));
                break;
            default:
                no = 0;
                break;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getNo() {
        return no;
    }

}
