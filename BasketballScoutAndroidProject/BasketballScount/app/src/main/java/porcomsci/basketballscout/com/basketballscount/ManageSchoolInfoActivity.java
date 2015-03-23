package porcomsci.basketballscout.com.basketballscount;

import android.app.AlertDialog;
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

import java.util.ArrayList;


public class ManageSchoolInfoActivity extends ActionBarActivity {

    ListView listView;
    ArrayAdapter adapter;
    ArrayList<String> schoolNameList;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_school_info);

        schoolNameList = new ArrayList<>();
        //query school info from database here and add to schoolNameList

        listView = (ListView) findViewById(R.id.manage_school_info_listView);
        refreshListView();
        setUpListView();

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

    private void addNewName(){
        schoolNameList.add( editText.getText().toString() );
    }

    private void refreshListView(){
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, schoolNameList);
        listView.setAdapter(adapter);
    }

    private void setUpListView(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditOrDeleteDialog editOrDeleteDialog = new EditOrDeleteDialog();
                AlertDialog dialog = (AlertDialog) editOrDeleteDialog.onCreateDialog(new Bundle());
                dialog.show();
            }
        });
    }
}
