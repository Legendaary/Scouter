package porcomsci.basketballscout.com.basketballscount;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DBSaveHelper;
import database.DatabaseHelper;
import database.entities.Player;
import database.entities.School;


public class PlayerChoosingActivity extends ActionBarActivity {

    ListView listView;
    ArrayList<PlayerChoosingItem> itemList;
    EditText editText;
    private DatabaseHelper databaseHelper = null;
    private int schoolId;
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

    private void determineSchoolId() {
        if(DBSaveHelper.playerChoosingSequence==1){
            schoolId = DBSaveHelper.school1Id;
        }else if(DBSaveHelper.playerChoosingSequence==2){
            schoolId = DBSaveHelper.school2Id;
        }
        setTitleFromSchoolId();

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
            DBSaveHelper.playerChoosingSequence = DBSaveHelper.playerChoosingSequence+1;
            if(DBSaveHelper.playerChoosingSequence==2) {
                Intent intent = new Intent(getApplicationContext(), PlayerChoosingActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(getApplicationContext(), QuarterChoosingActivity.class);
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void retrieveDataFromDB() throws SQLException {
        itemList = new ArrayList<>();
        /**
         * retrieve player name and number here
         */
        Dao<Player,Integer> playerDao = getHelper().getPlayerDao();
        System.out.println("*********************schoolId for saint do : "+schoolId);
        List<Player> retrievedList = playerDao.queryBuilder().where().
                eq("school_id",schoolId).query();
        List<String> playerNameFromDB = new ArrayList<>();
        for (Player player : retrievedList) {
            playerNameFromDB.add(player.getName());
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

        String newPlayerName =   editText.getText().toString();
        School school = new School();
        school.setId(schoolId);
        Player newPlayer = new Player();
        newPlayer.setName(newPlayerName);
        newPlayer.setSchool(school);
        getHelper().getPlayerDao().create(newPlayer);
        refreshListView();
    }

    private void refreshListView() throws SQLException {
        retrieveDataFromDB();
        listView.setAdapter(new PlayerChoosingListAdapter(this, itemList));
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
