package fr.utt.erasmutt.fragments;

import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import fr.utt.erasmutt.OnHeadlineSelectedListener;
import fr.utt.erasmutt.R;
import fr.utt.erasmutt.networkConnection.HttpCallbackByte;
import fr.utt.erasmutt.networkConnection.RetreiveImgTask;
import fr.utt.erasmutt.sqlite.DatabaseHelper;
import fr.utt.erasmutt.sqlite.model.Activities;

public class PopularActivitiesFragment extends Fragment{

	DatabaseHelper db;
	Bitmap bitmap;
	ImageView[] photo;
	Integer indice =0;
	List <Activities> act;
	LinearLayout linearHor; 
	LinearLayout linearVer; 
	
	OnHeadlineSelectedListener selectedActivityCallback;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	
    	indice =0;
    	db= new DatabaseHelper(getActivity());
    	LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.fragment_popular_activities, container, false);
    	ll.setBackgroundColor(getResources().getColor(R.color.background_grey));
    	
    	
    	act = db.getFocusOnActivities();
    	photo = new ImageView[act.size()];
    	
		  //TODO: Problème dépasse le tableau !
		  for(int i=0; i<act.size();i++){
		
	          if(!act.get(i).getPictureActivityString().equals("") && act.get(i).getPictureActivity()==null){
	        	 
	        	 photo[i] = new ImageView(getActivity());
	        	 
	         	 new RetreiveImgTask(new HttpCallbackByte() {
						@Override
						public Object call(byte[] imgbyte) {
							// TODO Auto-generated method stub
							Log.v("call ", "callback done");
							
							Bitmap b = BitmapFactory.decodeByteArray( imgbyte,  0,imgbyte.length);
							photo[indice].setImageBitmap(Bitmap.createScaledBitmap(b, 300, 300, false));
							act.get(indice).setPictureActivity(imgbyte);
							db.updateImageActivity(act.get(indice));
							indice++;
							
							return null;
						}
					}).execute(act.get(i).getPictureActivityString()); 	
	          }
	          else if(act.get(i).getPictureActivity()!=null){
	        	  Log.v(" Loading Image ", "--Charger image depuis la bd--");
	        	  photo[i] = new ImageView(getActivity());
	        	  Bitmap b = BitmapFactory.decodeByteArray( act.get(i).getPictureActivity(),  0,act.get(i).getPictureActivity().length);
				  photo[indice].setImageBitmap(Bitmap.createScaledBitmap(b, 300, 300, false));
	        	  indice++;
	          }else {
	        	  //TODO : pas d'image ds BDD
	          }
	          
	          
		  }
		
		  linearHor = new LinearLayout(getActivity());
		  
	      linearHor.setOrientation(LinearLayout.HORIZONTAL);
	      
	      //For all the focus Activities
		  for(int j=0;j<act.size();j++){
			  linearVer = new LinearLayout(getActivity());
			  linearVer.setOrientation(LinearLayout.VERTICAL);
			  linearVer.setGravity(Gravity.CENTER);
			  // We create a horizontal linearLayout every two elements 
			  if(j%2==0 && j!=0){
				ll.addView(linearHor);
				linearHor = new LinearLayout(getActivity());
		     	linearHor.setOrientation(LinearLayout.HORIZONTAL);
		     	  
			  }
			  
			  int dimen = getResources().getDimensionPixelSize(R.dimen.field_vertical_margin);
			  
			  LinearLayout.LayoutParams paramsImage = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			  paramsImage.setMargins(0, dimen, 0, 0);
			  photo[j].setLayoutParams(paramsImage);
			  linearVer.addView(photo[j]);
			 
			 linearVer.setBackgroundColor(getResources().getColor(R.color.background_white));
			 
			 TextView text = new TextView(getActivity());
			 text.setText(act.get(j).getName());
			 text.setGravity(Gravity.CENTER);
			 linearVer.addView(text);
			 
			 RatingBar ratingBar = (RatingBar) getActivity().getLayoutInflater().inflate(R.layout.custom_ratingbar_small, null);
			 ratingBar.setStepSize(0.5f);
			 ratingBar.setNumStars(5);
			 ratingBar.setRating(act.get(j).getAverageMark());
			 Log.v(act.get(j).getName(),String.valueOf(act.get(j).getAverageMark()));
			 
			 ratingBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			 
			 linearVer.addView(ratingBar);
			 
			 LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
			 
			 //Set padding if it's the first element, we reduce all the margins except the left one
			 if(j%2 == 0)
				 params.setMargins(dimen,dimen/2,dimen/2,dimen/2);
			 // Else if it's the second element, we reduce all the margins except the Right one
			 else
				 params.setMargins(dimen/2,dimen/2,dimen,dimen/2);
			 
			 linearVer.setLayoutParams(params);
			 linearVer.setTag(act.get(j).getIdActivity());
			 
			 linearVer.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Toast.makeText(getActivity(), "test "+v.getTag() , Toast.LENGTH_LONG).show();
					selectedActivityCallback.onArticleSelected(Integer.valueOf(v.getTag().toString()));
				}
			});
			 
			 linearHor.addView(linearVer);
	        	 
          }	
		  ll.addView(linearHor);
    	return ll;
        
    }
    
    
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception.
		try {
			selectedActivityCallback = (OnHeadlineSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+ " must implement OnHeadlineSelectedListener");
		}
	}
	
	
    
}
