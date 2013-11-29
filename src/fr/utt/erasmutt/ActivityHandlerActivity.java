package fr.utt.erasmutt;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ActivityHandlerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_handler);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_handler, menu);
		return true;
	}

}
