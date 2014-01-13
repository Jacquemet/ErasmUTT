package fr.utt.erasmutt;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import fr.utt.erasmutt.networkConnection.HttpCallback;
import fr.utt.erasmutt.networkConnection.HttpRequest;
/**
 * This activity allow to create an account
 * @author Thibault Jacquemet & Kévin Larue
 *
 */
public class NewAccountActivity extends Activity {

	EditText mail = null;
	EditText password = null;
	EditText repeatPassword = null;
	EditText firstname = null;
	EditText lastname = null;
	
	Button buttonSubmit = null;
	Button buttonCancel = null;
	
	LinearLayout layoutMdp = null;
	boolean mdpValide=false;
	
	private HttpRequest requestAddUser = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_account);
		
		//Instantiation of all the graphic elements
		mail = (EditText) findViewById(R.id.editTextCreateNewAccount_mail);
		password = (EditText) findViewById(R.id.editTextCreateNewAccount_password);
		repeatPassword = (EditText) findViewById(R.id.editTextCreateNewAccount_repeatPassword);
		firstname = (EditText) findViewById(R.id.editTextCreateNewAccount_firstname);
		lastname = (EditText) findViewById(R.id.editTextCreateNewAccount_lastname);
		buttonSubmit = (Button) findViewById(R.id.buttonCreateNewAccount_submit);
		buttonCancel = (Button) findViewById(R.id.buttonCreateNewAccount_cancel);
		layoutMdp = (LinearLayout) findViewById(R.id.passwordCaract);
		layoutMdp.setVisibility(View.GONE);
		// Put the fullscreen mode
		getActionBar().hide();


		password.setOnFocusChangeListener(new OnFocusChangeListener() {
		    @Override
		    public void onFocusChange(View v, boolean hasFocus) {
		        // TODO Auto-generated method stub
		    	if(hasFocus){layoutMdp.setVisibility(View.VISIBLE);}else{layoutMdp.setVisibility(View.GONE);}
		
		    }
		});


		password.addTextChangedListener(new TextWatcher() {

	        @Override
	        public void afterTextChanged(Editable s) {
	            // TODO Auto-generated method stub

	        }

	        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	            // TODO Auto-generated method stub

	        }

	        public void onTextChanged(CharSequence s, int start, int before, int count) {
	        
	        	if(s.length()<8){
	        		ImageView img = (ImageView) findViewById(R.id.valideTaille);
	        		img.setImageDrawable(getResources().getDrawable(R.drawable.close));
	        	}
	        	else{
	        		ImageView img = (ImageView) findViewById(R.id.valideTaille);
	        		img.setImageDrawable(getResources().getDrawable(R.drawable.check));
	        	}
	        	
	        	int indice=0;
	        	boolean hasUppercase=false;
	        	if(s.length()>0){
	        		do{
		        		if(Character.isUpperCase(s.charAt(indice))){
		        			hasUppercase=true;
		        		}
		        		indice++;
		        	}
		        	while(indice<s.length() && !hasUppercase);
	        	}
	        	
	        	
	        	if(!hasUppercase){
	        		ImageView img = (ImageView) findViewById(R.id.valideMaj);
	        		img.setImageDrawable(getResources().getDrawable(R.drawable.close));
	        		mdpValide=false;
	        	}else{
	        		ImageView img = (ImageView) findViewById(R.id.valideMaj);
	        		img.setImageDrawable(getResources().getDrawable(R.drawable.check));
	        		mdpValide=true;
	        	}
	        	
	        } 

	    });
		
		buttonSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(password.getText().toString().equals(repeatPassword.getText().toString())){
					if(mdpValide && password.getText().toString().length()>=8){
						requestAddUser = new HttpRequest(new HttpCallback() {
							
							@Override
							public Object call(JSONObject jsonResponse) {
		
								try {
									if (!jsonResponse.getBoolean("error")) {
										
										//Clear NewAccountActivity from the back stack and launch the current task of LoginActivity 
										Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
										intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										Toast.makeText(getApplicationContext(), R.string.createAccountOK, Toast.LENGTH_LONG).show();
										startActivity(intent);
										
									} else {
										Toast.makeText(getApplicationContext(), R.string.createAccountNOK, Toast.LENGTH_LONG).show();
									}
								} catch (JSONException e) {
									Toast.makeText(getApplicationContext(), R.string.error_network, Toast.LENGTH_LONG).show();
								}
								
								return null;
							}
						});
						
						//We check if there a valid network connexion (WIFI or 3G)
				        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
				        
				        //If it is we execute the request
				        if (activeInfo != null && activeInfo.isConnected()) {
				        	requestAddUser.execute(Constants.urlRoot+"connexion.php?typeConnexion=ajouter&mail="+mail.getText().toString()+"&password="+password.getText().toString()+"&firstname="+firstname.getText().toString()+"&lastname="+lastname.getText().toString());
				        }else {
				        	Toast.makeText(getApplicationContext(), R.string.network_disabled, Toast.LENGTH_LONG).show();
				        }
					}
					else{
						Toast.makeText(getApplicationContext(), R.string.password_error, Toast.LENGTH_LONG).show();
					}
				}else {
		        	Toast.makeText(getApplicationContext(), R.string.password_repeat_error, Toast.LENGTH_LONG).show();
		        }
			}
			
		});
		
		buttonCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Clear NewAccountActivity from the back stack and launch the current task of LoginActivity 
				Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		
	}

}
