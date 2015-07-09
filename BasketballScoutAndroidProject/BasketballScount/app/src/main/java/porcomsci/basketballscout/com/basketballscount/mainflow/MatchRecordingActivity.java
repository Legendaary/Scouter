package porcomsci.basketballscout.com.basketballscount.mainflow;

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
import com.j256.ormlite.stmt.DeleteBuilder;

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
import porcomsci.basketballscout.com.basketballscount.R;
import porcomsci.basketballscout.com.basketballscount.adapter.SubstitutionAdapter;
import porcomsci.basketballscout.com.basketballscount.utility.SegueHelper;


public class MatchRecordingActivity extends ActionBarActivity {

    private DatabaseHelper databaseHelper = null;

    Quarter quarter;
    List<MatchPlayer> team1MP;
    List<MatchPlayer> team2MP;
    List<Player> team1Players;
    List<Player> team2Players;
    List<Player> reservedPlayers1 = new ArrayList<>();
    List<Player> reservedPlayers2 = new ArrayList<>();
    Player[] lineupTeam1 = new Player[5];
    Player[] lineupTeam2 = new Player[5];
    String[] lineupName1 = new String[5];
    String[] lineupName2 = new String[5];
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
    private Dao<MatchPlayer, Integer> matchPlayerDao;
    private Dao<Quarter, Integer> quarterDao;
    private Dao<Substitution, Integer> substitutionsDao;
    private Dao<QuarterScoreSheet, Integer> quarterScoreSheetDao;



    private enum SubstitutionType {IN, OUT}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Quarter # " + DBSaveHelper.quarterNumber);
        setContentView(R.layout.activity_match_recording);
        initViewComponents();
        determineQuarter();
        try {
            initDao();
            initializeBasicData();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        listViewTeam1 = (ListView) findViewById(R.id.matchRecord_list1);
        listViewTeam2 = (ListView) findViewById(R.id.matchRecord_list2);

        initListViewTeam1(listViewTeam1, lineupName1);
        initListViewTeam2(listViewTeam2, lineupName2);
    }

    private void initViewComponents() {

        score1TextView = (TextView) findViewById(R.id.matchRecord_score1);
        score2TextView = (TextView) findViewById(R.id.matchRecord_score2);
        score1TextView.setText(String.valueOf(scoreTeam1));
        score2TextView.setText(String.valueOf(scoreTeam2));
        timeClock = (Chronometer) findViewById(R.id.matchRecord_chronometer);
        timeClock.setText("00:00");
        timeClock.setOnClickListener(ChronoMeterOnClick);
    }

