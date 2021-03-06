package porcomsci.basketballscout.com.basketballscount.mainflow;

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
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;

import database.DBSaveHelper;
import database.DatabaseHelper;
import database.entities.Quarter;
import porcomsci.basketballscout.com.basketballscount.R;
import porcomsci.basketballscout.com.basketballscount.utility.SegueHelper;


public class QuarterChoosingActivity extends ActionBarActivity {


    private DatabaseHelper databaseHelper = null;
    private Button quarterButton1;
    private Button quarterButton2;
    private Button quarterButton3;
    private Button quarterButton4;
    private Button quarterButton5;

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

    @Override
    protected void onStart() {
        super.onStart();
        checkAvailability();
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(QuarterChoosingActivity.this);
        builder.setMessage("ยกเลิกการบันทึกข้อมูลทั้งหมด?");
        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                deleteAllQuarterAndBack();
            }
        });
        builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }


    private void deleteAllQuarterAndBack() {

        SegueHelper.quarter1IsPlayed = false;
        SegueHelper.quarter2IsPlayed = false;
        SegueHelper.quarter3IsPlayed = false;
        SegueHelper.quarter4IsPlayed = false;
        SegueHelper.quarter5IsPlayed = false;


        DeleteBuilder<Quarter, Integer> quarterIntegerDeleteBuilder = quarterDao.deleteBuilder();
        try {
            quarterIntegerDeleteBuilder.where().eq("match_id", DBSaveHelper.match.getId());
            quarterIntegerDeleteBuilder.delete();
        } catch (SQLException e) {
            System.out.println("delete all quarter error");
        }
        super.onBackPressed();
    }


    private void checkAvailability() {

        System.out.println("checking avail ++++");
        if(SegueHelper.quarter1IsPlayed){
            quarterButton1.setEnabled(false);
            quarterButton2.setEnabled(true);
        }


        if(SegueHelper.quarter2IsPlayed){
            quarterButton1.setEnabled(false);
            quarterButton2.setEnabled(false);
            quarterButton3.setEnabled(true);
        }
        if(SegueHelper.quarter3IsPlayed){
            quarterButton1.setEnabled(false);
            quarterButton2.setEnabled(false);
            quarterButton3.setEnabled(false);
            quarterButton4.setEnabled(true);
        }
        if(SegueHelper.quarter4IsPlayed){
            quarterButton1.setEnabled(false);
            quarterButton2.setEnabled(false);
            quarterButton3.setEnabled(false);
            quarterButton4.setEnabled(false);
            quarterButton5.setEnabled(true);
        }
    }

    private void initButton() {
        quarterButton1 = (Button)findViewById(R.id.quarter1);

        quarterButton2 = (Button)findViewById(R.id.quarter2);
        quarterButton2.setEnabled(false);

        quarterButton3 = (Button)findViewById(R.id.quarter3);
        quarterButton3.setEnabled(false);

        quarterButton4 = (Button)findViewById(R.id.quarter4);
        quarterButton4.setEnabled(false);

        quarterButton5 = (Button)findViewById(R.id.quarter5);
        quarterButton5.setEnabled(false);

        setButtonListener();
    }

    private void setButtonListener() {
        quarterButton1.setOnClickListener(new View.OnClickListener() {
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
                Intent intent = new Intent(getApplicationContext(), MatchRecordingActivity.class);
                startActivity(intent);
            }
        });
        quarterButton2.setOnClickListener(new View.OnClickListener() {
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
                Intent intent = new Intent(getApplicationContext(), MatchRecordingActivity.class);
                startActivity(intent);
            }
        });
        quarterButton3.setOnClickListener(new View.OnClickListener() {
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
                Intent intent = new Intent(getApplicationContext(), MatchRecordingActivity.class);
                startActivity(intent);
            }
        });
        quarterButton4.setOnClickListener(new View.OnClickListener() {
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
                Intent intent = new Intent(getApplicationContext(), MatchRecordingActivity.class);
                startActivity(intent);
            }
        });
        quarterButton5.setOnClickListener(new View.OnClickListener() {
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
                Intent intent = new Intent(getApplicationContext(), MatchRecordingActivity.class);
                startActivity(intent);
            }
        });

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_competitor_choosing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.button_ToNextActivity) {
            boolean quarter1IsPlayed = SegueHelper.quarter1IsPlayed;
            boolean quarter2IsPlayed = SegueHelper.quarter2IsPlayed;
            boolean quarter3IsPlayed = SegueHelper.quarter3IsPlayed;
            boolean quarter4IsPlayed = SegueHelper.quarter4IsPlayed;
            if(quarter1IsPlayed&&quarter2IsPlayed&&quarter3IsPlayed&&quarter4IsPlayed){

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(QuarterChoosingActivity.this);
                builder.setMessage("ยืนยันการบันทึกการเล่นทั้งหมด?");
                builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), SummaryQuarterActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }else{
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(QuarterChoosingActivity.this);
                builder.setMessage("กรุณาบันทึกการเล่นให้ครบทุก Quarter ก่อน?");
                builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder.show();
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
}
