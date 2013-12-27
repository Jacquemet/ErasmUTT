package fr.utt.erasmutt;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;
import fr.utt.erasmutt.fragments.PopularActivitiesFragment;
import fr.utt.erasmutt.fragments.UserDetailsFragment;
import fr.utt.erasmutt.fragments.UserReviews;
import fr.utt.erasmutt.fragments.activities.DetailsActivityFragment;
import fr.utt.erasmutt.maps.MapActivity;
import fr.utt.erasmutt.sqlite.DatabaseHelper;
/**
 * This Activity is the main activity which manage the drawable and display the good fragment
 * @author Thibault Jacquemet & Kévin Larue
 *
 */
public class HomeActivity extends FragmentActivity implements OnHeadlineSelectedListener{

	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mTitles;
    
    private UserDetailsFragment userDetailsFrag;
    private PopularActivitiesFragment popularActivities;
    private UserReviews userReviews;
    private DatabaseHelper db;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Create the database
        db = new DatabaseHelper(getApplicationContext());
        //Get the drawer title
        mTitle = mDrawerTitle = getTitle();
        mTitles = getResources().getStringArray(R.array.text_menu_array);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        
        //Set a Hint
        searchView.setQueryHint(getString(R.string.query_hint));
        
        //Transparent white HintTextColor 
        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchView.findViewById(searchPlateId);
        if (searchPlate!=null) {
            int searchTextId = searchPlate.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView searchText = (TextView) searchPlate.findViewById(searchTextId);
            if (searchText!=null) {
	            searchText.setHintTextColor(getResources().getColor(R.color.transparent_white));
            }
        }
        
        searchView.setIconifiedByDefault(false);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        
        //Action on the SearchView
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				// We close the SearchView and we launch the result activity
				searchItem.collapseActionView();
				Intent intent = new Intent(getApplicationContext(), ActivityHandlerActivity.class);
				intent.putExtra("query", query);
				startActivity(intent);
	            return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
        
        case R.id.action_location:
			Intent intent = new Intent(getApplicationContext(), MapActivity.class); 
			intent.putExtra("LoadAll",true);
            startActivity(intent);
            return true;
        
        case R.id.action_settings:

        	return true;
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
        	
        	//Logout Action
        	db.userLogout();
        	String goodbyeMessage = String.format(getString(R.string.GoodBye), Constants.user.getFirstname());
        	Constants.user.logout();
        	
        	//Back to the LoginActivity
        	Intent  intentLogout = new Intent(getApplicationContext(),LoginActivity.class);
        	intentLogout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        	Toast.makeText(getApplicationContext(), goodbyeMessage, Toast.LENGTH_LONG).show();
        	startActivity(intentLogout);
        	
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
    	setupFragments();
    	
    	switch (position) {
		case 0:
			showFragment(popularActivities);
			break;
		case 1:
				showFragment(userDetailsFrag);
			break;
		case 2:
			//showFragment(listActivityFragment);
			break;
		case 3:
			showFragment(userReviews);
			break;	

		default:
			break;
		}
    	
    	//Change the Title, set the item to checked and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    
    /**
     * Change the title of the Drawer ActionBar
     */
    @Override
    public void setTitle(CharSequence title) {
    	mTitle = title;
    	getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    
	 // Setup the fragment to show here
    private void setupFragments() {
        FragmentManager fm = getSupportFragmentManager();
        
        this.userDetailsFrag = (UserDetailsFragment) fm.findFragmentById(R.id.fragment_user_details);
        if (this.userDetailsFrag == null) {
            this.userDetailsFrag = new UserDetailsFragment();
        }
        this.popularActivities = (PopularActivitiesFragment) fm.findFragmentById(R.id.fragment_popular_activities);
        if (this.popularActivities == null) {
        	this.popularActivities = new PopularActivitiesFragment();
        }
        this.userReviews = (UserReviews) fm.findFragmentById(R.id.fragment_activity_user_reviews);
        if (this.userReviews == null) {
        	this.userReviews = new UserReviews();
        }
    }
    
    //Show fragment in parameters
    private void showFragment(final Fragment fragment) {
        if (fragment == null)
            return;
 
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    //Load a fragment when we select a focusOn Activity
	@Override
	public void onArticleSelected(int position) {

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
