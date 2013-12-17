package fr.utt.erasmutt.fragments;

import java.util.List;

import fr.utt.erasmutt.Constants;
import fr.utt.erasmutt.R;
import fr.utt.erasmutt.R.id;
import fr.utt.erasmutt.fragments.activities.custom_adapter_reviews;
import fr.utt.erasmutt.fragments.activities.custom_adapter_reviews_user;
import fr.utt.erasmutt.sqlite.DatabaseHelper;
import fr.utt.erasmutt.sqlite.model.Review;
import fr.utt.erasmutt.tools.Utility;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

public class UserReviews extends Fragment {
	private List<Review> listReview;
    private custom_adapter_reviews_user ca;
    private DatabaseHelper db;
    private  ListView listViewReviews;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
    	db= new DatabaseHelper(getActivity());
        // Inflate the layout for this fragment
    	
        return inflater.inflate(R.layout.fragment_activity_user_reviews, container, false);
    }

    
    @Override
    public void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	
    	listReview=db.getReviewsByUserId(Constants.user.getIdUser());
		listViewReviews = (ListView)getActivity().findViewById(R.id.custom_list_reviewuser);
	
		ca =  new custom_adapter_reviews_user(getActivity().getLayoutInflater().getContext(), listReview);
		listViewReviews.setAdapter(ca);
    }
    
}
