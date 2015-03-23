package database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;


/**
 * Created by PorPaul on 17/3/2558.
 */

@DatabaseTable(tableName = "Tournament")
public class Tournament {

    @DatabaseField(id = true,generatedId = true)
    private int id;
    @DatabaseField
    private String competitionName;
    @DatabaseField
    private Integer matchNumber;



    public Tournament(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public Integer getMatchNumber() {
        return matchNumber;
    }

    public void setMatchNumber(Integer matchNumber) {
        this.matchNumber = matchNumber;
    }
}
