package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private int turn;

    @ElementCollection
    @Column(name="salvoes_location")
    private List<String> salvoesLocations;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    public Salvo(){
    }
    public Salvo(GamePlayer gamePlayer, List<String> salvoesLocations,int turn){
        this.gamePlayer = gamePlayer;
        this.salvoesLocations = salvoesLocations;
        this.turn = turn;
    }

    public Map<String, Object> makeSalvoDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turn", this.turn);
        dto.put("player", gamePlayer.getPlayer().getId());
        dto.put("locations",this.salvoesLocations.stream().collect(Collectors.toList()));
        return dto;
    }

    public long getId() {
        return id;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public List<String> getSalvoesLocations() {
        return salvoesLocations;
    }

    public void setSalvoesLocations(List<String> salvoesLocations) {
        this.salvoesLocations = salvoesLocations;
    }


}