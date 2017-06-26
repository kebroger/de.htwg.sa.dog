/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htwg.dog.view.tui;

import com.google.inject.Inject;
import de.htwg.dog.controller.IController;
import de.htwg.dog.view.IView;
import org.apache.log4j.Logger;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author kev
 */
public class Tui implements IView {

    private static final Logger LOGGER = Logger.getLogger((String)Tui.class.getName());
    private static final String NEWLINE = System.getProperty("line.separator");

    private final IController contr;
    private String currentPlayer;
    private final List<String> cards;
    private String info = "";

    @Inject
    public Tui(final IController controller) {
        this.currentPlayer = "";
        this.cards = new ArrayList<>();
        this.cards.add(" ");

        this.contr = controller;
        this.contr.addUpdateListener((ActionEvent e) -> update());
    }
    
    private String paintBoard() {

        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (IOException | InterruptedException ex) { 
            LOGGER.info(ex);
        }

        List<String> squares = new ArrayList<>();

        Pattern p = Pattern.compile("[FSH][0-9][0-9]?[P]?[0-3]?");
        Matcher m = p.matcher(BoardLayout.get());
        while (m.find()) {
            squares.add(m.group());
        }

        Map<String, String> mapOccupiedSquares = new HashMap<>();


        for(int no = 0; no < contr.getPlayerCnt(); no++) {
            for(String occupiedSquare : contr.getOccupiedSquares(no)){
                mapOccupiedSquares.put(occupiedSquare, "P" + no);
            }
        }

        List<String> tokens = new ArrayList<>();

        for (String square : squares) {
            String token = mapOccupiedSquares.get(square);

            if (token == null) {
                tokens.add("");
            } else {
                tokens.add(token);
            }
        }
        

        StringBuilder sb = new StringBuilder();
        Formatter fm = new Formatter(sb);
        fm.format(BoardLayout.get(), tokens.toArray());
        fm.close();

        return sb.toString();
    }
    
    @Override
    public void update() {
        List<String> currentCards = contr.getCurrentCards();
        Collections.sort(currentCards);
        this.info = contr.getInfo();
        
        currentPlayer = "Player " + contr.getCurrentPlayerNo();
        this.cards.clear();
        this.cards.addAll(currentCards);

        printTUI();
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Boolean processInput(String line) {

        if ("new".equals(line)) {
            contr.startGame();
            return true;
        }

        if ("auto".equals(line)) {
            if(contr.getWinnerNo() != -1)
                contr.startGame();
            contr.makeAutoDraw();
            return true;
        }
        
        if ("discard".equals(line)) {
            contr.discardCard();
            return true;
        }

        if ("exit".equals(line)) {
            return false;
        }

        if ("turn".equals(line)) {
            return contr.doTurn();
        }

        if (line.startsWith("configure")) {
            String[] parts = line.split(",");
            if (parts.length == 4) {
                try {
                    int playerCnt = Integer.parseInt(parts[1]);
                    int squareCnt = Integer.parseInt(parts[2]);
                    int tokenCnt = Integer.parseInt(parts[3]);
                    contr.configureGame(playerCnt, squareCnt, tokenCnt);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }


        if (line.startsWith("card")) {

            String[] parts = line.split(",");
            if (parts.length == 2) {
                String selectedCard = parts[1];
                contr.setSelectedCard(selectedCard);
                return true;
            }
        }

        if (line.startsWith("square")) {

            String[] parts = line.split(",");
            if (parts.length == 2) {
                String selectedSquare = parts[1];
                contr.setSelectedSquare(selectedSquare);
                return true;
            }
        }
        
        info = "Unknown command.";
        printTUI();
        
        return true;
    }
    
    private void printTUI() {

        LOGGER.info(this.toString());
    }

    public String toString() {

        String result = paintBoard() + NEWLINE;
        result += "Player: " + currentPlayer + NEWLINE;
        result += "Cards: " + cards + NEWLINE;
        result += "Selected [ " + contr.getSelectedCard() +
                " , " + contr.getSelectedSquare(1) +
                " , " + contr.getSelectedSquare(2) + "]" + NEWLINE;
        result += info + NEWLINE;
        result += "Commands:" + NEWLINE
                + "New Game = \"new\", Exit game = \"exit\": , Execute Turn = \"turn\"" + NEWLINE
                + "Discard cards = \"discard\", Select card = \"card,[name]\", Select square = \"square,[name]\"";

        return result;
    }

    public String toHtml() {
        String game = this.toString();
        String result = game.replace(NEWLINE, "<br>");
        //result = result.replace("     ", " &nbsp; &nbsp; ");
        //result = result.replace("   ", " &nbsp; ");
        return result;
    }

}
