package cl.miguelangelbravo.colour_memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.AlertDialog.Builder;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public Button button;
	public Button logo;
	public Button button_high_score;
	public TextView txt_score;
	public EditText player_name;
	public Toast toast_msg = null;
	public Builder alertDialog;
	public int identifier;
	public int t_buttons = 16;
	public int score = 0;
	public int res = 0;
	
	public List<Integer> pairs = new ArrayList<Integer>();
	public List<Integer> match = new ArrayList<Integer>();
	public List<Integer> click_button = new ArrayList<Integer>();
	public List<Integer> button_match = new ArrayList<Integer>();
	int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        txt_score = (TextView) findViewById(R.id.txt_score);
                
        logo = (Button) findViewById(R.id.logo);
        logo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alertDialog = new AlertDialog.Builder(MainActivity.this);				
				alertDialog.setTitle("Question");
		        alertDialog.setMessage("You want to restart the game?");
		        
		        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) { 
		            	prepare(null);
		            }
		         });
		        
		        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) { 
		            	// :)
		            }
		         });
		        
		        alertDialog.show();			
			}
		});
        
        
        button_high_score = (Button) findViewById(R.id.button_high_score);
        button_high_score.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					JSONArray jsonArrayHighScore = new JSONArray(getHighScore());
					if (jsonArrayHighScore.length() == 0) {
						message("No record of score :(");
						
					} else {
						Intent intent = new Intent(MainActivity.this, HighScores.class);
						startActivity(intent);
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
                
        prepare(null);
    }
    
    public void createPlayerNameEditText() {
    	player_name = new EditText(this);
        player_name.setSingleLine(true);
		player_name.setHint("Register your name");
		player_name.setInputType(InputType.TYPE_CLASS_TEXT);
		
		InputFilter alphaNumericFilter = new InputFilter() {   
			@Override  
			public CharSequence filter(CharSequence arg0, int arg1, int arg2, Spanned arg3, int arg4, int arg5) {  
                 for (int k = arg1; k < arg2; k++) {
                	 if (!Character.isLetterOrDigit(arg0.charAt(k))) {   
                		 return "";   
                	 }   
                 }   
                 return null;   
			}   
		};
        
        player_name.setFilters(new InputFilter[]{ alphaNumericFilter});
    }
    
    public void prepare(View v) {
    	logo.setEnabled(false);
    	score = 0;
    	txt_score.setText("Score: 0");
    	pairs.clear();
    	match.clear();
    	click_button.clear();
    	button_match.clear();
    	
    	int k = 1;
        for (int i = 1; i <= t_buttons; i++) {
        	pairs.add(k);
        	if ((i % 2) == 0) {
        		k = k + 1;
        	}
		}
        
        for (int i = 1; i <= t_buttons; i++) {
        	enabled_buttons(i, false);
		}
        
        distribute_cards();
        mySetOnClickListener();       
        
        new Handler().postDelayed(new Runnable() {
	        public void run() {		        	
				startGame();
				logo.setEnabled(true);
	        }
	    }, 3000);
    }
    
    public void mySetOnClickListener() {
    	for (int i = 0; i < t_buttons; i++) {
    		identifier = getResources().getIdentifier("button_" + Integer.toString(i + 1), "id", getPackageName());
    		button = (Button) findViewById(identifier);
    		button.setOnClickListener(myOnClickListener);
    	}
    }
    
    public OnClickListener myOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			clickButton(v);
		}

    };
    
    public void enabled_buttons(int position, boolean dis) {
		identifier = getResources().getIdentifier("button_" + Integer.toString(position), "id", getPackageName());
		button = (Button) findViewById(identifier);
		button.setEnabled(dis);

    }
    
    public void distribute_cards() {
    	Collections.shuffle(pairs);
    	for (int i = 0; i < t_buttons; i++) {
    		identifier = getResources().getIdentifier("button_" + Integer.toString(i + 1), "id", getPackageName());
    		button = (Button) findViewById(identifier);
    		
    		identifier = getResources().getIdentifier("button_colour" + Integer.toString(pairs.get(i)), "drawable", getPackageName());
    		button.setBackgroundResource(identifier);
		}
    }
    
    public void startGame() {
    	for (int i = 0; i < t_buttons; i++) {
    		identifier = getResources().getIdentifier("button_" + Integer.toString(i + 1), "id", getPackageName());
    		button = (Button) findViewById(identifier);
    		
    		button.setBackgroundResource(R.drawable.button_bg);
    		button.setEnabled(true);
		}
    	
    	message("Start!");
    }
    
    public void clickButton(View v) {    	
    	button = (Button) findViewById(v.getId());
    	String[] array_pos = v.getResources().getResourceName(v.getId()).split("_");
    	pos = Integer.parseInt(array_pos[2]) - 1;
    	
    	identifier = getResources().getIdentifier("button_colour" + Integer.toString(pairs.get(pos)), "drawable", getPackageName());
    	button.setBackgroundResource(identifier);
    	
    	match.add(pairs.get(pos));
    	click_button.add(Integer.parseInt(array_pos[2]));
    	enabled_buttons(Integer.parseInt(array_pos[2]), false);
    	
    	Log.d("clickButton", "match: " + match);
    	Log.d("clickButton", "click_button: " + click_button);
    	
    	if (match.size() == 2) {
    		Log.d("clickButton", "two click!");
    		
    		for (int i = 1; i <= t_buttons; i++) {
            	enabled_buttons(i, false);
    		}
    		
    		logo.setEnabled(false);
    		
	    	new Handler().postDelayed(new Runnable() {
		        public void run() {		
		        	if (match.size() != 2) {
		        		Log.d("clickButton", "ERROR!, Select more than two!");
		        		message("Selects One!");
		        		setBackgroundCard(true, click_button);
		        		
					} else {			        	
		    			if (match.get(0) == match.get(1)) {
		    				Log.d("clickButton", "equal!");
		    				button_match.add(click_button.get(0));
		    				button_match.add(click_button.get(1));
		    				
		    				score = score + 2;
		    				
		    			} else {
		    				Log.d("clickButton", "different!");
		    				setBackgroundCard(true, click_button);
		    				
		    				score = score - 1;
		    			}
					}
	    			
	    			match.clear();
	    			click_button.clear();
	    			
	    			for (int j = 1; j <= t_buttons; j++) {
    					if (!button_match.contains(j)) {
    						enabled_buttons(j, true);	
						}
    				}
	    			
	    			txt_score.setText("Score: " + Integer.toString(score));
	    			logo.setEnabled(true);
	    			
	    			Log.d("clickButton", "button_match: " + button_match.size());
	    			if (button_match.size() == t_buttons) {
	    				createPlayerNameEditText();
	    				alertDialog = new AlertDialog.Builder(MainActivity.this);				
	    				alertDialog.setTitle("Congratulations");
	    		        alertDialog.setMessage("Your score: " + Integer.toString(score) + " - Position: " + getPosition(score));
	    		        alertDialog.setView(player_name);
	    		        
	    		        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	    		            public void onClick(DialogInterface dialog, int which) { 
	    		            	if (player_name.getText().toString().contentEquals("")) {
									message("The name can not be empty!");
								} else {
									saveHighScore(player_name.getText().toString(), score);
								}
	    		            }
	    		         });
	    		        
	    		        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    		            public void onClick(DialogInterface dialog, int which) { 
	    		            	// :)
	    		            }
	    		         });
	    		        
	    		        alertDialog.show();
	    			}
		    	}		        
		    }, 1000);
	    	
    	} else if (match.size() > 2) {
    		setBackgroundCard(true, click_button);
    		
    		match.clear();
			click_button.clear();
		}
    }
    
    public void setBackgroundCard(boolean enabled, List<Integer> click_button_aux) {
    	for (int j = 0; j < click_button.size(); j++) {
			identifier = getResources().getIdentifier("button_" + Integer.toString(click_button_aux.get(j)), "id", getPackageName());
			button = (Button) findViewById(identifier);
			button.setBackgroundResource(R.drawable.button_bg);
	    	button.setEnabled(enabled);
		}    	
    }
    
    public void message(String msg) {
    	Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
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
	
	public String getPosition(int final_score) {
		int pos = 1;
		try {
			JSONArray jsonArrayHighScore = new JSONArray(getHighScore());
			List<Integer> jsonValues = new ArrayList<Integer>();
						
			for (int i = 0; i < jsonArrayHighScore.length(); i++) {
				JSONObject jsonObjectHighScore = jsonArrayHighScore.getJSONObject(i);
				jsonValues.add(Integer.parseInt(jsonObjectHighScore.getString("score")));
			}
			
			Collections.sort(jsonValues);
			Log.d("getPosition", "score: " + final_score + " - jsonValues: " + jsonValues);
			
			for (int i = jsonValues.size() - 1; i >= 0; i--) {
				if (jsonValues.get(i) > final_score) {
					pos = pos + 1;
				}
				
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Integer.toString(pos);
	}
	
	public void saveHighScore(String name, int score) {
		
		try {
			JSONArray jsonArrayHighScore = new JSONArray(getHighScore());
			JSONObject jsonObjectHighScore = new JSONObject();
			
			jsonObjectHighScore.put("player_name", name);
			jsonObjectHighScore.put("score", score);
			
			jsonArrayHighScore.put(jsonObjectHighScore);
			
			setHighScore(jsonArrayHighScore.toString());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
