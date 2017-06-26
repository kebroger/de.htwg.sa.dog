package de.htwg.dog.persistence.couchdb;

import org.ektorp.support.CouchDbDocument;

import java.util.ArrayList;
import java.util.List;

public class PersistentPlayer extends CouchDbDocument {

    public Integer playerNumber = 0;
    public List<String> cards = new ArrayList<>();
    public List<String> occupiedSquares = new ArrayList<>();

    public PersistentPlayer() {
    }
}
