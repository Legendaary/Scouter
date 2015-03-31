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

import database.SchoolChoosingHelper;
import database.entities.Match;

public class CompetitorChoosingActivity extends ActionBarActivity {

    private Match match;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitor_choosing);
        initMatch();
        checkReadyToGo();
        Button buttonTeamA = (Button) findViewById(R.id.button_team_a);
        buttonTeamA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SchoolListActivity.class);
                intent.putExtra("chooseTeam","team1");
                intent.putExtra("match",match);
                startActivity(intent);
            }
        });

        Button buttonTeamB = (Button) findViewById(R.id.button_team_b);
        buttonTeamB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SchoolListActivity.class);
                intent.putExtra("chooseTeam","team2");
                intent.putExtra("match",match);
                startActivity(intent);
            }
        });
    }

    private void initMatch() {
           match = (Match)getIntent().getSerializableExtra("match");
    }

    private void checkReadyToGo() {
        String comeFromTeam = getIntent().getStringExtra("teamChosen");
        if(comeFromTeam!=null) {
            if (comeFromTeam.equalsIgnoreCase("team1")) {
                SchoolChoosingHelper.team1Chosen = true;
            } else if (comeFromTeam.equalsIgnoreCase("team2")) {
                SchoolChoosingHelper.team2Chosen = true;
            }
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
            if(SchoolChoosingHelper.team1Chosen&&SchoolChoosingHelper.team2Chosen){
            Intent intent = new Intent(getApplicationContext(),PlayerChoosingActivity.class);
            intent.putExtra("team","1");
            startActivity(intent);
            }else{
                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage("Please choose both school before click!").setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                        .show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}///end class
