package porcomsci.basketballscout.com.basketballscount;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import database.DBSaveHelper;
import database.DatabaseHelper;
import database.entities.Quarter;


public class QuarterChoosingActivity extends ActionBarActivity {


    private DatabaseHelper databaseHelper = null;
    private Button quaterButton1;
    private Button quaterButton2;
    private Button quaterButton3;
    private Button quaterButton4;
    private Button quaterButton5;

    Dao<Quarter, Integer> quarterDao = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("เลือก Quarter");
        try {
            quarterDao = getHelper().getQuaterDao();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        setContentView(R.layout.activity_quater_choosing);
        initButton();
    }

    private void initButton() {
        quaterButton1 = (Button)findViewById(R.id.quarter1);
        quaterButton2 = (Button)findViewById(R.id.quarter2);
        quaterButton3 = (Button)findViewById(R.id.quarter3);
        quaterButton4 = (Button)findViewById(R.id.quarter4);
        quaterButton5 = (Button)findViewById(R.id.quarter5);
        setButtonListener();
    }

    private void setButtonListener() {
        quaterButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Quarter quarter = new Quarter();
                quarter.setMatch(DBSaveHelper.match);
                quarter.setQuaterNumber(1);
                try {
                    quarterDao.create(quarter);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                DBSaveHelper.quarter1 = quarter;
                DBSaveHelper.quarterNumber = 1;
                Intent intent = new Intent(getApplicationContext(), QuarterMenuActivity.class);
                startActivity(intent);
            }
        });
        quaterButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Quarter quarter = new Quarter();
                quarter.setMatch(DBSaveHelper.match);
                quarter.setQuaterNumber(2);
                try {
                    quarterDao.create(quarter);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                DBSaveHelper.quarter2 = quarter;
                DBSaveHelper.quarterNumber = 2;
                Intent intent = new Intent(getApplicationContext(), QuarterMenuActivity.class);
                startActivity(intent);
            }
        });
        quaterButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Quarter quarter = new Quarter();
                quarter.setMatch(DBSaveHelper.match);
                quarter.setQuaterNumber(3);
                try {
                    quarterDao.create(quarter);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                DBSaveHelper.quarter3 = quarter;
                DBSaveHelper.quarterNumber = 3;
                Intent intent = new Intent(getApplicationContext(), QuarterMenuActivity.class);
                startActivity(intent);
            }
        });
        quaterButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Quarter quarter = new Quarter();
                quarter.setMatch(DBSaveHelper.match);
                quarter.setQuaterNumber(4);
                try {
                    quarterDao.create(quarter);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                DBSaveHelper.quarter4 = quarter;
                DBSaveHelper.quarterNumber = 4;
                Intent intent = new Intent(getApplicationContext(), QuarterMenuActivity.class);
                startActivity(intent);
            }
        });
        quaterButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Quarter quarter = new Quarter();
                quarter.setMatch(DBSaveHelper.match);
                quarter.setQuaterNumber(5);
                try {
                    quarterDao.create(quarter);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                DBSaveHelper.quarter5 = quarter;
                DBSaveHelper.quarterNumber = 5;
                Intent intent = new Intent(getApplicationContext(), QuarterMenuActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quater_choosing, menu);
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
