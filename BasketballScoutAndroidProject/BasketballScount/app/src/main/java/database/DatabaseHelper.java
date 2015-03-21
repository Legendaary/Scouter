package porcomsci.basketballscout.com.basketballscount.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import porcomsci.basketballscout.com.basketballscount.database.entities.Match;

/**
 * Created by PorPaul on 17/3/2558.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "basketballscout.db";
    private static final int DATABASE_VERSION = 1;
    private Dao<Match, Integer> matchDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource)  {

        try {
            TableUtils.createTable(connectionSource, Match.class);
            matchDao = getMatchDao();
            Match firstMatch = new Match();
            firstMatch.setId(1);
            firstMatch.setMatchNumber(1);
            firstMatch.setPlace("St.dominic");
            firstMatch.setTime("17.00-18.00");
            matchDao.create(firstMatch);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public Dao<Match, Integer> getMatchDao() throws SQLException {
        if(matchDao==null){
            matchDao =  getDao(Match.class);
            return matchDao;
        }else{
            return matchDao;
        }
    }

    @Override
    public void close() {
        super.close();
        matchDao = null;
    }

}
