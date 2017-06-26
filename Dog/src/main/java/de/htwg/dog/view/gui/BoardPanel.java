/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htwg.dog.view.gui;

import de.htwg.dog.controller.IController;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kev
 */
public class BoardPanel extends JPanel {

    private int tokenNo = 4;
    private int squareNo = 48;
    private int playerNo = 4;

    private Board board;
    private static final Logger LOGGER = Logger.getLogger((String)BoardPanel.class.getName());

    static final Color colorSelectedS1 = Color.MAGENTA;
    static final Color colorSelectedS2 = Color.RED;
    static final Color colorBorder = Color.BLACK;

    private IController controller;
    
    public BoardPanel(IController controller) {

        this.controller = controller;

        setBackground(Color.lightGray);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) { 
                if (e.getButton() == 1) {

                    checkIfSquareIsClicked(e, board.squares);

                    for (Board.Player player : board.players) {

                        checkIfSquareIsClicked(e, player.finishSquares);
                        checkIfSquareIsClicked(e, player.homeSquares);
                    }

                }
            }
        });
            
        setDoubleBuffered(true);
    }

    private void drawSquareBorder(Graphics2D g, Board.Square square, int thickness) {
        if (square.getName().equals(controller.getSelectedSquare(1)))
            g.setColor(colorSelectedS1);
        else if (square.getName().equals(controller.getSelectedSquare(2)))
            g.setColor(colorSelectedS2);
        else 
            g.setColor(colorBorder);

        g.setStroke(new BasicStroke(thickness));
        g.draw(square);
        g.setStroke(new BasicStroke(0));
    }

    private void drawSquare(Graphics2D g, Board.Square square, Color color) {
        g.setColor(color);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
        g.fill(square);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    private void drawToken(Graphics2D g, Board.Square square, Color color, int thickness) {

        drawSquare(g, square, color);

        g.setColor(colorBorder);

        g.setStroke(new BasicStroke(thickness));
        Ellipse2D ell = new Ellipse2D.Double();
        ell.setFrame(square.getCenterX() - square.getWidth() / 10, square.getCenterY() - square.getWidth() / 10, square.getWidth() / 5, square.getWidth() / 5);
        g.draw(ell);
        g.setStroke(new BasicStroke(0));
    }

    private void drawBackgorundImage(Graphics2D g2d, Point center, int drawAreaSide) {

        try {
            BufferedImage image = ImageIO.read(getClass().getClassLoader().getResource("tailchase.png"));
            image = Images.scale(image, BufferedImage.TYPE_INT_ARGB, drawAreaSide, drawAreaSide);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
            g2d.drawImage(image, center.x - image.getWidth() / 2, center.y - image.getHeight() / 2, this);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        } catch (IOException ex) { 
            LOGGER.info(ex);
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        if(board != null)
            paintBoard(g);
    }

    public void update(){
        tokenNo = controller.getTokenCnt();
        squareNo = controller.getSquareCnt();
        playerNo = controller.getPlayerCnt();

        board = new Board(playerNo,squareNo, tokenNo);

        for(int no = 0; no < controller.getPlayerCnt(); no++) {
            setOccupiedSquares(no, controller.getOccupiedSquares(no));
        }
    }
    
    private void paintBoard(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        double angle = Math.toRadians(270);
        double step = 2 * Math.PI / squareNo;
        double x, y;
        int shortPanelSide = Math.min(getWidth(), getHeight());
        double drawAreaSide = shortPanelSide - (double)(shortPanelSide) / 3;
        Point center = new Point(shortPanelSide / 2, shortPanelSide - shortPanelSide / 2);
        double radius = drawAreaSide / 2;
        double radiusOfSquare = radius / (4 + squareNo*5/48) ;
        int thickness = (int) Math.round(radiusOfSquare / 6);

        drawBackgorundImage(g2d, center, (int)drawAreaSide);

        for (Board.Square square : board.squares) {

            x = center.x + radius * Math.cos(angle);
            y = center.y + radius * Math.sin(angle);

            square.setFrame(x - radiusOfSquare / 2, y - radiusOfSquare / 2, radiusOfSquare, radiusOfSquare);
            drawSquare(g2d, square, Color.white);
            drawSquareBorder(g2d, square, thickness);

            angle += step;
        }

        step = step * squareNo/playerNo;

        for (Board.Player player : board.players) {

            radius = drawAreaSide / 2;

            for (Board.Square finishSquare : player.finishSquares) {

                radius -= 1.3 * radiusOfSquare;

                x = center.x + radius * Math.cos(angle);
                y = center.y + radius * Math.sin(angle);

                finishSquare.setFrame(x - radiusOfSquare / 2, y - radiusOfSquare / 2, radiusOfSquare, radiusOfSquare);
                drawSquare(g2d, finishSquare, player.playerColor);
                drawSquareBorder(g2d, finishSquare, thickness);

            }

            radius = drawAreaSide / 2 + 1.3 * radiusOfSquare;
            double tmpAngle = angle;

            for (Board.Square homeSquare : player.homeSquares) {

                x = center.x + radius * Math.cos(tmpAngle);
                y = center.y + radius * Math.sin(tmpAngle);

                homeSquare.setFrame(x - radiusOfSquare / 2, y - radiusOfSquare / 2, radiusOfSquare, radiusOfSquare);
                drawSquare(g2d, homeSquare, player.playerColor);
                drawSquareBorder(g2d, homeSquare, thickness);

                tmpAngle -= step / (24/playerNo);
            }

            List<Board.Square> tmpList = new ArrayList<>();
            tmpList.addAll(board.squares);
            tmpList.addAll(player.homeSquares);
            tmpList.addAll(player.finishSquares);

            player.getOccupiedSquares().forEach(token -> {
                for (Board.Square square : tmpList) {
                    if (square.name.equals(token)) {
                        drawToken(g2d, square, player.playerColor, thickness);
                        drawSquareBorder(g2d, square, thickness);
                    }
                }
            });

            angle += step;
        }
    }

    private void checkIfSquareIsClicked(MouseEvent e, List<Board.Square> squareList) {
        for (Board.Square square : squareList) {
            if (square.contains(e.getX(), e.getY()) && e.getButton() == 1) {
                squareClicked(square);
            }
        }
    }
    
    public void setOccupiedSquares(int playerNo, List<String> occupiedSquares) {
        board.players.get(playerNo).setOccupiedSquares(occupiedSquares);
    }

    private void squareClicked(Board.Square clickedSquare) {
        controller.setSelectedSquare(clickedSquare.getName());
    }

    public class Board {

        private final List<Square> squares = new ArrayList<>();
        private final List<Player> players = new ArrayList<>();
        
        private Board(int playerNo, int squareNo, int tokenNo) {

            if(squareNo % playerNo == 0) {
                for (int i = 0; i < squareNo; i++) {
                    squares.add(new Square("S" + i));
                }

                for(int i = 0; i < playerNo; i++)
                {
                    players.add(new Player(StringToColor.intToColor(i), i, tokenNo));
                }
            }
        }     
        
        private class Square extends Ellipse2D.Double {

            private final String name;

            private Square(String name) {
                this.name = name;
            }

            private String getName() {
                return name;
            }

            @Override
            public boolean equals(Object obj) {
                if (!super.equals(obj))
                    return false;

                Square fobj = (Square) obj;
                if (name.equals(fobj.getName()))
                    return true;

                return false;
            }

            @Override
            public int hashCode() {
                return super.hashCode() + name.hashCode();
            }
        }

        private class Player {
            
            private final int playerNumber;
            private Color playerColor;
            private final List<Square> homeSquares;
            private final List<Square> finishSquares;
            private List<String> occupiedSquares;

            private Player(Color color, int number, int tokenNo) {

                playerNumber = number;
                homeSquares = new ArrayList<>();
                finishSquares = new ArrayList<>();
                occupiedSquares = new ArrayList<>();
                playerColor = color;

                for (int i = 0; i < tokenNo; i++) {
                    homeSquares.add(new Square("H" + i + "P" + playerNumber));
                    finishSquares.add(new Square("F" + i + "P" + playerNumber));
                }

            }

            private List<String> getOccupiedSquares() {
                return occupiedSquares;
            }

            private void setOccupiedSquares(List<String> occupiedSquares) {
                this.occupiedSquares = occupiedSquares;
            }
        }
    }

}
