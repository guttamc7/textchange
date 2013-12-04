package com.textchange;
import org.json.JSONException;
import org.json.JSONObject;
import com.textchange.utils.databaseAPI;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.net.Uri;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.content.Intent;
public class AdDetails extends Activity {
	public static Button adcourseB;
	public static Button dateB;
	public static Button authorB;
	public static Button titleB;
	public static Button isbnB;
	public static Button priceB;
	public static Button contactseller;
	public static String telephone;
	public static String email;
	public static String sellername;
	public static String id;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addetails);
		adcourseB=(Button)findViewById(R.id.ad_course);
		dateB=(Button)findViewById(R.id.ad_dateB);
		titleB=(Button)findViewById(R.id.addetails_title);
		authorB=(Button)findViewById(R.id.addetails_author);
		isbnB=(Button)findViewById(R.id.addetails_isbn);
		priceB=(Button)findViewById(R.id.addetails_price);
		contactseller=(Button)findViewById(R.id.contact_seller);
		id=SearchResults.getId();
		System.out.println("In adDetials id="+id);
		getTextData();
		//TODO get course,date,author,title,isbn,price
		contactseller.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				showDialog(1);
			}
		});
	}
	public void getTextData(){
		 TextData data=new TextData();
		 data.start();
			try{
				data.join();
				}catch(Exception e){}
			JSONObject temp=data.getdata();
			try{
				//System.out.println((String)temp.get("date"));
				 dateB.setText((String)temp.get("date"));
				 adcourseB.setText((String)temp.get("course"));
				 titleB.setText((String)temp.get("title"));
				 isbnB.setText((String)temp.get("isbn"));
				 authorB.setText("By: "+(String)temp.get("course"));
				 priceB.setText("Ask Price: "+(String)temp.get("price"));
				 email=(String)temp.get("email");
				 telephone=(String)temp.get("phone");
				 sellername=(String)temp.get("name");
			}
			 catch(JSONException e){
	        	 System.out.println("Failed due to JSON"); 
	         }
	}
	
	protected Dialog onCreateDialog(int id) {
		  AlertDialog dialogDetails = null;
		  switch (id) {
		  case 1:
		   LayoutInflater inflater = LayoutInflater.from(this);
		   View dialogview = inflater.inflate(R.layout.contact_dialog, null);
		   AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
		   dialogbuilder.setView(dialogview);
		   dialogDetails = dialogbuilder.create();
		   break;
		  }
		  return dialogDetails;
		 }
		 
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	    	Intent in=getIntent();
	    	setResult(RESULT_OK,in);     
     	    finish();
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
	
	protected void onPrepareDialog(int id, Dialog dialog) {

		  switch (id) {
		  case 1:
		   final AlertDialog alertDialog = (AlertDialog) dialog;
		   Button mailbutton = (Button) alertDialog
		     .findViewById(R.id.send_mail);
		   Button callbutton = (Button) alertDialog
		     .findViewById(R.id.call);
		   mailbutton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		     alertDialog.dismiss();
		     Intent emailIntent = new Intent(Intent.ACTION_SEND);
		  // The intent does not have a URI, so declare the "text/plain" MIME type
		     emailIntent.setType("message/rfc822");
		     emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email}); // recipients
		     emailIntent.putExtra(Intent.EXTRA_SUBJECT, "TextChange Textbook Information");
		     emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi "+sellername+",");
		     startActivity(emailIntent);
		    }
		   });
		   callbutton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		     alertDialog.dismiss();
		     Uri number = Uri.parse("tel:"+telephone);
		     Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
		     startActivity(callIntent);
		    }
		   });
		   break;
		  }
		 }
	public class TextData extends Thread{
		JSONObject data=null;
		public void run(){
			data=databaseAPI.getTextbookData(id);			
		}
		
		public JSONObject getdata(){
			return data;
		}
	}

}	
