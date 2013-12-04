package com.textchange;
import org.json.JSONException;
import android.content.Intent;
import org.json.JSONObject;
import com.textchange.utils.databaseAPI;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
public class UserAdDetails extends Activity {
	public static Button useradcourseB;
	public static Button userdateB;
	public static Button userauthorB;
	public static Button usertitleB;
	public static Button userisbnB;
	public static Button userpriceB;
	public static Button deletead;
	public static String id;
	public static boolean deleted=false;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_useraddetails);
		useradcourseB=(Button)findViewById(R.id.userad_course);
		userdateB=(Button)findViewById(R.id.user_dateB);
		usertitleB=(Button)findViewById(R.id.userdetails_title);
		userauthorB=(Button)findViewById(R.id.userdetails_author);
		userisbnB=(Button)findViewById(R.id.userdetails_isbn);
		userpriceB=(Button)findViewById(R.id.userdetails_price);
		deletead=(Button)findViewById(R.id.delete_ad);
		id=UserAds.getId();
		//System.out.println("In userDetials id="+id);
		getTextData();
		deletead.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				
				AlertDialog.Builder builder = new AlertDialog.Builder(UserAdDetails.this);
				builder.setMessage("Are you sure you want to delete?");
				builder.setTitle("Delete Ad");
				builder.setIcon(R.drawable.ic_error);
				builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) {
		            	deleted=deleteAd();
		            	if(deleted==true){
		            		dialog.cancel();
		            		AlertDialog.Builder builder1 = new AlertDialog.Builder(UserAdDetails.this);
		    				builder1.setMessage("Your Ad Has Been Deleted");
		    				builder1.setTitle("Deleted");
		    				builder1.setIcon(R.drawable.ic_action_accept);
		    				builder1.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
		    		            public void onClick(DialogInterface dialog, int id) {
		    		            	Intent i=new Intent(UserAdDetails.this,UserHome.class);
		    		            	startActivity(i);
		    		            	dialog.cancel();
		    		            }
		    				});
		    				AlertDialog dialog2 = builder1.create();
		    				dialog2.show();
		    				
		            	}
		            	else{
		            		AlertDialog.Builder builder2 = new AlertDialog.Builder(UserAdDetails.this);
		    				builder2.setMessage("There was an Error Deleting. Sorry :(");
		    				builder2.setTitle("Error");
		    				builder2.setIcon(R.drawable.ic_error);
		    				builder2.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
		    					public void onClick(DialogInterface dialog, int id) {
		    						dialog.cancel();
		    					}
		    				});
		    				AlertDialog dialog3 = builder2.create();
		    				dialog3.show();
		            	}
		            	
		            }
		        }).setNegativeButton("No", null).show();
			}
		});
	}
	public void getTextData(){
		 UserTextData data=new UserTextData();
		 data.start();
			try{
				data.join();
				}catch(Exception e){}
			JSONObject temp=data.getdata();
			try{
				//System.out.println((String)temp.get("date"));
				 userdateB.setText((String)temp.get("date"));
				 useradcourseB.setText((String)temp.get("course"));
				 usertitleB.setText((String)temp.get("title"));
				 userisbnB.setText((String)temp.get("isbn"));
				 userauthorB.setText("By: "+(String)temp.get("course"));
				 userpriceB.setText("Ask Price: "+(String)temp.get("price"));
			}
			 catch(JSONException e){
	        	 System.out.println("Failed due to JSON"); 
	         }
	}
	public boolean deleteAd(){
		boolean verify=false;
		DeleteAd ad=new DeleteAd();
		 ad.start();
			try{
				ad.join();
				}catch(Exception e){}
			JSONObject temp=ad.getresult();
			try{
				verify=(Boolean)temp.get("textbookDeleted");
			}
			catch(JSONException e){
				 System.out.println("Failed due to JSON"); 
			}
		return verify;
	}
	
	public class UserTextData extends Thread{
		JSONObject data=null;
		public void run(){
			data=databaseAPI.getTextbookData(id);			
		}
		
		public JSONObject getdata(){
			return data;
		}
	}
	
	public class DeleteAd extends Thread{
		JSONObject data=null;
		public void run(){
			data=databaseAPI.deleteAd(id);			
		}
		
		public JSONObject getresult(){
			return data;
		}
	}
			
}
