package porcomsci.basketballscout.com.basketballscount.mainflow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DBSaveHelper;
import database.DatabaseHelper;
import database.entities.Match;
import database.entities.School;
import porcomsci.basketballscout.com.basketballscount.R;
import porcomsci.basketballscout.com.basketballscount.utility.SegueHelper;


public class SchoolListActivity extends ActionBarActivity {

    private DatabaseHelper databaseHelper = null;
    ListView schoolListView;
    List<String> schoolList = new ArrayList<>();
    List<School> schoolIdList = new ArrayList<>();
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_list);
        getSupportActionBar().setTitle("รายชื่อโรงเรียน โรงเรียน" + getIntent().getStringExtra("chooseTeam"));
        schoolListView = (ListView)findViewById(R.id.schoolListView);
        try {
            Dao<School,Integer> schoolDao = getHelper().getSchoolDao();
            List<School> retrievedList  =  schoolDao.queryForAll();
            for (School school : retrievedList) {
                schoolList.add(school.getName());
                schoolIdList.add(school);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, schoolList);
        schoolListView.setAdapter(adapter);
        setUpListViewItemListener();
    }

    private void setUpListViewItemListener(){
        schoolListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    clickAtSchoolName(position);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    public void clickAtSchoolName(int pos) throws Exception {
        int schoolId = schoolIdList.get(pos).getId();
        Match match = DBSaveHelper.match;

        if(getIntent().getStringExtra("chooseTeam").equalsIgnoreCase("1")){
            DBSaveHelper.school1Id = schoolId;
            DBSaveHelper.school1 = schoolIdList.get(pos);
            System.out.println("Choosing schoolId for school 1  : "+schoolId);
            SegueHelper.team1Chosen = true;
            match.setSchoolA(DBSaveHelper.school1);
            int rowUpdated = getHelper().getMatchDao().update(match);
            getHelper().getMatchDao().refresh(match);
            if( 1 != rowUpdated){
                throw new Exception("Update School in Match Error");
            }
        }else if(getIntent().getStringExtra("chooseTeam").equalsIgnoreCase("2")){
            DBSaveHelper.school2Id = schoolId;
            DBSaveHelper.school2 = schoolIdList.get(pos);
            System.out.println("Choosing schoolId for school 2  : "+schoolId);
            SegueHelper.team2Chosen = true;
            match.setSchoolB(DBSaveHelper.school2);
            int rowUpdated = getHelper().getMatchDao().update(match);
            getHelper().getMatchDao().refresh(match);
            if( 1 != rowUpdated){
                throw new Exception("Update School in Match Error");
            }
        }
        this.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_school_list, menu);
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
