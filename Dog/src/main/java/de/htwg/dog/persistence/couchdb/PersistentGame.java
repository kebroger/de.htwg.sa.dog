package de.htwg.dog.persistence.couchdb;

import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

import java.util.ArrayList;
import java.util.List;

public class PersistentGame extends CouchDbDocument {

	@TypeDiscriminator
	public String id;

	public List<PersistentPlayer> players = new ArrayList<>();

	public int cardsPerHand;
	public int currentPlayerNo;

	public String info = "";
	public String name = "";

	public int playerCnt;
	public int squareCnt;
	public int tokenCnt;

	public PersistentGame() {
	}
}
