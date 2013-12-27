package fr.utt.erasmutt;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import fr.utt.erasmutt.fragments.activities.DetailsActivityFragment;
import fr.utt.erasmutt.fragments.activities.ListActivityFragment;
import fr.utt.erasmutt.networkConnection.HttpRequest;
import fr.utt.erasmutt.sqlite.DatabaseHelper;

/**
 * This Activity show a list of interest point and show the details of it
 * This Activity manage a frame when the user is using a smartphone and also manage 2 fragments when the user is using a tablet
 * @author Thibault Jacquemet & Kévin Larue
 *
 */
public class ActivityHandlerActivity extends FragmentActivity implements OnHeadlineSelectedListener{

	HttpRequest requestSearch;
	String[] titleActivity;
	DetailsActivityFragment articleFrag; 
	Menu menu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
   	 	// Capture the article fragment from the activity layout
		setContentView(R.layout.activity_activity_handler);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(getResources().getString(R.string.title_activity_activity_handler) +" "+ getIntent().getExtras().getString("query"));
		
	     // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.content_frame) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create an instance of ExampleFragment
            ListActivityFragment firstFragment = new ListActivityFragment();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'content_frame' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.content_frame, firstFragment).commit();
        }
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_handler, menu);
		this.menu = menu;
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action buttons
        switch(item.getItemId()) {
        
        case R.id.action_help:
        	
        	Intent i = new Intent(Intent.ACTION_VIEW);
        	i.setData(Uri.parse(Constants.urlHelp));
        	
            if (i.resolveActivity(getPackageManager()) != null) {
            	startActivity(i);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
        	
        	return true;	
        case R.id.action_logout:
        	DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        	db.userLogout();
        	String goodbyeMessage = String.format(getString(R.string.GoodBye), Constants.user.getFirstname());
        	Constants.user.logout();
        	Intent  intentLogout = new Intent(getApplicationContext(),LoginActivity.class);
        	intentLogout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        	
        	Toast.makeText(getApplicationContext(), goodbyeMessage, Toast.LENGTH_LONG).show();
        	startActivity(intentLogout);
        	
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	//Lorsque l'on sélectionne un élément on charge le détails de l'activité dans le Frame ou on actualise le contenu du fragment
	@Override
	public void onArticleSelected(int position) {
	    // The user selected the headline of an article from the HeadlinesFragment

        // Capture the article fragment from the activity layout
        articleFrag = (DetailsActivityFragment) getSupportFragmentManager().findFragmentById(R.id.details_activity_frag);

        //If the detail fragment is here, we are using a tablet with 2 fragments
        if (articleFrag != null) {
            
            // Call a method in the ArticleFragment to update its content
            articleFrag.updateArticleView(position);

        }
        //Else we are using a smartphone with 1 frameLayout
        else {

            // Create fragment and give it an argument for the selected article
            DetailsActivityFragment newFragment = new DetailsActivityFragment();
            Bundle args = new Bundle();
            args.putInt(DetailsActivityFragment.ARG_ID_ACTIVITY, position);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.content_frame, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }		
	}
	
}
