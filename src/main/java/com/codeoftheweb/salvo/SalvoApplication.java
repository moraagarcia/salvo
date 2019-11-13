package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.models.Game;
import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Player;
import com.codeoftheweb.salvo.repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.repositories.GameRepository;
import com.codeoftheweb.salvo.repositories.PlayerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GamePlayerRepository gamePlayerRepository, GameRepository gameRepository) {
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
			gamePlayerRepository.save(gamePlayer3);

		};
	}


}
