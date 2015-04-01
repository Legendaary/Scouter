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

import java.sql.SQLException;
import java.util.ArrayList;



public class PlayerChoosingActivity extends ActionBarActivity {

    ListView listView;
    ArrayAdapter adapter;
    ArrayList playerList;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_choosing);

        listView = (ListView) findViewById(R.id.player_choosing_listView);
        playerList = new ArrayList();
        setUpButtonAdd();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player_choosing, menu);
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

    private void setUpButtonAdd(){
        editText = (EditText) findViewById(R.id.player_choosing_editText);
        Button buttonAdd = (Button) findViewById(R.id.player_choosing_button_add);
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

    private void addNewName() throws SQLException {
        String newPlayerName =   editText.getText().toString();


        /**
         * implement Insert player name here
         */
//        Player newEntry = new Player();
//        newEntry.setName(newPlayerName);
//        PlayerDao.create(newEntry);
//        retrievePlayerListFromDB();
        refreshListView();
    }

    private void refreshListView(){

        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, playerList);
        listView.setAdapter(adapter);
    }

}
