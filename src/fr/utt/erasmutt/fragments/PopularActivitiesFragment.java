package fr.utt.erasmutt.fragments;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;

import fr.utt.erasmutt.R;
import fr.utt.erasmutt.networkConnection.HttpCallbackByte;
import fr.utt.erasmutt.sqlite.DatabaseHelper;
import fr.utt.erasmutt.sqlite.model.Activities;
import android.app.ActionBar.LayoutParams;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.Visibility;
import android.os.AsyncTask;
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
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PopularActivitiesFragment extends Fragment{

	DatabaseHelper db;
	Bitmap bitmap;
	ImageView[] photo = new ImageView[4];
	Integer indice =0;
	List <Activities> act;
	LinearLayout linearHor; 
	LinearLayout linearVer; 
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	indice =0;
    	db= new DatabaseHelper(getActivity());
    	LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.fragment_popular_activities, container, false);
    	ll.setBackgroundColor(getResources().getColor(R.color.background_grey));
		
    	
    	
    	act = db.getFocusOnActivities();
		  
		  for(int i=0; i<act.size();i++){
		
	          if(act.get(i).getPictureActivityString()!=""&& act.get(i).getPictureActivity()==null){
	        	 
	        	 photo[i] = new ImageView(getActivity());
	        	 
	         	 new RetreiveImgTask(new HttpCallbackByte() {
						@Override
						public Object call(byte[] imgbyte) {
							// TODO Auto-generated method stub
							Log.v("call ", "callback done");
							
							Bitmap b = BitmapFactory.decodeByteArray( imgbyte,  0,imgbyte.length);
							photo[indice].setImageBitmap(Bitmap.createScaledBitmap(b, 200, 200, false));
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
				  photo[indice].setImageBitmap(Bitmap.createScaledBitmap(b, 200, 200, false));
	        	  indice++;
	          }
	          
	          
		  }
		
		  linearHor = new LinearLayout(getActivity());
		  
	      linearHor.setOrientation(LinearLayout.HORIZONTAL);
	      
		  for(int j=0;j<4;j++){
			  linearVer = new LinearLayout(getActivity());
			  linearVer.setOrientation(LinearLayout.VERTICAL);
			  linearVer.setGravity(Gravity.CENTER);
			  if(j%2==0 && j!=0){
				ll.addView(linearHor);
				linearHor = new LinearLayout(getActivity());
		     	linearHor.setOrientation(LinearLayout.HORIZONTAL);
		     	  
			  }
			 
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
			 ratingBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			 
			 linearVer.addView(ratingBar);
			 
			 LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
			 params.setMargins(10, 10, 10, 10);
			 linearVer.setLayoutParams(params);
			 linearVer.setTag(act.get(j).getIdActivity());
			 
			 linearVer.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), "test "+v.getTag() , Toast.LENGTH_LONG).show();
					
				}
			});
			 
			 linearHor.addView(linearVer);
	        	 
          }	
		  ll.addView(linearHor);
    	return ll;
        
    }
    
    private byte[] getLogoImage(String url){
	     try {
	             URL imageUrl = new URL(url);
	             URLConnection ucon = imageUrl.openConnection();
	             InputStream is = ucon.getInputStream();
	             BufferedInputStream bis = new BufferedInputStream(is);
	             ByteArrayBuffer baf = new ByteArrayBuffer(500);
	             int current = 0;
	             while ((current = bis.read()) != -1) {
	                     baf.append((byte) current);
	             }
	             Log.v("getLogoImage Fin de url : ",url);
	             return baf.toByteArray();
	     } catch (Exception e) {
	             Log.v("ImageManager", "Error: " + e.toString());
	     }
	     return null;
	}
	
	
	class RetreiveImgTask extends AsyncTask<String, Void, byte[]> {

	    private Exception exception;

	    HttpCallbackByte callback;
	
	    RetreiveImgTask(HttpCallbackByte Callback) {
	        callback = Callback;	        
	    }
	    
		protected byte[] doInBackground(String... params) {
	        try {
	            return getLogoImage(params[0]);
	        } catch (Exception e) {
	            this.exception = e;
	            return null;
	        }
	    }

	    protected void onPostExecute(byte[] tabImg) {
	    	this.callback.call(tabImg);
	    }
	}
}
