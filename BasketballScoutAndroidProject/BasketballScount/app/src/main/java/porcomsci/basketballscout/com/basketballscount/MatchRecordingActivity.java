package porcomsci.basketballscout.com.basketballscount;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TextView;

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
import database.entities.QuarterScoreSheet;
import database.entities.Substitution;


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
    TextView score1TextView, score2TextView;
    Chronometer timeClock;

    int scoreTeam1 = 0;
    int scoreTeam2 = 0;
    int timeoutCountTeam1 = 0;
    int timeoutCountTeam2 = 0;
    int foulsCountTeam1 = 0;
    int foulsCountTeam2 = 0;


    private Dao<QuarterPlayerInfo, Integer> quarterInfoDao;
    private Dao<MatchPlayer,Integer> matchPlayerDao;
    private Dao<Quarter,Integer> quarterDao;
    private Dao<Substitution,Integer> substitutionsDao;
    private Dao<QuarterScoreSheet,Integer> quarterScoreSheetDao;

    private  enum SubstitutionType { IN,OUT }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Quarter # " + DBSaveHelper.quarterNumber);
        setContentView(R.layout.activity_match_recording);
        initViewComponents();
        determineQuater();
        try {
            initDao();
            initializeBasicData();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        listViewTeam1 = (ListView) findViewById(R.id.matchRecord_list1);
        listViewTeam2 = (ListView) findViewById(R.id.matchRecord_list2);

        initListViewTeam1(listViewTeam1, lineupAdapter1);
        initListViewTeam2(listViewTeam2, lineupAdapter2);
    }

    private void initViewComponents() {

        score1TextView = (TextView)findViewById(R.id.matchRecord_score1);
        score2TextView = (TextView)findViewById(R.id.matchRecord_score2);
        score1TextView.setText(String.valueOf(scoreTeam1));
        score2TextView.setText(String.valueOf(scoreTeam2));
        timeClock      = (Chronometer)findViewById(R.id.matchRecord_chronometer);
        timeClock.setText("00:00");
        timeClock.setOnClickListener(ChronoMeterOnClick);
    }

    boolean tick = false;
    View.OnClickListener ChronoMeterOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            if(tick){
                stopTime();
                tick = false;
            }else{
                startTime();
                tick = true;
            }
        }
    };

    private void startTime() {
        int stoppedMilliseconds = 0;

        String chronometerText = timeClock.getText().toString();
        String array[] = chronometerText.split(":");
        if (array.length == 2) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                    + Integer.parseInt(array[1]) * 1000;
        }
        timeClock.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
        timeClock.start();
    }

    private void stopTime() {
        timeClock.stop();
    }


    private void initDao() throws SQLException {
        quarterInfoDao = getHelper().getQuarterPlayerInfoDao();
        matchPlayerDao = getHelper().getMatchPlayerDao();
        quarterDao = getHelper().getQuaterDao();
        substitutionsDao = getHelper().getSubstitutionDao();
        quarterScoreSheetDao = getHelper().getQuarterScoreSheetDao();
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
            for (Player player : team1Players) {
                QuarterPlayerInfo quarterPlayerInfo = new QuarterPlayerInfo();
                quarterPlayerInfo.setQuarter(quarter);
                quarterPlayerInfo.setPlayer(player);
                quarterInfoDao.create(quarterPlayerInfo);
            }
    }

    private void initEachTeamMP() throws SQLException {
        team1MP =  retrievedMatchPlayerOfSchoolId(DBSaveHelper.school1Id);
        team2MP =  retrievedMatchPlayerOfSchoolId(DBSaveHelper.school2Id);
    }

    private List<MatchPlayer> retrievedMatchPlayerOfSchoolId(int schoolID) throws SQLException {
        return matchPlayerDao.queryBuilder().where().eq("match_id", DBSaveHelper.match.getId()).and().eq("schoolId", schoolID).query();
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

    private void initSubstitutionData(Player[] lineupTeam) throws SQLException {

        for (Player player : lineupTeam) {
            Substitution substitution = new Substitution();
            substitution.setQuarter(quarter);
            substitution.setTime("00:00");
            substitution.setType(SubstitutionType.IN.name());
            substitution.setPlayer(player);
            substitutionsDao.create(substitution);
        }
    }


    // call this method to init the both ListView
    /**
     * Set content which is the line up players list to a ListView
     * @param listView which you want the content to display
     * @param lineupAdapter1 string array that contains line up players list
     */
    private void initListViewTeam1(ListView listView, String[] lineupAdapter1) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>( this, android.R.layout.simple_list_item_1, lineupAdapter1);
        listView.setAdapter( adapter );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //@Por
                // you can get the selected position of ListView item here
                showManagePlayersMenuPopUpTeam1(position);
            }
        });
    }
    private void initListViewTeam2(ListView listView, String[] lineupAdapter2) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>( this, android.R.layout.simple_list_item_1, lineupAdapter2);
        listView.setAdapter( adapter );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //@Por
                // you can get the selected position of ListView item here
                showManagePlayersMenuPopUpTeam2(position);
            }
        });
    }

    private void showManagePlayersMenuPopUpTeam1(int pos)
    {
        String[] menu = {"1 คะแนน", "2 คะแนน","3 คะแนน", "ฟาล์ว" ,"เปลี่ยนตัวผู้เล่น", "ยกเลิก"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menu);
        final int selectPos = pos;
        System.out.println("selected pos :"+selectPos);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override//@Por implement the cases
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        //+1 point
                        scoreTeam1++;
                        score1TextView.setText(String.valueOf(scoreTeam1));
                        saveScoreSheet(selectPos , 1 , 1);
                        break;
                    case 1:
                        //+2
                        scoreTeam1+=2;
                        score1TextView.setText(String.valueOf(scoreTeam1));
                        saveScoreSheet(selectPos, 2 , 1);
                        break;
                    case 2:
                        //+3
                        scoreTeam1+=3;
                        score1TextView.setText(String.valueOf(scoreTeam1));
                        saveScoreSheet(selectPos, 3 , 1 );
                        break;
                    case 3:
                        //foul
                        foulsCountTeam1++;
                        //save player fouls in MatchPlayer
                        increasePlayerFouls(selectPos ,1);
                        break;
                    case 4:
                        //change



                        break;
                    case 5:
                        //cancel
                        break;
                }
            }
        });
        builder.create().show();
    }


    private void showManagePlayersMenuPopUpTeam2(int pos)
    {
        String[] menu = {"1 คะแนน", "2 คะแนน","3 คะแนน", "ฟาล์ว" ,"เปลี่ยนตัวผู้เล่น", "ยกเลิก"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menu);
        final int selectPos = pos;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override//@Por implement the cases
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        //+1 point
                        scoreTeam2++;
                        score2TextView.setText(String.valueOf(scoreTeam2));
                        saveScoreSheet(selectPos, 1, 2);
                        break;
                    case 1:
                        //+2
                        scoreTeam2 += 2;
                        score2TextView.setText(String.valueOf(scoreTeam2));
                        saveScoreSheet(selectPos, 2, 2);
                        break;
                    case 2:
                        //+3
                        scoreTeam2 += 3;
                        score2TextView.setText(String.valueOf(scoreTeam2));
                        saveScoreSheet(selectPos, 3, 2);
                        break;
                    case 3:
                        //foul
                        foulsCountTeam2++;
                        increasePlayerFouls(selectPos, 2);
                        break;
                    case 4:
                        //change
                        break;
                    case 5:
                        //cancel
                        break;
                }
            }
        });
        builder.create().show();
    }


    private void saveScoreSheet(int selectPos, int score, int team) {

        Player actionPlayer = null;
        if(1 == team){
            actionPlayer = lineupTeam1[selectPos];
        }else{
            actionPlayer = lineupTeam2[selectPos];
        }

        QuarterScoreSheet quarterScoreSheet = new QuarterScoreSheet();
        quarterScoreSheet.setQuarter(quarter);
        quarterScoreSheet.setPlayer(actionPlayer);
        quarterScoreSheet.setScoreCount(score);
        try {
            quarterScoreSheetDao.create(quarterScoreSheet);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void increasePlayerFouls(int selectPos, int team) {
        Player actionPlayer = null;
        if(1 == team){
            actionPlayer = lineupTeam1[selectPos];
        }else{
            actionPlayer = lineupTeam2[selectPos];
        }
        try {
            List<MatchPlayer> matchPlayerWantedToUpdate = matchPlayerDao.queryBuilder().where().eq("match_id",DBSaveHelper.match.getId()).and().eq("player_id",actionPlayer.getId()).query();
            if(1 != matchPlayerWantedToUpdate.size()){
                System.out.println("Query error!");
                System.exit(-1);
            }
            MatchPlayer matchPlayer = matchPlayerWantedToUpdate.get(0);
            matchPlayerDao.refresh(matchPlayer);
            matchPlayer.setPlayerFouls(matchPlayer.getPlayerFouls()+1);
            matchPlayerDao.update(matchPlayer);

            List<QuarterPlayerInfo> quarterPlayerInfos = quarterInfoDao.queryBuilder().where().eq("quarter_id",quarter.getId()).and().eq("player_id",actionPlayer.getId()).query();
            if(1 != quarterPlayerInfos.size()){
                System.out.println("Query error!");
                System.exit(-1);
            }
            QuarterPlayerInfo quarterPlayerInfo = quarterPlayerInfos.get(0);
            quarterInfoDao.refresh(quarterPlayerInfo);
            quarterPlayerInfo.setFoulsSummary(quarterPlayerInfo.getFoulsSummary()+1);
            quarterInfoDao.update(quarterPlayerInfo);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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

