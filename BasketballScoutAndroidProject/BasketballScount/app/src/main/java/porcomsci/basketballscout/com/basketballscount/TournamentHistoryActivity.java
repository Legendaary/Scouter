package porcomsci.basketballscout.com.basketballscount;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseHelper;
import database.entities.Tournament;


public class TournamentHistoryActivity extends ActionBarActivity {

    private DatabaseHelper databaseHelper = null;
    private ListView tournamentListView;
    private List<String> tournamentList = new ArrayList<>();
    private Dao<Tournament, Integer> tournamentDao = null;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_history);

        try {
            tournamentDao = getHelper().getTournamentDao();
            List<Tournament> retrievedList = tournamentDao.queryForAll();
            for (Tournament tournament : retrievedList) {
                tournamentList.add(tournament.getCompetitionName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, tournamentList);
        tournamentListView = (ListView) findViewById(R.id.tournament_history_list);
        tournamentListView.setAdapter(adapter);
        /**
         * set onclick listener for each tournament show.
         * when click , send position with intent (the position also imply the id of tournament.)
         */
        tournamentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MatchHistoryActivity.class);
                String tournamentId = "";
                try {
                    List<Tournament> retrievedList = tournamentDao.queryBuilder().where().
                            eq("competitionName", tournamentList.get(position)).query();
                   tournamentId =  String.valueOf(retrievedList.get(0).getId());

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                intent.putExtra("tournamentId", tournamentId);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tournament_history, menu);
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

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }
}//end class
