package nf.co.olle.nhlresults.model.builder;

import java.time.ZonedDateTime;

import nf.co.olle.nhlresults.model.Game;
import nf.co.olle.nhlresults.model.GameStatus;
import nf.co.olle.nhlresults.model.Team;

public class GameBuilder {
    private Team homeTeam;
    private Team awayTeam;
    private Integer homeScore;
    private Integer awayScore;
    private GameStatus gameStatus;
    private ZonedDateTime startGameTime;

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    public void setAwayScore(Integer awayScore) {
        this.awayScore = awayScore;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public void setStartGameTime(ZonedDateTime startGameTime) {
        this.startGameTime = startGameTime;
    }

    public Game build() {
        return new Game(homeTeam, awayTeam, homeScore, awayScore, gameStatus, startGameTime);
    }
}
