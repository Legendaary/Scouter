package porcomsci.basketballscout.com.basketballscount;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
            clearAllPreviousSaveForMatchSchoolPlayer();
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
    private void clearAllPreviousSaveForMatchSchoolPlayer() throws SQLException {

        Dao<MatchPlayer, Integer> matchPlayerDao = getHelper().getMatchPlayerDao();
        DeleteBuilder<MatchPlayer, Integer> deleteBuilder = matchPlayerDao.deleteBuilder();
        deleteBuilder.where().eq("match_id",DBSaveHelper.match).and().eq("schoolId", schoolId);
        deleteBuilder.delete();
    }

    /**
     * overide hardware back button
     */
    public void onBackPressed(){
        SegueHelper.playerChoosingSequence--;
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
        buttonAdd.setOnClickListener( new View.OnClickListener() {
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
//                matchPlayer.setPlayer_number(playerNumber);
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
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String getPlayerNumberAtListPosition (int position) {
        EditText playerNumberEditText = (EditText) listView.getChildAt(position).findViewById(R.id.player_name_and_number_list_item_player_number_editText);
        return playerNumberEditText.getText().toString();
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
