package database.entities;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by PorPaul on 23/3/2558.
 */
public class MatchPlayer {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int player_number;
    @DatabaseField
    private int school_id;
    @DatabaseField
    private int player_fouls;
    @DatabaseField
    private int time_spent;
    @DatabaseField
    private int scored;
    @DatabaseField
    private int rebound;
    @DatabaseField
    private int steal;
    @DatabaseField
    private boolean isStartUp;
    @DatabaseField (foreign = true,columnName = "match_id")
    private Match match;
    @DatabaseField (foreign = true,columnName = "player_id")
    private Player player;

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getPlayer_number() {
        return player_number;
    }

    public void setPlayer_number(int player_number) {
        this.player_number = player_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayer_fouls() {
        return player_fouls;
    }

    public void setPlayer_fouls(int player_fouls) {
        this.player_fouls = player_fouls;
    }

    public int getTime_spent() {
        return time_spent;
    }

    public void setTime_spent(int time_spent) {
        this.time_spent = time_spent;
    }

    public int getScored() {
        return scored;
    }

    public void setScored(int scored) {
        this.scored = scored;
    }

    public int getRebound() {
        return rebound;
    }

    public void setRebound(int rebound) {
        this.rebound = rebound;
    }

    public int getSteal() {
        return steal;
    }

    public void setSteal(int steal) {
        this.steal = steal;
    }

    public boolean isStartUp() {
        return isStartUp;
    }

    public void setStartUp(boolean isStartUp) {
        this.isStartUp = isStartUp;
    }
}
