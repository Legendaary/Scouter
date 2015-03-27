package porcomsci.basketballscout.com.basketballscount;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import database.DatabaseSaveHelperDTO;


public class TournamentActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);

        Button buttonNext =  (Button) findViewById(R.id.button_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * create new tournament object set all value the create.
                 * set this tournament to DatabaseSaveHelperDTO.tournament.setId();
                 */
                saveData();
                Intent intent = new Intent(getApplicationContext(),MatchActivity.class);
                startActivity(intent);
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

    private void saveData()
    {
        EditText tournamentTextBox = (EditText) findViewById(R.id.tournament_tournament_name_editText);
        EditText numOfMatchesTextBox = (EditText) findViewById(R.id.tournament_number_of_matches_editText);
        /**
         * Implement to save Tournament Name, number of matches here
         */
//        Whatever TournamentName = tournamentTextBox.getText().toString();
//        Whatever numOfMatches = numOfMatchesTextBox.getText().toString();

    }
}
