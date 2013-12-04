package com.textchange;
import android.widget.EditText;
import org.json.*;
import com.textchange.utils.databaseAPI;
import android.app.AlertDialog;
import android.widget.Button;
import android.widget.ImageButton;
import android.os.Bundle;
import android.content.Intent;
import android.app.Activity;
import android.view.*;
import android.content.DialogInterface;
public class Search extends Activity{
	private static String mTitle;
	private static String mAuthor;
	private static String mISBN;
	public static String mDept;
	public static String mCourse;
	//UI references
	private EditText mTitleView;
	private EditText mAuthorView;
	private EditText mISBNView;
	private ImageButton mDeptView;
	private ImageButton mCourseView;
	public static Button deptButton;
	public static Button courseButton;
	public static Button searchButton;
	public static String depname;
	public static boolean result;
	public static String coursename;
	public static String callingAct;  
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		Intent intentEmail = getIntent();
	    callingAct = intentEmail.getStringExtra("calling-activity");
		mTitleView=(EditText) findViewById(R.id.book_title);
		mAuthorView=(EditText) findViewById(R.id.book_author);
		mISBNView=(EditText)findViewById(R.id.book_isbn);
		deptButton=(Button)findViewById(R.id.dept_button);
		courseButton=(Button)findViewById(R.id.course_button);
		searchButton=(Button)findViewById(R.id.search_success);
		mDeptView=(ImageButton)findViewById(R.id.imageNextDepartment);
		mCourseView=(ImageButton)findViewById(R.id.imageNextCourse);
		mTitle=mTitleView.getText().toString();
		mAuthor=mAuthorView.getText().toString();
		mISBN=mISBNView.getText().toString();
		mDeptView.setOnClickListener(new View.OnClickListener(){
			   public void onClick(View v) {
				   Intent dept=new Intent(Search.this,Department.class);
				   dept.putExtra("calling-activity","search");
				   dept.putExtra("searchTitle", mTitle);
				   dept.putExtra("searchAuthor",mAuthor);
				   dept.putExtra("searchAuthor",mISBN);
		           startActivityForResult(dept,1);
		           }
		});
		mCourseView.setOnClickListener(new View.OnClickListener(){
			   public void onClick(View v) {
				   
					Intent courseIntent=new Intent(Search.this,Course.class);
					courseIntent.putExtra("calling-activity", "search");
					courseIntent.putExtra("searchTitle", mTitle);
					courseIntent.putExtra("searchAuthor",mAuthor);
					courseIntent.putExtra("searchAuthor",mISBN);
					startActivityForResult(courseIntent,1);
			   }
		});
		searchButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mTitle=mTitleView.getText().toString();
				mAuthor=mAuthorView.getText().toString();
				mISBN=mISBNView.getText().toString();
				if (depname == null) {
					  depname="";
					}
				if (coursename == null) {
					  coursename="";
					}
				System.out.println("Title="+mTitle+"Author="+mAuthor+"ISBN="+mISBN);
				result= verifySearchResults();
				System.out.println(result);
				if(result==true){
					Intent searchR=new Intent(Search.this,SearchResults.class);
					searchR.putExtra("title",mTitle);
					searchR.putExtra("author",mAuthor);
					searchR.putExtra("ISBN",mISBN);
					searchR.putExtra("department", depname);
					searchR.putExtra("course",coursename);
					startActivity(searchR);
				
				}
			else{
				AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
				builder.setMessage("Oops! Your search returned no results! :(");
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
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		        super.onActivityResult(requestCode, resultCode, data);
		        if(data.getExtras().containsKey("department")){
		        	depname=data.getStringExtra("department");
		            deptButton.setText(data.getStringExtra("department"));
		
		        }
		        if(data.getExtras().containsKey("course")){
		        	coursename=data.getStringExtra("course");
		            courseButton.setText(data.getStringExtra("course"));
		        }
		    }

	
	public boolean verifySearchResults(){
		boolean verify=false;
		VerifySearch search=new VerifySearch();
		 search.start();
			try{
				search.join();
				}catch(Exception e){}
			JSONObject temp=search.getSearchdata();
			try{
				int num=Integer.parseInt(temp.get("NUM").toString());
				if(num==0)
					verify= false;
				else
					verify= true;
			}
			catch(JSONException e){
				 System.out.println("Failed due to JSON"); 
			}
		return verify;
	}
	public void onBackPressed() {
		if(callingAct.equals("main")){
		Intent mainIntent = new Intent(Search.this, MainActivity.class);
		startActivity(mainIntent);
		finish();
		}
		else if(callingAct.equals("userhome")){
			Intent userIntent = new Intent(Search.this,UserHome.class);
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
	public String getDepartment(){
		return depname;
	}
	protected String getCourse(){
		return coursename;
	}
	
	public class VerifySearch extends Thread{
		JSONObject data=null;
		public void run(){
			data=databaseAPI.searchTextbookAds(mTitle,mAuthor,mISBN,depname,coursename);			
		}
		
		public JSONObject getSearchdata(){
			return data;
		}
	}
	
}
