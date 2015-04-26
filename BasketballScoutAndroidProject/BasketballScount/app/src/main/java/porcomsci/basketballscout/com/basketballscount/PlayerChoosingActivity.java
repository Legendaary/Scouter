package porcomsci.basketballscout.com.basketballscount;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
    List<Integer> selectedPosition, playerIdFromDB;
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
            setListViewOnItemClick();
            refreshListView();
            setUpButtonAdd();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

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
            // I don't understand this if condition
            // what is the playerChoosingSequence mean?
            DBSaveHelper.playerChoosingSequence = DBSaveHelper.playerChoosingSequence+1;
            if(DBSaveHelper.playerChoosingSequence==2) {
                Intent intent = new Intent(getApplicationContext(), PlayerChoosingActivity.class);
                startActivity(intent);
            }else if(DBSaveHelper.playerChoosingSequence>2){
                // the next activity is QuarterChoosingActivity right?
                // so, I put this code to check conditions and save data
                // before go to the next activity
                if(isPassAllConditions())
                {
                    saveData();
                    Intent intent = new Intent(getApplicationContext(), QuarterChoosingActivity.class);
                    startActivity(intent);
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void retrieveDataFromDB() throws SQLException {
        itemList = new ArrayList<>();

        Dao<Player,Integer> playerDao = getHelper().getPlayerDao();
        System.out.println("*********************schoolId for saint do : "+schoolId);
        List<Player> retrievedList = playerDao.queryBuilder().where().
                eq("school_id",schoolId).query();
        List<String> playerNameFromDB = new ArrayList<>();
        playerIdFromDB = new ArrayList<>();
        for (Player player : retrievedList) {
            playerNameFromDB.add(player.getName());
            /**
             * retrieve player id or PK here
             */
//            playerIdFromDB.add(ID);
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

    private void setListViewOnItemClick(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox listItemCheckBox = (CheckBox) view.findViewById(R.id.player_name_and_number_list_item_checkBox);
                if( !listItemCheckBox.isChecked() )
                {
                    listItemCheckBox.setChecked(true);
                    selectedPosition.add(position);
                }
                else
                {
                    listItemCheckBox.setChecked(false);
                    selectedPosition.remove(selectedPosition.indexOf(position));
                }
            }
        });

    }

    private boolean isPassAllConditions(){
        if(selectedPosition.size() < 5)
        {
            showAlertDialog("กรุณาเลือกผู้เล่นตัวจริงให้ครบ 5 คน");
            return false;
        }
        else if(selectedPosition.size() > 5)
        {
            showAlertDialog("กรุณาเลือกผู้เล่นตัวจริงเพียง 5 คนเท่านั้น");
            return false;
        }
        else
        {
            for(int listPosition : selectedPosition )
            {
                String playerNumberText = String.valueOf( listView.getChildAt(listPosition).findViewById(R.id.player_name_and_number_list_item_player_number_editText) );
                if(playerNumberText.length() == 0)
                {
                    showAlertDialog("กรุณากรอกหมายเลขของผู้เล่นตัวจริงให้ครบ 5 คน");
                    return false;
                }
            }
        }
        return true;
    }

    private void saveData()
    {
        for(int i=0; i<listView.getCount(); i++)
        {
            // save players number
            String playerNumberText = String.valueOf( listView.getChildAt(i).findViewById(R.id.player_name_and_number_list_item_player_number_editText) );
            if(playerNumberText.length() == 0)
            {
                int playerId = playerIdFromDB.get(i);
                int playerNumber = Integer.parseInt( playerNumberText );
                /**
                 * implement to save player number here
                 */
                //add playerNumber at playerId
            }

            //save 5 selected players ID
            for(int position : selectedPosition)
            {
                int playerId = playerIdFromDB.get(position);
                /**
                 * implement to 5 selected players ID here
                 */
                // set flag y at playerId
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
