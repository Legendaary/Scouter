package porcomsci.basketballscout.com.basketballscount.mainflow;

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
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DBSaveHelper;
import database.DatabaseHelper;
import database.entities.Match;
import database.entities.MatchPlayer;
import database.entities.Player;
import database.entities.School;
import porcomsci.basketballscout.com.basketballscount.R;
import porcomsci.basketballscout.com.basketballscount.utility.DialogUtil;
import porcomsci.basketballscout.com.basketballscount.utility.SegueHelper;


public class PlayerChoosingActivity extends ActionBarActivity {

    ListView listView;
    ArrayList<PlayerChoosingItem> itemList;
    List<Integer> selectedPosition = new ArrayList<>();
    List<Player> playerListFromDB;
    Dao<MatchPlayer, Integer> matchPlayerDao = null;
    private DatabaseHelper databaseHelper = null;
    private int schoolId, matchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_choosing);
        try {
            matchPlayerDao = getHelper().getMatchPlayerDao();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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
     * overide hardware back button
     */
    @Override
    public void onBackPressed() {
        deleteExistingRecords();
        SegueHelper.playerChoosingSequence--;
        super.onBackPressed();
    }


    public void deleteExistingRecords() {

        try {
            DeleteBuilder<MatchPlayer, Integer> deleteBuilder = matchPlayerDao.deleteBuilder();
            deleteBuilder.where().eq("match_id", DBSaveHelper.match.getId()).and().eq("schoolId", schoolId);
            deleteBuilder.delete();

            //debug purpose
            List<MatchPlayer> match_id = matchPlayerDao.queryForEq("match_id", matchId);
            System.out.println("PlayerChoosingActivity --> deleteExisting method --> matchPlayer record count for this match and school : " + match_id.size());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * find school id from application flow then set the school name to title bar.
     */
    private void determineSchoolId() {
        if (SegueHelper.playerChoosingSequence == 1) {

            getSupportActionBar().setTitle(DBSaveHelper.school1.getName());
            schoolId = DBSaveHelper.school1Id;

        } else if (SegueHelper.playerChoosingSequence == 2) {

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
            if (isPassAllConditions()) {
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
        Dao<Player, Integer> playerDao = getHelper().getPlayerDao();
        playerListFromDB = playerDao.queryBuilder().where().
                eq("school_id", schoolId).query();
        for (Player player : playerListFromDB) {
            itemList.add(new PlayerChoosingItem(player.getName(), ""));
        }
    }

    private void setUpButtonAdd() {
        Button buttonAdd = (Button) findViewById(R.id.player_choosing_button_add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPlayerNameDialog();
            }
        });
    }

    private void showAddPlayerNameDialog() {
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
        if (newPlayerName.length() > 0) {
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

    }

    private boolean isPassAllConditions() {
        selectedPosition.clear();
        for (int i = 0; i < listView.getCount(); i++) {
            CheckBox checkBox = (CheckBox) listView.getChildAt(i).findViewById(R.id.player_name_and_number_list_item_checkBox);
            if (checkBox.isChecked()) {
                selectedPosition.add(i);
            }
        }

        if (selectedPosition.size() < 5) {
            DialogUtil.showOkDialog(this, "กรุณาเลือกผู้เล่นตั้งแต่ 5 คนขึ้นไป");
            selectedPosition.clear();
            return false;
        } else {
            for (int listPosition : selectedPosition) {
                String playerNumberText = getPlayerNumberAtListPosition(listPosition);
                if (playerNumberText.length() == 0) {
                    DialogUtil.showOkDialog(this, "กรุณากรอกหมายเลขผู้เล่นที่เลือกให้ครบ");
                    selectedPosition.clear();
                    return false;
                }
            }
        }
        return true;
    }

    private void saveData() throws SQLException {

        deleteExistingRecords();
        for (int listPosition : selectedPosition) {
            // save players number
            String playerNumberText = getPlayerNumberAtListPosition(listPosition);
            if (playerNumberText.length() != 0) {
                Player player = playerListFromDB.get(listPosition);
                Integer playerNumber = Integer.parseInt(playerNumberText);
                MatchPlayer matchPlayer = new MatchPlayer();
                matchPlayer.setPlayer(player);
                matchPlayer.setPlayerNumber(playerNumber);
                matchPlayer.setSchoolId(schoolId);
                matchPlayer.setMatch(DBSaveHelper.match);
                 matchPlayerDao.create(matchPlayer);
            }
        }
        //debug purpose
        List<MatchPlayer> match_id = matchPlayerDao.queryForEq("match_id", matchId);
        System.out.println("PlayerChoosingActivity --> savedata method --> matchPlayer record count for this match and school : " + match_id.size());
    }


    private String getPlayerNumberAtListPosition(int position) {
        EditText playerNumberEditText = (EditText) listView.getChildAt(position).findViewById(R.id.player_name_and_number_list_item_player_number_editText);
        return playerNumberEditText.getText().toString();
    }

    private void showStartUpPlayerPopUp() {
        String[] playerName = getSelectedPlayerName();
        final ArrayList<Integer> selectedItems = new ArrayList<>();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("เลือก 5 ผู้เล่นตัวจริง")
                .setMultiChoiceItems(playerName, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            selectedItems.add(which);
                        } else {
                            selectedItems.remove(which);
                        }
                    }
                })
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (selectedItems.size() == 5) {
                                    saveStartUpPlayer(selectedItems);
                                    //go to next activity
                                    SegueHelper.playerChoosingSequence++;
                                    if (SegueHelper.playerChoosingSequence == 2) {
                                        Intent intent = new Intent(getApplicationContext(), PlayerChoosingActivity.class);
                                        startActivity(intent);
                                    } else if (SegueHelper.playerChoosingSequence > 2) {
                                        Intent intent = new Intent(getApplicationContext(), QuarterChoosingActivity.class);
                                        startActivity(intent);
                                    }
                                } else {
                                    DialogUtil.showOkDialog( builder.getContext(), "กรุณาเลือก 5 ผู้เล่นตัวจริง" );
                                }//end check selectedItem size
                            }//end on click
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

    private String[] getSelectedPlayerName() {
        List<MatchPlayer> selectedPlayerList = getSelectedPlayerList();
        String[] playerName = new String[selectedPlayerList.size()];
        for (int i = 0; i < selectedPlayerList.size(); i++) {
            playerName[i] = selectedPlayerList.get(i).getPlayer().getName();
        }
        return playerName;
    }


    private List<MatchPlayer> getSelectedPlayerList() {

        List<MatchPlayer> selectedPlayerList = null;
        try {
            QueryBuilder<MatchPlayer, Integer> matchPlayerIntegerQueryBuilder = matchPlayerDao.queryBuilder();
            matchPlayerIntegerQueryBuilder.where().eq("match_id", DBSaveHelper.match).and().eq("schoolId", schoolId);
            selectedPlayerList = matchPlayerIntegerQueryBuilder.query();
            refreshAllDataOfMatchPlayer(selectedPlayerList);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return selectedPlayerList;
    }

    private void refreshAllDataOfMatchPlayer(List<MatchPlayer> selectedPlayerList) throws SQLException {
        for (MatchPlayer matchPlayer : selectedPlayerList) {
            matchPlayerDao.refresh(matchPlayer);
            Match match = matchPlayer.getMatch();
            Player player = matchPlayer.getPlayer();
            getHelper().getMatchDao().refresh(match);
            getHelper().getPlayerDao().refresh(player);
        }
    }

    private void saveStartUpPlayer(ArrayList<Integer> selectedItems) {
        List<MatchPlayer> selectedPlayerList = getSelectedPlayerList();
        for (Integer selectedItem : selectedItems) {
            selectedPlayerList.get(selectedItem).setIsStartPlayer(true);
        }
        try {

            for (MatchPlayer matchPlayer : selectedPlayerList) {
                    matchPlayerDao.update(matchPlayer);
            }
        } catch (SQLException e) {
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





}
