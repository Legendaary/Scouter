package porcomsci.basketballscout.com.basketballscount;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import porcomsci.basketballscout.com.basketballscount.database.DatabaseHelper;
import porcomsci.basketballscout.com.basketballscount.database.entities.Match;


public class MainActivity extends ActionBarActivity {

    private DatabaseHelper databaseHelper = null;
    private TextView showView;
    private Button buttonQuery;
    private EditText textbox;
    private Button buttonSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.textbox = (EditText)findViewById(R.id.textbox1);
        this.buttonSave = (Button)findViewById(R.id.buttonSaveMatch);
        this.buttonQuery = (Button)findViewById(R.id.buttonShow);
        this.showView = (TextView)findViewById(R.id.text1);
        setListener();
    }
    public void setListener(){
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Dao<Match, Integer> matchDao = getHelper().getMatchDao();
                    Match mock = new Match();
                    mock.setId(101);
                    mock.setMatchNumber(Integer.parseInt(textbox.getText().toString()));
                    matchDao.create(mock);
                    System.out.println("Save finish");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        buttonQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Dao<Match, Integer> matchDao = getHelper().getMatchDao();
                    Match mock= matchDao.queryForId(101);
                    System.out.println("dd");
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            databaseHelper =  OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }
}