    boolean tick = false;
    View.OnClickListener ChronoMeterOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            if (tick) {
                stopTime();
                tick = false;
            } else {
                startTime();
                tick = true;
            }
        }
    };

    private void startTime() {
        int stoppedMilliseconds = 0;

        String chronometerText = timeClock.getText().toString();
        String array[] = chronometerText.split(":");
        stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                + Integer.parseInt(array[1]) * 1000;
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

    private void determineQuarter() {
        int quarterNo = DBSaveHelper.quarterNumber;
        if (1 == quarterNo) {
            quarter = DBSaveHelper.quarter1;
        } else if (2 == quarterNo) {
            quarter = DBSaveHelper.quarter2;
        } else if (3 == quarterNo) {
            quarter = DBSaveHelper.quarter3;
        } else if (4 == quarterNo) {
            quarter = DBSaveHelper.quarter4;
        } else if (5 == quarterNo) {
            quarter = DBSaveHelper.quarter5;
        }
    }

    private void initializeBasicData() throws SQLException {
        initEachTeamMP();
        initAllPlayersList();
        createQuarterInfoRecord(team1Players);
        createQuarterInfoRecord(team2Players);
        getLineUpPlayer(lineupTeam1, team1MP);
        getLineUpPlayer(lineupTeam2, team2MP);
        getLineupPlayersName(lineupTeam1, lineupName1);
        getLineupPlayersName(lineupTeam2, lineupName2);
        initSubstitutionData(lineupTeam1);
        initSubstitutionData(lineupTeam2);
    }

    private void initEachTeamMP() throws SQLException {
        team1MP = retrievedMatchPlayerOfSchoolId(DBSaveHelper.school1Id);
        team2MP = retrievedMatchPlayerOfSchoolId(DBSaveHelper.school2Id);
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


    private void createQuarterInfoRecord(List<Player> exPlayerList) throws SQLException {

        for (Player player : exPlayerList) {
            QuarterPlayerInfo quarterPlayerInfo = new QuarterPlayerInfo();
            quarterPlayerInfo.setQuarter(quarter);
            quarterPlayerInfo.setPlayer(player);
            quarterInfoDao.create(quarterPlayerInfo);
        }
    }

    private void getLineUpPlayer(Player[] lineupPlayerArrays, List<MatchPlayer> matchPlayerList) {

        int index = 0;

        for (MatchPlayer matchPlayer : matchPlayerList) {
            if (matchPlayer.getIsStartPlayer()) {
                lineupPlayerArrays[index] = matchPlayer.getPlayer();
                index++;
            }
        }
        if (5 < index) {
            System.out.println("Something went wrong! Line up more than 5.");
            System.exit(-1);
        }
    }//end get line up player

    private void getLineupPlayersName(Player[] lineupTeam, String[] lineupAdapter) {
        for (int i = 0; i < lineupTeam.length; i++) {
            lineupAdapter[i] = lineupTeam[i].getName();
        }
    }

    private List<String> getPlayersName(List<Player> playersList)
    {
        List<String> playersName = new ArrayList<>();
        for(Player player : playersList)
        {
            playersName.add(player.getName());
        }
        return playersName;
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
     *
     * @param listView       which you want the content to display
     * @param lineupAdapter1 string array that contains line up players list
     */
    private void initListViewTeam1(ListView listView, String[] lineupAdapter1) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lineupAdapter1);
        listView.setAdapter(adapter);

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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lineupAdapter2);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //@Por
                // you can get the selected position of ListView item here
                showManagePlayersMenuPopUpTeam2(position);
            }
        });
    }

    private void showManagePlayersMenuPopUpTeam1(int pos) {
        String[] menu = {"1 คะแนน", "2 คะแนน", "3 คะแนน", "ฟาล์ว", "เปลี่ยนตัวผู้เล่น", "ยกเลิก"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menu);
        final int selectPos = pos;
        System.out.println("selected pos :" + selectPos);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override//@Por implement the cases
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        //+1 point
                        scoreTeam1++;
                        score1TextView.setText(String.valueOf(scoreTeam1));
                        saveScoreSheet(selectPos, 1, 1);
                        break;
                    case 1:
                        //+2
                        scoreTeam1 += 2;
                        score1TextView.setText(String.valueOf(scoreTeam1));
                        saveScoreSheet(selectPos, 2, 1);
                        break;
                    case 2:
                        //+3
                        scoreTeam1 += 3;
                        score1TextView.setText(String.valueOf(scoreTeam1));
                        saveScoreSheet(selectPos, 3, 1);
                        break;
                    case 3:
                        //foul
                        foulsCountTeam1++;
                        increasePlayerFouls(selectPos, 1);
                        break;
                    case 4:
                        //change
                        substitute(selectPos, 1);
                        break;
                    case 5:
                        //cancel
                        break;
                }
            }
        });
        builder.create().show();
    }


    private void showManagePlayersMenuPopUpTeam2(int pos) {
        String[] menu = {"1 คะแนน", "2 คะแนน", "3 คะแนน", "ฟาล์ว", "เปลี่ยนตัวผู้เล่น", "ยกเลิก"};
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
                        substitute(selectPos, 2);
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
        if (1 == team) {
            actionPlayer = lineupTeam1[selectPos];
        } else {
            actionPlayer = lineupTeam2[selectPos];
        }


        String time = (String) timeClock.getText();
        QuarterScoreSheet quarterScoreSheet = new QuarterScoreSheet();
        quarterScoreSheet.setQuarter(quarter);
        quarterScoreSheet.setPlayer(actionPlayer);
        quarterScoreSheet.setScoreCount(score);
        quarterScoreSheet.setTime(time);
        if(1==team){
            quarterScoreSheet.setSchoolId(DBSaveHelper.school1Id);
        }else{
            quarterScoreSheet.setSchoolId(DBSaveHelper.school2Id);
        }
        try {
            quarterScoreSheetDao.create(quarterScoreSheet);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void increasePlayerFouls(int selectPos, int team) {
        Player actionPlayer = null;
        if (1 == team) {
            actionPlayer = lineupTeam1[selectPos];
        } else {
            actionPlayer = lineupTeam2[selectPos];
        }
        try {
            List<QuarterPlayerInfo> quarterPlayerInfos = quarterInfoDao.queryBuilder().where().eq("quarter_id", quarter.getId()).and().eq("player_id", actionPlayer.getId()).query();
            if (1 != quarterPlayerInfos.size()) {
                System.out.println("Query error!");
                System.exit(-1);
            }
            QuarterPlayerInfo quarterPlayerInfo = quarterPlayerInfos.get(0);
            quarterInfoDao.refresh(quarterPlayerInfo);
            quarterPlayerInfo.setFoulsSummary(quarterPlayerInfo.getFoulsSummary() + 1);
            quarterInfoDao.update(quarterPlayerInfo);
            quarterInfoDao.refresh(quarterPlayerInfo);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void substitute( final int lineupPos, final int teamNumber) {
        getReservedPlayers(teamNumber);
        List<String> reservedPlayersName;
        if(teamNumber == 1)
            reservedPlayersName = getPlayersName(reservedPlayers1);
        else
            reservedPlayersName = getPlayersName(reservedPlayers2);

        ArrayAdapter<String> adapter = createListViewAdapter(reservedPlayersName);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                swapPlayers(lineupPos, teamNumber, which);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void getReservedPlayers(int teamNumber)
    {
        if(teamNumber == 1)
        {
            if(!reservedPlayers1.isEmpty())
                reservedPlayers1.clear();
            for(Player player : team1Players)
            {
                reservedPlayers1.add(player);
            }
            for( Player lineup : lineupTeam1)
            {
                reservedPlayers1.remove(lineup);
            }
        }
        else
        {
            if(!reservedPlayers2.isEmpty())
                reservedPlayers2.clear();
            for(Player player : team2Players)
            {
                reservedPlayers2.add(player);
            }
            for( Player lineup : lineupTeam2)
            {
                reservedPlayers2.remove(lineup);
            }
        }
    }

    private ArrayAdapter<String> createListViewAdapter( List<String> dataList )
    {
        return new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
    }

    private ArrayAdapter<String> createListViewAdapter( String[] dataString )
    {
        return new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataString);
    }

    private void swapPlayers(int lineupPos, int teamNumber, int reservedPos)
    {
        String[] lineupPlayersName = new String[5];
        if( teamNumber == 1)
        {
            Player currentPlayer = lineupTeam1[lineupPos];
            Player newPlayer = reservedPlayers1.get(reservedPos);
            lineupTeam1[lineupPos] = newPlayer;
            getLineupPlayersName(lineupTeam1, lineupPlayersName);
            listViewTeam1.setAdapter(createListViewAdapter(lineupPlayersName));
            substitutionSaving(currentPlayer, newPlayer , DBSaveHelper.school1Id);
        }
        else
        {
            Player currentPlayer = lineupTeam2[lineupPos];
            Player newPlayer = reservedPlayers2.get(reservedPos);
            lineupTeam2[lineupPos] = newPlayer;
            getLineupPlayersName(lineupTeam2, lineupPlayersName);
            listViewTeam2.setAdapter(createListViewAdapter(lineupPlayersName));
            substitutionSaving(currentPlayer, newPlayer,DBSaveHelper.school2Id );
        }
    }

    private void substitutionSaving(Player currentPlayer, Player newPlayer, int schoolId) {

        String time = (String) timeClock.getText();
        Substitution currentOut = new Substitution();
        currentOut.setPlayer(currentPlayer);
        currentOut.setType(SubstitutionType.OUT.name());
        currentOut.setQuarter(quarter);
        currentOut.setTime(time);
        currentOut.setSchoolId(schoolId);

        Substitution newIn = new Substitution();
        newIn.setPlayer(newPlayer);
        newIn.setType(SubstitutionType.IN.name());
        newIn.setQuarter(quarter);
        newIn.setTime(time);
        newIn.setSchoolId(schoolId);
        try {
            substitutionsDao.create(currentOut);
            substitutionsDao.create(newIn);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Substitution insert error!");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_match_recording, menu);
        return true;
    }


    /**
     * MenuMethod
     * @param item
     */
    public void showScoreDialog(MenuItem item) {




    }

    /**
     * MenuMethod
     * @param item
     */
    public void showSubstituteDialog(MenuItem item) throws SQLException {

        List<Substitution> school1SubList = substitutionsDao.queryBuilder().where().eq("schoolId", DBSaveHelper.school1Id).and().eq("quarter_id", quarter.getId()).query();
        List<Substitution> school2SubList = substitutionsDao.queryBuilder().where().eq("schoolId", DBSaveHelper.school2Id).and().eq("quarter_id", quarter.getId()).query();
        int sub1Size = school1SubList.size();
        int sub2Size = school1SubList.size();

        String[] school1PlayerNumberArray = new String[sub1Size];
        String[] school1TimeArray = new String[sub1Size];
        String[] school1FlagArray = new String[sub1Size];

        getPlayerNumberFromMatchPlayer(school1SubList,school1PlayerNumberArray);
        String[] school2PlayerNumberArray = new String[sub2Size];
        String[] school2TimeArray = new String[sub2Size];
        String[] school2FlagArray = new String[sub2Size];
        getPlayerNumberFromMatchPlayer(school2SubList,school2PlayerNumberArray);




        SubstitutionAdapter school1Adapter;



    }

    private void getPlayerNumberFromMatchPlayer(List<Substitution> schoolSubList, String[] schoolPlayerNumberArray) throws SQLException {

        for (Substitution substitution : schoolSubList) {
            matchPlayerDao.queryBuilder().where().eq("match_id", DBSaveHelper.match.getId()).and().eq("player_id", substitution.getPlayer().getId()).query();
        }



    }

    /**
     * MenuMethod
     * @param item
     */
    public void saveDataFinishRecording(MenuItem item) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(MatchRecordingActivity.this);
        builder.setMessage("บันทึกข้อมูลการบันทึกการแข่งขัน?");
        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                summarizeAllData();
                setSegue();
                //go back to quarter
                back();
            }
        });
        builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void setSegue() {
        switch (quarter.getQuaterNumber()) {
            case 1:
                SegueHelper.quarter1IsPlayed = true;
                System.out.println("quarter 1 is played");
                break;
            case 2:
                SegueHelper.quarter2IsPlayed = true;
                System.out.println("quarter 2 is played");
                break;
            case 3:
                SegueHelper.quarter3IsPlayed = true;
                System.out.println("quarter 3 is played");
                break;
            case 4:
                SegueHelper.quarter4IsPlayed = true;
                System.out.println("quarter 4 is played");
                break;
            case 5:
                SegueHelper.quarter5IsPlayed = true;
                System.out.println("quarter 5 is played");
                break;
        }
    }

    private void summarizeAllData() {

        finishLastPlayersSetSub();
        System.out.println("insert last set of player sub");
        summarizeSubstitutionToQuarterInfo();
        System.out.println("get all substitute done");
        saveFoulsAndTimeFromQuarterPlayerInfoAndsaveScoreSheetToMatchPlayer();
        System.out.println("summarize all to match player");
        saveQuarterInfo();
        System.out.println("summarize to quarter info");
    }


    /**
     * create substitution records for all last set of players
     */
    private void finishLastPlayersSetSub() {
        String time = (String) timeClock.getText();
        Substitution lastSet = null;
        for (Player player : lineupTeam1) {
            lastSet = new Substitution();
            lastSet.setPlayer(player);
            lastSet.setType(SubstitutionType.OUT.name());
            lastSet.setQuarter(quarter);
            lastSet.setTime(time);
            try {
                substitutionsDao.create(lastSet);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        for (Player player : lineupTeam2) {
            lastSet = new Substitution();
            lastSet.setPlayer(player);
            lastSet.setType(SubstitutionType.OUT.name());
            lastSet.setQuarter(quarter);
            lastSet.setTime(time);
            try {
                substitutionsDao.create(lastSet);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * summarize and calculate time spent.
     */
    private void summarizeSubstitutionToQuarterInfo() {

        List<Player> allPlayer = team1Players.subList(0, team1Players.size());
        allPlayer.addAll(team2Players);
        for (Player player : allPlayer) {
            int totalPlayTimeSecond = 0;
            try {
                List<Substitution> eachSub = substitutionsDao.queryBuilder().where().eq("player_id", player.getId()).and().eq("quarter_id", quarter.getId()).query();
                System.out.println("sub size for each : " + eachSub.size());
                if (eachSub.size() == 0 ) {
                    continue;
                } else if (eachSub.size() % 2 != 0) {
                    System.out.println("Substitution data error");
                    System.exit(-1);
                } else {
                    for (int i = 0; i < eachSub.size(); i += 2) {
                        Substitution thisSubstitutionIn = eachSub.get(i);
                        Substitution thisSubstitutionOut = eachSub.get(i + 1);
                        if (! (thisSubstitutionIn.getType().equals(SubstitutionType.IN.name()))  || ! (thisSubstitutionOut.getType().equals(SubstitutionType.OUT.name()))) {
                            System.out.println("Substitution data error IN/OUT not match");
                            System.exit(-1);
                        }
                        int inFieldTimeSecond = 0;
                        int outFieldTimeSecond = 0;
                        String timeIn = thisSubstitutionIn.getTime();
                        String timeOut = thisSubstitutionOut.getTime();
                        String team1TimeArray[] = timeIn.split(":");
                        String team2TimeArray[] = timeOut.split(":");
                        inFieldTimeSecond = Integer.parseInt(team1TimeArray[0]) * 60
                                + Integer.parseInt(team1TimeArray[1]);
                        outFieldTimeSecond = Integer.parseInt(team2TimeArray[0]) * 60
                                + Integer.parseInt(team2TimeArray[1]);
                        totalPlayTimeSecond = totalPlayTimeSecond + (outFieldTimeSecond - inFieldTimeSecond);
                    }
                    List<QuarterPlayerInfo> quarterPlayerInfos = quarterInfoDao.queryBuilder().where().eq("quarter_id", quarter.getId()).and().eq("player_id", player.getId()).query();
                    if (1 != quarterPlayerInfos.size()) {
                        System.out.println("Query error!");
                        System.exit(-1);
                    }
                    QuarterPlayerInfo quarterPlayerInfo = quarterPlayerInfos.get(0);
                    quarterInfoDao.refresh(quarterPlayerInfo);
                    quarterPlayerInfo.setTimeSpent(totalPlayTimeSecond);
                    quarterInfoDao.update(quarterPlayerInfo);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    /**
     * Summarize all data in scoresheet to get score and update in matchplayer.
     * get FoulsSummary in quarterPlayerInfo and update MatchPlayer.
     * scoresheet --> QuarterPlayerInfo , MatchPlayer++
     * quarterPlayerInfo(timespent , foulsSummary) --> MatchPlayer++
     */
    private void saveFoulsAndTimeFromQuarterPlayerInfoAndsaveScoreSheetToMatchPlayer() {

        List<Player> allPlayer = team1Players.subList(0, team1Players.size());
        allPlayer.addAll(team2Players);

        for (Player player : allPlayer) {

            int scoreSum = 0;

            int foulSum = 0;
            int timeSpentSec = 0;
            try {

                List<QuarterPlayerInfo> quarterPlayerInfos = quarterInfoDao.queryBuilder().where().eq("quarter_id", quarter.getId()).and().eq("player_id", player.getId()).query();
                if (quarterPlayerInfos.size() != 1) {
                    System.out.println("quarter info for player id : " + player.getId() + " not found.");
                    System.exit(-1);
                } else {
                    QuarterPlayerInfo quarterPlayerInfo = quarterPlayerInfos.get(0);
                    quarterInfoDao.refresh(quarterPlayerInfo);
                    foulSum = quarterPlayerInfo.getFoulsSummary();
                    timeSpentSec = quarterPlayerInfo.getTimeSpent();
                }
                List<QuarterScoreSheet> quarterScoreSheetList = quarterScoreSheetDao.queryBuilder().where().eq("quarter_id", quarter.getId()).and().eq("player_id", player.getId()).query();
                if (quarterScoreSheetList.size() <= 0) {
                    continue;
                } else {
                    for (QuarterScoreSheet eachScore : quarterScoreSheetList) {
                        scoreSum+=eachScore.getScoreCount();
                    }
                }

                //save to match player
                List<MatchPlayer> matchPlayerList = matchPlayerDao.queryBuilder().where().eq("match_id", DBSaveHelper.match.getId()).and().eq("player_id", player.getId()).query();
                if(matchPlayerList.size()!=1){
                    System.out.println("Match player query fail");
                    System.exit(-1);
                }else{

                    MatchPlayer matchPlayer = matchPlayerList.get(0);
                    matchPlayerDao.refresh(matchPlayer);
                    matchPlayer.setPlayerFouls(matchPlayer.getPlayerFouls() + foulSum);
                    matchPlayer.setTimeSpent(matchPlayer.getTimeSpent() + timeSpentSec);
                    matchPlayer.setScore(matchPlayer.getScore() + scoreSum);
                    matchPlayerDao.update(matchPlayer);
                    matchPlayerDao.refresh(matchPlayer);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }


        }
    }

    /**
     * Collect all data and set to this Quarter.
     */
    private void saveQuarterInfo() {
        quarter.setFoulA(foulsCountTeam1);
        quarter.setFoulB(foulsCountTeam2);
        quarter.setScoreA(scoreTeam1);
        quarter.setScoreB(scoreTeam2);
        try {
            quarterDao.update(quarter);
            quarterDao.refresh(quarter);
        } catch (SQLException e) {
            System.out.println("update quarter after save has some error!");
            System.out.println(e.getMessage());
        }

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

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(MatchRecordingActivity.this);
        builder.setMessage("การย้อนกลับจะยกเลิกข้อมูลของควอเตอร์นี้ทั้งหมด?");
        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteQuarterData();
                back();
            }
        });
        builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void back() {
        super.onBackPressed();
    }

    private void deleteQuarterData() {
        try {
            DeleteBuilder<QuarterPlayerInfo, Integer> quarterPlayerInfoIntegerDeleteBuilder = quarterInfoDao.deleteBuilder();
            quarterPlayerInfoIntegerDeleteBuilder.where().eq("quarter_id", quarter.getId());
            quarterPlayerInfoIntegerDeleteBuilder.delete();

            DeleteBuilder<Substitution, Integer> substitutionIntegerDeleteBuilder = substitutionsDao.deleteBuilder();
            substitutionIntegerDeleteBuilder.where().eq("quarter_id", quarter.getId());
            substitutionIntegerDeleteBuilder.delete();

            DeleteBuilder<QuarterScoreSheet, Integer> quarterScoreSheetIntegerDeleteBuilder = quarterScoreSheetDao.deleteBuilder();
            quarterScoreSheetIntegerDeleteBuilder.where().eq("quarter_id", quarter.getId());
            quarterPlayerInfoIntegerDeleteBuilder.delete();
            System.out.println("delete all quarter info. complete");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("delete fail on back pressed");
        }
    }
}

