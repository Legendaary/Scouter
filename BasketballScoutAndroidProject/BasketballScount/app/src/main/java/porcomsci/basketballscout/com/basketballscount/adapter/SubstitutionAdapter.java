package porcomsci.basketballscout.com.basketballscount.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import porcomsci.basketballscout.com.basketballscount.R;

/**
 * Created by PorPaul on 9/7/2558.
 */
public class SubstitutionAdapter extends BaseAdapter {

    private Context context;
    private String[] playerNo;
    private String[] point;
    private String[] time;

    public SubstitutionAdapter(Context context, String[] playerNo, String[] point, String[] time) {
        this.context = context;
        this.playerNo = playerNo;
        this.point = point;
        this.time = time;
    }

    @Override
    public int getCount() {
        return playerNo.length;
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
            convertView = mInflater.inflate(R.layout.substitution_row, parent, false);

        TextView playerView = (TextView)convertView.findViewById(R.id.sub_player_no);
        playerView.setText(playerNo[position]);

        TextView timeView = (TextView)convertView.findViewById(R.id.sub_player_time);
        timeView.setText(time[position]);

        TextView flagView = (TextView)convertView.findViewById(R.id.sub_player_flag);
        flagView.setText(point[position]);

        return convertView;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String[] getPlayerNo() {
        return playerNo;
    }

    public void setPlayerNo(String[] playerNo) {
        this.playerNo = playerNo;
    }

    public String[] getPoint() {
        return point;
    }

    public void setPoint(String[] point) {
        this.point = point;
    }

    public String[] getTime() {
        return time;
    }

    public void setTime(String[] time) {
        this.time = time;
    }
}
