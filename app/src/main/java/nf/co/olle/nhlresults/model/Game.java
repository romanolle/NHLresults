package nf.co.olle.nhlresults.model;

import java.time.ZonedDateTime;

public class Game {
    private final Team homeTeam;
    private final Team awayTeam;
    private final Integer homeScore;
    private final Integer awayScore;
    private final GameStatus gameStatus;
    private final ZonedDateTime startGameTime;

    public Game(Team homeTeam, Team awayTeam, Integer homeScore, Integer awayScore, GameStatus gameStatus, ZonedDateTime startGameTime) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.gameStatus = gameStatus;
        this.startGameTime = startGameTime;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public Integer getAwayScore() {
        return awayScore;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public ZonedDateTime getStartGameTime() {
        return startGameTime;
    }
}
