package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.models.Game;
import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Player;
import com.codeoftheweb.salvo.models.Ship;
import com.codeoftheweb.salvo.repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.repositories.GameRepository;
import com.codeoftheweb.salvo.repositories.PlayerRepository;
import com.codeoftheweb.salvo.repositories.ShipRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GamePlayerRepository gamePlayerRepository, GameRepository gameRepository, ShipRepository shipRepository) {
		return (args) -> {
			Player player1 = new Player("jack@gba.com");
			Player player2 = new Player("chloe@gba.com");
			Player player3 = new Player("kim@net.com");
			Player player4 = new Player("david@fff.com");
			Player player5 = new Player("michelle@gmail.com");
			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);
			playerRepository.save(player5);

			Game game1 = new Game();
			gameRepository.save(game1);
			GamePlayer gamePlayer1 = new GamePlayer(game1,player1);
			GamePlayer gamePlayer2 = new GamePlayer(game1,player5);
			gamePlayerRepository.save(gamePlayer1);
			gamePlayerRepository.save(gamePlayer2);

			Game game2 = new Game();
			gameRepository.save(game2);
			GamePlayer gamePlayer3 = new GamePlayer(game2,player3);
			GamePlayer gamePlayer4 = new GamePlayer(game2,player2);
			gamePlayerRepository.save(gamePlayer3);
			gamePlayerRepository.save(gamePlayer4);

			Set<String> locations1 = new HashSet<>();
			Set<String> locations2 = new HashSet<>();
			Set<String> locations3 = new HashSet<>();
			Set<String> locations4 = new HashSet<>();
			locations1.add("H1");
			locations1.add("H2");
			locations1.add("H3");
			locations1.add("H4");
			locations1.add("H5");
			locations2.add("B1");
			locations2.add("B2");
			locations2.add("B3");
			locations2.add("B4");
			locations3.add("C1");
			locations3.add("C2");
			locations3.add("C3");
			locations4.add("D5");
			locations4.add("D6");
			Ship ship1 = new Ship(gamePlayer3, "Carrier",locations1);
			Ship ship2 = new Ship(gamePlayer1,"Battleship",locations2);
			Ship ship3 = new Ship(gamePlayer1,"Destroyer",locations3);
			Ship ship4 = new Ship(gamePlayer4,"Patrol Boat",locations4);
			Ship ship5 = new Ship(gamePlayer2, "Patrol Boat",locations4);
			gamePlayer3.addShip(ship1);
			gamePlayer1.addShip(ship2);
			gamePlayer1.addShip(ship3);
			gamePlayer4.addShip(ship4);
			gamePlayer2.addShip(ship5);
			shipRepository.save(ship1);
			shipRepository.save(ship2);
			shipRepository.save(ship3);
			shipRepository.save(ship4);
			shipRepository.save(ship5);

		};
	}


}
