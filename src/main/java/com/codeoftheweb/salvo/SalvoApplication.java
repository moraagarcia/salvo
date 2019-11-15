package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GamePlayerRepository gamePlayerRepository, GameRepository gameRepository, ShipRepository shipRepository, SalvoRepository salvoRepository) {
		return (args) -> {
		    //inicializo players
			Player player1 = new Player("jack@gba.com");
			Player player2 = new Player("chloe@gba.com");
			Player player3 = new Player("kim@net.com");
			Player player4 = new Player("david@fff.com");
			Player player5 = new Player("michelle@gmail.com");

			//guardo los players
			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);
			playerRepository.save(player5);

			//inicializo games y gameplayers y los guardo
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

			//inicializo los ships con sus respectivas locaciones
            Set<String> locations1 = Stream.of("H1", "H2", "H3","H4","H5").collect(Collectors.toSet());
            Set<String> locations2 = Stream.of("B1", "B2", "B3","B4").collect(Collectors.toSet());
            Set<String> locations3 = Stream.of("C1", "C2", "C3").collect(Collectors.toSet());
            Set<String> locations4 = Stream.of("D5", "D6").collect(Collectors.toSet());
			Ship ship1 = new Ship(gamePlayer3, "Carrier",locations1);
			Ship ship2 = new Ship(gamePlayer1,"Battleship",locations2);
			Ship ship3 = new Ship(gamePlayer1,"Destroyer",locations3);
			Ship ship4 = new Ship(gamePlayer4,"Patrol Boat",locations4);
			Ship ship5 = new Ship(gamePlayer2, "Patrol Boat",locations4);

			//asigno los ships a los gameplayers y guardo los ships
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

			//inicializo los salvos, los guardo y se los asigno a los players
			//primer turno
            Set<String> salvoLocations1 = Stream.of("A1", "B7", "H3","J2","C8").collect(Collectors.toSet());
            Set<String> salvoLocations2 = Stream.of("F6", "D5", "E9","B2","I4").collect(Collectors.toSet());
            Set<String> salvoLocations3 = Stream.of("C7", "H1", "J9","A10","D2").collect(Collectors.toSet());
            Set<String> salvoLocations4 = Stream.of("F10", "I5", "G7","E2","G4").collect(Collectors.toSet());
            Salvo salvo1 = new Salvo(gamePlayer1,salvoLocations1,1);
            Salvo salvo2 = new Salvo(gamePlayer2,salvoLocations2,1);
            Salvo salvo3 = new Salvo(gamePlayer3,salvoLocations3,1);
            Salvo salvo4 = new Salvo(gamePlayer4,salvoLocations4,1);
            salvoRepository.save(salvo1);
            salvoRepository.save(salvo2);
            salvoRepository.save(salvo3);
            salvoRepository.save(salvo4);

            //segundo turno
			Set<String> salvoLocations5 = Stream.of("I6", "E4", "D1","F9","C7").collect(Collectors.toSet());
			Set<String> salvoLocations6 = Stream.of("C1", "G8", "A4","H10","I6").collect(Collectors.toSet());
			Salvo salvo5 = new Salvo(gamePlayer1,salvoLocations5,2);
			Salvo salvo6 = new Salvo(gamePlayer2,salvoLocations6,2);
			salvoRepository.save(salvo5);
			salvoRepository.save(salvo6);
		};
	}


}
