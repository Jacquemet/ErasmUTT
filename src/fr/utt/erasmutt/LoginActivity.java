package fr.utt.erasmutt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginActivity extends Activity {

	TextView textViewLogin = null;
	TextView textViewPassword = null;
	
	EditText login = null;
    EditText password = null;
    
    Button buttonSignIn = null;
    Button buttonNewAccount = null;
    
    ProgressBar progBar = null;
    

	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		getActionBar().hide();
		
		textViewLogin = (TextView)findViewById(R.id.textView_mail);
		textViewPassword =  (TextView)findViewById(R.id.textView_password);
		login = (EditText)findViewById(R.id.editText_mail);
        password = (EditText)findViewById(R.id.editText_password);
        buttonSignIn = (Button)findViewById(R.id.button_signIn);
        buttonNewAccount = (Button)findViewById(R.id.button_createAccount);
        progBar =  (ProgressBar)findViewById(R.id.progressBar);
        
        
        buttonSignIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
	            startLoading();

		        Bundle bundle = new Bundle();
		        bundle.putString("token","");
		        bundle.putString("login",login.getText().toString());
		        bundle.putString("password",password.getText().toString());
		        Intent intent = new Intent(v.getContext(), HomeActivity.class);
		        intent.putExtras(bundle);
		        startActivity(intent);
			}
		});
        
        buttonNewAccount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO : 
			}
		});
	}
	
	private void startLoading(){
    	progBar.setVisibility(View.VISIBLE);
    	
    	login.setVisibility(View.INVISIBLE);
    	password.setVisibility(View.INVISIBLE);
    	buttonSignIn.setVisibility(View.INVISIBLE);
    	buttonNewAccount.setVisibility(View.INVISIBLE);
    	textViewLogin.setVisibility(View.INVISIBLE);
    	textViewPassword.setVisibility(View.INVISIBLE);
	}
	
	private void endLoading() {
    	progBar.setVisibility(View.INVISIBLE);
    	
    	login.setVisibility(View.VISIBLE);
    	password.setVisibility(View.VISIBLE);
    	buttonSignIn.setVisibility(View.VISIBLE);
    	buttonNewAccount.setVisibility(View.VISIBLE);
    	textViewLogin.setVisibility(View.VISIBLE);
    	textViewPassword.setVisibility(View.VISIBLE);
		
	}
	
	
	
}
