package database.entities;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by PorPaul on 23/3/2558.
 */
public class QuarterPlayerInfo
{
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private Integer scoreSummary;
    @DatabaseField
    private Integer timeSpent;
    @DatabaseField
    private Integer foulsSummary;
    @DatabaseField (foreign = true,columnName = "player_id")
    private Player player;
    @DatabaseField (foreign = true,columnName = "quarter_id")
    private Quarter quarter;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getScoreSummary() {
        return scoreSummary;
    }

    public void setScoreSummary(Integer scoreSummary) {
        this.scoreSummary = scoreSummary;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Integer getFoulsSummary() {
        return foulsSummary;
    }

    public void setFoulsSummary(Integer foulsSummary) {
        this.foulsSummary = foulsSummary;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Quarter getQuarter() {
        return quarter;
    }

    public void setQuarter(Quarter quarter) {
        this.quarter = quarter;
    }
}
