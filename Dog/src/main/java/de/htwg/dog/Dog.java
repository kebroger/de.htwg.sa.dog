/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.htwg.dog;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import de.htwg.dog.controller.IController;
import de.htwg.dog.view.gui.Gui;
import de.htwg.dog.view.tui.Tui;
import org.apache.log4j.PropertyConfigurator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Scanner;
import java.util.List;

/**
 *
 * @author kev
 */
public final class Dog {

    private static Scanner scanner;
    private Tui tui;
    private Gui gui;
    protected IController controller;
    private static Dog instance;

    static {
        instance = null;
    }

    private Dog() {
        // Set up logging through log4j
        PropertyConfigurator.configure(this.getClass().getClassLoader().getResource("log4j.properties"));

        Injector injector = Guice.createInjector((Module[])new Module[]{new DogModule()});
        this.controller = injector.getInstance(IController.class);

        this.gui = injector.getInstance(Gui.class);
        this.tui = injector.getInstance(Tui.class);

        this.controller.startGame();
    }

    public static Dog getInstance() {
        if (instance == null) {
            instance = new Dog();
        }
        return instance;
    }

    public IController getController() {
        return this.controller;
    }
    public Tui getTui() {
        return this.tui;
    }

    public static void main(String[] args) {
        Dog game = Dog.getInstance();

        boolean continu = true;
        Scanner scanner = new Scanner(System.in);
        while (continu) {
            continu = game.tui.processInput(scanner.next());
        }

        game.gui.dispose();
    }
}
