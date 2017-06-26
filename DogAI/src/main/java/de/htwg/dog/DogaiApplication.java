package de.htwg.dog;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwg.dog.model.ICard;
import de.htwg.dog.model.IModel;
import de.htwg.dog.model.IPlayer;
import de.htwg.dog.model.ISquare;
import de.htwg.dog.model.impl.Draw;
import de.htwg.dog.model.impl.Game;
import de.htwg.dog.model.impl.ValueEnum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.List;


@SpringBootApplication
public class DogaiApplication {

	public static void main(String[] args) {
        System.setProperty("server.max-http-header-size","60000");
		SpringApplication.run(DogaiApplication.class, args);
	}
}

@RestController
@RequestMapping(value="/rest/")
class AiService{

	@RequestMapping(value="/",method = RequestMethod.GET)
	public String getNextDraw(@RequestParam("game") String json){
        Game game = null;
        ObjectMapper objectMapper = new ObjectMapper();

        try
        {
            game = objectMapper.readValue(json, Game.class);

        } catch (IOException e){
            System.out.println(e.getMessage());
        }


        IPlayer player = game.getCurrentPlayer();

        for (ISquare fromSquare : player.getHomeSquares()) {
            for (ICard card: player.getCards()) {
                String valStr = card.toString().substring(card.toString().indexOf("_") + 1);
                ValueEnum val = ValueEnum.fromString(valStr);

                if (Draw.isDrawAllowed(fromSquare, player.getStartSquare(), val, player)) {
                    return String.join(",", new String[]{fromSquare.getName(), player.getStartSquare().getName(), card.getName()});
                }
            }
        }

        List<ISquare> squares = game.getSquares();
        Collections.reverse(squares);

        for (ISquare fromSquare: squares) {
            if(fromSquare.getName().startsWith("F") || fromSquare.getName().startsWith("H"))
                continue;
            if(fromSquare.getToken() == player.getPlayerNumber())
            {
                for (ICard card: player.getCards()) {
                    String valStr = card.toString().substring(card.toString().indexOf("_") + 1);
                    ValueEnum val = ValueEnum.fromString(valStr);

                    for (ISquare toSquare : player.getFinishSquares()) {
                        if(Draw.isDrawAllowed(fromSquare, toSquare, val, player))
                        {
                            return String.join(",", new String[]{fromSquare.getName(), toSquare.getName(), card.getName()});
                        }
                    }

                    for (ISquare toSquare : game.getSquares()) {
                        if(Draw.isDrawAllowed(fromSquare, toSquare, val, player))
                        {
                            return String.join(",", new String[]{fromSquare.getName(), toSquare.getName(), card.getName()});
                        }
                    }
                }
            }
        }

        return "";
	}
}
