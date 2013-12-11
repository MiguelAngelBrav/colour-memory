package cl.miguelangelbravo.colour_memory;

import java.util.List;

import org.json.JSONArray;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HighScoreRowAdapter extends ArrayAdapter<String> {
	private Context context;
	int layout;
	JSONArray jsonArrayFrecuentes;
	List<String> player_name_list;
	List<String> score_list;
	
	public HighScoreRowAdapter(Context context, int layout, List<String> player_name_list, List<String> score_list) {

		super(context, layout, player_name_list);
		this.context = context;
		this.layout = layout;
		this.player_name_list = player_name_list;
		this.score_list = score_list;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
					
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.high_score_row, null);
		}
		
		TextView player_name = (TextView) v.findViewById(R.id.player_name);
		TextView score = (TextView) v.findViewById(R.id.score);
		
		player_name.setText("Player: " + player_name_list.get(position));
		score.setText("Score: " + score_list.get(position));
		

		return v;
	}

}
