package com.textchange;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.content.Intent;
public class UserHome extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userhome);
		getActionBar().setDisplayHomeAsUpEnabled(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	 public void searchFrame(View view){
		    Intent intent = new Intent(this,Search.class);	
		    intent.putExtra("calling-activity","userhome");
		    startActivity(intent);	
		    }
		    
		    public void loginFrame(View view){
		    	Intent intent = new Intent(this,UserProfile.class);	
		        startActivity(intent);		
		    }
		    public void postFrame(View view){
		    	Intent intent = new Intent(this,Post.class);	
		    	intent.putExtra("calling-activity","userhome");
		        startActivity(intent);	
		    }
     public void onBackPressed() {
		        return;
		     }

}
