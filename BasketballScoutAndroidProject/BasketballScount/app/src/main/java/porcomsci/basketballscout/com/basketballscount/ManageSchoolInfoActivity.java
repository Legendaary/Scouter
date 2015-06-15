package porcomsci.basketballscout.com.basketballscount;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    ArrayAdapter<String> adapter;
    List<String> schoolList;
    EditText editText;
    private DatabaseHelper databaseHelper = null;
    private  Dao<School,Integer> schoolDao = null;

    AlertDialog editOrDeleteDialog; // dialog options to edit/delete school name, manage players
    int selectedPosition = 0; //selected position of ListView (which school name is selected to edit/delete)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_school_info);
        listView = (ListView) findViewById(R.id.manage_school_info_listView);
        retrieveSchoolListFromDB();
        refreshListView();
        setUpListViewItemListener();
        setUpButtonAdd();
        setUpEditOrDeleteDialog();
    }

    /**
     *  query school info from database here and add to schoolNameList ArrayList.
     */
    private void retrieveSchoolListFromDB() {
        try {
            schoolDao = getHelper().getSchoolDao();
            schoolList = new ArrayList<>();
            List<School> retrievedList  =  schoolDao.queryForAll();
            for (School school : retrievedList) {
                schoolList.add(school.getName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//end query

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

    private void refreshListView(){

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, schoolList);
        listView.setAdapter(adapter);
    }
    private void setUpListViewItemListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editOrDeleteDialog.show();
                selectedPosition = position;
            }
        });
    }

    private void addNewName() throws SQLException {
        String newSchoolName =   editText.getText().toString();
        School newEntry = new School();
        newEntry.setName(newSchoolName);
        schoolDao.create(newEntry);
        retrieveSchoolListFromDB();
        refreshListView();
    }

    private void setUpEditOrDeleteDialog(){

        String[] options = { "แก้ไขชื่อโรงเรียน" , "ลบชื่อโรงเรียน" , "แก้ไขรายชื่อผู้เล่น" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                if(which == 0) // edit school name
                {
                    showEditSchoolNameDialog();
                }
                else if(which==1) // delete school name
                {
                    showDeleteSchoolNameDialog();
                }
                else //manage player
                {


                }

            }
        });
        editOrDeleteDialog = builder.create();
    }

    private void setUpButtonAdd(){
        editText = (EditText) findViewById(R.id.player_choosing_editText);
        Button buttonAdd = (Button) findViewById(R.id.manage_school_info_button_add);
        buttonAdd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addNewName();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                editText.setText("");
            }
        });
    }

    private void showEditSchoolNameDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText schoolName = new EditText(this);
        schoolName.setText(schoolList.get(selectedPosition));
        schoolName.setWidth(250);
        builder.setView(schoolName)
                .setTitle(R.string.edit_school_name)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            updateSchoolName(schoolName.getText().toString());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create().show();
    }

    private void updateSchoolName(String schoolName) throws SQLException {
        /*
            implement update school name on database here
        */
        List<School> retrievedList = schoolDao.queryBuilder().where().
                eq("name",schoolList.get(selectedPosition)).query();
        School currentSchool = retrievedList.get(0);
        currentSchool.setName(schoolName);
        schoolDao.update(currentSchool);
        retrieveSchoolListFromDB();
        refreshListView();
    }

    private void deleteSchoolName() throws SQLException {

        //use query for foreign.
        List<School> retrievedList = schoolDao.queryBuilder().where().
                eq("name",schoolList.get(selectedPosition)).query();
        schoolDao.delete(retrievedList.get(0));
        retrieveSchoolListFromDB();
        refreshListView();
    }

    private void showDeleteSchoolNameDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_school_name)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            deleteSchoolName();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create().show();
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
