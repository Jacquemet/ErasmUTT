package fr.utt.erasmutt.fragments;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import fr.utt.erasmutt.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PopularActivitiesFragment extends Fragment{

	Bitmap bitmap;
	ImageView photo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

    	LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.fragment_popular_activities, container, false);
	
	/*try {
		  photo = new ImageView(getActivity());
		  Log.v("pop","0");
		  URL url = new URL("http://www.goldarpp.sitew.org/fs/Root/normal/3fevv-logo_test.jpg");
		  bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
		  Log.v("pop","1");
		  photo.setImageBitmap(bitmap);
		  Log.v("pop","2");
		  ll.addView(photo);
		  Log.v("pop","3");
	} catch (MalformedURLException e) {
		  e.printStackTrace();
	} catch (IOException e) {
		  e.printStackTrace();
	}*/
    	
    	
    	return ll;
        
    }
}
