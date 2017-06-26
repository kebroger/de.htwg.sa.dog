package de.htwg.dog.persistence.hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Player")
public class PersistentPlayer implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Column(name = "PlayerNumber")
	public Integer playerNumber;

	@ElementCollection
	@CollectionTable(name="Cards", joinColumns=@JoinColumn(name="PlayerID"))
    public List<String> cards = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name="OccupiedSquares", joinColumns=@JoinColumn(name="PlayerID"))
	public List<String> occupiedSquares = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "GameID")
	public PersistentGame game;

	public PersistentPlayer() {
	}
}
