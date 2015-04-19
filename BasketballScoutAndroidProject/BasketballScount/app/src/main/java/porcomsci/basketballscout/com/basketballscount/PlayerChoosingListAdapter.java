package porcomsci.basketballscout.com.basketballscount;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Opal on 4/1/15 AD.
 */
public class PlayerChoosingListAdapter extends ArrayAdapter<PlayerChoosingItem> {

    Context context;
    ArrayList<PlayerChoosingItem> listItem;

    private static LayoutInflater inflater = null;

    PlayerChoosingListAdapter(Context context, ArrayList<PlayerChoosingItem> listItem)
    {
        super(context, R.layout.player_name_and_number_list_item, listItem);
        this.context = context;
        this.listItem = listItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.player_name_and_number_list_item, parent, false);
        TextView playerNameCheckbox = (TextView) view.findViewById(R.id.player_name_and_number_list_item_checkBox);
        EditText playerNumberEditText = (EditText) view.findViewById(R.id.player_name_and_number_list_item_player_number_editText);
        playerNameCheckbox.setText(this.listItem.get(position).getPlayerName());
        playerNumberEditText.setText(this.listItem.get(position).playerNumber());
        return view;
    }
}
