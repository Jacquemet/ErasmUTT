package fr.utt.erasmutt.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import fr.utt.erasmutt.Constants;
import fr.utt.erasmutt.R;
import fr.utt.erasmutt.sqlite.DatabaseHelper;
import fr.utt.erasmutt.sqlite.model.Review;
import fr.utt.erasmutt.tools.CustomAdapterReviewsUser;

public class UserReviews extends Fragment {
	private List<Review> listReview;
    private CustomAdapterReviewsUser ca;
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
    	super.onStart();
    	
    	listReview=db.getReviewsByUserId(Constants.user.getIdUser());
    	TextView anyReview = (TextView)getActivity().findViewById(R.id.anyReviews);
		
		if(listReview.size()>0){
			listViewReviews = (ListView)getActivity().findViewById(R.id.custom_list_reviewuser);
			
			ca =  new CustomAdapterReviewsUser(getActivity().getLayoutInflater().getContext(), listReview);
			listViewReviews.setAdapter(ca);
			anyReview.setVisibility(View.GONE);
		}
		else{
			
			anyReview.setVisibility(View.VISIBLE);
		}
    }
    
}
