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
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author kev
 */
public final class CardPanel extends JPanel {

    private static final Logger LOGGER = Logger.getLogger((String)CardPanel.class.getName());

    List<ActionListener> listeners = new ArrayList<>();
    private List<Card> cards = new ArrayList<>();
    private IController controller;

    public CardPanel(IController controller) {
        this.controller = controller;

        setBackground(Color.lightGray);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) { 
                if (e.getButton() == 1) {
                    ListIterator iter = cards.listIterator(cards.size());
                    while (iter.hasPrevious()) {
                        Card card = (Card) iter.previous();
                        if (card.getRect().contains(e.getX(), e.getY())) {
                            controller.setSelectedCard(card.getValue());
                            return;
                        }
                    }
                }
            }
        });
    }
    
    public void setCards(List<String> cards) {
        this.cards = new ArrayList<>();

        if(cards!=null){
            cards.forEach(card -> {
                Card c = new Card(card);
                this.cards.add(c);
            });
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (!cards.isEmpty()) {

            int start = 10;
            int step = 0;
            if(cards.size() > 1)
            {
                step = (this.getHeight() - 2 * start - cards.get(0).getHeight()) / (cards.size() - 1);
            }
            int x = start;
            int y = start;
            String selectedCard = controller.getSelectedCard();
            for (Card card : cards) {

                card.setRect(new Rectangle2D.Double(x, y, card.getWidth(), card.getHeight()));

                if (card.getValue().equals(selectedCard)) {
                    g2d.setColor(Color.RED);
                } else {
                    g2d.setColor(Color.BLACK);
                }
                g2d.setStroke(new BasicStroke(3));
                g2d.drawImage(card.getImage(), card.getX(), card.getY(), null);
                g2d.drawRect(card.getX(), card.getY(), card.getWidth(), card.getHeight());
                g2d.setStroke(new BasicStroke());
                y += step;

            }
        }
    }

    private class Card {

        private BufferedImage image = null;
        private Rectangle2D rect;
        private String value;
        
        public Card(String value) {
            this.value = value;

            try {
                this.image = ImageIO.read(getClass().getClassLoader().getResource(value + ".jpg"));
            } catch (IOException ex) {
                LOGGER.info(ex);
            }

            this.rect = new Rectangle2D.Double(0, 0, image.getWidth(), image.getHeight());
        }

        private BufferedImage getImage() {
            return image;
        }

        private String getValue() {
            return value;
        }

        private int getX() {
            return (int) rect.getX();
        }

        private int getY() {
            return (int) rect.getY();
        }

        private int getWidth() {
            return (int) rect.getWidth();
        }

        private int getHeight() {
            return (int) rect.getHeight();
        }

        private void setRect(Rectangle2D rectangle) {
            rect = rectangle;
        }

        private Rectangle2D getRect() {
            return rect;
        }
    }
}
