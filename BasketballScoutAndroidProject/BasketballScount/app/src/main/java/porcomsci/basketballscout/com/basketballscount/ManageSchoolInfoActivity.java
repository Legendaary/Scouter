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
    ArrayAdapter adapter;
    List<String> schoolList = new ArrayList<>();
    EditText editText;
    private DatabaseHelper databaseHelper = null;
    private  Dao<School,Integer> schoolDao = null;
    AlertDialog editOrDeleteDialog;
    int selectedPosition = 0; //selected position of listview (which school name is selected to edit/delete)

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
        setUpListView();
        setUpButtonAdd();
        setUpEditOrDeleteDialog();

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

    private void setUpListView(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editOrDeleteDialog.show();
                selectedPosition = position;
            }
        });
    }

    private void setUpEditOrDeleteDialog(){
        /*  custom dialog
         *
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.edit_or_delete_dialog, null));

        // Create the AlertDialog object and return it
        dialog = builder.create();  */


        String[] options = { "Edit" , "Delete" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                if(which == 0) // edit
                {
                    AlertDialog editSchoolNameDialog = setUpEditSchoolNameDialog();
                    editSchoolNameDialog.show();
                }
                else // delete
                {
                    deleteSchoolName();
                }

            }
        });
        editOrDeleteDialog = builder.create();
    }

    private void setUpButtonAdd(){
        editText = (EditText) findViewById(R.id.manage_school_info_editText);
        Button buttonAdd = (Button) findViewById(R.id.manage_school_info_button_add);
        buttonAdd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewName();
                refreshListView();
                editText.setText("");
            }
        });
    }

    private AlertDialog setUpEditSchoolNameDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText schoolName = new EditText(this);
        schoolName.setText(schoolNameList.get(selectedPosition));
        schoolName.setWidth(250);
        builder.setView(schoolName)
                .setTitle("Edit School Name")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateSchoolName(schoolName);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    private void updateSchoolName(EditText schoolName)
    {
        /*
            implement update school name on database here
        */

        schoolNameList.set(selectedPosition,schoolName.getText().toString());
        refreshListView();
    }

    private void deleteSchoolName(){
        /*
            implement delete school name from database here
         */
        schoolNameList.remove(selectedPosition);
        refreshListView();
    }
}
