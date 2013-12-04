package com.textchange;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.View;
import android.content.DialogInterface;
import android.content.Intent;
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage("Are you sure you want to exit?");
		builder.setTitle("Exit");
		builder.setIcon(R.drawable.ic_error);
		builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                moveTaskToBack(true);
            }
        }).setNegativeButton("No", null).show();
	}
	
	 public void searchFrame(View view){
		    Intent intent = new Intent(this,Search.class);	
		    intent.putExtra("calling-activity","main");
		    startActivity(intent);	
		    }
		    
		    public void loginFrame(View view){
		    	Intent intent = new Intent(this,Profile.class);	
		    	intent.putExtra("calling-activity","main");
		        startActivity(intent);		
		    }
		    public void postFrame(View view){
		    	Intent intent = new Intent(this,Post.class);	
		    	intent.putExtra("calling-activity","main");
		        startActivity(intent);	
		    }

}
