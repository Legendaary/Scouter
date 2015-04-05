package porcomsci.basketballscout.com.basketballscount;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import database.DatabaseHelper;
import database.entities.Match;
import database.entities.School;
import database.entities.Tournament;


public class MatchHistoryActivity extends ActionBarActivity {

    private String tournamentId;
    private DatabaseHelper databaseHelper = null;
    ListView matchListView;
    List<String> matchList = new ArrayList<>();
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_history);
        tournamentId = getIntent().getStringExtra("tournamentId");
        try {
            Dao<Match, Integer> matchDao = getHelper().getMatchDao();
            //use query for foreign.
            List<Match> retrievedList = matchDao.queryBuilder().where().
                    eq("tournament_id",tournamentId).query();
            for (Match match : retrievedList) {
                matchDao.refresh(match);
                Dao<School, Integer> schoolDao = getHelper().getSchoolDao();
                School schoolA = match.getSchoolA();
                School schoolB = match.getSchoolB();
                schoolDao.refresh(schoolA);
                schoolDao.refresh(schoolB);
                matchList.add(schoolA.getName()+" - "+schoolB.getName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, matchList);
        matchListView = (ListView) findViewById(R.id.match_history_listview);
        matchListView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_match_history, menu);
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
}
