package fr.utt.erasmutt.fragments.activities;

import java.util.List;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import fr.utt.erasmutt.R;
import fr.utt.erasmutt.maps.MapActivity;
import fr.utt.erasmutt.networkConnection.HttpCallbackByte;
import fr.utt.erasmutt.networkConnection.RetreiveImgTask;
import fr.utt.erasmutt.sqlite.DatabaseHelper;
import fr.utt.erasmutt.sqlite.model.Activities;
import fr.utt.erasmutt.sqlite.model.Review;

public class DetailsActivityFragment extends Fragment {

	    public final static String ARG_ID_ACTIVITY = "position";
	    int mCurrentPosition = -1;
	    
	    DatabaseHelper db;
	    
	    private Activities activityDetails;
	    
	    private ImageView imageActivity;
	    private TextView titleActivity;
	    
	    private TextView textAddress;
	    private TextView valueAddress;
	    private ImageButton imageButtonLocation;
	    
	    private TextView textDescription;
	    private TextView valueDescription;
	    
	    private TextView textLink;
	    private TextView linkWebsite;
	    
	    private TextView textRating;
	    private RatingBar averageRatingBarActivity;
	    private TextView labelNumberReview;
	    
	    private Button writeReview;
	    
	    private List<Review> listReview;
	    
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
	        Bundle savedInstanceState) {

	        // If activity recreated (such as from screen rotate), restore
	        // the previous article selection set by onSaveInstanceState().
	        // This is primarily necessary when in the two-pane layout.
	        if (savedInstanceState != null) {
	            mCurrentPosition = savedInstanceState.getInt(ARG_ID_ACTIVITY);
	        }

	        // Inflate the layout for this fragment
	        return inflater.inflate(R.layout.fragment_details_activity, container, false);
	    }

	    @Override
	    public void onStart() {
	        super.onStart();

	        // During startup, check if there are arguments passed to the fragment.
	        // onStart is a good place to do this because the layout has already been
	        // applied to the fragment at this point so we can safely call the method
	        // below that sets the article text.
	        Bundle args = getArguments();
	        if (args != null) {
	            // Set article based on argument passed in
	            updateArticleView(args.getInt(ARG_ID_ACTIVITY));
	        } else if (mCurrentPosition != -1) {
	            // Set article based on saved instance state defined during onCreateView
	            updateArticleView(mCurrentPosition);
	        }
	    }

	    //Update the view with the position placed in parameters
	    public void updateArticleView(int position) {
	       
	    	db = new DatabaseHelper(getActivity());
	        activityDetails = db.getActivitiesById(position);
	        
	        imageActivity = (ImageView) getActivity().findViewById(R.id.imageViewAcivityDetails);

	        if(activityDetails.getPictureActivityString()!="" && activityDetails.getPictureActivity()==null){
		        	 
	         	 new RetreiveImgTask(new HttpCallbackByte() {
						@Override
						public Object call(byte[] imgbyte) {
							
							Bitmap b = BitmapFactory.decodeByteArray( imgbyte,  0,imgbyte.length);
							imageActivity.setImageBitmap(Bitmap.createScaledBitmap(b, 300, 300, false));
							activityDetails.setPictureActivity(imgbyte);
							db.updateImageActivity(activityDetails);
							
							return null;
						}
					}).execute(activityDetails.getPictureActivityString()); 	
	          }
	          else if(activityDetails.getPictureActivity()!=null){
	        	  Bitmap b = BitmapFactory.decodeByteArray(activityDetails.getPictureActivity(), 0, activityDetails.getPictureActivity().length);
	        	  imageActivity.setImageBitmap(Bitmap.createScaledBitmap(b, 300, 300, false));
	          }
	        
	        //Title ActionBar update with the name of the selected element
	    	getActivity().getActionBar().setTitle(activityDetails.getName());
	        
	        titleActivity = (TextView) getActivity().findViewById(R.id.titleActivityDetails);
	        titleActivity.setText(activityDetails.getName());
	        titleActivity.setVisibility(View.VISIBLE);

	        textAddress = (TextView) getActivity().findViewById(R.id.labelAddressActivity);
	        textAddress.setVisibility(View.VISIBLE);
	        
	        valueAddress = (TextView) getActivity().findViewById(R.id.valueAddressActivity);
	        valueAddress.setText(activityDetails.getAddress());
	        
	        imageButtonLocation = (ImageButton) getActivity().findViewById(R.id.imageButtonLocation);
	        imageButtonLocation.setVisibility(View.VISIBLE);
	        imageButtonLocation.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity().getApplicationContext(), MapActivity.class); 
					intent.putExtra("LoadAll",false);
					intent.putExtra("idActivity", activityDetails.getIdActivity());
		            startActivity(intent);
				}
			});
	        
	        textDescription = (TextView) getActivity().findViewById(R.id.labelDescription);
	        textDescription.setVisibility(View.VISIBLE);
	        
	        valueDescription = (TextView) getActivity().findViewById(R.id.valueDescription);
	        valueDescription.setText(activityDetails.getDesciptionActivity());	   
	        
	        textLink = (TextView) getActivity().findViewById(R.id.labelWebsite);
	        textLink.setVisibility(View.VISIBLE);
	        
	        linkWebsite = (TextView) getActivity().findViewById(R.id.linkWebsite);
	        linkWebsite.setText(activityDetails.getWebsite());	
	        
	        //Launch a web search for the website of the element
	        linkWebsite.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
		            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
		            intent.putExtra(SearchManager.QUERY, linkWebsite.getText().toString());
		            // catch event that there's no activity to handle intent
		            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
		                startActivity(intent);
		            } else {
		                Toast.makeText(getActivity(), R.string.app_not_available, Toast.LENGTH_LONG).show();
		            }
				}
			});
	        
	        textRating = (TextView) getActivity().findViewById(R.id.labelRating);
	        textRating.setVisibility(View.VISIBLE);
	        
	        averageRatingBarActivity =  (RatingBar) getActivity().findViewById(R.id.averageRatingBarActivity);
	        averageRatingBarActivity.setRating(activityDetails.getAverageMark());
	        averageRatingBarActivity.setVisibility(View.VISIBLE);
	        
	        
	        listReview = db.getReviewsByActivity(activityDetails.getIdActivity());
	        
	        labelNumberReview =  (TextView) getActivity().findViewById(R.id.labelNumberReview);
	        
	        //We use a plurals string with a number of reviews in parameter
	        Resources res = getResources();
	        String numberOfReviews = res.getQuantityString(R.plurals.labelNumberReview, listReview.size());
	        String messageReviews = String.format(numberOfReviews, String.valueOf(listReview.size()));
	        labelNumberReview.setText(messageReviews);
	        
	        writeReview = (Button) getActivity().findViewById(R.id.writeReview);
	        writeReview.setVisibility(View.VISIBLE);
	        
	        
	        mCurrentPosition = position;
	    }

	    @Override
	    public void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);

	        // Save the current article selection in case we need to recreate the fragment
	        outState.putInt(ARG_ID_ACTIVITY, mCurrentPosition);
	    }
	
}
