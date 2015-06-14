package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import database.entities.Match;
import database.entities.MatchPlayer;
import database.entities.Player;
import database.entities.Quarter;
import database.entities.QuarterPlayerInfo;
import database.entities.QuarterScoreSheet;
import database.entities.School;
import database.entities.Substitution;
import database.entities.Tournament;


/**
 * Created by PorPaul on 17/3/2558.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "basketballscout.db";
    private static final int DATABASE_VERSION = 1;
    private Dao<Match, Integer> matchDao = null;
    private Dao<Tournament, Integer> tournamentDao = null;
    private Dao<MatchPlayer, Integer> matchPlayersDao = null;
    private Dao<School, Integer>  schoolDao = null;
    private Dao<Player, Integer> playerDao = null;
    private Dao<Quarter, Integer> quatersDao = null;
    private Dao<QuarterPlayerInfo, Integer> quaterInfoDao = null;
    private Dao<QuarterScoreSheet, Integer> quaterScoreSheetDao = null;
    private Dao<Substitution, Integer> substitutionDao = null;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource)  {

        /**
         * Default database creation and added default data for some tables.
         */
        try {
            TableUtils.createTable(connectionSource, Tournament.class);
            TableUtils.createTable(connectionSource, Match.class);
            TableUtils.createTable(connectionSource, MatchPlayer.class);
            TableUtils.createTable(connectionSource, School.class);
            TableUtils.createTable(connectionSource, Player.class);
            TableUtils.createTable(connectionSource, Quarter.class);
            TableUtils.createTable(connectionSource, QuarterPlayerInfo.class);
            TableUtils.createTable(connectionSource, QuarterScoreSheet.class);
            TableUtils.createTable(connectionSource, Substitution.class);
            initSchool();
            initSaintDominicPlayer();
            initTournament();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initSaintDominicPlayer() throws SQLException {
        School saintDominic = new School();
        saintDominic.setId(1);
        Player one = new Player();
        one.setName("อาชัญ เอี่ยมจุ้ย");
        one.setSchool(saintDominic);
        Player two = new Player();
        two.setName("ธนธัช บุญอนุวัฒน์");
        two.setSchool(saintDominic);
        Player three = new Player();
        three.setName("เจตนิพัธท์  พบสุข์");
        three.setSchool(saintDominic);
        Player four = new Player();
        four.setName("test4");
        four.setSchool(saintDominic);
        Player five = new Player();
        five.setName("test5");
        five.setSchool(saintDominic);
        getPlayerDao().create(one);
        getPlayerDao().create(two);
        getPlayerDao().create(three);
        getPlayerDao().create(four);
        getPlayerDao().create(five);
    }

    private void initTournament() throws SQLException {
        tournamentDao = getTournamentDao();
        Tournament casualTour = new Tournament();
        casualTour.setCompetitionName("casual");
        tournamentDao.create(casualTour);
    }

    public void initSchool() throws SQLException {
        insertSchoolByName("เซนต์ดอมินิก");
        insertSchoolByName("วัดสุวรรณ");
        insertSchoolByName("อัสสัมชัญกรุงเทพ");
        insertSchoolByName("กรุงเทพคริสเตียน");
        insertSchoolByName("สุเหร่า คลองจั่น");
        insertSchoolByName("ศึกษานารี");
        insertSchoolByName("ทิวไผ่งาม");
    }

    public void insertSchoolByName(String schoolName) throws SQLException {
        schoolDao = getSchoolDao();
        School inputSchool = new School();
        inputSchool.setName(schoolName);
        schoolDao.create(inputSchool);
    }


    public Dao<Tournament, Integer> getTournamentDao() throws SQLException {
        if(tournamentDao==null){
            tournamentDao =  getDao(Tournament.class);
            return tournamentDao;
        }else{
            return tournamentDao;
        }
    }
    public Dao<MatchPlayer, Integer> getMatchPlayerDao() throws SQLException {
        if(matchPlayersDao==null){
            matchPlayersDao =  getDao(MatchPlayer.class);
            return matchPlayersDao;
        }else{
            return matchPlayersDao;
        }
    }

    public Dao<School, Integer> getSchoolDao() throws SQLException {
        if(schoolDao==null){
            schoolDao =  getDao(School.class);
            return schoolDao;
        }else{
            return schoolDao;
        }
    }
    public Dao<Player, Integer> getPlayerDao() throws SQLException {
        if(playerDao==null){
            playerDao =  getDao(Player.class);
            return playerDao;
        }else{
            return playerDao;
        }
    }
    public Dao<Quarter, Integer> getQuaterDao() throws SQLException {
        if(quatersDao==null){
            quatersDao =  getDao(Quarter.class);
            return quatersDao;
        }else{
            return quatersDao;
        }
    }
    public Dao<QuarterPlayerInfo, Integer> getQuaterInfoDao() throws SQLException {
        if(quaterInfoDao==null){
            quaterInfoDao =  getDao(QuarterPlayerInfo.class);
            return quaterInfoDao;
        }else{
            return quaterInfoDao;
        }
    }
    public Dao<Match, Integer> getMatchDao() throws SQLException {
        if(matchDao==null){
            matchDao =  getDao(Match.class);
            return matchDao;
        }else{
            return matchDao;
        }
    }
    public Dao<QuarterScoreSheet, Integer> getQuaterScoreSheetDao() throws SQLException {
        if(quaterScoreSheetDao==null){
            quaterScoreSheetDao =  getDao(QuarterScoreSheet.class);
            return quaterScoreSheetDao;
        }else{
            return quaterScoreSheetDao;
        }
    }
    public Dao<Substitution, Integer> getSubstitutionDao() throws SQLException {
        if(substitutionDao==null){
            substitutionDao =  getDao(Substitution.class);
            return substitutionDao;
        }else{
            return substitutionDao;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
    @Override
    public void close() {
        super.close();
        matchDao = null;
    }


}
