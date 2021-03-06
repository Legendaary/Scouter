package porcomsci.basketballscout.com.basketballscount.menu;

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
import porcomsci.basketballscout.com.basketballscount.mainflow.ManageSchoolInfoActivity;
import porcomsci.basketballscout.com.basketballscount.mainflow.MatchActivity;
import porcomsci.basketballscout.com.basketballscount.R;
import porcomsci.basketballscout.com.basketballscount.mainflow.TournamentActivity;
import porcomsci.basketballscout.com.basketballscount.history.TournamentHistoryActivity;

public class MainActivity extends ActionBarActivity {

    private DatabaseHelper databaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView menuListView = (ListView) findViewById(R.id.listView);
        String[] menu = {"ทัวร์นาเม้นท์", "แมทช์", "ประวัติ", "จัดการข้อมูล โรงเรียน&ผู้เล่น", "ตั้งค่า"};
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (int i = 0; i < menu.length; i++) {
            stringArrayList.add(menu[i]);
        }
        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, stringArrayList);
        menuListView.setAdapter(adapter);
        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * 0 is tournament.
                 * 1 is casual.
                 * 2 is history.
                 * 3 is manage.
                 */
                if (position == 0) {
                    Intent intent = new Intent(getApplicationContext(), TournamentActivity.class);
                    startActivity(intent);
                } else if (position == 1) {
                    try {
                        Tournament casual = getHelper().getTournamentDao().queryForId(1);
                        DBSaveHelper.tournament = casual;
                        Intent intent = new Intent(getApplicationContext(), MatchActivity.class);
                        startActivity(intent);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (position == 2) {
                    Intent intent = new Intent(getApplicationContext(), TournamentHistoryActivity.class);
                    startActivity(intent);
                } else if (position == 3) {
                    Intent intent = new Intent(getApplicationContext(), ManageSchoolInfoActivity.class);
                    startActivity(intent);
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
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }
}