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

import database.DBSaveHelper;
import database.DatabaseHelper;
import database.entities.Tournament;


public class TournamentActivity extends ActionBarActivity {

    private DatabaseHelper databaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);

        Button buttonNext =  (Button) findViewById(R.id.button_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveDataAndSendIdToNextActivity();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tournament, menu);
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

    /**
     * create new tournament object set all value to it. then save and get Id.
     * send the id to next activity.
     */
    private void saveDataAndSendIdToNextActivity() throws SQLException {
        EditText tournamentTextBox = (EditText) findViewById(R.id.tournament_tournament_name_editText);
        EditText numOfMatchesTextBox = (EditText) findViewById(R.id.tournament_number_of_matches_editText);
        Tournament tournament  = new Tournament();
        tournament.setCompetitionName( tournamentTextBox.getText().toString());
        tournament.setMatchNumber(Integer.valueOf(numOfMatchesTextBox.getText().toString()));
        //save and get id
        Dao<Tournament,Integer> tournamentDao = getHelper().getTournamentDao();
        tournamentDao.create(tournament);
        tournament = tournamentDao.queryForSameId(tournament);
        Intent intent = new Intent(getApplicationContext(),MatchActivity.class);
        DBSaveHelper.tournament = tournament;
        startActivity(intent);
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
