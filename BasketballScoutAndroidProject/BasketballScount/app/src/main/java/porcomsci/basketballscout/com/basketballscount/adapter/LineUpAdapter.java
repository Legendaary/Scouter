package porcomsci.basketballscout.com.basketballscount.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import porcomsci.basketballscout.com.basketballscount.R;

/**
 * Created by GiftzyEiei on 10/7/2558.
 */
public class LineUpAdapter extends BaseAdapter {

    private Context context;
    private String[] playerNum;

    public LineUpAdapter(Context context, String[] playerNum)
    {
        this.context = context;
        this.playerNum = playerNum;
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.lineup_item, parent, false);

        TextView lineUpNumTextview = (TextView) convertView.findViewById(R.id.lineup_number);

        lineUpNumTextview.setText(this.playerNum[position]);

        return convertView;
    }
}
