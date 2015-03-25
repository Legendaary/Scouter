package database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;


/**
 * Created by PorPaul on 17/3/2558.
 */

@DatabaseTable(tableName = "Match")
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
    @DatabaseField
    private String referee;
    @DatabaseField
    private String umpire;
    @DatabaseField
    private School schoolA;
    @DatabaseField
    private School schoolB;
    @DatabaseField
    private School winner;

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

    public String getReferee() {
        return referee;
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }

    public String getUmpire() {
        return umpire;
    }

    public void setUmpire(String umpire) {
        this.umpire = umpire;
    }

    public School getSchoolA() {
        return schoolA;
    }

    public void setSchoolA(School schoolA) {
        this.schoolA = schoolA;
    }

    public School getSchoolB() {
        return schoolB;
    }

    public void setSchoolB(School schoolB) {
        this.schoolB = schoolB;
    }

    public School getWinner() {
        return winner;
    }

    public void setWinner(School winner) {
        this.winner = winner;
    }
}//end class
