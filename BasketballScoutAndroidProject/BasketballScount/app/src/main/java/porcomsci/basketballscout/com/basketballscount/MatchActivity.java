package porcomsci.basketballscout.com.basketballscount;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import database.DatabaseHelper;
import database.entities.Match;
import database.entities.Tournament;


public class MatchActivity extends ActionBarActivity {

    private String tournamentId;
    private DatabaseHelper databaseHelper = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        tournamentId = getIntent().getStringExtra("tournamentId");
        Button buttonNext = (Button) findViewById(R.id.button_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * set match info on Match object in save helper below here before startNewActivity;
                 * e.g., DatabaseSaveHelperDTO.match.set();
                 */
                try {
                    saveDataAndGo();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }//end on create

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_match, menu);
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
    private void saveDataAndGo() throws SQLException {

        EditText matchNumberTextBox = (EditText) findViewById(R.id.match_matchNumber_editText);
        EditText dateTextBox = (EditText) findViewById(R.id.match_date_editText);
        EditText timeTextBox = (EditText) findViewById(R.id.match_time_editText);
        EditText placeTextBox = (EditText) findViewById(R.id.match_place_editText);
        EditText refereeTextBox = (EditText) findViewById(R.id.match_referee_editText);
        EditText umpireTextBox = (EditText) findViewById(R.id.match_umpire_editText);

        //save data
        Dao<Match, Integer> matchDao =  getHelper().getMatchDao();
        Tournament parent = (Tournament)getIntent().getSerializableExtra("tournament");
        Match childMatch = new Match();
        childMatch.setMatchNumber(Integer.valueOf(matchNumberTextBox.getText().toString()));
        childMatch.setPlace(placeTextBox.getText().toString());
        childMatch.setReferee(refereeTextBox.getText().toString());
        childMatch.setTournament(parent);
        matchDao.create(childMatch);
        matchDao.refresh(childMatch);
        Intent intent = new Intent(getApplicationContext(),CompetitorChoosingActivity.class);
        intent.putExtra("match",childMatch);
        startActivity(intent);
    }

    private void setUpDateDialog()
    {

        EditText dateTextBox = (EditText) findViewById(R.id.match_date_editText);
        dateTextBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                datePickerDialog.show();
            }
        });

    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }


}
