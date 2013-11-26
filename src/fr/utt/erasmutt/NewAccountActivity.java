package fr.utt.erasmutt;

import org.json.JSONException;
import org.json.JSONObject;

import fr.utt.erasmutt.networkConnection.HttpCallback;
import fr.utt.erasmutt.networkConnection.HttpRequest;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NewAccountActivity extends Activity {

	EditText mail = null;
	EditText password = null;
	EditText repeatPassword = null;
	EditText firstname = null;
	EditText lastname = null;
	
	Button buttonSubmit = null;
	Button buttonCancel = null;
	
	private HttpRequest requestAddUser = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_account);
		
		mail = (EditText) findViewById(R.id.editTextCreateNewAccount_mail);
		password = (EditText) findViewById(R.id.editTextCreateNewAccount_password);
		repeatPassword = (EditText) findViewById(R.id.editTextCreateNewAccount_repeatPassword);
		firstname = (EditText) findViewById(R.id.editTextCreateNewAccount_firstname);
		lastname = (EditText) findViewById(R.id.editTextCreateNewAccount_lastname);
		buttonSubmit = (Button) findViewById(R.id.buttonCreateNewAccount_submit);
		buttonCancel = (Button) findViewById(R.id.buttonCreateNewAccount_cancel);
		
		getActionBar().hide();
		
		buttonSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(password.getText().toString().equals(repeatPassword.getText().toString())){
					requestAddUser = new HttpRequest(new HttpCallback() {
						
						@Override
						public Object call(JSONObject jsonResponse) {
	
							try {
								//TODO : Exploiter les données reçues
								if (!jsonResponse.getBoolean("error")) {
									
									Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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
					
					//On vérifie que la connexion au réseau est valide
			        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
			        
			        if (activeInfo != null && activeInfo.isConnected()) {
			        	requestAddUser.execute(Constants.urlRoot+"connexion.php?typeConnexion=ajouter&mail="+mail.getText().toString()+"&password="+password.getText().toString()+"&firstname="+firstname.getText().toString()+"&lastname="+lastname.getText().toString());
			        }else {
			        	Toast.makeText(getApplicationContext(), R.string.network_disabled, Toast.LENGTH_LONG).show();
			        }
				}else {
		        	Toast.makeText(getApplicationContext(), R.string.password_repeat_error, Toast.LENGTH_LONG).show();
		        }
			}
			
		});
		
		buttonCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(intent);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_account, menu);
		return true;
	}

}
