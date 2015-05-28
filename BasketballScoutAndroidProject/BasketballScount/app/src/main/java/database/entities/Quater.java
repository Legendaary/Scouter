package database.entities;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

/**
 * Created by PorPaul on 23/3/2558.
 */
public class Quater {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private Integer quaterNumber;
    @DatabaseField
    private Integer scoreA;
    @DatabaseField
    private Integer scoreB;
    @DatabaseField
    private Integer timeOutA;
    @DatabaseField
    private Integer timeOutB;
    @DatabaseField
    private Integer foulA;
    @DatabaseField
    private Integer foulB;
    @DatabaseField (foreign = true,columnName = "match_id")
    private Match match;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getQuaterNumber() {
        return quaterNumber;
    }

    public void setQuaterNumber(Integer quaterNumber) {
        this.quaterNumber = quaterNumber;
    }

    public Integer getScoreA() {
        return scoreA;
    }

    public void setScoreA(Integer scoreA) {
        this.scoreA = scoreA;
    }

    public Integer getScoreB() {
        return scoreB;
    }

    public void setScoreB(Integer scoreB) {
        this.scoreB = scoreB;
    }

    public Integer getTimeOutA() {
        return timeOutA;
    }

    public void setTimeOutA(Integer timeOutA) {
        this.timeOutA = timeOutA;
    }

    public Integer getTimeOutB() {
        return timeOutB;
    }

    public void setTimeOutB(Integer timeOutB) {
        this.timeOutB = timeOutB;
    }

    public Integer getFoulA() {
        return foulA;
    }

    public void setFoulA(Integer foulA) {
        this.foulA = foulA;
    }

    public Integer getFoulB() {
        return foulB;
    }

    public void setFoulB(Integer foulB) {
        this.foulB = foulB;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
