package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.models.Player;
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
	public CommandLineRunner initData(PlayerRepository repository) {
		return (args) -> {
			repository.save(new Player("jack@gba.com"));
			repository.save(new Player("chloe@gba.com"));
			repository.save(new Player("kim@net.com"));
			repository.save(new Player("david@fff.com"));
			repository.save(new Player("michelle@gmail.com"));
		};
	}
}
