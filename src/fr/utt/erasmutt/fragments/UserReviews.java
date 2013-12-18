package fr.utt.erasmutt.fragments;

import java.util.List;

import fr.utt.erasmutt.Constants;
import fr.utt.erasmutt.R;
import fr.utt.erasmutt.R.id;
import fr.utt.erasmutt.sqlite.DatabaseHelper;
import fr.utt.erasmutt.sqlite.model.Review;
import fr.utt.erasmutt.tools.Utility;
import fr.utt.erasmutt.tools.custom_adapter_reviews;
import fr.utt.erasmutt.tools.custom_adapter_reviews_user;
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
import android.widget.TextView;

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
    	TextView anyReview = (TextView)getActivity().findViewById(R.id.anyReviews);
		
		if(listReview.size()>0){
			listViewReviews = (ListView)getActivity().findViewById(R.id.custom_list_reviewuser);
			
			ca =  new custom_adapter_reviews_user(getActivity().getLayoutInflater().getContext(), listReview);
			listViewReviews.setAdapter(ca);
			anyReview.setVisibility(View.GONE);
		}
		else{
			
			anyReview.setVisibility(View.VISIBLE);
		}
    }
    
}
