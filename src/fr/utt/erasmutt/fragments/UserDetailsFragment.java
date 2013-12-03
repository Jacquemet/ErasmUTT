package fr.utt.erasmutt.fragments;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
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
import fr.utt.erasmutt.LoginActivity;
import fr.utt.erasmutt.R;
import fr.utt.erasmutt.networkConnection.HttpCallback;
import fr.utt.erasmutt.networkConnection.HttpRequest;

public class UserDetailsFragment extends Fragment {

	EditText mail = null;
	EditText firstname = null;
	EditText lastname = null;
	
	Button buttonSubmit = null;
	Button buttonCancel = null;
	ImageView imgEdit = null;
	ProgressBar progBar = null;
	private HttpRequest requestSetUser = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ScrollView sv = (ScrollView) inflater.inflate(R.layout.fragment_user_details, container, false);
		
		
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
		buttonSubmit.setEnabled(false);
		buttonCancel.setEnabled(false);
		
		imgEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mail.setEnabled(!mail.isEnabled());
				firstname.setEnabled(!firstname.isEnabled());
				lastname.setEnabled(!lastname.isEnabled());
				buttonSubmit.setEnabled(!buttonSubmit.isEnabled());
				buttonCancel.setEnabled(!buttonCancel.isEnabled());
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
							//TODO : Exploiter les données reçues
							if (!jsonResponse.getBoolean("error")) {
								
								Constants.user.setFirstname(firstname.getText().toString());
								Constants.user.setLastname(lastname.getText().toString());
								Constants.user.setMail(mail.getText().toString());
								
								Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
								progBar.setVisibility(View.INVISIBLE);
								Toast.makeText(getActivity().getApplicationContext(), R.string.setAccountOK, Toast.LENGTH_LONG).show();
								startActivity(intent);
								
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
					buttonSubmit.setEnabled(false);
					buttonCancel.setEnabled(false);
					progBar.setVisibility(View.VISIBLE);
		        }else {
		        	Toast.makeText(getActivity().getApplicationContext(), R.string.network_disabled, Toast.LENGTH_LONG).show();
		        }
			}
			
			
		});
		
		return sv;
	}

}
