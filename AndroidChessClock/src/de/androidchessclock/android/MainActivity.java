package de.androidchessclock.android;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	/** Time control values */
	private static String NO_DELAY = "None";
	private static String FISCHER = "Fischer";
	private static String BRONSTEIN = "Bronstein";

	/** Object variables */
	private Handler myHandler = new Handler();
	private PowerManager pm;
	private WakeLock wl;
	private String delay = NO_DELAY;
	private String alertTone;
	private Ringtone ringtone = null;

	/** Int/long values */
	private int time_W;
	private int time_B;
	private int b_delay;
	private long t_P1;
	private long t_P2;
	private int delay_time;
	private int onTheClock = 0;
	private int savedOTC = 0;

	/** Boolean values */
	private boolean haptic = false;
	private boolean blink = false;
	private boolean timeup = false;
	private boolean prefmenu = false;
	private boolean delayed = false;
	private boolean hapticChange = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** Get rid of the status bar */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/** Create a PowerManager object so we can get the wakelock */
		pm = (PowerManager) getSystemService(MainActivity.POWER_SERVICE);  
		wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "ChessWakeLock");
		setContentView(R.layout.activity_main);
		SetupGame();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
    	prefmenu = true;
		if ( null != ringtone ) {
			if ( ringtone.isPlaying() ) {
				ringtone.stop();
			}
		}
		PauseGame();
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.action_settings:
			showPrefs();
			return true;
		case R.id.action_reset_clocks:
			Dialog reset = ResetDialog();
			reset.show();
			return true;
		case R.id.action_help:
			HelpDialog help = new HelpDialog(this);
			help.setTitle(R.string.help_title);
			help.show();
			return true;
		case R.id.action_about:
			AboutDialog about = new AboutDialog(this);
			about.setTitle(R.string.about_title);
			about.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onWindowFocusChanged(boolean b) {
		if ( !prefmenu ) {
			CheckForNewPrefs();
		} else {
			prefmenu = false;
		}
	}
	
	@Override
	public void onPause() {
		if ( wl.isHeld() ) {
			wl.release();
		}
		if (null != ringtone) {
			if ( ringtone.isPlaying() ) {
				ringtone.stop();
			}
		}
		PauseGame();
		super.onPause();
	}

	@Override
	public void onResume() {
		/** Get the wakelock */
		wl.acquire();
		if (null != ringtone) {
			if ( ringtone.isPlaying() ) {
				ringtone.stop();
			}
		}
		super.onResume();
	}

	@Override
	public void onDestroy() {
		if (null != ringtone) {
			if ( ringtone.isPlaying() ) {
				ringtone.stop();
			}
		}
		super.onDestroy();
	}

	/**
	 * Formats the provided time to a readable string
	 * @param time - time to format
	 * @return str_time - formatted time (String)
	 */
	private String FormatTime(long time) {
		int secondsLeft = (int)time / 1000;
		int minutesLeft = secondsLeft / 60;
		secondsLeft     = secondsLeft % 60;
		
		String str_time;
		
		if (secondsLeft < 10) {
			str_time = "" + minutesLeft + ":0" + secondsLeft;
		} else {
			str_time = "" + minutesLeft + ":" + secondsLeft;
		}
		
		return str_time;
	}

	/** Starts the Preferences menu intent */
	private void showPrefs() {
		Intent prefsActivity = new Intent(MainActivity.this, Prefs.class);
		startActivity(prefsActivity);
	}
	
	private Dialog ResetDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.reset_question)
			.setCancelable(false)
			.setPositiveButton(getString(R.string.reset_yes), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					SetupGame();
					onTheClock = 0;
					dialog.dismiss();
				}
			})
			.setNegativeButton(getString(R.string.reset_no), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
		AlertDialog alert = builder.create();
		return alert;
	}

	/** 
	 * Checks for changes to the current preferences. We only want
	 * to re-create the game if something has been changed, so we
	 * check for differences any time onWindowFocusChanged() is called.
	 */
	public void CheckForNewPrefs() {
		SharedPreferences prefs = PreferenceManager
    	.getDefaultSharedPreferences(this);
		
		alertTone = prefs.getString("prefAlertSound", Settings.System.DEFAULT_RINGTONE_URI.toString());
		
		/** Check for a new delay style */
		String new_delay = prefs.getString("prefDelay","None");
		if (new_delay.equals("")) {
			new_delay = "None";
			Editor e = prefs.edit();
			e.putString("prefDelay", "None");
			e.commit();
		}
		
		if ( new_delay != delay ) {
			SetupGame();
		}
		
		/** Check for a new game time setting */
		int new_time;
		
		try {
			new_time = Integer.parseInt( prefs.getString("prefTimeW", "10") );
		} catch (Exception ex) {
			new_time = 10;
			Editor e = prefs.edit();
			e.putString("prefTimeW", "10");
			e.commit();
		}
		
		if ( new_time != time_W ) {
			SetupGame();
		}
		try {
			new_time = Integer.parseInt( prefs.getString("prefTimeB", "10") );
		} catch (Exception ex) {
			new_time = 10;
			Editor e = prefs.edit();
			e.putString("prefTimeB", "10");
			e.commit();
		}
		
		if ( new_time != time_B ) {
			SetupGame();
		}
		
		/** Check for a new delay time */
		int new_delay_time;
		try {
			new_delay_time = Integer.parseInt( prefs.getString("prefDelayTime", "0" ) );
		} catch (Exception ex) {
			new_delay_time = 0;
			Editor e = prefs.edit();
			e.putString("prefDelayTime", "0");
			e.commit();
		}
		
		if ( new_delay_time != delay_time ) {
			SetupGame();
		}
		
		boolean new_haptic = prefs.getBoolean("prefHaptic", false);
		if ( new_haptic != haptic ) {
			// No reason to reload the clocks for this one
			hapticChange = true;
			SetupGame();
		}
	}
	
	private void P1Click() {
		Button p1 = (Button)findViewById(R.id.Player1);
		Button p2 = (Button)findViewById(R.id.Player2);
		p1.setEnabled(false);
		p2.setEnabled(true);
		p1.performHapticFeedback(1);
		PowerManager pm = (PowerManager)getBaseContext().getSystemService(
			Context.POWER_SERVICE);
		pm.userActivity(1, true);
		/** Check if this is valid (i.e. if our time is running */
		if ( onTheClock == 1 )
			return;
		/** 
		 * Register that our time is running now 
		 * and that we haven't yet received our delay
		 */
		onTheClock = 1;
		if ( savedOTC == 0 ) {
			delayed = false;
		} else {
			savedOTC = 0;
		}
		if ( delay.equals(BRONSTEIN) ) {
			TextView tv_p2 = (TextView)findViewById(R.id.t_Player2);
			int secondsLeft = (int) (t_P2 / 1000);
			int minutesLeft = secondsLeft / 60;
			secondsLeft     = secondsLeft % 60;
			secondsLeft += 1;
			if ( secondsLeft == 60 ) {
				minutesLeft += 1;
				secondsLeft = 0;
			} else if ( t_P2 == 0 ) {
				secondsLeft = 0;
			} else if ( t_P2 == time_B * 60000 ) {
				secondsLeft -= 1;
			}
			if (secondsLeft < 10) {
				tv_p2.setText("" + minutesLeft + ":0" + secondsLeft);
			} else {
				tv_p2.setText("" + minutesLeft + ":" + secondsLeft);
			}
		}
		Button pp = (Button)findViewById(R.id.Pause);
		pp.setBackgroundResource(R.drawable.button_pause);
		pp.setTextColor(Color.DKGRAY);
		/** 
		 * Unregister the handler from player 1's clock and 
		 * create a new one which we register with player 2's clock.
		 */
		myHandler.removeCallbacks(mUpdateTimeTask1);
		myHandler.removeCallbacks(mUpdateTimeTask2);
		myHandler.postDelayed(mUpdateTimeTask2, 100);
	}

	/** Handles the "tick" event for Player 1's clock */
	private Runnable mUpdateTimeTask1 = new Runnable() {
		public void run() {
			TextView tv_p1 = (TextView)findViewById(R.id.t_Player1);
			String delay_string = "";
			/** Check for delays and apply them */
			if ( delay.equals(FISCHER) && !delayed ) {
				delayed = true;
				t_P1 += delay_time * 1000;
			} else if ( delay.equals(BRONSTEIN) && !delayed ) {
				delayed = true;
				b_delay = delay_time * 1000; //Deduct the first .1s;
				t_P1 += 100; //We'll deduct this again shortly
				delay_string = "+" + (b_delay / 1000 );
			} else if ( delay.equals(BRONSTEIN) && delayed ) {
				if ( b_delay > 0 ) {
					b_delay -= 100;
					t_P1 += 100;
				}
				if (b_delay > 0 ) {
					delay_string = "+" + ( ( b_delay / 1000 ) + 1 );
				}
			}
			/** Deduct 0.1s from P1's clock */
			t_P1 -= 100;
			long timeLeft = t_P1;
			/** Format for display purposes */
			int secondsLeft = (int) (timeLeft / 1000);
			int minutesLeft = secondsLeft / 60;
			secondsLeft     = secondsLeft % 60;
			secondsLeft += 1;
			if ( secondsLeft == 60 ) {
				minutesLeft += 1;
				secondsLeft = 0;
			} else if ( timeLeft == 0 ) {
				secondsLeft = 0;
			} else if ( timeLeft == time_W * 60000 ) {
				secondsLeft -= 1;
			}
			/** Did we run out of time? */
			if ( timeLeft == 0 ) {
				timeup = true;
				Button b1 = (Button)findViewById(R.id.Player1);
				Button b2 = (Button)findViewById(R.id.Player2);
				Button pp = (Button)findViewById(R.id.Pause);
				/** Set P1's button and clock text to red */
				tv_p1.setTextColor(Color.RED);
				b1.setClickable(false);
				b2.setClickable(false);
				pp.setClickable(false);
				Uri uri = Uri.parse(alertTone);
				ringtone = RingtoneManager.getRingtone(getBaseContext(), uri);
				if ( null != ringtone ) {
					ringtone.play();
				}
				/** Blink the clock display */
				myHandler.removeCallbacks(mUpdateTimeTask2);
				myHandler.postDelayed(Blink1, 500);
				return;
			}
			/** Color clock yellow if we're under 1 minute */
			if ( timeLeft < 60000 ) {
				tv_p1.setTextColor(Color.YELLOW);
			} else {
				tv_p1.setTextColor(getResources().getColor(R.color.app_white));
			}
			/** Display the time, omitting leading 0's for times < 10 minutes */
			if (secondsLeft < 10) {
				tv_p1.setText("" + minutesLeft + ":0" + secondsLeft + delay_string);
			} else {
				tv_p1.setText("" + minutesLeft + ":" + secondsLeft + delay_string);
			}
			/** Re-post the handler so it fires in another 0.1s */
			myHandler.postDelayed(this, 100);
		}
	};

	/** Called when P2ClickHandler registers a click/touch event */
	private void P2Click() {
		Button p1 = (Button)findViewById(R.id.Player1);
		Button p2 = (Button)findViewById(R.id.Player2);
		p1.setEnabled(true);
		p2.setEnabled(false);
		p2.performHapticFeedback(1);
		PowerManager pm = (PowerManager)getBaseContext().getSystemService(
			Context.POWER_SERVICE);
		pm.userActivity(1, true);
		/** Check if this is valid (i.e. if our time is running */
		if ( onTheClock == 2 )
			return;
		/** 
		 * Register that our time is running now 
		 * and that we haven't yet received our delay
		 */
		onTheClock = 2;
		if ( savedOTC == 0 ) {
			delayed = false;
		} else {
			savedOTC = 0;
		}
		/** 
		 * Make the other player's button green and our
		 * button and the pause button gray.
		 */
		if ( delay.equals(BRONSTEIN) ) {
			TextView tv_p1 = (TextView)findViewById(R.id.t_Player1);
			int secondsLeft = (int) (t_P1 / 1000);
			int minutesLeft = secondsLeft / 60;
			secondsLeft = secondsLeft % 60;
			secondsLeft += 1;
			if ( secondsLeft == 60 ) {
				minutesLeft += 1;
				secondsLeft = 0;
			} else if ( t_P1 == 0 ) {
				secondsLeft = 0;
			} else if ( t_P1 == time_W * 60000 ) {
				secondsLeft -= 1;
			}
			if (secondsLeft < 10) {
				tv_p1.setText("" + minutesLeft + ":0" + secondsLeft);
			} else {
				tv_p1.setText("" + minutesLeft + ":" + secondsLeft);            
			}
		}
		Button pp = (Button)findViewById(R.id.Pause);
		pp.setBackgroundResource(R.drawable.button_pause);
		pp.setTextColor(Color.DKGRAY);
		/** 
		 * Unregister the handler from player 2's clock and 
		 * create a new one which we register with player 1's clock.
		 */
		myHandler.removeCallbacks(mUpdateTimeTask1);
		myHandler.removeCallbacks(mUpdateTimeTask2);
		myHandler.postDelayed(mUpdateTimeTask1, 100);
	}

	/** Handles the "tick" event for Player 2's clock */
	private Runnable mUpdateTimeTask2 = new Runnable() {
		public void run() {
			TextView tv_p2 = (TextView)findViewById(R.id.t_Player2);
			String delay_string = "";
			/** Check for delays and apply them */
			if ( delay.equals(FISCHER) && !delayed ) {
				delayed = true;
				t_P2 += delay_time * 1000;
			} else if ( delay.equals(BRONSTEIN) && !delayed ) {
				delayed = true;
				b_delay = delay_time * 1000; //Deduct the first .1s;
				t_P2 += 100; //We'll deduct this again shortly
				delay_string = "+" + ( b_delay / 1000 );
			} else if ( delay.equals(BRONSTEIN) && delayed ) {
				if ( b_delay > 0 ) {
					b_delay -= 100;
					t_P2 += 100;
				}
				if (b_delay > 0 ) {
					delay_string = "+" + ( ( b_delay / 1000 ) + 1 );
				}
			}
			/** Deduct 0.1s from P2's clock */
			t_P2 -= 100;
			long timeLeft = t_P2;
			/** Format for display purposes */
			int secondsLeft = (int) (timeLeft / 1000);
			int minutesLeft = secondsLeft / 60;
			secondsLeft     = secondsLeft % 60;
			secondsLeft += 1;
			if ( secondsLeft == 60 ) {
				minutesLeft += 1;
				secondsLeft = 0;
			} else if ( timeLeft == 0 ) {
				secondsLeft = 0;
			} else if ( timeLeft == time_B * 60000 ) {
				secondsLeft -= 1;
			}
			/** Did we run out of time? */
			if ( timeLeft == 0 ) {
				timeup = true;
				Button b1 = (Button)findViewById(R.id.Player1);
				Button b2 = (Button)findViewById(R.id.Player2);
				Button pp = (Button)findViewById(R.id.Pause);
				/** Set P1's button and clock text to red */
				tv_p2.setTextColor(Color.RED);
				b1.setClickable(false);
				b2.setClickable(false);
				pp.setClickable(false);
				Uri uri = Uri.parse(alertTone);
				ringtone = RingtoneManager.getRingtone(getBaseContext(), uri);
				if ( null != ringtone ) {
					ringtone.play();
				}
				/** Blink the clock display */
				myHandler.removeCallbacks(mUpdateTimeTask2);
				myHandler.postDelayed(Blink2, 500);
				return;		
			}
			/** Color clock yellow if we're under 1 minute */
			if ( timeLeft < 60000) {
				tv_p2.setTextColor(Color.YELLOW);
			} else {
				tv_p2.setTextColor(getResources().getColor(R.color.app_black));
			}
			/** Display the time, omitting leading 0's for times < 10 minutes */
			if (secondsLeft < 10) {
				tv_p2.setText("" + minutesLeft + ":0" + secondsLeft + delay_string);
			} else {
				tv_p2.setText("" + minutesLeft + ":" + secondsLeft + delay_string);
			}
			/** Repost the handler so it fires in another 0.1s */
			myHandler.postDelayed(this, 100);
		}
	};

	/** Blinks the clock text if Player 1's time hits 0:00 */
	private Runnable Blink1 = new Runnable() {
		public void run() {
			TextView tv_p1 = (TextView)findViewById(R.id.t_Player1);
			/**
			 * Display the clock if it's blank, or blank it if
			 * it's currently displayed.
			 */
			if ( !blink ) {
				blink = true;
				tv_p1.setText("");
			} else {
				blink = false;
				tv_p1.setText("0:00");
			}
			/** Register a handler to fire again in 0.5s */
			myHandler.postDelayed(this, 500);	
		}
	};

	/** Blinks the clock text if Player 2's time hits 0:00 */
	private Runnable Blink2 = new Runnable() {
		public void run() {
			TextView tv_p2 = (TextView)findViewById(R.id.t_Player2);
			
			/**
			 * Display the clock if it's blank, or blank it if
			 * it's currently displayed.
			 */
			if ( !blink ) {
				blink = true;
				tv_p2.setText("");
			} else {
				blink = false;
				tv_p2.setText("0:00");
			}
			
			/** Register a handler to fire again in 0.5s */
			myHandler.postDelayed(this, 500);	
		}
	};

	/** Click handler for player 1's clock. */
	public OnClickListener P1ClickHandler = new OnClickListener() {
		public void onClick(View v) {
			P1Click();
		}
	};

	/** Click handler for player 2's clock */
	public OnClickListener P2ClickHandler = new OnClickListener() {
		public void onClick(View v) {
			P2Click();
		}
	};

	/** Click handler for the pause button */
	public OnClickListener PauseClickHandler = new OnClickListener() {
		public void onClick(View v) {
			PauseToggle();
		}
	};

	public OnClickListener PauseListener = new OnClickListener() {
		public void onClick(View v) {
			PauseToggle();
		}
	};

	private void PauseToggle() {
		Button p1 = (Button)findViewById(R.id.Player1);
		Button p2 = (Button)findViewById(R.id.Player2);
		Button pp = (Button)findViewById(R.id.Pause);
		
		pp.performHapticFeedback(1);
		
		/** Figure out if we need to pause or unpause */
		if ( onTheClock != 0 ) {
			savedOTC = onTheClock;
			onTheClock = 0;
			
			pp.setBackgroundResource(R.drawable.button_pause_pressed);
			pp.setTextColor(getResources().getColor(R.color.app_pause));
			p1.setEnabled(true);
			p2.setEnabled(true);
		
			myHandler.removeCallbacks(mUpdateTimeTask1);
			myHandler.removeCallbacks(mUpdateTimeTask2);
		} else {
			if ( savedOTC == 1 ) {
				P1Click();
			} else if ( savedOTC == 2 ) {
				P2Click();
			} else {
				return;
			}
		}
	}

	/** Set up (or refresh) all game parameters */
	private void SetupGame() { 
	    /** Load all stored preferences */
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	    
	    /** Take care of a haptic change if needed */
		haptic = prefs.getBoolean("prefHaptic", false);

		Button p1 = (Button)findViewById(R.id.Player1);
		Button p2 = (Button)findViewById(R.id.Player2);
		Button pp = (Button)findViewById(R.id.Pause);
		p1.setOnClickListener(P1ClickHandler);
		p2.setOnClickListener(P2ClickHandler);
		pp.setOnClickListener(PauseClickHandler);
		TextView tv_p1 = (TextView)findViewById(R.id.t_Player1);
		TextView tv_p2 = (TextView)findViewById(R.id.t_Player2);
		tv_p1.setTextColor(getResources().getColor(R.color.app_white));
		tv_p2.setTextColor(getResources().getColor(R.color.app_black));
		tv_p1.bringToFront();
		tv_p2.bringToFront();
		p1.setEnabled(true);
		p2.setEnabled(true);
		p1.setHapticFeedbackEnabled(haptic);
		p2.setHapticFeedbackEnabled(haptic);
		pp.setHapticFeedbackEnabled(haptic);
		pp.setTextColor(Color.DKGRAY);

		if (hapticChange)
        {
        	/**
        	 * We're just changing haptic feedback on this run through,
        	 * don't reload everything else!
        	 */
        	hapticChange = false;
        	return;
        }
	    
		delay = prefs.getString("prefDelay","None");
		if ( delay.equals("")) {
			delay = "None";
			Editor e = prefs.edit();
			e.putString("prefDelay", "None");
			e.commit();
		}
		
		try {
			time_W = Integer.parseInt( prefs.getString("prefTimeW", "10") );	
		} catch (Exception ex) {
			time_W = 10;
			Editor e = prefs.edit();
			e.putString("prefTimeW", "10");
			e.commit();
		}
		try {
			time_B = Integer.parseInt( prefs.getString("prefTimeB", "10") );	
		} catch (Exception ex) {
			time_B = 10;
			Editor e = prefs.edit();
			e.putString("prefTimeB", "10");
			e.commit();
		}
		
		try {
			delay_time = Integer.parseInt( prefs.getString("prefDelayTime", "0") );
		} catch (Exception ex) {
			delay_time = 0;
			Editor e = prefs.edit();
			e.putString("prefDelayTime", "0");
			e.commit();
		}
		
		alertTone = prefs.getString("prefAlertSound", Settings.System.DEFAULT_RINGTONE_URI.toString());		
		if (alertTone.equals("")) {
			alertTone = Settings.System.DEFAULT_RINGTONE_URI.toString();
			Editor e = prefs.edit();
			e.putString("prefAlertSound", alertTone);
			e.commit();
		}
		
		Uri uri = Uri.parse(alertTone);
		ringtone = RingtoneManager.getRingtone(getBaseContext(), uri);
		
		/** Set time equal to minutes * ms per minute */
		t_P1 = time_W * 60000;
		t_P2 = time_B * 60000;
		
		/** Set up the buttons */
		pp.setBackgroundResource(R.drawable.button_pause);

		/** Format and display the clocks */
		tv_p1.setText(FormatTime(t_P1));
		tv_p2.setText(FormatTime(t_P2));

		/** 
		 * Register the click listeners and unregister any
		 * text blinking timers that may exist.
		 */
		p1.setOnClickListener(P1ClickHandler);
		p2.setOnClickListener(P2ClickHandler);
		pp.setOnClickListener(PauseListener);
		myHandler.removeCallbacks(Blink1);
		myHandler.removeCallbacks(Blink2);
	}

	private void PauseGame() {
		Button p1 = (Button)findViewById(R.id.Player1);
		Button p2 = (Button)findViewById(R.id.Player2);
		Button pp = (Button)findViewById(R.id.Pause);
		/** Save the currently running clock, then pause */
		if ( ( onTheClock != 0 ) && ( !timeup ) ) {
			savedOTC = onTheClock;
			onTheClock = 0;
			pp.setBackgroundResource(R.drawable.button_pause_pressed);
			pp.setTextColor(getResources().getColor(R.color.app_pause));
			p1.setEnabled(true);
			p2.setEnabled(true);
			myHandler.removeCallbacks(mUpdateTimeTask1);
			myHandler.removeCallbacks(mUpdateTimeTask2);
		}
	}

}
