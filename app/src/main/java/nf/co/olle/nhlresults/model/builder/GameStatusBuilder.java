package nf.co.olle.nhlresults.model.builder;

import nf.co.olle.nhlresults.model.GameStatus;

public class GameStatusBuilder {
    private String state;
    private String stateCode;
    private String statusCode;

    public void setState(String state) {
        this.state = state;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public GameStatus build() {
        return new GameStatus(state, stateCode, statusCode);
    }
}
