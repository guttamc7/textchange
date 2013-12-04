package com.textchange;
import org.json.JSONException;
import org.json.JSONObject;

import com.textchange.utils.databaseAPI;

import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
public class UserProfile extends Activity {
	public static Button user_emailB;
	public static EditText user_nameE;
	public static EditText user_phoneE;
	public static String username;
	public static String userphone;
	public static Button save_changesB;
	public static Button see_adsB;
	public static Button sign_outB;
	public static boolean updateds=false;
	Login loginObj;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userprofile);
		loginObj=new Login();
		user_emailB=(Button)findViewById(R.id.user_email);
		user_emailB.setText(loginObj.getEmail());
		user_nameE=(EditText)findViewById(R.id.user_name);
		user_phoneE=(EditText)findViewById(R.id.user_phone);
		save_changesB=(Button)findViewById(R.id.save_changes);
		see_adsB=(Button)findViewById(R.id.see_ads);
		sign_outB=(Button)findViewById(R.id.sign_out);
		getUserDetails();
		user_nameE.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
		     //TODO Get Request
				//user_nameE.setText(username);
				}
		});
		user_phoneE.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				//user_phoneE.setText(userphone);
		  //TODO Get Request
				}
		});
		save_changesB.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				username=user_nameE.getText().toString();
				userphone=user_phoneE.getText().toString();
				updateds=updateUserInfo();
				if(updateds==true){
					
				AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
				builder.setMessage("Your Changes have been saved :)");
				builder.setTitle("Thank You.");
				builder.setIcon(R.drawable.ic_action_accept);
				builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                    dialog.cancel();
	                    Intent updateInt = new Intent(UserProfile.this,UserHome.class);
	    				startActivity(updateInt);
	                }
	            });
				AlertDialog dialog = builder.create();
				dialog.show();
				}
				else{
					AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
					builder.setMessage("Your Changes have been saved :)");
					builder.setTitle("Thank You.");
					builder.setIcon(R.drawable.ic_action_accept);
					builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int id) {
		                    dialog.cancel();
		                    Intent updateInt = new Intent(UserProfile.this,UserHome.class);
		    				startActivity(updateInt);
		                }
		            });
					AlertDialog dialog = builder.create();
					dialog.show();
				}
		//TODO Save new Changes-Post Request
				}
		});
		see_adsB.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				Intent newIntent = new Intent(UserProfile.this,UserAds.class);
				startActivity(newIntent);
				}
		});
		sign_outB.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
				builder.setMessage("Signing Out. Have a Great Day. :)");
				builder.setTitle("Sign Out");
				builder.setIcon(R.drawable.ic_action_accept);
				builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                    dialog.cancel();
	                    Intent postLogin = new Intent(UserProfile.this,MainActivity.class);
	    				startActivity(postLogin);
	                }
	            });
				AlertDialog dialog = builder.create();
				dialog.show();
				}
		});
	}
	
	public void getUserDetails(){
		UserDetails data=new UserDetails();
		 data.start();
			try{
				data.join();
				}catch(Exception e){}
			JSONObject temp=data.getUserdata();
			try{
				//System.out.println((String)temp.get("date"));
				 user_nameE.setText((String)temp.get("name"));
				 user_phoneE.setText((String)temp.get("phone"));
			}
			 catch(JSONException e){
	        	 System.out.println("Failed due to JSON"); 
	         }
	}
	public boolean updateUserInfo(){
		boolean updated=false;
		UpdateUser data=new UpdateUser();
		 data.start();
			try{
				data.join();
				}catch(Exception e){}
			JSONObject temp=data.getUserdata();
			try{
				updated=(Boolean)temp.get("userInfoUpdated");
			}
			 catch(JSONException e){
	        	 System.out.println("Failed due to JSON"); 
	         }
			return updated;
	}
	
	public class UserDetails extends Thread{
		JSONObject data=null;
		databaseAPI api=new databaseAPI();
		public void run(){
			data=databaseAPI.getUserDetails(loginObj.getEmail());		
		}
		
		public JSONObject getUserdata(){
			return data;
		}
	}
	public class UpdateUser extends Thread{
		JSONObject data=null;
		databaseAPI api=new databaseAPI();
		public void run(){
			data=databaseAPI.updateUser(loginObj.getEmail(),username,userphone);		
		}
		
		public JSONObject getUserdata(){
			return data;
		}
	}
}
