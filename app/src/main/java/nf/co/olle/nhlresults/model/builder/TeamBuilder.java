package nf.co.olle.nhlresults.model.builder;

import nf.co.olle.nhlresults.model.Team;

public class TeamBuilder {
    private int id;
    private String name;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Team build() {
        return new Team(id, name);
    }
}
