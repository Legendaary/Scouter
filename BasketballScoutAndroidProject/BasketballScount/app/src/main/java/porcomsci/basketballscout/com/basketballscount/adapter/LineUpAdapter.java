package porcomsci.basketballscout.com.basketballscount.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
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
        return playerNum.length;
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
        LayoutInflater mInflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null)
            convertView = mInflater.inflate(R.layout.lineup_item, parent, false);

//        convertView.setLayoutParams(new ListView.LayoutParams());
//        convertView.setlayoutparams(new Listview.setlayoutparams(width, height));

        TextView lineUpNumTextView = (TextView) convertView.findViewById(R.id.lineup_number);
        lineUpNumTextView.setText(this.playerNum[position]);
        return convertView;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String[] getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(String[] playerNum) {
        this.playerNum = playerNum;
    }
}
