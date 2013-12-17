package fr.utt.erasmutt.fragments;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;
import fr.utt.erasmutt.Constants;
import fr.utt.erasmutt.R;
import fr.utt.erasmutt.networkConnection.HttpCallback;
import fr.utt.erasmutt.networkConnection.HttpCallbackByte;
import fr.utt.erasmutt.networkConnection.HttpRequest;
import fr.utt.erasmutt.networkConnection.RetreiveImgTask;
import fr.utt.erasmutt.sqlite.DatabaseHelper;

public class UserDetailsFragment extends Fragment {

	EditText mail = null;
	EditText firstname = null;
	EditText lastname = null;
	
	Button buttonSubmit = null;
	Button buttonCancel = null;
	ImageView imgEdit = null;
	ProgressBar progBar = null;
	
	ImageView imgProfil = null;
	
	DatabaseHelper db;
	
	private HttpRequest requestSetUser = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ScrollView sv = (ScrollView) inflater.inflate(R.layout.fragment_user_details, container, false);
		
		db = new DatabaseHelper(getActivity());
		
		imgProfil =  (ImageView) sv.findViewById(R.id.imageViewProfil);
		
		if(!Constants.user.getPictureString().equals("") && Constants.user.getPicture()==null){
       	 
        	 new RetreiveImgTask(new HttpCallbackByte() {
					@Override
					public Object call(byte[] imgbyte) {
						
						Bitmap b = BitmapFactory.decodeByteArray( imgbyte,  0,imgbyte.length);
						imgProfil.setImageBitmap(Bitmap.createScaledBitmap(b, 200, 200, false));
						Constants.user.setPicture(imgbyte);
						db.updateUser(Constants.user);
						
						return null;
					}
				}).execute(Constants.user.getPictureString()); 	
         }
         else if(Constants.user.getPicture()!=null && !Constants.user.getPictureString().equals("")){
       	  Bitmap b = BitmapFactory.decodeByteArray(Constants.user.getPicture(), 0, Constants.user.getPicture().length);
       	  imgProfil.setImageBitmap(Bitmap.createScaledBitmap(b, 200, 200, false));
         }
		
		mail = (EditText) sv.findViewById(R.id.editTextDescAccount_mail);
		firstname = (EditText) sv.findViewById(R.id.editTextDescAccount_firstname);
		lastname = (EditText) sv.findViewById(R.id.editTextDescAccount_lastname);
		buttonSubmit = (Button) sv.findViewById(R.id.buttonDescAccount_submit);
		buttonCancel = (Button) sv.findViewById(R.id.buttonDescAccount_cancel);
		imgEdit = (ImageView) sv.findViewById(R.id.imageEditButtoun);
		
		progBar = (ProgressBar) sv.findViewById(R.id.progressDescBar);
		
		mail.setText(Constants.user.getMail());
		firstname.setText(Constants.user.getFirstname());
		lastname.setText(Constants.user.getLastname());
		
		mail.setEnabled(false);
		firstname.setEnabled(false);
		lastname.setEnabled(false);
		
		imgEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mail.setEnabled(!mail.isEnabled());
				firstname.setEnabled(!firstname.isEnabled());
				lastname.setEnabled(!lastname.isEnabled());
				
				if(buttonSubmit.getVisibility() == View.VISIBLE) {
					buttonSubmit.setVisibility(View.INVISIBLE);
					buttonCancel.setVisibility(View.INVISIBLE);
				} else {
					buttonSubmit.setVisibility(View.VISIBLE);
					buttonCancel.setVisibility(View.VISIBLE);
				}
			}
		});
		
		buttonCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mail.setText(Constants.user.getMail());
				firstname.setText(Constants.user.getFirstname());
				lastname.setText(Constants.user.getLastname());
				
			}
		});
		
		buttonSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			
				requestSetUser = new HttpRequest(new HttpCallback() {
					
					@Override
					public Object call(JSONObject jsonResponse) {
		
						try {
							if (!jsonResponse.getBoolean("error")) {
								
								//User constant update
								Constants.user.setFirstname(firstname.getText().toString());
								Constants.user.setLastname(lastname.getText().toString());
								Constants.user.setMail(mail.getText().toString());
								
								//Update of user in database
								db.updateUser(Constants.user);
								
								progBar.setVisibility(View.INVISIBLE);
								buttonSubmit.setVisibility(View.INVISIBLE);
								buttonCancel.setVisibility(View.INVISIBLE);
								
								Toast.makeText(getActivity().getApplicationContext(), R.string.setAccountOK, Toast.LENGTH_LONG).show();
							} else {
								Toast.makeText(getActivity().getApplicationContext(), R.string.setAccountNOK, Toast.LENGTH_LONG).show();
							}
						} catch (JSONException e) {
							Toast.makeText(getActivity().getApplicationContext(), R.string.error_network, Toast.LENGTH_LONG).show();
						}
						
						return null;
					}
				});
				
				//On vérifie que la connexion au réseau est valide
		        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
		        
		        if (activeInfo != null && activeInfo.isConnected()) {
		        	//transforme les espace en %20 pour l'adresse url
		        	requestSetUser.execute(Constants.urlRoot+"connexion.php?typeConnexion=modifier&mail="+mail.getText().toString()+"&token="+Constants.user.getToken()+"&firstname="+firstname.getText().toString().replace(" ", "%20")+"&lastname="+lastname.getText().toString().replace(" ", "%20"));
		        	mail.setEnabled(false);
					firstname.setEnabled(false);
					lastname.setEnabled(false);
					progBar.setVisibility(View.VISIBLE);
		        }else {
		        	Toast.makeText(getActivity().getApplicationContext(), R.string.network_disabled, Toast.LENGTH_LONG).show();
		        }
			}
			
			
		});
		
		return sv;
	}

}
