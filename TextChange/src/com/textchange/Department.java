package com.textchange;
import android.os.Bundle;
import org.json.*;
import com.textchange.R.color;
import com.textchange.utils.databaseAPI;

import java.util.List;
import java.util.ArrayList;
import android.app.Activity;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.view.View;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.content.Intent;
public class Department extends Activity {
private ListView list;
private String name;
ArrayAdapter<String> adapterdept;
public static String selectedDepartment;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_departments);
		list=new ListView(this);
		list.setCacheColorHint(color.PaleGoldenrod);
		setContentView(list);
	    System.out.println("calling activity is"+name);
		adapterdept = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getData());
        list.setAdapter(adapterdept);
        list.setFastScrollEnabled(true);
        list.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			   selectedDepartment = ((TextView) view).getText().toString();	
			   Intent intent = getIntent();
			   name = intent.getStringExtra("calling-activity");
        	   if(name.equals("search")){
       				intent.putExtra("department",selectedDepartment);
            	    setResult(RESULT_OK,intent);     
            	    finish();
       		 	}
       			else if(name.equals("post")){
       				intent.putExtra("department",selectedDepartment);
       				setResult(RESULT_OK,intent);     
       				finish();
       		}
        }
        });
     		
	}
	private List<String> getData(){
		 List<String> data = new ArrayList<String>();
		 GetDepartments alldeps= new GetDepartments();
		 alldeps.start();
			try{
				alldeps.join();
				}catch(Exception e){}
		JSONObject temp=alldeps.getDepartments();
		try{
		int num=Integer.parseInt(temp.get("NUM").toString());
         for(int n=1;n<=num;n++){
        	 data.add((String)temp.get(""+n));
        	 //System.out.println(temp.get(""+n));
         }
		}
		catch(Exception e){
			 System.out.println("Failed due to JSON"); 
		}
		return data;
	}
	
	public String getSelectedDept(){
		return selectedDepartment;
	}

	public class GetDepartments extends Thread{
	JSONObject data=null;
	public void run(){
		data=databaseAPI.getDepartments();			
	}
	public JSONObject getDepartments(){
		return data;
	}
}

}
