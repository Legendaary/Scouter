package porcomsci.basketballscout.com.basketballscount;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class CompetitorChoosingActivity extends ActionBarActivity {

    boolean teamOneChosen = false;
    boolean teamTwoChosen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitor_choosing);
        String comeFromTeam = getIntent().getStringExtra("teamChosen");
        if(comeFromTeam.equalsIgnoreCase("team1")){
            teamOneChosen = true;
        }else if(comeFromTeam.equalsIgnoreCase("team2")){
            teamTwoChosen = true;
        }

        Button buttonTeamA = (Button) findViewById(R.id.button_team_a);
        buttonTeamA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SchoolListActivity.class);
                intent.putExtra("team1","team1");
                startActivity(intent);
            }
        });

        Button buttonTeamB = (Button) findViewById(R.id.button_team_b);
        buttonTeamB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SchoolListActivity.class);
                intent.putExtra("team1","team2");
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}///end class
