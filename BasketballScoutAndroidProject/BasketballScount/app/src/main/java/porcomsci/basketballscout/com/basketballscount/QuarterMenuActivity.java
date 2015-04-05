package porcomsci.basketballscout.com.basketballscount;

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

import java.sql.SQLException;
import java.util.ArrayList;

import database.DBSaveHelper;
import database.DatabaseHelper;
import database.entities.Tournament;


public class QuarterMenuActivity extends ActionBarActivity {

    private DatabaseHelper databaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quater_menu);
        final ListView menuListView = (ListView) findViewById(R.id.listView);
        String[] menu = {"Start Record", "Action record", "Quarter Information", "Substitution Information", "Score Information", "Back to menu"};
        ArrayList<String> stringArrayList = new ArrayList<>();
        for( int i = 0; i < menu.length ; i++ )
        {
            stringArrayList.add(menu[ i ]);
        }
        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, stringArrayList);
        menuListView.setAdapter(adapter);
        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * 0 is start record match.
                 * 1 is start record Action of players.
                 * 2 is see the info of this quarter.
                 * 3 is see the substitution info.
                 * 4 is score info.
                 * 5 is back to choose quarter
                 */
                if(position == 0)
                {

                }
                else if(position == 1)
                {

                }
                else if(position == 2)
                {

                }
                else if(position == 3)
                {

                }
                else if(position == 4)
                {

                }
                else if(position == 5)
                {
                    Intent intent = new Intent(getApplicationContext(), QuarterChoosingActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quater_menu, menu);
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
