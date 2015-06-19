package database.entities;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by PorPaul on 23/3/2558.
 */
public class MatchPlayer {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private Integer playerNumber;
    @DatabaseField(defaultValue = "0")
    private Integer playerFouls;
    @DatabaseField(defaultValue = "0")
    private Integer timeSpent;
    @DatabaseField(defaultValue = "0")
    private Integer score;
    @DatabaseField(defaultValue = "0")
    private Integer rebound;
    @DatabaseField(defaultValue = "0")
    private Integer steal;
    @DatabaseField(defaultValue = "false")
    private Boolean isStartPlayer;
    @DatabaseField
    private Integer schoolId;
    @DatabaseField (foreign = true,columnName = "player_id",foreignAutoRefresh = true)
    private Player player;
    @DatabaseField (foreign = true,columnName = "match_id",foreignAutoRefresh = true)
    private Match match;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(Integer playerNumber) {
        this.playerNumber = playerNumber;
    }

    public Integer getPlayerFouls() {
        return playerFouls;
    }

    public void setPlayerFouls(Integer playerFouls) {
        this.playerFouls = playerFouls;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getRebound() {
        return rebound;
    }

    public void setRebound(Integer rebound) {
        this.rebound = rebound;
    }

    public Integer getSteal() {
        return steal;
    }

    public void setSteal(Integer steal) {
        this.steal = steal;
    }

    public Boolean getIsStartPlayer() {
        return isStartPlayer;
    }

    public void setIsStartPlayer(Boolean isStartPlayer) {
        this.isStartPlayer = isStartPlayer;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
