package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gameplayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="ship_location")
    private Set<String> shipLocations;
    private String type;


    public Ship(){
    }

    public Ship(GamePlayer gamePlayer, String type, Set<String> locations){
        this.gamePlayer = gamePlayer;
        this.type = type;
        this.shipLocations = locations;
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public Set<String> getLocations() {
        return shipLocations;
    }

    public void setLocations(Set<String> locations) {
        this.shipLocations = locations;
    }

    public Map<String, Object> getShipData(){
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("type", type);
        map.put("locations", shipLocations.stream().collect(Collectors.toList()));
        return map;
    }
}
