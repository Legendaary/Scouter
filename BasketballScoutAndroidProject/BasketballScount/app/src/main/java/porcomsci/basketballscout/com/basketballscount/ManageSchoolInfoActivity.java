package porcomsci.basketballscout.com.basketballscount;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseHelper;
import database.entities.School;


public class ManageSchoolInfoActivity extends ActionBarActivity {

    ListView listView;
    ArrayAdapter adapter;
    List<String> schoolList = new ArrayList<>();
    EditText editText;
    private DatabaseHelper databaseHelper = null;
    private  Dao<School,Integer> schoolDao = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_school_info);

        //query school info from database here and add to schoolNameList
        try {

            schoolDao = getHelper().getSchoolDao();
            List<School> retrievedList  =  schoolDao.queryForAll();
            for (School school : retrievedList) {
                schoolList.add(school.getName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listView = (ListView) findViewById(R.id.manage_school_info_listView);
        refreshListView();

        editText = (EditText) findViewById(R.id.manage_school_info_editText);
        Button buttonAdd = (Button) findViewById(R.id.manage_school_info_button_add);
        buttonAdd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addNewName();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                refreshListView();
                editText.setText("");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_school_info, menu);
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

    private void addNewName() throws SQLException {
        String newSchool =   editText.getText().toString();
        schoolList.add(newSchool);
        School newEntry = new School();
        newEntry.setName(newSchool);
        schoolDao.create(newEntry);

    }

    private void refreshListView(){

        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, schoolList);
        listView.setAdapter(adapter);
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
