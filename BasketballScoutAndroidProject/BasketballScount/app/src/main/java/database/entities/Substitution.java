package database.entities;
import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

/**
 * Created by PorPaul on 23/3/2558.
 */
public class Substitution {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private Date time;
    @DatabaseField
    private String type;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
