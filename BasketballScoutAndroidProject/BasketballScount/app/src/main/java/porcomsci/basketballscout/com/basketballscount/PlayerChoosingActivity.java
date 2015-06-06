package porcomsci.basketballscout.com.basketballscount;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

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
import database.entities.School;
import porcomsci.basketballscout.com.basketballscount.utility.SegueHelper;


public class PlayerChoosingActivity extends ActionBarActivity {

    ListView listView;
    ArrayList<PlayerChoosingItem> itemList;
    List<Integer> selectedPosition = new ArrayList<>();
    List<Player> playerListFromDB;
    private DatabaseHelper databaseHelper = null;
    private int schoolId, matchId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_choosing);
        determineSchoolId();
        try {
            debugMatchPlayerValue();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        listView = (ListView) findViewById(R.id.player_choosing_listView);
        try {
            retrieveDataFromDB();
            refreshListView();
            setUpButtonAdd();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * clear all previous saved regarding inconsistency or error that might be caused by onBackPressed.
     * clear all data to prepare to save new one.
     */
    private void debugMatchPlayerValue() throws SQLException {

        System.out.println("count : "+getHelper().getMatchPlayerDao().countOf());
        List<MatchPlayer> matchPlayerList = getHelper().getMatchPlayerDao().queryForAll();
        for (MatchPlayer matchPlayer : matchPlayerList) {
            System.out.println("match  id : "+matchPlayer.getMatch().getId());
            System.out.println("player id : "+matchPlayer.getPlayer().getId());
            System.out.println("school id : "+matchPlayer.getSchoolId());
        }

    }

    /**
     * overide hardware back button
     */
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        deleteExistingRecords();
        SegueHelper.playerChoosingSequence--;
    }


    public void deleteExistingRecords(){
        Dao<MatchPlayer, Integer> matchPlayerDao = null;
        try {
            matchPlayerDao = getHelper().getMatchPlayerDao();
            DeleteBuilder<MatchPlayer, Integer> deleteBuilder = matchPlayerDao.deleteBuilder();
            deleteBuilder.where().eq("match_id",DBSaveHelper.match).and().eq("schoolId", schoolId);
            deleteBuilder.delete();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * find school id from application flow then set the school name to title bar.
     */
    private void determineSchoolId() {
        if(SegueHelper.playerChoosingSequence==1){

            getSupportActionBar().setTitle(DBSaveHelper.school1.getName());
            schoolId = DBSaveHelper.school1Id;

        }else if(SegueHelper.playerChoosingSequence==2){

            getSupportActionBar().setTitle(DBSaveHelper.school2.getName());
            schoolId = DBSaveHelper.school2Id;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_competitor_choosing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.button_ToNextActivity) {
            if(isPassAllConditions())
            {
                try {
                    saveData();
                    showStartUpPlayerPopUp();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void retrieveDataFromDB() throws SQLException {
        matchId = DBSaveHelper.match.getId();

        itemList = new ArrayList<>();
        Dao<Player,Integer> playerDao = getHelper().getPlayerDao();
        System.out.println("*********************schoolId for saint do : "+schoolId);
        playerListFromDB = playerDao.queryBuilder().where().
                eq("school_id",schoolId).query();
        for (Player player : playerListFromDB) {
            itemList.add(new PlayerChoosingItem(player.getName(),""));
        }   
    }

    private void setUpButtonAdd(){
        Button buttonAdd = (Button) findViewById(R.id.player_choosing_button_add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPlayerNameDialog();
            }
        });
    }

    private void showAddPlayerNameDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText playerName = new EditText(this);
        playerName.setWidth(250);
        builder.setView(playerName)
                .setTitle(R.string.add_new_player_name)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            addNewName(playerName.getText().toString());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create().show();
    }

    private void addNewName(String newPlayerName) throws SQLException {
        if(newPlayerName.length() > 0)
        {
            School school = new School();
            school.setId(schoolId);
            Player newPlayer = new Player();
            newPlayer.setName(newPlayerName);
            newPlayer.setSchool(school);
            getHelper().getPlayerDao().create(newPlayer);
            refreshListView();
        }
    }

    private void refreshListView() throws SQLException {
        retrieveDataFromDB();
        listView.setAdapter(new PlayerChoosingListAdapter(this, itemList));
        selectedPosition.clear();
    }

    private boolean isPassAllConditions(){
        selectedPosition.clear();
        for(int i=0; i<listView.getCount(); i++)
        {
            CheckBox checkBox = (CheckBox) listView.getChildAt(i).findViewById(R.id.player_name_and_number_list_item_checkBox);
            if(checkBox.isChecked())
            {
                selectedPosition.add(i);
            }
        }
        if(selectedPosition.size() < 5)
        {
            showAlertDialog("กรุณาเลือกผู้เล่นตั้งแต่ 5 คนขึ้นไป");
            return false;
        }
        else
        {
            for( int listPosition : selectedPosition )
            {
                String playerNumberText = getPlayerNumberAtListPosition(listPosition);
                if(playerNumberText.length() == 0)
                {
                    showAlertDialog("กรุณากรอกหมายเลขผู้เล่นที่ถูกเลือกให้ครบ");
                    return false;
                }
            }
        }
        return true;
    }

    private void saveData() throws SQLException {
        for( int listPosition : selectedPosition)
        {
            // save players number
            String playerNumberText = getPlayerNumberAtListPosition(listPosition);
            if(playerNumberText.length() != 0)
            {
                Player player = playerListFromDB.get(listPosition);
                Integer playerNumber = Integer.parseInt( playerNumberText );
                MatchPlayer matchPlayer = new MatchPlayer();
                matchPlayer.setPlayer(player);
                matchPlayer.setPlayerNumber(playerNumber);
                matchPlayer.setSchoolId(schoolId);
                matchPlayer.setMatch(DBSaveHelper.match);
                getHelper().getMatchPlayerDao().create(matchPlayer);
            }

        }
    }

    private void showAlertDialog(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String getPlayerNumberAtListPosition (int position) {
        EditText playerNumberEditText = (EditText) listView.getChildAt(position).findViewById(R.id.player_name_and_number_list_item_player_number_editText);
        return playerNumberEditText.getText().toString();
    }

    private void showStartUpPlayerPopUp()
    {
        String[] playerName =  getSelectedPlayerName();
        final ArrayList<Integer> selectedItems = new ArrayList();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("เลือก 5 ผู้เล่นตัวจริง")
                .setMultiChoiceItems( playerName, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if( isChecked )
                        {
                            selectedItems.add( which );
                        }
                        else
                        {
                            selectedItems.remove( which );
                        }
                    }
                })
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                saveStartUpPlayer( selectedItems );
                                //go to next activity
                                SegueHelper.playerChoosingSequence++;
                                if(SegueHelper.playerChoosingSequence==2) {
                                    Intent intent = new Intent(getApplicationContext(), PlayerChoosingActivity.class);
                                    startActivity(intent);
                                }else if(SegueHelper.playerChoosingSequence>2){
                                    Intent intent = new Intent(getApplicationContext(), QuarterChoosingActivity.class);
                                    startActivity(intent);
                                }
                            }
                        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private String[] getSelectedPlayerName()
    {
//        List<MatchPlayer> selectedPlayerList = getSelectedPlayerList();
//        String[] playerName = new String[selectedPlayerList.size()];
//        for(int i=0; i<selectedPlayerList.size(); i++)
//        {
//            playerName[i] = selectedPlayerList.get(i).getPlayer().getName();
//        }
//        return playerName;
        return new String[1];
    }


    private List<MatchPlayer> getSelectedPlayerList()
    {
        /**
         * Retrieve selected player of this match
         */
//        List<MatchPlayer> selectedPlayerList = //Retrieve MatchPlayer here// ;
//        return selectedPlayerList;
        return new ArrayList<>();
    }

    private void saveStartUpPlayer( ArrayList<Integer> selectedItems )
    {
        List<MatchPlayer> selectedPlayerList = getSelectedPlayerList();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.button_ToNextActivity) {
            if(isPassAllConditions())
            {
                try {
                    deleteExistingRecords();
                    saveData();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                SegueHelper.playerChoosingSequence++;
                if(SegueHelper.playerChoosingSequence==2) {
                    Intent intent = new Intent(getApplicationContext(), PlayerChoosingActivity.class);
                    startActivity(intent);
                }else if(SegueHelper.playerChoosingSequence>2){
                    Intent intent = new Intent(getApplicationContext(), QuarterChoosingActivity.class);
                    startActivity(intent);
                }
            }
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
