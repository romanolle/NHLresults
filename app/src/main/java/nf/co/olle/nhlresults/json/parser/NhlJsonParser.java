package nf.co.olle.nhlresults.json.parser;

import android.app.Activity;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import nf.co.olle.nhlresults.model.Game;
import nf.co.olle.nhlresults.model.GameStatus;
import nf.co.olle.nhlresults.model.Team;
import nf.co.olle.nhlresults.model.builder.GameBuilder;
import nf.co.olle.nhlresults.model.builder.GameStatusBuilder;
import nf.co.olle.nhlresults.model.builder.TeamBuilder;

public class NhlJsonParser {

    private static final String DATES_KEY = "dates";
    private static final String GAMES_KEY = "games";
    private static final String GAME_DATE_KEY = "gameDate";
    private static final String STATUS_KEY = "status";
    private static final String DETAILED_STATE_KEY = "detailedState";
    private static final String STATUS_CODE_KEY = "statusCode";
    private static final String GAME_STATE_KEY = "codedGameState";
    private static final String TEAMS_KEY = "teams";
    private static final String AWAY_KEY = "away";
    private static final String HOME_KEY = "home";
    private static final String SCORE_KEY = "score";
    private static final String TEAM_KEY = "team";
    private static final String TEAM_ID_KEY = "id";
    private static final String TEAM_NAME_KEY = "name";

    ArrayList<HashMap<String, String>> arraylist;

    public NhlJsonParser(){
    }

    public ArrayList<Game> parse(String response) {
        ArrayList<Game> games = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);

            arraylist = new ArrayList<HashMap<String, String>>();
            JSONArray datesArray = jsonObject.getJSONArray(DATES_KEY);

            for (int i = 0; i < datesArray.length(); i++) {
                games.addAll(parseDates(datesArray.getJSONObject(i)));
            }




        } catch (JSONException e) {
            e.printStackTrace();
        }
        return games;
    }

    private Collection<Game> parseDates(JSONObject dateObj) throws JSONException {
        ArrayList<Game> games = new ArrayList<>();

        JSONArray gamesArray = dateObj.getJSONArray(GAMES_KEY);

        for (int i = 0; i < gamesArray.length(); i++) {
            games.add(parseGames(gamesArray.getJSONObject(i)));
        }
        return games;
    }

    private Game parseGames(JSONObject gameObj) throws JSONException {

        GameBuilder gameBuilder = new GameBuilder();

        JSONObject teamsObj = gameObj.getJSONObject(TEAMS_KEY);
        JSONObject homeObj = teamsObj.getJSONObject(HOME_KEY);
        JSONObject awayObj = teamsObj.getJSONObject(AWAY_KEY);

        gameBuilder.setHomeTeam(parseTeam(homeObj.getJSONObject(TEAM_KEY)));
        gameBuilder.setAwayTeam(parseTeam(awayObj.getJSONObject(TEAM_KEY)));

        gameBuilder.setGameStatus(parseGameStatus(gameObj.getJSONObject(STATUS_KEY)));

        gameBuilder.setHomeScore(homeObj.getInt(SCORE_KEY));
        gameBuilder.setAwayScore(awayObj.getInt(SCORE_KEY));

        //TODO date
        //ZonedDateTime.parse(gameObj.getString(GAME_DATE_KEY));

        return gameBuilder.build();
    }

    private Team parseTeam(JSONObject teamObj) throws JSONException {
        TeamBuilder teamBuilder = new TeamBuilder();

        teamBuilder.setId(teamObj.getInt(TEAM_ID_KEY));
        teamBuilder.setName(teamObj.getString(TEAM_NAME_KEY));

        return teamBuilder.build();
    }

    private GameStatus parseGameStatus(JSONObject statusObj) throws JSONException {
        GameStatusBuilder statusBuilder = new GameStatusBuilder();

        statusBuilder.setStatusCode(statusObj.getString(STATUS_CODE_KEY));
        statusBuilder.setState(statusObj.getString(DETAILED_STATE_KEY));
        statusBuilder.setStateCode(statusObj.getString(STATUS_CODE_KEY));

        return statusBuilder.build();
    }

}