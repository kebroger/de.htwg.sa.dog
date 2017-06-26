package de.htwg.dog.persistence.hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Game")
public class PersistentGame implements Serializable {

	@Id
	@Column(name = "id")
	public String id;

	@OneToMany(mappedBy = "game")
	public List<PersistentPlayer> players = new ArrayList<>();

	@Column(name = "CardsPerHand")
	public int cardsPerHand;

	@Column(name = "CurrentPlayerNo")
	public int currentPlayerNo;

	@Column(name = "Info")
	public String info = "";

	@Column(name = "Name")
	public String name = "";

	@Column(name = "PlayerCnt")
	public int playerCnt;

	@Column(name = "SquareCnt")
	public int squareCnt;

	@Column(name = "TokenCnt")
	public int tokenCnt;
	
	public PersistentGame() {
	}
}
