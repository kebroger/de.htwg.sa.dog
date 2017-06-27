/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htwg.dog.model.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwg.dog.model.ICard;
import de.htwg.dog.model.IModel;
import de.htwg.dog.model.IPlayer;
import de.htwg.dog.model.ISquare;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;

/**
 *
 * @author kev
 */
public final class Game implements IModel {

    private List<IPlayer> players = new ArrayList<>();
    private Deck deck;
    private int cardsPerHand;
    @JsonProperty
    private IPlayer currentPlayer;
    @JsonProperty
    private Board board;
    private String info = "";
    private IPlayer winner;
    private String name = "";
    public String id = "";


    private int playerCnt = 4;

    private int squareCnt = 48;

    private int tokenCnt = 4;

    @JsonProperty
    private ISquare selectedSquare1 = null;
    @JsonProperty
    private ISquare selectedSquare2 = null;
    @JsonProperty
    private ICard selectedCard = null;

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String info) {this.info = info;}

    @Override
    public int getCardsPerHand() { return cardsPerHand; }
    @Override
    public void setCardsPerHand(int cardsPerHand) { this.cardsPerHand = cardsPerHand; }

    @Override
    public String getId() {
        return id;
    }
    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Deck getDeck() { return deck; }

    @Override
    public void setDeck(Deck deck) { this.deck = deck; }

    @Override
    @JsonIgnore
    public void setCurrentPlayer(int playerNo) { currentPlayer = players.get(playerNo); }

    private void start() {

        this.id = UUID.randomUUID().toString();

        resetSelectedSquares();

        board = new Board(squareCnt);
        Draw.squareCnt = squareCnt;

        players = new ArrayList<>();
        for(int i = 0; i < playerCnt; i++)
        {
            String squareName = "S" + (squareCnt/playerCnt)*i;
            players.add(new Player(board.getSquareByName(squareName), i, tokenCnt));
        }

        for (IPlayer player : players) {
            board.getSquares().addAll(player.getFinishSquares());
            board.getSquares().addAll(player.getHomeSquares());
        }

        deck = new Deck();
        cardsPerHand = 6;
        currentPlayer = players.get(0);
        winner = null;

        info = "New game started.";

        newRound();
    }

    public void resetSelectedSquares()
    {
        selectedSquare1 = null;
        selectedSquare2 = null;
    }

    @JsonIgnore
    public void setSelectedCard(String card) {
        selectedCard = currentPlayer.getCardByName(card);
        if(selectedCard != null)
            info = "Card selected.";
    }

    @JsonIgnore
    public ICard getSelectedCard() {
        return selectedCard;
    }

    @JsonIgnore
    public void setSelectedSquare(String selectedSquare) {

        ISquare s = board.getSquareByName(selectedSquare);

        if(s != null){
            if (getSelectedSquare(1) == null && s != getSelectedSquare(2)) {
                info = "Square selected.";
                selectedSquare1 = s;
            } else if (getSelectedSquare(1) == s) {
                info = "Square deselected.";
                selectedSquare1 = null;
            } else if (getSelectedSquare(2) == null && s != getSelectedSquare(1)) {
                info = "Square selected.";
                selectedSquare2 = s;
            } else if (getSelectedSquare(2) == s) {
                info = "Square deselected.";
                selectedSquare2 = null;
            }
        }
    }

    @JsonIgnore
    public ISquare getSelectedSquare(int i)
    {
        if(i == 1)
            return selectedSquare1;
        else
            return selectedSquare2;
    }

    void newRound() {

        if (cardsPerHand == 1) {
            cardsPerHand = 6;
        }

        if (cardsPerHand * 4 > deck.undealedCards.size()) {
            deck = new Deck();
        }

        players.forEach(player -> {
            player.getCards().clear();
            for (int i = 0; i < cardsPerHand; i++) {
                Card card = deck.undealedCards.get(new Random().nextInt(deck.undealedCards.size() - 1));
                deck.undealedCards.remove(card);
                player.getCards().add(card);
            }
        });
        cardsPerHand--;
    }

    public void nextPlayer() {

        if (!checkVictoryConditions()) {
            int playerNumber = currentPlayer.getPlayerNumber();
            IPlayer nextPlayer;

            if (playerNumber == playerCnt-1) {
                playerNumber = 0;
            } else {
                playerNumber++;
            }

            nextPlayer = players.get(playerNumber);

            while (nextPlayer.getCards().isEmpty()) {
                if (nextPlayer == currentPlayer) {
                    newRound();
                    nextPlayer();
                    return;
                }

                if (playerNumber == playerCnt-1) {
                    playerNumber = 0;
                } else {
                    playerNumber++;
                }

                nextPlayer = players.get(playerNumber);
            }

            currentPlayer = nextPlayer;
        }
    }

    public List<IPlayer> getPlayers() {
        return players;
    }

    @Override
    public IPlayer getWinner() {
        return winner;
    }

    @Override
    public IPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public void discardCards() {
        currentPlayer.getCards().clear();
        info = "Player " + currentPlayer.getPlayerNumber() + " discards cards.";
        nextPlayer();
    }

    @Override
    public ISquare getSquare(String name) {
        return board.getSquareByName(name);
    }

    @Override
    public ICard getCard(String name) {
        return currentPlayer.getCardByName(name);
    }

    @Override
    @JsonIgnore
    public List<ISquare> getSquares() {
        List<ISquare> squares = new ArrayList<>();

        for (ISquare square : this.board.getSquares()) {
            squares.add(square);
        }

        return squares;
    }

    public boolean hasMissingParameter(ISquare s1, ISquare s2, ICard card){
        if (card == null) {
            info = "No card selected.";
            return true;
        } else if (s1 == null || s2 == null) {
            info = "No square selected.";
            return true;
        }
        return false;
    }

    public boolean hasErroneousParameter(ISquare from, ISquare to, ICard card) {
        if (from == null || to == null) {
            info = "Unknown square selected.";
            return true;
        } else if (card == null) {
            info = "Unknown card selected.";
            return true;
        }else if (from.getToken() == -1) {
            info = "Square without token selected.";
            return true;
        }

        return false;
    }

    @Override
    public boolean doTurn() {

        if (checkVictoryConditions() || hasMissingParameter(selectedSquare1, selectedSquare2, selectedCard))
            return false;

        ISquare from = selectedSquare1;
        ISquare to = selectedSquare2;
        ICard card = selectedCard;

        if(hasErroneousParameter(from, to, card))
            return false;

        ValueEnum val = ValueEnum.fromString(card.toString().substring(card.toString().indexOf("_") + 1));

        if (!Draw.isDrawAllowed(from, to, val, currentPlayer)) {
            info = "Turn is not allowed.";
            return false;
        }

        from.setOccupation(-1);

        if(to.getToken() != -1){
            if (val == ValueEnum.JACK)
                from.setOccupation(to.getToken());
            else
                sendPlayerTokenHome(to.getToken());
        }

        to.setOccupation(currentPlayer.getPlayerNumber());
        currentPlayer.getCards().remove(selectedCard);

        nextPlayer();
        info = "Turn was successfull.";

        return true;
    }

    private void sendPlayerTokenHome(int playerNo){
        IPlayer player = players.get(playerNo);

        for (ISquare homeSquare : player.getHomeSquares()) {
            if (homeSquare.getToken() == -1) {
                homeSquare.setOccupation(playerNo);
                break;
            }
        }
    }

    private boolean checkVictoryConditions() {

        boolean hasWon = true;

        for (ISquare square : currentPlayer.getFinishSquares()) {
            hasWon &= square.getToken() != -1;
        }

        if (hasWon) {
            winner = currentPlayer;
            info = "The game is over.";
        }

        return hasWon;
    }

    @Override
    public void startGame() {
        this.start();
    }

    @Override
    public void configureGame(int playerCnt, int squareCnt, int tokenCnt) {
        this.info = "Saved configuration.";
        this.playerCnt = playerCnt;
        this.squareCnt = squareCnt;
        this.tokenCnt = tokenCnt;
    }

    @Override
    public int getPlayerCnt(){
        return playerCnt;
    }

    @Override
    public int getSquareCnt(){
        return squareCnt;
    }

    @Override
    public int getTokenCnt(){
        return tokenCnt;
    }

    @Override
    public String getName() { return name; }

    @Override
    public void setName(String name){
        this.name = name;
    }

    @Override
    public String toJson() {
        String result = "";
        try {

            //HashMap mapGame = new HashMap();
            //mapGame.put("game", this);

            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
            result = mapper.writeValueAsString(this);

            try{
                Game blu = mapper.readValue(result, Game.class);
            }
            catch (Exception e)
            {
                System.out.println();
            }

        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void makeAutoDraw() throws Exception
    {
        selectedCard = null;
        resetSelectedSquares();

        String query = String.format("game=%s", URLEncoder.encode(this.toJson(), "UTF-8"));
        String url = "http://127.0.0.1:8080/rest/";

        URLConnection connection = new URL(url + "?" + query).openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        InputStream instr = connection.getInputStream();

        BufferedReader in = new BufferedReader(new InputStreamReader(instr));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        if(response.toString().equals(""))
        {
            this.discardCards();
        }
        else {
            List<String> l = Arrays.asList(response.toString().split(","));
            setSelectedSquare(l.get(0));
            setSelectedSquare(l.get(1));
            setSelectedCard(l.get(2));
            doTurn();

            selectedCard = null;
            resetSelectedSquares();
        }
    }
}
