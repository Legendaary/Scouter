package porcomsci.basketballscout.com.basketballscount;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by Opal on 4/1/15 AD.
 */
public class PlayerChoosingListAdapter extends BaseAdapter {

    Context context;
    String playerName;
    String playerNumber;
    private static LayoutInflater inflater = null;

    PlayerChoosingListAdapter(Context context, String playerName)
    {
        this.context = context;
        this.playerName = playerName;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null)
        {
            view = inflater.inflate(R.layout.player_name_and_number_list_item, null);

        }
    }
}
