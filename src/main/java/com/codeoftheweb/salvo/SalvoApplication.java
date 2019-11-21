package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GamePlayerRepository gamePlayerRepository, GameRepository gameRepository, ShipRepository shipRepository, SalvoRepository salvoRepository,ScoreRepository scoreRepository) {
		return (args) -> {
		    //inicializo players
			Player player1 = new Player("jack@gba.com");
			Player player2 = new Player("chloe@gba.com");
			Player player3 = new Player("kim@net.com");
			Player player4 = new Player("david@fff.com");
			Player player5 = new Player("michelle@gmail.com");
			Player player6 = new Player("t.almeida@ctu.gov");

			//guardo los players
			playerRepository.saveAll(Arrays.asList(player1,player2,player3,player4,player5,player6));

			//inicializo games y gameplayers y los guardo
			Game game1 = new Game();
			Date startDate1 = new Date();
			game1.setCreationDate(startDate1);
			gameRepository.save(game1);
			GamePlayer gamePlayer1 = new GamePlayer(game1,player1);
			GamePlayer gamePlayer2 = new GamePlayer(game1,player5);
			gamePlayerRepository.saveAll(Arrays.asList(gamePlayer1,gamePlayer2));

			Game game2 = new Game();
			Date startDate2 = Date.from(startDate1.toInstant().plusSeconds(3600));
			game2.setCreationDate(startDate2);
			gameRepository.save(game2);
			GamePlayer gamePlayer3 = new GamePlayer(game2,player3);
			GamePlayer gamePlayer4 = new GamePlayer(game2,player2);
			gamePlayerRepository.saveAll(Arrays.asList(gamePlayer3,gamePlayer4));

			Game game3 = new Game();
			Date startDate3 = Date.from(startDate2.toInstant().plusSeconds(3600));
			game3.setCreationDate(startDate3);
			gameRepository.save(game3);
			GamePlayer gamePlayer5 = new GamePlayer(game3,player4);
			GamePlayer gamePlayer6 = new GamePlayer(game3,player6);
			gamePlayerRepository.saveAll(Arrays.asList(gamePlayer5,gamePlayer6));

			//inicializo los ships con sus respectivas locaciones
            Set<String> locations1 = Stream.of("H1", "H2", "H3","H4","H5").collect(Collectors.toSet());
            Set<String> locations2 = Stream.of("B1", "B2", "B3","B4").collect(Collectors.toSet());
            Set<String> locations3 = Stream.of("C1", "C2", "C3").collect(Collectors.toSet());
            Set<String> locations4 = Stream.of("D5", "D6").collect(Collectors.toSet());
			Set<String> locations5 = Stream.of("G6", "H6","I6").collect(Collectors.toSet());
			Set<String> locations6 = Stream.of("A10","B10").collect(Collectors.toSet());

			Ship ship1 = new Ship(gamePlayer3, "Carrier",locations1);
			Ship ship2 = new Ship(gamePlayer1,"Battleship",locations2);
			Ship ship3 = new Ship(gamePlayer1,"Destroyer",locations3);
			Ship ship4 = new Ship(gamePlayer4,"Patrol Boat",locations4);
			Ship ship5 = new Ship(gamePlayer2, "Patrol Boat",locations4);
			Ship ship6 = new Ship(gamePlayer5,"Submarine",locations5);
			Ship ship7 = new Ship(gamePlayer6,"Patrol boat",locations6);

			//asigno los ships a los gameplayers y guardo los ships
			gamePlayer3.addShip(ship1);
			gamePlayer1.addShip(ship2);
			gamePlayer1.addShip(ship3);
			gamePlayer4.addShip(ship4);
			gamePlayer2.addShip(ship5);
			gamePlayer5.addShip(ship6);
			gamePlayer6.addShip(ship7);
			shipRepository.saveAll(Arrays.asList(ship1,ship2,ship3,ship4,ship5,ship6,ship7));

			//inicializo los salvos, los guardo y se los asigno a los players
			//primer turno
            Set<String> salvoLocations1 = Stream.of("A1", "B7", "H3","J2","C8").collect(Collectors.toSet());
            Set<String> salvoLocations2 = Stream.of("F6", "D5", "E9","B2","I4").collect(Collectors.toSet());
            Set<String> salvoLocations3 = Stream.of("C7", "H1", "J9","A10","D2").collect(Collectors.toSet());
            Set<String> salvoLocations4 = Stream.of("F10", "I5", "G7","E2","G4").collect(Collectors.toSet());
			Set<String> salvoLocations5 = Stream.of("H2", "F4", "C8","A9","B7").collect(Collectors.toSet());
			Set<String> salvoLocations6 = Stream.of("I2", "E6", "D3","F1","G10").collect(Collectors.toSet());


			Salvo salvo1 = new Salvo(gamePlayer1,salvoLocations1,1);
            Salvo salvo2 = new Salvo(gamePlayer2,salvoLocations2,1);
            Salvo salvo3 = new Salvo(gamePlayer3,salvoLocations3,1);
            Salvo salvo4 = new Salvo(gamePlayer4,salvoLocations4,1);
            Salvo salvo5 = new Salvo(gamePlayer5,salvoLocations5,1);
            Salvo salvo6 = new Salvo(gamePlayer6,salvoLocations6,1);
			salvoRepository.saveAll(Arrays.asList(salvo1,salvo2,salvo3,salvo4,salvo5,salvo6));

            //segundo turno
			Set<String> salvoLocations7 = Stream.of("I6", "E4", "D1","F9","C7").collect(Collectors.toSet());
			Set<String> salvoLocations8 = Stream.of("C1", "G8", "A4","H10","I6").collect(Collectors.toSet());
			Salvo salvo7 = new Salvo(gamePlayer1,salvoLocations7,2);
			Salvo salvo8 = new Salvo(gamePlayer2,salvoLocations8,2);
			salvoRepository.saveAll(Arrays.asList(salvo7,salvo8));

			//genero los scores de los games
			Date finishDate1 = Date.from(startDate1.toInstant().plusSeconds(1800));;
			Score score1 = new Score(game1,player1,0,finishDate1);  //deberia modificar los del inicio
			Score score2 = new Score(game1,player5,1,finishDate1);
			Date finishDate2 = Date.from(startDate2.toInstant().plusSeconds(1800));
			Score score3 = new Score(game2,player3,0.5,finishDate2);
			Score score4 = new Score(game2,player2,0.5,finishDate2);
			scoreRepository.saveAll(Arrays.asList(score1,score2,score3,score4));

		};
	}


}
