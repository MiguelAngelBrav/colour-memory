package cl.miguelangelbravo.colour_memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

public class HighScores extends Activity {
	public ListView highScoreListView;
	public HighScoreRowAdapter highScoreAdapter;
	public JSONArray jsonArrayHighScore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_high_scores);
		
		completeList();
	}

	public void completeList() {
		try {
			List<String> player_name_list = new ArrayList<String>();
			List<String> score_list = new ArrayList<String>();
			
			jsonArrayHighScore = new JSONArray(getHighScore());
			
			List<JSONObject> jsons = new ArrayList<JSONObject>();
			for (int i = 0; i < jsonArrayHighScore.length(); i++) {
		        jsons.add(jsonArrayHighScore.getJSONObject(i));
		    }
					
			Collections.sort(jsons, new Comparator<JSONObject>() {
		        @Override
		        public int compare(JSONObject lhs, JSONObject rhs) {
		            Integer lid = 0, rid = 0;
					try {
						lid = lhs.getInt("score");
						rid = rhs.getInt("score");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            
		            return lid.compareTo(rid);
		        }
		    });
						
			jsonArrayHighScore = new JSONArray(jsons);
			for (int i = jsonArrayHighScore.length() - 1; i >= 0; i--) {
				JSONObject jsonObjectHighScore = jsonArrayHighScore.getJSONObject(i);				
				player_name_list.add(jsonObjectHighScore.getString("player_name"));
				score_list.add(jsonObjectHighScore.getString("score"));
	    	}
			
			highScoreListView = (ListView) findViewById(R.id.high_score_list);
			highScoreAdapter = new HighScoreRowAdapter(this, R.layout.high_score_row, player_name_list, score_list);
			highScoreListView.setAdapter(highScoreAdapter);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getHighScore() {
		SharedPreferences preferences = getSharedPreferences("high_score", MODE_PRIVATE);
		return preferences.getString("high_score", "[]");

	}

	public void setHighScore(String high_score) {
		SharedPreferences preferences = getSharedPreferences("high_score", MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("high_score", high_score);
		editor.commit();
	}

}
