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
    private Dao<School, Integer> schoolDao = null;
    private Dao<Player, Integer> playerDao = null;
    private Dao<Quarter, Integer> quartersDao = null;
    private Dao<QuarterPlayerInfo, Integer> quarterPlayerInfoDao = null;
    private Dao<QuarterScoreSheet, Integer> quarterScoreSheetDao = null;
    private Dao<Substitution, Integer> substitutionDao = null;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

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
        one.setName("ธนภูมิ  บุญธีรวร");
        one.setSchool(saintDominic);
        Player two = new Player();
        two.setName("กาญจน์  กาญจนอัศว์");
        two.setSchool(saintDominic);
        Player three = new Player();
        three.setName("ศุภวิชญ์  ทนงศักดิ์");
        three.setSchool(saintDominic);
        Player four = new Player();
        four.setName("สวิตต์  วิรุฬห์บรรเทิง");
        four.setSchool(saintDominic);
        Player five = new Player();
        five.setName("ณัฐวัฒน์  พุฒิบูรณวัฒน์");
        five.setSchool(saintDominic);
        Player six = new Player();
        six.setName("ภูมิ  เตชะทัศนสุนทร");
        six.setSchool(saintDominic);
        Player seven = new Player();
        seven.setName("วรากร  สิทธเนตรสกุล์");
        seven.setSchool(saintDominic);
        Player eight = new Player();
        eight.setName("ณฐกฤต  อารยะสัจพงษ์");
        eight.setSchool(saintDominic);
        Player nine = new Player();
        nine.setName("ธนโชติ  กวิลเติมทรัพย์");
        nine.setSchool(saintDominic);
        Player ten = new Player();
        ten.setName("พิเชฐ  ตั้งชัยสิน์");
        ten.setSchool(saintDominic);
        Player eleven = new Player();
        eleven.setName("สุธีร์  กิจนาบูรณ์");
        eleven.setSchool(saintDominic);
        Player twelve = new Player();
        twelve.setName("ณัฐธัญ  สีหาทัพ์");
        twelve.setSchool(saintDominic);
        getPlayerDao().create(one);
        getPlayerDao().create(two);
        getPlayerDao().create(three);
        getPlayerDao().create(four);
        getPlayerDao().create(five);
        getPlayerDao().create(six);
        getPlayerDao().create(seven);
        getPlayerDao().create(eight);
        getPlayerDao().create(nine);
        getPlayerDao().create(ten);
        getPlayerDao().create(eleven);
        getPlayerDao().create(twelve);
    }

    private void initTournament() throws SQLException {
        tournamentDao = getTournamentDao();
        Tournament casualTour = new Tournament();
        casualTour.setCompetitionName("การแข่งขันทั่วไป");
        tournamentDao.create(casualTour);
    }

    public void initSchool() throws SQLException {
        insertSchoolByName("เซนต์ดอมินิก");
        insertSchoolByName("สาธิต มศว");
        insertSchoolByName("อัสสัมชัญกรุงเทพ");
    }

    public void insertSchoolByName(String schoolName) throws SQLException {
        schoolDao = getSchoolDao();
        School inputSchool = new School();
        inputSchool.setName(schoolName);
        schoolDao.create(inputSchool);
    }


    public Dao<Tournament, Integer> getTournamentDao() throws SQLException {
        if (tournamentDao == null) {
            tournamentDao = getDao(Tournament.class);
            return tournamentDao;
        } else {
            return tournamentDao;
        }
    }

    public Dao<MatchPlayer, Integer> getMatchPlayerDao() throws SQLException {
        if (matchPlayersDao == null) {
            matchPlayersDao = getDao(MatchPlayer.class);
            return matchPlayersDao;
        } else {
            return matchPlayersDao;
        }
    }

    public Dao<School, Integer> getSchoolDao() throws SQLException {
        if (schoolDao == null) {
            schoolDao = getDao(School.class);
            return schoolDao;
        } else {
            return schoolDao;
        }
    }

    public Dao<Player, Integer> getPlayerDao() throws SQLException {
        if (playerDao == null) {
            playerDao = getDao(Player.class);
            return playerDao;
        } else {
            return playerDao;
        }
    }

    public Dao<Quarter, Integer> getQuaterDao() throws SQLException {
        if (quartersDao == null) {
            quartersDao = getDao(Quarter.class);
            return quartersDao;
        } else {
            return quartersDao;
        }
    }

    public Dao<QuarterPlayerInfo, Integer> getQuarterPlayerInfoDao() throws SQLException {
        if (quarterPlayerInfoDao == null) {
            quarterPlayerInfoDao = getDao(QuarterPlayerInfo.class);
            return quarterPlayerInfoDao;
        } else {
            return quarterPlayerInfoDao;
        }
    }

    public Dao<Match, Integer> getMatchDao() throws SQLException {
        if (matchDao == null) {
            matchDao = getDao(Match.class);
            return matchDao;
        } else {
            return matchDao;
        }
    }

    public Dao<QuarterScoreSheet, Integer> getQuarterScoreSheetDao() throws SQLException {
        if (quarterScoreSheetDao == null) {
            quarterScoreSheetDao = getDao(QuarterScoreSheet.class);
            return quarterScoreSheetDao;
        } else {
            return quarterScoreSheetDao;
        }
    }

    public Dao<Substitution, Integer> getSubstitutionDao() throws SQLException {
        if (substitutionDao == null) {
            substitutionDao = getDao(Substitution.class);
            return substitutionDao;
        } else {
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
