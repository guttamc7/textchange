package com.textchange;

import org.json.JSONObject;
import android.app.AlertDialog;
import com.textchange.R;
import com.textchange.utils.databaseAPI;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
public class Post extends Activity {
	private static String pTitle;
	private static String pAuthor;
	private static String pISBN;
	private static String pEdition;
	private static String pPrice;
	public static String pDept;
	public static String pCourse;
	//UI references
	private EditText pTitleView;
	private EditText pAuthorView;
	private EditText pISBNView;
	private EditText pEditionView;
	private EditText pPriceView;
	public  static String email;
	private ImageButton pDeptView;
	private ImageButton pCourseView;
	public static Button pdeptButton;
	public static Button pcourseButton;
	public static Button postButton;
	private String pdepname;
	private boolean success;
	public static boolean verifyIn;
	private String pcoursename;
	Login obj;
	public static String callingAct;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		obj=new Login();
		Intent intentEmail = getIntent();
	    callingAct = intentEmail.getStringExtra("calling-activity");
	    email=obj.getEmail();
		pTitleView=(EditText) findViewById(R.id.book_title);
		pAuthorView=(EditText) findViewById(R.id.book_author);
		pISBNView=(EditText)findViewById(R.id.book_isbn);
		pEditionView=(EditText)findViewById(R.id.book_edition);
		pPriceView=(EditText)findViewById(R.id.book_price);
		pdeptButton=(Button)findViewById(R.id.dept_button);
		pcourseButton=(Button)findViewById(R.id.course_button);
		postButton=(Button)findViewById(R.id.post_success);
		pDeptView=(ImageButton)findViewById(R.id.imageNextDepartment);
		pCourseView=(ImageButton)findViewById(R.id.imageNextCourse);
		pDeptView.setOnClickListener(new View.OnClickListener(){
			   public void onClick(View v) {
				   Intent dept=new Intent(Post.this,Department.class);
				   dept.putExtra("calling-activity","post");
		           startActivityForResult(dept,1);
			   }
		});
		pCourseView.setOnClickListener(new View.OnClickListener(){
			   public void onClick(View v) {
					Intent courseIntent=new Intent(Post.this,Course.class);
					courseIntent.putExtra("calling-activity","post");
			        startActivityForResult(courseIntent,1);
			   }
		});
		postButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (pdepname == null) {
					  pdepname="";
					}
				if (pcoursename == null) {
					  pcoursename="";
					}
				pTitle=pTitleView.getText().toString();
				pAuthor=pAuthorView.getText().toString();
				pISBN=pISBNView.getText().toString();
				pEdition=pEditionView.getText().toString();
				pPrice=pPriceView.getText().toString();
				//System.out.println("title="+pTitle+"author="+pAuthor+"isbn="+pISBN+"pEdition="+pEdition+"price="+pPrice);
				//success=postTextBook();
				//System.out.println("Post Ad="+success);
				//System.out.println(callingAct);
				if(callingAct.equals("main")){
					AlertDialog.Builder builder = new AlertDialog.Builder(Post.this);
					builder.setMessage("Oops! Please Login in order to Post an Ad! :)");
					builder.setTitle("Error");
					builder.setIcon(R.drawable.ic_error);
					builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int id) {
		                    dialog.cancel();
		                    if(callingAct.equals("main")){
		                    Intent postLogin = new Intent(Post.this,Profile.class);
		    				startActivity(postLogin);
		    				finish();
		                    }
		                }
		            });
					AlertDialog dialog = builder.create();
					dialog.show();
					
				}
					else if(callingAct.equals("userhome"))
					{
						//TODO User Post!Success
						verifyIn=verifyInput(pTitle,pPrice);
						if( verifyIn==true){
						success=postTextBook();
						if(success=true){
						AlertDialog.Builder builder = new AlertDialog.Builder(Post.this);
						builder.setMessage("Your Ad has been posted! :)");
						builder.setTitle("Success");
						builder.setIcon(R.drawable.ic_action_accept);
						builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog, int id) {
			                    dialog.cancel();
			                    Intent postLogin = new Intent(Post.this,UserHome.class);
			    				startActivity(postLogin);
			                }
			            });
						AlertDialog dialog = builder.create();
						dialog.show();
					}
					}
					else if(verifyIn==false){
						AlertDialog.Builder builder = new AlertDialog.Builder(Post.this);
						builder.setMessage("Please Enter Atleast Title and Price! :)");
						builder.setTitle("Error");
						builder.setIcon(R.drawable.ic_error);
						builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog, int id) {
			                    dialog.cancel();
			                    Intent postLogin = new Intent(Post.this,UserHome.class);
			    				startActivity(postLogin);
			                }
			            });
						AlertDialog dialog = builder.create();
						dialog.show();
					}
			}
			}
	});
  }
	public boolean verifyInput(String title,String price){
		if(title.length()!=0 && price.length()!=0){
			return true;
		}
		return false;
	}
	public boolean postTextBook(){
		 PostAd ad= new PostAd();
		 ad.start();
			try{
				ad.join();
				}catch(Exception e){}
		JSONObject temp=ad.getResult();
		try{
			success=(Boolean)temp.get("textbookAdded");
			
		}
		catch(Exception e){
			 System.out.println("Failed due to JSON-Login"); 
		}
		return success;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getExtras().containsKey("department")){
        	pdepname=data.getStringExtra("department");
            pdeptButton.setText(data.getStringExtra("department"));

        }
        if(data.getExtras().containsKey("course")){
        	pcoursename=data.getStringExtra("course");
            pcourseButton.setText(data.getStringExtra("course"));
        }
    }
	
	public void onBackPressed() {
		if(callingAct.equals("main")){
		callingAct="";
		Intent mainIntent = new Intent(Post.this, MainActivity.class);
		startActivity(mainIntent);
		finish();
		}
		else if(callingAct.equals("userhome")){
			callingAct="";
			Intent userIntent = new Intent(Post.this,UserHome.class);
			startActivity(userIntent);
			finish();
		}
	}
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	public class PostAd extends Thread{
		JSONObject data=null;
		public void run(){
			data=databaseAPI.postTextBook(email,pTitle,pAuthor,pISBN,pPrice,pcoursename);
		}
		
		public JSONObject getResult(){
			return data;
		}
	}
}	
