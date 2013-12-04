package com.textchange;
import org.json.*;
import com.textchange.utils.databaseAPI;

import android.widget.EditText;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Button;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.*;
public class Login extends Activity {
	private static String mEmail;
	private static String mPass;
	private EditText mEmailView;
	private EditText mPassView;
	private Button loginButton;
	public static boolean verified;
	public static String reason;
	public static boolean res;
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_login);
			mEmailView = (EditText) findViewById(R.id.login_email);
			mPassView = (EditText) findViewById(R.id.login_pass);
			loginButton = (Button) findViewById(R.id.login_login);
			loginButton.setOnClickListener(new View.OnClickListener(){
				public void onClick(View v){
					mEmail = mEmailView.getText().toString();
					mPass = mPassView.getText().toString();
					System.out.println("Email="+mEmail+" Password="+mPass);
					verified=verifyEmailPassword();
					if(verified==true){
						Intent logIntent=new Intent(Login.this,UserHome.class);
						startActivity(logIntent);
						finish();
					}
					else{
						AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
						builder.setMessage(reason);
						builder.setTitle("Login Failed.");
						builder.setIcon(R.drawable.ic_error);
						builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog, int id) {
			                    dialog.cancel();
			                }
			            });
						AlertDialog dialog = builder.create();
						dialog.show();
					}
					
				}
			});
	}
	
	public boolean verifyEmailPassword(){
		 VerifyLogin log= new VerifyLogin();
		 log.start();
			try{
				log.join();
				}catch(Exception e){}
		JSONObject temp=log.getResult();
		//System.out.println("LOGIN VERIFIED-BOOH");
		try{
			
			res=(Boolean)temp.get("loginVerified");
			
			//System.out.println(temp.get("loginVerified"));
			if(res==false){
				reason=(String)temp.get("reason");
			}
			
		}
		catch(Exception e){
			 System.out.println("Failed due to JSON-Login"); 
		}
		return res;
	}
	public String getEmail(){
		return mEmail;
	}
	
	public class VerifyLogin extends Thread{
		private JSONObject data=null;
	
		public void run(){
			this.data=databaseAPI.verifyLogin(mEmail,mPass);			
		}
		public JSONObject getResult(){
			return data;
		}
	}
}
