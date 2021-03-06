package database.entities;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by PorPaul on 23/3/2558.
 */
public class Player {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String position;
    @DatabaseField(foreign = true,columnName = "school_id",foreignAutoRefresh = true)
    private School school;

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
