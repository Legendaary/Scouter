package database.entities;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by PorPaul on 23/3/2558.
 */
public class QuaterInfo {
    @DatabaseField(id = true,generatedId = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
