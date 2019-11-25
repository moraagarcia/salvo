package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<Score> scores;

    private String userName;
    private String password;

    public Player() {
    }

    public Player(String userName,String password) {
        this.userName = userName;
        this.scores = new LinkedHashSet<>();
        this.password = password;
    }

    public Map<String, Object> makePlayerDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("email", this.getUserName());
        return dto;
    }

    public Map<String,Object> getScoreDTO(Game game){
        Score score = this.getScore(game);
        if (score == null) return null;
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("player",id);
        dto.put("score", score.getScore());
        dto.put("finishDate", score.getFinishDate());
        return dto;
    }

    public Score getScore(Game game){
        List<Score> listScores = scores.stream().filter(score -> score.getGame().getId() == game.getId()).collect(Collectors.toList());
        if (listScores.isEmpty()) return null;
        return listScores.get(0);
    }

    public long getTotalWinCount(){                                  //puedo poner aca Collectors.counting()?? me va a pisar mi Scores??
        return scores.stream().filter(score -> score.getScore() == 1 ).count();
    }

    public long getTotalLossCount(){
        return scores.stream().filter(score -> score.getScore() == 0 ).count();
    }

    public long getTotalTieCount(){
        return scores.stream().filter(score -> score.getScore() == 0.5 ).count();
    }

    public double getTotalScore(){
        double totalScore = 0;
        for (Score score:scores) {
            totalScore+= score.getScore();
        }
        return totalScore;
    }

    public Map<String,Object> makeLeaderBoardDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("player", userName);
        dto.put("total score", this.getTotalScore());
        dto.put("wins", this.getTotalWinCount());
        dto.put("losses", this.getTotalLossCount());
        dto.put("tieds", this.getTotalTieCount());
        return dto;
    }

    public long getId() {
        return id;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public Set<Score> getScores() {
        return scores;
    }

}
