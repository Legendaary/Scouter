package porcomsci.basketballscout.com.basketballscount.history;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import database.entities.Match;
import database.entities.School;
import porcomsci.basketballscout.com.basketballscount.R;


public class MatchHistoryActivity extends ActionBarActivity {

    private String tournamentId;
    private DatabaseHelper databaseHelper = null;
    private Dao<School, Integer> schoolDao;
    private List<Match> matchList;

    ListView matchListView;
    List<String> matchNameList = new ArrayList<>();
    List<Integer> matchIdList = new ArrayList<>();
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_history);
        tournamentId = HistorySegue.historyTournamentId;
        try {
            schoolDao = getHelper().getSchoolDao();
            Dao<Match, Integer> matchDao = getHelper().getMatchDao();
            //use query for foreign.
            matchList = matchDao.queryBuilder().where().
                    eq("tournament_id", tournamentId).query();
            for (Match match : matchList) {
                matchDao.refresh(match);
                School schoolA = match.getSchoolA();
                School schoolB = match.getSchoolB();
                schoolDao.refresh(schoolA);
                schoolDao.refresh(schoolB);
                matchIdList.add(match.getId());
                matchNameList.add(schoolA.getName() + " vs " + schoolB.getName() + " - " + match.getTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, matchNameList);
        matchListView = (ListView) findViewById(R.id.match_history_listview);
        matchListView.setAdapter(adapter);
        /**
         * set onclick listener for each tournament show.
         * when click , send position with intent (the position also imply the id of tournament.)
         */
        matchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MatchHistoryActivity.class);
                try {
                    HistorySegue.schoolAHistory = matchList.get(position).getSchoolA();
                    HistorySegue.schoolBHistory = matchList.get(position).getSchoolB();
                    HistorySegue.matchHistory = matchList.get(position);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                startActivity(intent);
            }
        });
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
