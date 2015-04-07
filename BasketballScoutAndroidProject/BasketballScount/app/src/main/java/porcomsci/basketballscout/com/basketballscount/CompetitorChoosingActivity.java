package porcomsci.basketballscout.com.basketballscount;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DBSaveHelper;
import database.DatabaseHelper;
import database.entities.Match;
import database.entities.School;

public class CompetitorChoosingActivity extends ActionBarActivity {

    private Match match = DBSaveHelper.match;
    private DatabaseHelper databaseHelper = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("เลือกทีมแข่งขัน");
        setContentView(R.layout.activity_competitor_choosing);
        checkReadyToGo();
        Button buttonTeamA = (Button) findViewById(R.id.button_team_a);
        buttonTeamA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SchoolListActivity.class);
                intent.putExtra("chooseTeam","1");
                startActivity(intent);
            }
        });

        Button buttonTeamB = (Button) findViewById(R.id.button_team_b);
        buttonTeamB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SchoolListActivity.class);
                intent.putExtra("chooseTeam","2");
                startActivity(intent);
            }
        });
    }

    private void checkReadyToGo() {
        String comeFromTeam = getIntent().getStringExtra("teamChosen");
        try {
        if(comeFromTeam!=null) {
            if (comeFromTeam.equalsIgnoreCase("1")) {
                DBSaveHelper.team1Chosen = true;
                School school1 = getHelper().getSchoolDao().queryForId(Integer.valueOf( DBSaveHelper.school1Id));
                match.setSchoolA(school1);
                getHelper().getMatchDao().update(match);
            } else if (comeFromTeam.equalsIgnoreCase("2")) {
                DBSaveHelper.team2Chosen = true;
                School school2 = getHelper().getSchoolDao().queryForId(Integer.valueOf( DBSaveHelper.school2Id));
                match.setSchoolB(school2);
                getHelper().getMatchDao().update(match);
            }
                }//end check not null
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


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
            if(DBSaveHelper.team1Chosen&& DBSaveHelper.team2Chosen){
            Intent intent = new Intent(getApplicationContext(),PlayerChoosingActivity.class);
            DBSaveHelper.playerChoosingSequence = 1;
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            DBSaveHelper.team1Chosen = false;
            DBSaveHelper.team2Chosen = false;
            startActivity(intent);
            }else{
                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage("โปรดเลือกทั้ง 2 ทีมก่อน!").setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                        .show();
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
}///end class
