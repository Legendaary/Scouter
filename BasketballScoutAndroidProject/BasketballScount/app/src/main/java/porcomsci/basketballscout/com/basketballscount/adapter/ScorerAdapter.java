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
public class ScorerAdapter extends BaseAdapter {

    private Context context;
    private String[] playerNo;
    private String[] time;
    private String[] point;

    public ScorerAdapter(Context context, String[] playerNo, String[] time, String[] point) {
        this.context = context;
        this.playerNo = playerNo;
        this.time = time;
        this.point = point;
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
            convertView = mInflater.inflate(R.layout.scorer_row, parent, false);

        TextView playerView = (TextView)convertView.findViewById(R.id.score_player_no);
        playerView.setText(playerNo[position]);

        TextView pointView = (TextView)convertView.findViewById(R.id.score_player_point);
        pointView.setText(point[position]);

        TextView timeView = (TextView)convertView.findViewById(R.id.score_player_time);
        timeView.setText(time[position]);

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

    public String[] getTime() {
        return time;
    }

    public void setTime(String[] time) {
        this.time = time;
    }

    public String[] getPoint() {
        return point;
    }

    public void setPoint(String[] point) {
        this.point = point;
    }
}
