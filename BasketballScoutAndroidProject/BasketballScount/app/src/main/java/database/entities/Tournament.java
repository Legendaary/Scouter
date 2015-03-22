package database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;


/**
 * Created by PorPaul on 17/3/2558.
 */

@DatabaseTable(tableName = "Tournament")
public class Tournament {

    @DatabaseField(id = true)
    private Integer id;
    @DatabaseField
    private String competitionName;
    @DatabaseField
    private int matchNumber;

    public Tournament(){

    }





}
