/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htwg.dog.view.gui;

import com.google.inject.Inject;
import de.htwg.dog.controller.IController;
import de.htwg.dog.view.IView;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author kev
 */
public final class Gui extends JFrame implements IView {

    JSplitPane splitPaneV;
    JSplitPane splitPaneH;
    BoardPanel boardPanel;
    CardPanel cardPanel;
    JPanel controlPanel;
    JTextArea commandTextArea;
    JButton executeButton;
    JButton discardButton;
    JButton autoDrawButton;
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem startMenuItem;
    JMenuItem loadMenuItem;
    JMenuItem saveMenuItem;
    JLabel label;
    JTextField infoBox;

    IController contr;
    
    @Inject
    public Gui(final IController contr) {

        this.contr=contr;
        this.contr.addUpdateListener((ActionEvent e) -> update());
        
        setTitle("Dog");

        createGui();
        
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void createGui(){
        
        this.getContentPane().removeAll();

        ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("tailchase.png"));
        this.setIconImage(img.getImage());
        
        commandTextArea = new JTextArea();
        executeButton = new JButton("Zug ausführen");
        discardButton = new JButton("Karten verwerfen");
        autoDrawButton = new JButton("Automatischer Zug");
        menuBar = new JMenuBar();
        menu = new JMenu("Menu");
        startMenuItem = new JMenuItem("New Game");
        loadMenuItem = new JMenuItem("Load Game");
        saveMenuItem = new JMenuItem("Save Game");
        label = new JLabel("");
        infoBox = new JTextField("");
        
        JPanel topPanel = new JPanel();
        topPanel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        topPanel.setLayout(new BorderLayout());
        setJMenuBar(menuBar);
        getContentPane().add(topPanel);
        menuBar.add(menu);
        menu.add(startMenuItem);
        startMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                contr.startGame();
            }
        });
        menu.addSeparator();
        menu.add(loadMenuItem);
        menu.add(saveMenuItem);
        saveMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                new SaveDialog(Gui.this, contr);
            }
        });
        loadMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                new LoadDialog(Gui.this, contr);
            }
        });

        Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();

        boardPanel = new BoardPanel(contr);
        boardPanel.setBorder(BorderFactory.createCompoundBorder(raisedbevel, loweredbevel));
        boardPanel.setPreferredSize(new Dimension(500, 500));
        boardPanel.setMinimumSize(new Dimension(200, 200));

        cardPanel = new CardPanel(contr);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(raisedbevel, loweredbevel));
        cardPanel.setMinimumSize(new Dimension(200, 200));
        cardPanel.setPreferredSize(new Dimension(200, 300));

        controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBorder(BorderFactory.createCompoundBorder(raisedbevel, loweredbevel));
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        JPanel panel2 = new JPanel(new BorderLayout(0, 5));
        panel2.setBackground(Color.lightGray);
        panel.setBorder(new EmptyBorder(new Insets(3, 3, 3, 3)));

        panel2.add(label, BorderLayout.CENTER);
        buttonPanel.add(discardButton);
        buttonPanel.add(executeButton);
        buttonPanel.add(autoDrawButton);
        buttonPanel.add(infoBox);
        infoBox.setEditable(false);
        
        label.setFont(new Font("Helvetica", Font.BOLD, 32));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        discardButton.setBorder(raisedbevel);
        discardButton.addActionListener((ActionEvent e) -> contr.discardCard());
        executeButton.setBorder(raisedbevel);
        executeButton.addActionListener((ActionEvent e) -> {contr.doTurn();});
        autoDrawButton.setBorder(raisedbevel);
        autoDrawButton.addActionListener((ActionEvent e) -> {contr.makeAutoDraw();});
        JScrollPane scrollPane = new JScrollPane(commandTextArea);
        scrollPane.setBorder(loweredbevel);
        panel2.add(buttonPanel, BorderLayout.PAGE_END);
        panel.add(panel2, BorderLayout.PAGE_START);
        panel.add(scrollPane, BorderLayout.CENTER);
        commandTextArea.setLineWrap(true);
        commandTextArea.setEditable(false);
        controlPanel.setBackground(Color.lightGray);
        panel.setBackground(Color.lightGray);
        controlPanel.add(panel, BorderLayout.CENTER);

        splitPaneH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPaneH.setBorder(null);
        BasicSplitPaneUI ui = (BasicSplitPaneUI) splitPaneH.getUI();
        BasicSplitPaneDivider divider = ui.getDivider();
        divider.setBorder(new EmptyBorder(new Insets(2, 2, 2, 2)));
        topPanel.add(splitPaneH, BorderLayout.CENTER);

        splitPaneV = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPaneV.setBorder(null);

        ui = (BasicSplitPaneUI) splitPaneV.getUI();
        divider = ui.getDivider();
        divider.setBorder(new EmptyBorder(new Insets(2, 2, 2, 2)));

        splitPaneV.setMinimumSize(new Dimension(175, 600));
        splitPaneV.setLeftComponent(cardPanel);
        splitPaneV.setRightComponent(controlPanel);

        splitPaneH.setLeftComponent(splitPaneV);
        splitPaneH.setRightComponent(boardPanel);

        setMinimumSize(new Dimension(500, 500));
        pack();
    }

    @Override
    public void update() {
        List<String> currentCards = contr.getCurrentCards();
        Collections.sort(currentCards);
        cardPanel.setCards(currentCards);
        
        infoBox.setText(contr.getInfo());
        
        String currentPlayer = "Player " + contr.getCurrentPlayerNo();
        label.setText(currentPlayer);

        boardPanel.update();
        cardPanel.invalidate();
        
        if(contr.getWinnerNo() >= 0){
            commandTextArea.setText("Player " + contr.getWinnerNo() + " hat gewonnen!\n"
                    + "Herzlichen Glückwunsch!\n"
                    + "Starten sie ein neues Spiel.");    
        }
        
        repaint();
    }
}