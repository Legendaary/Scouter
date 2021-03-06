package database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * Created by PorPaul on 17/3./2558.
 */

@DatabaseTable(tableName = "Match")
public class Match implements Serializable {

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
    private String winner;
    @DatabaseField(foreign = true)
    private School schoolA;
    @DatabaseField(foreign = true)
    private School schoolB;
    @DatabaseField(foreign = true, columnName = "tournament_id" ,foreignAutoRefresh = true )
    private Tournament tournament;
    @ForeignCollectionField()
    private Collection<Quarter> quarter = null;
    @ForeignCollectionField
    private Collection<MatchPlayer> matchPlayers = null;


    public Match() {

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

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
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

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Collection<Quarter> getQuarter() {
        return quarter;
    }

    public void setQuarter(Collection<Quarter> quarter) {
        this.quarter = quarter;
    }

    public Collection<MatchPlayer> getMatchPlayers() {
        return matchPlayers;
    }

    public void setMatchPlayers(Collection<MatchPlayer> matchPlayers) {
        this.matchPlayers = matchPlayers;
    }
}//end class
