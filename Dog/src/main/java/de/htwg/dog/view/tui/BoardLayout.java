/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htwg.dog.view.tui;

/**
 *
 * @author kev
 */
public final class BoardLayout {

    private static final String NEWLINE = System.getProperty("line.separator");

    private BoardLayout(){
    }
    
    public static String get(){
        
    return "\n\n   | %3s  | %3s  | %3s  | %3s  |                                         | %3s  | %3s  | %3s  | %3s  |   " + NEWLINE
                + "   | H3P0 | H2P0 | H1P0 | H0P0 |                                         | H0P1 | H1P1 | H2P1 | H3P1 |   " + NEWLINE
                + "                            |                                               |                            " + NEWLINE
                + "          ------------------|-----------------------------------------------|------------------          " + NEWLINE
                + "          |%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|          " + NEWLINE
                + "          |S44|S45|S46|S47|S0 |S1 |S2 |S3 |S4 |S5 |S6 |S7 |S8 |S9 |S10|S11|S12|S13|S14|S15|S16|          " + NEWLINE
                + "----------------------------|-----------------------------------------------|--------------------------------" + NEWLINE
                + "|         |                 |                                               |                 |         |" + NEWLINE
                + "| %3s  S43|                 |                                               |                 |S17 %3s  |" + NEWLINE
                + "|         |           |F0P0|F1P0|F2P0|F3P0|                   |F3P1|F2P1|F1P1|F0P1|           |         |" + NEWLINE
                + "|----------           |%3s |%3s |%3s |%3s |                   |%3s |%3s |%3s |%3s |           ----------|" + NEWLINE
                + "|         |                                                                                   |         |" + NEWLINE
                + "| %3s  S42|                                                                                   |S18 %3s  |" + NEWLINE
                + "|         |                                                                                   |         |" + NEWLINE
                + "|----------           |%3s |%3s |%3s |%3s |                   |%3s |%3s |%3s |%3s |           ----------|" + NEWLINE
                + "|         |           |F0P3|F0P3|F0P3|F0P3|                   |F3P2|F2P2|F1P2|F0P2|           |         |" + NEWLINE
                + "| %3s  S41|                 |                                               |                 |S19 %3s  |" + NEWLINE
                + "|         |                 |                                               |                 |         |" + NEWLINE
                + "----------------------------|-----------------------------------------------|----------------------------" + NEWLINE
                + "          |S40|S39|S38|S37|S36|S35|S34|S33|S32|S31|S30|S29|S28|S27|S26|S25|S24|S23|S22|S21|S20|          " + NEWLINE
                + "          |%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|%3s|          " + NEWLINE
                + "          ------------------|-----------------------------------------------|------------------          " + NEWLINE
                + "                            |                                               |                            " + NEWLINE
                + "   | H3P3 | H2P3 | H1P3 | H0P3 |                                         | H0P2 | H1P2 | H2P2 | H3P2 |   " + NEWLINE
                + "   | %3s  | %3s  | %3s  | %3s  |                                         | %3s  | %3s  | %3s  | %3s  |   " + NEWLINE;

    }
}
