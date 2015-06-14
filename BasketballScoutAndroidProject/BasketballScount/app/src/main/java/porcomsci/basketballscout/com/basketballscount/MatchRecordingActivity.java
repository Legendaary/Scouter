package porcomsci.basketballscout.com.basketballscount;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.DBSaveHelper;
import database.DatabaseHelper;
import database.entities.MatchPlayer;
import database.entities.Player;
import database.entities.Quarter;
import database.entities.QuarterPlayerInfo;
import database.entities.School;


public class MatchRecordingActivity extends ActionBarActivity {

    private DatabaseHelper databaseHelper = null;

    Quarter quarter;
    List<MatchPlayer> team1MP;
    List<MatchPlayer> team2MP;
    List<Player> team1Players;
    List<Player> team2Players;

    int scoreTeam1 = 0;
    int scoreTeam2 = 0;
    int timeoutCountTeam1 = 0;
    int timeoutCountTeam2 = 0;
    int foulsCountTeam1 = 0;
    int foulsCountTeam2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_recording);
        determineQuater();
        try {
            initializeBasicData();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    private void determineQuater() {
        int quarterNo = DBSaveHelper.quarterNumber;
        if(1 == quarterNo){
            quarter = DBSaveHelper.quarter1;
        }else if(2 == quarterNo){
            quarter = DBSaveHelper.quarter2;
        }else if(3 == quarterNo){
            quarter = DBSaveHelper.quarter3;
        }else if(4 == quarterNo){
            quarter = DBSaveHelper.quarter4;
        }else if(5 == quarterNo){
            quarter = DBSaveHelper.quarter5;
        }
    }

    private void initializeBasicData() throws SQLException {
        initEachTeamMP();
        initAllPlayersList();
        createQuarterInfoRecord(team1Players, team2Players);
        getLineUpPlayer(, team1MP);
        getLineUpPlayer(, team2MP);
    }

    private void createQuarterInfoRecord(List<Player> team1Players, List<Player> team2Players) throws SQLException {
            //join 2 players
            team1Players.addAll(team2Players);
            Dao<QuarterPlayerInfo, Integer> quarterInfoDao = getHelper().getQuarterPlayerInfoDao();
            for (Player player : team1Players) {
                QuarterPlayerInfo quarterPlayerInfo = new QuarterPlayerInfo();
                quarterPlayerInfo.setQuarter(quarter);
                quarterPlayerInfo.setPlayer(player);
                quarterInfoDao.create(quarterPlayerInfo);
            }
    }

    private void initEachTeamMP() throws SQLException {
        team1MP =  retrievedMatchPlayerOfSchoolId(DBSaveHelper.school1);
        team2MP =  retrievedMatchPlayerOfSchoolId(DBSaveHelper.school2);
    }

    private List<MatchPlayer> retrievedMatchPlayerOfSchoolId(School schoolID) throws SQLException {
        return getHelper().getMatchPlayerDao().queryBuilder().where().eq("match_id", DBSaveHelper.match.getId()).and().eq("school_id", schoolID).query();
    }

    private void initAllPlayersList() {
        team1Players = getPlayerListFromMatchPlayerList(team1MP);
        team2Players = getPlayerListFromMatchPlayerList(team2MP);
    }

    private List<Player> getPlayerListFromMatchPlayerList(List<MatchPlayer> team1MP) {

        List<Player> playerList = new ArrayList<>();
        for (MatchPlayer matchPlayer : team1MP) {
            playerList.add(matchPlayer.getPlayer());
        }
        return playerList;
    }

    private void getLineUpPlayer(Player[] lineupPlayerArrays, List<MatchPlayer> matchPlayerList) {

        int index = 0;

        for (MatchPlayer matchPlayer : matchPlayerList) {
            if (matchPlayer.getIsStartPlayer()) {
                lineupPlayerArrays[index] = matchPlayer.getPlayer();
                index++;
            }
        }
    }//end get line up player



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_match_recording, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

}

