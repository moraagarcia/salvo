package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

 //lo nuevo agregado
    @OneToMany(mappedBy ="gamePlayer", fetch = FetchType.EAGER) //DUDAA hay que dejar el mappedby o sacarlo
    private Set<Ship> ships;

    private Date joinDate;

    public GamePlayer(){
    }

    public GamePlayer(Game game,Player player){  //deberia pasar los ships por aca tambien??
        this.joinDate = new Date();
        this.game = game;
        this.player = player;
        this.ships = new HashSet<>();
    }

    public void addShip(Ship ship) {
        ships.add(ship);
    }

    public long getId() {
        return id;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Map<String, Object> makePlayerDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("player", this.getPlayer().getPlayerData());
        return dto;
    }

}
