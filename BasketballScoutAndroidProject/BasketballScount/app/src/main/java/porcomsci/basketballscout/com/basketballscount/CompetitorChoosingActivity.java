package porcomsci.basketballscout.com.basketballscount;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;

import database.DBSaveHelper;
import database.DatabaseHelper;
import porcomsci.basketballscout.com.basketballscount.utility.SegueHelper;

public class CompetitorChoosingActivity extends ActionBarActivity {

    private DatabaseHelper databaseHelper = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("เลือกทีมแข่งขัน");
        setContentView(R.layout.activity_competitor_choosing);
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
                Intent intent = new Intent(getApplicationContext(), SchoolListActivity.class);
                intent.putExtra("chooseTeam", "2");
                startActivity(intent);
            }
        });
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
            if(SegueHelper.team1Chosen&& SegueHelper.team2Chosen){
                if(DBSaveHelper.school1Id != DBSaveHelper.school2Id){

                    try {
                        getHelper().getSchoolDao().refresh(DBSaveHelper.school1);
                        getHelper().getSchoolDao().refresh(DBSaveHelper.school2);
                    } catch (SQLException e) {
                        System.out.println("refresh error!");
                    }
                    Intent intent = new Intent(getApplicationContext(),PlayerChoosingActivity.class);
                    SegueHelper.playerChoosingSequence = 1;
                    SegueHelper.team1Chosen = false;
                    SegueHelper.team2Chosen = false;
                    startActivity(intent);
                }else{
                    new AlertDialog.Builder(this)
                            .setTitle("Alert")
                            .setMessage("โรงเรียนที่แข่งซ้ำกัน!").setNegativeButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                            .show();
                }
            }else
               {
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
