/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htwg.dog.model.impl;

import de.htwg.dog.model.IPlayer;
import de.htwg.dog.model.ISquare;

/**
 *
 * @author kev
 */
public final class Draw {

    private Draw() {
    }

    public static int squareCnt = 48;

    public static boolean isDrawAllowed(ISquare from, ISquare to, ValueEnum card, IPlayer player) {

        boolean validDraw = false;

        if (card != ValueEnum.JOKER) 
            return checkNonJokerCardDraw(from, to, card, player);
        
        for (ValueEnum value : ValueEnum.values()) {
            if (value != ValueEnum.JOKER && value != ValueEnum.JACK) {
                validDraw = checkNonJokerCardDraw(from, to, value, player);
                if(validDraw)
                    break;
            }
        }
        
        return validDraw;
    }
        
    private static boolean checkNonJokerCardDraw(ISquare from, 
            ISquare to, ValueEnum card, IPlayer player) {
        
        boolean validDraw = false;
        
        if (from.getToken() != player.getPlayerNumber())
            return false;

        //from home to start square
        if (from.getName().startsWith("H") && to == player.getStartSquare() &&
                player.getStartSquare().getToken() != player.getPlayerNumber())
            validDraw = card == ValueEnum.ACE || card == ValueEnum.KING;
        
        if(from.getName().startsWith("S")) {
            //from standart to standart square
            if (to.getName().startsWith("S"))
                validDraw = fromStandartToStandart(from, to, card, player);

            //from standart to finish square
            if (to.getName().startsWith("F"))
                validDraw = fromStandartToFinish(from, to, card, player);
        }

        return validDraw;
    }
    
    private static boolean fromStandartToStandart(ISquare from, ISquare to, ValueEnum card, IPlayer player) {
        boolean validDraw = false;
        validDraw |= fromStandartToStandart(from, to, card.getI1());
        validDraw |= fromStandartToStandart(from, to, card.getI2());
        validDraw |= (card == ValueEnum.JACK
                        && to.getToken() != -1
                        && to.getToken() != player.getPlayerNumber());
        validDraw &= !from.getName().equals(to.getName());

        return validDraw;
    }

    private static boolean fromStandartToStandart(ISquare from, ISquare to, int valueToGo) {
        int actualValueToGo = valueToGo;
        if (valueToGo < 0) {
            actualValueToGo += squareCnt;
        }
        int difference = getIntDifference(from.getNo(), to.getNo());
        return difference == actualValueToGo;
    }

    private static boolean fromStandartToFinish(ISquare from, ISquare to, ValueEnum card, IPlayer player) {
        boolean validDraw = false;
        
        for (ISquare finishSquare : player.getFinishSquares()) {
            if (to.getName().equals(finishSquare.getName())) {
                validDraw |= fromStandartToFinish(from, to, card.getI1(), player.getStartSquare());
                validDraw |= fromStandartToFinish(from, to, card.getI2(), player.getStartSquare());
            }
        }

        return validDraw && to.getToken() != player.getPlayerNumber() && !from.getName().equals(player.getStartSquare().getName());
    }
    
    public static boolean fromStandartToFinish(ISquare from, ISquare to, int valueToGo, ISquare startSquare) {
        if (from != startSquare && valueToGo > 0) {
            int difference = getIntDifference(from.getNo(), startSquare.getNo());
            return difference + to.getNo() + 1 == valueToGo;
        }

        return false;
    }

    private static int getIntDifference(int i1, int i2) {
        int diff = i2 - i1;
        if (diff < 0) {
            diff += squareCnt;
        }
        return diff;
    }
}
