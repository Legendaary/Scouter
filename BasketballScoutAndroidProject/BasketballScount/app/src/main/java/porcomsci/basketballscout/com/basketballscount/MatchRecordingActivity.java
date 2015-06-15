package porcomsci.basketballscout.com.basketballscount;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    Player[] lineupTeam1 = new Player[5];
    Player[] lineupTeam2 = new Player[5];
    String[] lineupAdapter1  = new String[5];
    String[] lineupAdapter2  = new String[5];
    ListView listViewTeam1, listViewTeam2;

    int scoreTeam1 = 0;
    int scoreTeam2 = 0;
    int timeoutCountTeam1 = 0;
    int timeoutCountTeam2 = 0;
    int foulsCountTeam1 = 0;
    int foulsCountTeam2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Quarter # " + DBSaveHelper.quarterNumber);
        setContentView(R.layout.activity_match_recording);
        determineQuater();
        try {
            initializeBasicData();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        listViewTeam1 = (ListView) findViewById(R.id.matchRecord_list1);
        listViewTeam2 = (ListView) findViewById(R.id.matchRecord_list2);

        printOutAllList(lineupAdapter1);
        printOutAllList(lineupAdapter2);
        initListView( listViewTeam1, lineupAdapter1 );
        initListView( listViewTeam2, lineupAdapter2 );
    }

    private void printOutAllList(String[] lineupAdapter2) {

        for (String s : lineupAdapter2) {
            System.out.println("Player name : "+s);
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
        getLineUpPlayer( lineupTeam1 , team1MP);
        getLineUpPlayer( lineupTeam2 , team2MP);
        getPlayerNameAdapter(lineupTeam1, lineupAdapter1);
        getPlayerNameAdapter(lineupTeam2, lineupAdapter2);
        initSubstitutionData(lineupTeam1);
        initSubstitutionData(lineupTeam2);
    }

    private void createQuarterInfoRecord(List<Player> team1Players, List<Player> team2Players) throws SQLException {
            //join 2 player lists
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
        return getHelper().getMatchPlayerDao().queryBuilder().where().eq("match_id", DBSaveHelper.match.getId()).and().eq("schoolId", schoolID).query();
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
        if(5 < index){
            System.out.println("Something went wrong! Line up more than 5.");
            System.exit(-1);
        }
    }//end get line up player

    private void getPlayerNameAdapter(Player[] lineupTeam, String[] lineupAdapter) {
       for( int i = 0 ; i < lineupTeam.length ; i++){
           lineupAdapter[i] = lineupTeam[i].getName();
       }
    }

    /**
     *
     * TODO
     * @param lineupTeam
     */
    private void initSubstitutionData(Player[] lineupTeam) {

    }


    // call this method to init the both ListView
    /**
     * Set content which is the line up players list to a ListView
     * @param listView which you want the content to display
     * @param lineupAdapter string array that contains line up players list
     */
    private void initListView( ListView listView, String[] lineupAdapter )
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<>( this, android.R.layout.simple_list_item_1, lineupAdapter);
        listView.setAdapter( adapter );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //@Por
                // you can get the selected position of ListView item here
                showManagePlayersMenuPopUp();
            }
        });
    }

    private void showManagePlayersMenuPopUp()
    {
        String[] menu = {"1 แต้ม", "2 แต้ม","3 แต้ม", "ฟาวล์" ,"เปลี่ยนตัว", "ยกเลิก"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menu);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override//@Por implement the cases
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        //+1 point
                        break;
                    case 1:
                        //+2
                        break;
                    case 3:
                        //+3
                        break;
                    case 4:
                        //foul
                        break;
                    case 5:
                        //change
                        break;
                    case 6:
                        //cancel
                        break;
                }
            }
        });
        builder.create().show();
    }

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

