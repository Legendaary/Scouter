package database.entities;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by PorPaul on 23/3/2558.
 */

public class School implements Serializable{

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String logoFilePath;
    @DatabaseField (foreign = true,columnName = "match_id",foreignAutoRefresh = true)
    private Match match;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoFilePath() {
        return logoFilePath;
    }

    public void setLogoFilePath(String logoFilePath) {
        this.logoFilePath = logoFilePath;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
