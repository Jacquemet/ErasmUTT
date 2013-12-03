package fr.utt.erasmutt;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import fr.utt.erasmutt.fragments.activities.DetailsActivityFragment;
import fr.utt.erasmutt.fragments.activities.ListActivityFragment;
import fr.utt.erasmutt.fragments.activities.ListActivityFragment.OnHeadlineSelectedListener;
import fr.utt.erasmutt.networkConnection.HttpRequest;

public class ActivityHandlerActivity extends FragmentActivity implements OnHeadlineSelectedListener{

	HttpRequest requestSearch;
	String[] titleActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_handler);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(R.string.title_activity_activity_handler + getIntent().getExtras().getString("query"));
		
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
		return true;
	}
	
	
	//Lorsque l'on sélectionne un élément on charge le détails de l'activité dans le Frame ou on actualise le contenu du fragment
	@Override
	public void onArticleSelected(int position) {
	       // The user selected the headline of an article from the HeadlinesFragment

        // Capture the article fragment from the activity layout
        DetailsActivityFragment articleFrag = (DetailsActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_user_details);

        //Si le détail de l'activité est présent alors on est dans un affichage tablette
        if (articleFrag != null) {
            // Call a method in the ArticleFragment to update its content
            articleFrag.updateArticleView(position);

        }
        //Sinon on est dans un affichage smartphone avec un framelayout
        else {

            // Create fragment and give it an argument for the selected article
            DetailsActivityFragment newFragment = new DetailsActivityFragment();
            Bundle args = new Bundle();
            args.putInt(DetailsActivityFragment.ARG_POSITION, position);
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
