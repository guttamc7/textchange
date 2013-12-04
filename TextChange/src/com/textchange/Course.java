package com.textchange;
import android.os.Bundle;
import org.json.*;
import com.textchange.utils.databaseAPI;
import java.util.List;
import java.util.ArrayList;
import android.app.Activity;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.TextView;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.content.Intent;

public class Course extends Activity {
private ListView list;
private String name;
Department depObject;
ArrayAdapter<String> adaptercourse;
public static String selectedcourse;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_courses);
		depObject=new Department();
		list=new ListView(this);
		setContentView(list);
		Intent intentEmail = getIntent();
	    name = intentEmail.getStringExtra("calling-activity");
		adaptercourse = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getData());
        list.setAdapter(adaptercourse);
        list.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
        		Intent intentEmail = getIntent();
        	    name = intentEmail.getStringExtra("calling-activity");
        		 selectedcourse = ((TextView) view).getText().toString();		
        		if(name.equals("search")){
        			intentEmail.putExtra("course",selectedcourse);
        			intentEmail.putExtra("department", depObject.getSelectedDept());
        			setResult(RESULT_OK,intentEmail);     
             	    finish();
        		 }
        		else if(name.equals("post")){
        			intentEmail.putExtra("course",selectedcourse);
        			intentEmail.putExtra("department", depObject.getSelectedDept());
        			setResult(RESULT_OK,intentEmail);     
             	    finish();
        		}
        	}
        });
     		
	}
	private List<String> getData(){
		 List<String> data = new ArrayList<String>();
		 GetCourses allcourses= new GetCourses();
		 allcourses.start();
			try{
				allcourses.join();
				}catch(Exception e){}
		JSONObject temp=allcourses.getCoursesByDepartments();
		try{
		int num=Integer.parseInt(temp.get("NUM").toString());
         for(int n=1;n<=num;n++){
        	 data.add((String)temp.get(""+n));
         }
		}
		catch(Exception e){
			 System.out.println("Failed due to JSON"); 
		}
		return data;
	}
	public class GetCourses extends Thread{	
	Department dep=new Department();
	JSONObject data=null;
	public void run(){
		data=databaseAPI.getCoursesByDepartments(dep.getSelectedDept());			
	}
	public JSONObject getCoursesByDepartments(){
		return data;
		}
	}
}	