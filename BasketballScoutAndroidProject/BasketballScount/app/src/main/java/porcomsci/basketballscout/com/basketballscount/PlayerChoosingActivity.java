package porcomsci.basketballscout.com.basketballscount;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import database.DBSaveHelper;
import database.DatabaseHelper;
import database.entities.MatchPlayer;
import database.entities.Player;
import database.entities.School;


public class PlayerChoosingActivity extends ActionBarActivity {

    ListView listView;
    ArrayList<PlayerChoosingItem> itemList;
    List<Integer> selectedPosition = new ArrayList<>();
    List<Player> playerListFromDB;
    EditText editText;
    private DatabaseHelper databaseHelper = null;
    private int schoolId, matchId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_choosing);
        determineSchoolId();
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
     * find school id from application flow then set the school name to title bar.
     */
    private void determineSchoolId() {
        if(DBSaveHelper.playerChoosingSequence==1){
            schoolId = DBSaveHelper.school1Id;
            setTitleFromSchoolId();
        }else if(DBSaveHelper.playerChoosingSequence==2){
            schoolId = DBSaveHelper.school2Id;
            setTitleFromSchoolId();
        }
    }

    private void setTitleFromSchoolId() {
        School school = null;
        try {
            school = getHelper().getSchoolDao().queryForId(schoolId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getSupportActionBar().setTitle(school.getName());
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
                DBSaveHelper.playerChoosingSequence = DBSaveHelper.playerChoosingSequence+1;
                if(DBSaveHelper.playerChoosingSequence==2) {
                    Intent intent = new Intent(getApplicationContext(), PlayerChoosingActivity.class);
                    startActivity(intent);
                }else if(DBSaveHelper.playerChoosingSequence>2){
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
        editText = (EditText) findViewById(R.id.player_choosing_editText);
        Button buttonAdd = (Button) findViewById(R.id.player_choosing_button_add);
        buttonAdd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addNewName();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                editText.setText("");
            }
        });
    }

    private void addNewName() throws SQLException {
        String newPlayerName = editText.getText().toString();
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
        builder.setPositiveButton("ตกลง",
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
