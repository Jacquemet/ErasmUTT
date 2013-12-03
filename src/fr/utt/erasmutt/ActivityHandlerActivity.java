package fr.utt.erasmutt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Toast;
import fr.utt.erasmutt.fragments.activities.ListActivityFragment;
import fr.utt.erasmutt.networkConnection.HttpCallback;
import fr.utt.erasmutt.networkConnection.HttpRequest;

public class ActivityHandlerActivity extends FragmentActivity {

	HttpRequest requestSearch;
	String[] titleActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_handler);
		
		//On vérifie que la connexion au réseau est valide
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
        	loadActitivies();
        } else {
        	Toast.makeText(getApplicationContext(), R.string.network_disabled, Toast.LENGTH_LONG).show();
        }
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_handler, menu);
		return true;
	}
	
	private void loadActitivies(){
		requestSearch = new HttpRequest(new HttpCallback() {
			
			@Override
			public Object call(JSONObject jsonResponse) {

				try {
					//TODO : Exploiter les données reçues
					if (!jsonResponse.getBoolean("error")) {

						JSONArray jObject = jsonResponse.getJSONArray("listActivities");

				        for (int i = 0; i < jObject.length(); i++) {
				        	 JSONObject menuObject = jObject.getJSONObject(i);
				        	 titleActivity[i] = menuObject.getString("name");
				        }
						displayList();
					} else {
						
					}
				} catch (JSONException e) {
					Toast.makeText(getApplicationContext(),"erreur parsage", Toast.LENGTH_LONG).show();
				}
				
				return null;
			}
		});
		
		requestSearch.execute(Constants.urlRoot+"activiesManager.php?typeActivies=lister&token="+Constants.user.getToken());
	
		
	}

	
	private void displayList() {
		
	     // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.content_frame) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            // if (savedInstanceState != null) {
            //    return;
            //}

            // Create an instance of ExampleFragment
            ListActivityFragment firstFragment = new ListActivityFragment();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());
            Bundle bundle = new Bundle();
            bundle.putStringArray("titleActivity", titleActivity);
            firstFragment.setArguments(bundle);

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.content_frame, firstFragment).commit();
        }
        
	}
	
/*    @Override
    protected void onNewIntent(Intent intent) {
    	setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            Toast.makeText(getApplicationContext(),"Query : "+query, Toast.LENGTH_LONG).show();
        }
    }
*/


}
