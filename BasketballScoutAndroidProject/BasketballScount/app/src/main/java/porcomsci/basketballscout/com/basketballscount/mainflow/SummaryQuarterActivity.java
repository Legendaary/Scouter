package porcomsci.basketballscout.com.basketballscount.mainflow;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import database.DBSaveHelper;
import database.DatabaseHelper;
import database.entities.Quarter;
import porcomsci.basketballscout.com.basketballscount.R;

public class SummaryQuarterActivity extends ActionBarActivity {

    private DatabaseHelper databaseHelper = null;
    TextView scoreTeam1View;
    TextView scoreTeam2View;
    Dao<Quarter, Integer> quaterDao;

    int score1;
    int score2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_quarter);
        initView();
        try {
            initHelper();
            getResultAndSetView();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void initHelper() throws SQLException {
            quaterDao  = getHelper().getQuaterDao();
    }

    private void getResultAndSetView() throws SQLException {
        List<Quarter> quarterList = quaterDao.queryBuilder().where().eq("match_id", DBSaveHelper.match.getId()).query();
        for (Quarter quarter : quarterList) {
            quaterDao.refresh(quarter);
            score1+=quarter.getScoreA();
            score2+=quarter.getScoreB();
        }
        scoreTeam1View.setText(String.valueOf(score1));
        scoreTeam2View.setText(String.valueOf(score2));
    }

    private void initView() {
        scoreTeam1View = (TextView) findViewById(R.id.summary_quarter_score1);
        scoreTeam2View = (TextView) findViewById(R.id.summary_quarter_score2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_summary_quarter, menu);
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
