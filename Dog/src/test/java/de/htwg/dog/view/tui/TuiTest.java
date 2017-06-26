/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htwg.dog.view.tui;

import de.htwg.dog.controller.impl.Controller;
import de.htwg.dog.model.impl.ModelFactory;
import org.junit.Before;

/**
 *
 * @author kev
 */
public class TuiTest {
    
    Tui tui = new Tui(new Controller(new ModelFactory(), null));
    
    public TuiTest() {
    }
    
    @Before
    public void setUp() {
        tui.processInput("neu");
    }
    
}
