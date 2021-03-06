package porcomsci.basketballscout.com.basketballscount.mainflow;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import database.DBSaveHelper;
import database.DatabaseHelper;
import database.entities.Match;
import database.entities.Tournament;
import porcomsci.basketballscout.com.basketballscount.R;


public class MatchActivity extends ActionBarActivity {

    private String tournamentId;
    private DatabaseHelper databaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("รายละเอียดการแข่งขัน");
        setContentView(R.layout.activity_match);
        tournamentId = String.valueOf(DBSaveHelper.tournament.getId());
    }//end on create

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
            try {
                saveDataAndGo();
            } catch (SQLException e) {
                e.printStackTrace();
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

    private void saveDataAndGo() throws SQLException {

        //save data
        Dao<Match, Integer> matchDao = getHelper().getMatchDao();
        Tournament parent = DBSaveHelper.tournament;
        Match childMatch = new Match();
        saveValueAndCheckNull(childMatch);
        childMatch.setTournament(parent);
        matchDao.create(childMatch);
        matchDao.refresh(childMatch);
        DBSaveHelper.match = childMatch;
        Intent intent = new Intent(getApplicationContext(), CompetitorChoosingActivity.class);
        startActivity(intent);
    }

    private void saveValueAndCheckNull(Match childMatch) {

        EditText matchNumberTextBox = (EditText) findViewById(R.id.match_matchNumber_editText);
        EditText placeTextBox = (EditText) findViewById(R.id.match_place_editText);
        EditText refereeTextBox = (EditText) findViewById(R.id.match_referee_editText);
        EditText umpireTextBox = (EditText) findViewById(R.id.match_umpire_editText);

        Editable text = matchNumberTextBox.getText();
        String matchNumber = text.toString();
        if(!matchNumber.equalsIgnoreCase("")) {
            childMatch.setMatchNumber(Integer.valueOf(matchNumberTextBox.getText().toString()));
        }
        if(null != placeTextBox.getText()) {
            childMatch.setPlace(placeTextBox.getText().toString());
        }
        if(null != refereeTextBox.getText()) {
            childMatch.setReferee(refereeTextBox.getText().toString());
        }
        if(null != umpireTextBox.getText()) {
            childMatch.setUmpire(umpireTextBox.getText().toString());
        }

        EditText dateTextBox = (EditText) findViewById(R.id.match_date_editText);
        EditText timeTextBox = (EditText) findViewById(R.id.match_time_editText);

    }

    private void setUpDateDialog() {

        EditText dateTextBox = (EditText) findViewById(R.id.match_date_editText);
        dateTextBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                datePickerDialog.show();
            }
        });

    }

}