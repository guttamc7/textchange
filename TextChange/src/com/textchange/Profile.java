package com.textchange;
	import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;
	public class Profile extends Activity {
		private static Button signupButton;
		private static Button loginButton;
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_profile);
			signupButton=(Button)findViewById(R.id.signup_msg);
			loginButton=(Button)findViewById(R.id.login_msg);
			signupButton.setOnClickListener(new View.OnClickListener(){
				   public void onClick(View v) {
					   Intent signupIntent=new Intent(Profile.this,SignUp.class);
			           startActivity(signupIntent);
				   }
			});
			loginButton.setOnClickListener(new View.OnClickListener(){
				   public void onClick(View v) {
						Intent loginIntent=new Intent(Profile.this,Login.class);
						startActivity(loginIntent);
				   }
			});	
		}
		}
		