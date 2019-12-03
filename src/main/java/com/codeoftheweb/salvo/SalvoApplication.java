package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GamePlayerRepository gamePlayerRepository, GameRepository gameRepository, ShipRepository shipRepository, SalvoRepository salvoRepository,ScoreRepository scoreRepository) {
		return (args) -> {
		    //inicializo players
			Player player1 = new Player("jack@gba.com",passwordEncoder().encode("pass1"));
			Player player2 = new Player("chloe@gba.com",passwordEncoder().encode("pass2"));
			Player player3 = new Player("kim@net.com",passwordEncoder().encode("pass3"));
			Player player4 = new Player("david@fff.com",passwordEncoder().encode("pass4"));
			Player player5 = new Player("michelle@gmail.com",passwordEncoder().encode("pass5"));
			Player player6 = new Player("t.almeida@ctu.gov",passwordEncoder().encode("pass6"));

			//guardo los players
			playerRepository.saveAll(Arrays.asList(player1,player2,player3,player4,player5,player6));

			//inicializo games y gameplayers y los guardo
			Game game1 = new Game();
			Date startDate1 = new Date();
			game1.setCreationDate(startDate1);
			GamePlayer gamePlayer1 = new GamePlayer(game1,player1);
			GamePlayer gamePlayer2 = new GamePlayer(game1,player5);

			Game game2 = new Game();
			Date startDate2 = Date.from(startDate1.toInstant().plusSeconds(3600));
			game2.setCreationDate(startDate2);
			GamePlayer gamePlayer3 = new GamePlayer(game2,player3);
			GamePlayer gamePlayer4 = new GamePlayer(game2,player2);

			Game game3 = new Game();
			Date startDate3 = Date.from(startDate2.toInstant().plusSeconds(3600));
			game3.setCreationDate(startDate3);
			GamePlayer gamePlayer5 = new GamePlayer(game3,player4);
			GamePlayer gamePlayer6 = new GamePlayer(game3,player6);

			Game game4 = new Game();
			Date startDate4 = Date.from(startDate3.toInstant().plusSeconds(3600));
			game4.setCreationDate(startDate4);
			GamePlayer gamePlayer7 = new GamePlayer(game4,player4);
			GamePlayer gamePlayer8 = new GamePlayer(game4,player5);

            Game game5 = new Game();
            Date startDate5 = Date.from(startDate4.toInstant().plusSeconds(3600));
            game5.setCreationDate(startDate5);
            GamePlayer gamePlayer9 = new GamePlayer(game5,player3);

            gameRepository.saveAll(Arrays.asList(game1,game2,game3,game4,game5));
            gamePlayerRepository.saveAll(Arrays.asList(gamePlayer1,gamePlayer2,gamePlayer3,gamePlayer4,gamePlayer5,gamePlayer6,gamePlayer7,gamePlayer8,gamePlayer9));

			//inicializo los ships con sus respectivas locaciones
            List<String> locations1 = Stream.of("H1", "H2", "H3","H4","H5").collect(Collectors.toList());
            List<String> locations2 = Stream.of("B1", "B2", "B3","B4").collect(Collectors.toList());
            List<String> locations3 = Stream.of("C1", "C2", "C3").collect(Collectors.toList());
            List<String> locations4 = Stream.of("D5", "D6").collect(Collectors.toList());
			List<String> locations5 = Stream.of("G6", "H6","I6").collect(Collectors.toList());
			List<String> locations6 = Stream.of("A10","B10").collect(Collectors.toList());
			List<String> locations7 = Stream.of("J8","J9","J10").collect(Collectors.toList());
			List<String> locations8 = Stream.of("H5","I5","J5").collect(Collectors.toList());
			List<String> locations9 = Stream.of("J2","J3","J4","J5","J6").collect(Collectors.toList());
			List<String> locations10 = Stream.of("C9","C10").collect(Collectors.toList());
			List<String> locations11 = Stream.of("A10","B10","C10","D10").collect(Collectors.toList());
			List<String> locations12 = Stream.of("J1","J2").collect(Collectors.toList());
			List<String> locations13 = Stream.of("G4","G5","G6").collect(Collectors.toList());
			List<String> locations14 = Stream.of("F1","F2","F3").collect(Collectors.toList());
			List<String> locations15 = Stream.of("F5","G5","H5").collect(Collectors.toList());
			List<String> locations16 = Stream.of("D1","E1","F1").collect(Collectors.toList());
			List<String> locations17 = Stream.of("H7","H8").collect(Collectors.toList());
			List<String> locations18 = Stream.of("C6","C7","C8","C9","C10").collect(Collectors.toList());
			List<String> locations19 = Stream.of("A8","B8","C8","D8","E8").collect(Collectors.toList());
			List<String> locations20 = Stream.of("D1","D2","D3","D4","D5").collect(Collectors.toList());
			List<String> locations21 = Stream.of("C1","D1","E1").collect(Collectors.toList());
			List<String> locations22 = Stream.of("I4","I5","I6","I7").collect(Collectors.toList());
			List<String> locations23 = Stream.of("J1","J2","J3","J4").collect(Collectors.toList());
			List<String> locations24 = Stream.of("G5","G6","G7").collect(Collectors.toList());

			Ship ship1 = new Ship(gamePlayer3, "carrier",locations1);
			Ship ship2 = new Ship(gamePlayer1,"battleship",locations2);
			Ship ship3 = new Ship(gamePlayer1,"destroyer",locations3);
			Ship ship4 = new Ship(gamePlayer4,"patrolboat",locations4);
			Ship ship5 = new Ship(gamePlayer2, "patrolboat",locations4);
			Ship ship6 = new Ship(gamePlayer5,"submarine",locations5);
			Ship ship7 = new Ship(gamePlayer6,"patrolboat",locations6);
			Ship ship8 = new Ship(gamePlayer7,"destroyer",locations7);
			Ship ship9 = new Ship(gamePlayer7,"submarine",locations8);
			Ship ship10 = new Ship(gamePlayer8,"carrier",locations9);
			Ship ship11 = new Ship(gamePlayer8,"patrolboat",locations10);
		    Ship ship12 = new Ship(gamePlayer3,"battleship",locations11);
            Ship ship13 = new Ship(gamePlayer3,"patrolboat",locations12);
            Ship ship14 = new Ship(gamePlayer3,"destroyer",locations13);
            Ship ship15 = new Ship(gamePlayer3,"submarine",locations14);
            Ship ship16 = new Ship(gamePlayer4,"submarine",locations15);
            Ship ship17 = new Ship(gamePlayer4,"destroyer",locations16);
            Ship ship18 = new Ship(gamePlayer4,"carrier",locations18);
            Ship ship19 = new Ship(gamePlayer4,"battleship",locations22);
            Ship ship20 = new Ship(gamePlayer5,"patrolboat",locations17);
            Ship ship21 = new Ship(gamePlayer5,"carrier",locations19);
            Ship ship22 = new Ship(gamePlayer5,"destroyer",locations21);
            Ship ship23 = new Ship(gamePlayer5,"battleship",locations23);
            Ship ship24 = new Ship(gamePlayer6,"carrier",locations20);
            Ship ship25 = new Ship(gamePlayer6,"submarine",locations24);
            Ship ship26 = new Ship(gamePlayer1,"patrolboat",locations12);
            Ship ship27 = new Ship(gamePlayer1,"submarine",locations15);
            Ship ship28 = new Ship(gamePlayer1,"carrier",locations20);
            Ship ship29 = new Ship(gamePlayer2,"carrier",locations9);
            Ship ship30 = new Ship(gamePlayer2,"submarine",locations8);
            Ship ship31 = new Ship(gamePlayer2,"battleship",locations11);
            Ship ship32 = new Ship(gamePlayer2,"destroyer",locations13);
            Ship ship33 = new Ship(gamePlayer6,"battleship",locations11);
            Ship ship34 = new Ship(gamePlayer6,"destroyer",locations13);
            Ship ship35 = new Ship(gamePlayer7,"patrolboat",locations12);
            Ship ship36 = new Ship(gamePlayer7,"carrier",locations20);
            Ship ship37 = new Ship(gamePlayer7,"battleship",locations22);
            Ship ship38 = new Ship(gamePlayer8,"destroyer",locations13);
            Ship ship39 = new Ship(gamePlayer8,"submarine",locations14);
            Ship ship40 = new Ship(gamePlayer8,"battleship",locations2);

			//asigno los ships a los gameplayers y guardo los ships
			gamePlayer3.addShip(ship1);
			gamePlayer1.addShip(ship2);
			gamePlayer1.addShip(ship3);
			gamePlayer1.addShip(ship26);
			gamePlayer1.addShip(ship27);
			gamePlayer1.addShip(ship28);
			gamePlayer2.addShip(ship29);
			gamePlayer2.addShip(ship30);
			gamePlayer2.addShip(ship31);
			gamePlayer2.addShip(ship32);
			gamePlayer4.addShip(ship4);
			gamePlayer2.addShip(ship5);
			gamePlayer5.addShip(ship6);
			gamePlayer6.addShip(ship7);
			gamePlayer7.addShip(ship8);
			gamePlayer7.addShip(ship9);
			gamePlayer8.addShip(ship10);
			gamePlayer8.addShip(ship11);
		    gamePlayer1.addShip(ship26);
            gamePlayer1.addShip(ship27);
            gamePlayer1.addShip(ship28);
            gamePlayer2.addShip(ship29);
            gamePlayer2.addShip(ship30);
            gamePlayer2.addShip(ship31);
            gamePlayer2.addShip(ship32);
            gamePlayer3.addShip(ship12);
            gamePlayer3.addShip(ship13);
            gamePlayer3.addShip(ship14);
            gamePlayer3.addShip(ship15);
            gamePlayer4.addShip(ship16);
            gamePlayer4.addShip(ship17);
            gamePlayer4.addShip(ship18);
            gamePlayer4.addShip(ship19);
            gamePlayer5.addShip(ship20);
            gamePlayer5.addShip(ship21);
            gamePlayer5.addShip(ship22);
            gamePlayer5.addShip(ship23);
            gamePlayer6.addShip(ship24);
            gamePlayer6.addShip(ship25);
            gamePlayer6.addShip(ship33);
            gamePlayer6.addShip(ship34);
            gamePlayer7.addShip(ship35);
            gamePlayer7.addShip(ship36);
            gamePlayer7.addShip(ship37);
            gamePlayer8.addShip(ship38);
            gamePlayer8.addShip(ship39);
            gamePlayer8.addShip(ship40);


			shipRepository.saveAll(Arrays.asList(ship1,ship2,ship3,ship4,ship5,ship6,ship7,ship8,ship9,ship10,ship11,ship12,ship13,ship14,ship15,ship16,ship17,ship18,ship19,ship20,ship21,ship22,ship23,ship24,ship25,ship26,ship27,ship28,ship29,ship29,ship30,ship31,ship32,ship33,ship34,ship35,ship36,ship37,ship38,ship39,ship40));


			//inicializo los salvos, los guardo y se los asigno a los players
			//primer turno
            List<String> salvoLocations1 = Stream.of("A1", "B7", "H3","J2","C8").collect(Collectors.toList());
            List<String> salvoLocations2 = Stream.of("F5", "G5", "H5","B1","B2").collect(Collectors.toList());
            List<String> salvoLocations3 = Stream.of("I4","I5","I6","I7","D1").collect(Collectors.toList());
            List<String> salvoLocations4 = Stream.of("A10", "B10", "C10","D10","F1").collect(Collectors.toList());
			List<String> salvoLocations5 = Stream.of("H2", "F4", "C8","A9","B7").collect(Collectors.toList());
			List<String> salvoLocations6 = Stream.of("I2", "E6", "D3","F1","G10").collect(Collectors.toList());
			List<String> salvoLocations7 = Stream.of("B4", "G6", "B9","I3","H1").collect(Collectors.toList());
			List<String> salvoLocations8 = Stream.of("G3", "A1", "I9","B4","J5").collect(Collectors.toList());

			Salvo salvo1 = new Salvo(gamePlayer1,salvoLocations1,1);
            Salvo salvo2 = new Salvo(gamePlayer2,salvoLocations2,1);
            Salvo salvo3 = new Salvo(gamePlayer3,salvoLocations3,1);
            Salvo salvo4 = new Salvo(gamePlayer4,salvoLocations4,1);
            Salvo salvo5 = new Salvo(gamePlayer5,salvoLocations5,1);
            Salvo salvo6 = new Salvo(gamePlayer6,salvoLocations6,1);
			Salvo salvo7 = new Salvo(gamePlayer7,salvoLocations7,1);
			Salvo salvo8 = new Salvo(gamePlayer8,salvoLocations8,1);

            //segundo turno
			List<String> salvoLocations9 = Stream.of("I6", "E4", "D1","F9","C7").collect(Collectors.toList());
			List<String> salvoLocations10 = Stream.of("B3", "B4", "C1","C2","C3").collect(Collectors.toList());
            List<String> salvoLocations11 = Stream.of("E1", "F1", "C6","C7","C8").collect(Collectors.toList());
            List<String> salvoLocations12 = Stream.of("F2", "F3", "G4","G5","G6").collect(Collectors.toList());
			Salvo salvo9 = new Salvo(gamePlayer1,salvoLocations9,2);
			Salvo salvo10 = new Salvo(gamePlayer2,salvoLocations10,2);
			Salvo salvo11 = new Salvo(gamePlayer3,salvoLocations11,2);
			Salvo salvo12 = new Salvo(gamePlayer4,salvoLocations12,2);

			//tercer turno
            List<String> salvoLocations13 = Stream.of("H2", "B5", "I7","H10","G1").collect(Collectors.toList());
            List<String> salvoLocations14 = Stream.of("D1", "D2", "D3","D4","D5").collect(Collectors.toList());
            List<String> salvoLocations15 = Stream.of("C9", "C10", "D5","D6","F5").collect(Collectors.toList());
            List<String> salvoLocations16 = Stream.of("H1", "H2", "H3","H4","H5").collect(Collectors.toList());
            Salvo salvo13 = new Salvo(gamePlayer1,salvoLocations13,3);
            Salvo salvo14 = new Salvo(gamePlayer2,salvoLocations14,3);
            Salvo salvo15 = new Salvo(gamePlayer3,salvoLocations15,3);
            Salvo salvo16 = new Salvo(gamePlayer4,salvoLocations16,3);

            //cuarto turno
            List<String> salvoLocations17 = Stream.of("F3", "D9", "I9","J6","E3").collect(Collectors.toList());
            List<String> salvoLocations18 = Stream.of("J1", "J2", "J3","J4","J5").collect(Collectors.toList());
            List<String> salvoLocations19 = Stream.of("G5", "H5", "I10","J2","G1").collect(Collectors.toList());
            List<String> salvoLocations20 = Stream.of("J1", "J2", "I7","B7","A1").collect(Collectors.toList());
            Salvo salvo17 = new Salvo(gamePlayer1,salvoLocations17,4);
            Salvo salvo18 = new Salvo(gamePlayer2,salvoLocations18,4);
            Salvo salvo19 = new Salvo(gamePlayer3,salvoLocations19,4);
            Salvo salvo20 = new Salvo(gamePlayer4,salvoLocations20,4);

            salvoRepository.saveAll(Arrays.asList(salvo1,salvo2,salvo3,salvo4,salvo5,salvo6,salvo7,salvo8,salvo9,salvo10,salvo11,salvo12,salvo13,salvo14,salvo15,salvo16,salvo17,salvo18,salvo19,salvo20));
            //genero los scores de los games
			Date finishDate1 = Date.from(startDate1.toInstant().plusSeconds(1800));
			Score score1 = new Score(game1,player1,0,finishDate1);
			Score score2 = new Score(game1,player5,1,finishDate1);
			Date finishDate2 = Date.from(startDate2.toInstant().plusSeconds(1800));
			Score score3 = new Score(game2,player3,0.5,finishDate2);
			Score score4 = new Score(game2,player2,0.5,finishDate2);
			scoreRepository.saveAll(Arrays.asList(score1,score2,score3,score4));


		};
	}

}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			Player player = playerRepository.findByUserName(inputName);
			if (player != null) {
				return new User(player.getUserName(),player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/web/games.html").permitAll()
				.antMatchers("/web/**").permitAll()
				.antMatchers("/api/login","/api/logout").permitAll()
				.antMatchers("/api/games").permitAll()
				.antMatchers("/api/players").permitAll()
				.antMatchers("/web/game.html?gp=*", "/api/game_view/*").hasAuthority("USER")
				.antMatchers("/api/games/players/{gamePlayerId}/ships").hasAuthority("USER")
				//.antMatchers("/rest").denyAll()
				.antMatchers("/api/games/players/{gamePlayerId}/salvoes").hasAuthority("USER")
				.antMatchers("/api/game/{gameId}/players").hasAuthority("USER")
				.anyRequest().denyAll();

		http.formLogin()
				.usernameParameter("name")
				.passwordParameter("pwd")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");

		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}
	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}

}