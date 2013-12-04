package com.textchange;
import org.json.JSONException;
import org.json.JSONObject;
import com.textchange.utils.databaseAPI;

import android.widget.EditText;
import android.widget.Button;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.*;
public class SignUp extends Activity {
		private static String mEmail_;
		private static String mName;
		private static String mPhone;
		private static String mPass1;
		private static String mPass2;
		private EditText mEmailView;
		private EditText mNameView;
		private EditText mPhoneView;
		private EditText mPassView1;
		private EditText mPassView2;
		private static Button regButton;
		public static boolean verifyE = false;
		public static boolean verifyA = false;
		public static boolean verifyServer;
		public static boolean userAdd;
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_signup);
			mEmailView = (EditText) findViewById(R.id.register_email_req);
			mNameView = (EditText) findViewById(R.id.register_name_req);
			mPhoneView = (EditText) findViewById(R.id.register_phone_num);
			mPassView1 = (EditText) findViewById(R.id.register_pass);
			mPassView2 = (EditText) findViewById(R.id.register_pass_re);
			regButton= (Button)findViewById(R.id.register_button);
			
			
			regButton.setOnClickListener(new View.OnClickListener(){
				public void onClick(View v){
					mEmail_ = mEmailView.getText().toString();
					mPass1 = mPassView1.getText().toString();
					mPass2 = mPassView2.getText().toString();
					mName = mNameView.getText().toString();
					mPhone = mPhoneView.getText().toString();
					verifyA = verifyAll(mName,mPass1,mPass2,mEmail_);
					verifyE = verifyEmail(mEmail_);
					verifyServer= verifySigning();
					System.out.println("Email Verify"+verifyE);
					System.out.println("Verify All"+verifyA);
					System.out.println("Email="+verifyServer);
					if(verifyA==true && verifyE==true && verifyServer==true && getUserAdd()==false){
						AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
						builder.setMessage("An Email verification has been sent to "+mEmail_+". Thank You.");
						builder.setTitle("Success");
						builder.setIcon(R.drawable.ic_action_accept);
						builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog, int id) {
			                    dialog.cancel();
			                    Intent i=new Intent(SignUp.this,Profile.class);
			                    startActivity(i);
			                    finish();
			                }
			            });
						AlertDialog dialog = builder.create();
						dialog.show();
					}else{
						//Show Dialog
						AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
						builder.setMessage("Oops! There was an Error.");
						builder.setTitle("Error");
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
	public static boolean verifyAll(String name,String password,String repass,String email)	{
		if(email.length()!=0 && password.length()!=0 && repass.length()!=0 && name.length()!=0){
			if(password.length()==repass.length()){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}
		
	public static boolean verifyEmail(String mEmail){
		String[] str;
		if(mEmail.contains("@")){
			str = mEmail.split("@");
			if(str[1].equals("purdue.edu"))
				return true;
			else
				return false;
			
		}else{
			return false;
		}
	}
	public boolean verifySigning(){
		boolean verify=false;
		VerifySignUp signup=new VerifySignUp();
		 signup.start();
			try{
				signup.join();
				}catch(Exception e){}
			JSONObject temp=signup.getdata();
			try{
				verify=(Boolean)temp.get("userAdded");
				if(verify==false)
					userAdd=(Boolean)temp.get("userExists");
			}
			catch(JSONException e){
				 System.out.println("Failed due to JSON"); 
			}
		return verify;
	}
	public static boolean getUserAdd(){
		return userAdd;
	}
	
	public class VerifySignUp extends Thread{
		JSONObject data=null;
		public void run(){
			data=databaseAPI.addUser(mEmail_,mPass1,mName,mPhone);			
		}
		
		public JSONObject getdata(){
			return data;
		}
	}
	
}