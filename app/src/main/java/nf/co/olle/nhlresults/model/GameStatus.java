package nf.co.olle.nhlresults.model;

public class GameStatus {
    private final String state;
    private final String stateCode;
    private final String statusCode;

    public GameStatus(String state, String stateCode, String statusCode) {
        this.state = state;
        this.stateCode = stateCode;
        this.statusCode = statusCode;
    }

    public String getState() {
        return state;
    }

    public String getStateCode() {
        return stateCode;
    }

    public String getStatusCode() {
        return statusCode;
    }
}
