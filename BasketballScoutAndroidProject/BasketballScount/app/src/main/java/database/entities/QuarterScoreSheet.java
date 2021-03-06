package database.entities;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by PorPaul on 23/3/2558.
 */
public class QuarterScoreSheet {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private Integer scoreCount;
    @DatabaseField
    private String time;
    @DatabaseField
    private Integer schoolId;
    @DatabaseField(foreign = true, columnName = "player_id", foreignAutoRefresh = true)
    private Player player;
    @DatabaseField(foreign = true, columnName = "quarter_id", foreignAutoRefresh = true)
    private Quarter quarter;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getScoreCount() {
        return scoreCount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setScoreCount(Integer scoreCount) {
        this.scoreCount = scoreCount;
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

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }
}
