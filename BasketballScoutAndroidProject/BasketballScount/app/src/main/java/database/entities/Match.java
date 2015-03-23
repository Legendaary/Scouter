package database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;


/**
 * Created by PorPaul on 17/3/2558.
 */

@DatabaseTable(tableName = "database.entities.Match")
public class Match {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int matchNumber;
    @DatabaseField
    private Date date;
    @DatabaseField
    private String time;
    @DatabaseField
    private String place;


    public Match(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMatchNumber() {
        return matchNumber;
    }

    public void setMatchNumber(int matchNumber) {
        this.matchNumber = matchNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
