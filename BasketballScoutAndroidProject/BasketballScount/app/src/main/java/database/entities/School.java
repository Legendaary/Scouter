package database.entities;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by PorPaul on 23/3/2558.
 */
public class School {

    @DatabaseField(id = true,generatedId = true)
    private int id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String logoFilePath;


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
}
