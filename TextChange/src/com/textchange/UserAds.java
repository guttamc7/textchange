package com.textchange;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import com.textchange.utils.databaseAPI;
import android.widget.AdapterView;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
//import android.content.Intent;
public class UserAds extends Activity {
	private ListView list;
	Login loginObj;
	public String email;
	ArrayAdapter<String> adapterresults;
	public static String idno;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userads);
		loginObj=new Login();
		email=loginObj.getEmail();
		ArrayList<UserListItem> data=getSearchData();
		list=(ListView)findViewById(R.id.userads_list);
		list.setAdapter(new UserListAdapter(this,data));
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
            	Object o = list.getItemAtPosition(position);
               UserListItem uData = (UserListItem) o;
               Intent detailIntent=new Intent(UserAds.this,UserAdDetails.class);
               startActivity(detailIntent);
               setId(uData.getbookId());
            }
        });
	}
	public static void setId(String id){
		idno=id;
		
	}
	
	public static String getId(){
		return idno;
	}
	
	private ArrayList<UserListItem> getSearchData(){
		ArrayList<UserListItem> results = new ArrayList<UserListItem>();
        UserListItem uData;
        UserData user=new UserData();
		user.start();
			try{
				user.join();
				}catch(Exception e){}
			JSONObject temp=user.getSearchdata();
			try{
		    int num=Integer.parseInt(temp.get("NUM").toString());
		   // System.out.println(num);
		    JSONObject eachTextbook;
		    for(int n=1;n<=num;n++){
		    	eachTextbook=(JSONObject)temp.get(""+n);
		    	//System.out.println((String)eachTextbook.get("title"));
		    	uData=new UserListItem();
		    	uData.setbookTitle((String)eachTextbook.get("title"));
		    	uData.setbookAuthor((String)eachTextbook.get("author"));
		    	uData.setbookPrice((String)eachTextbook.get("price"));
		    	uData.setbookId((String)eachTextbook.get("id"));
		    	results.add(uData);
		    }
			}
         catch(JSONException e){
        	 System.out.println("Failed due to JSON"); 
         }
        return results;
	}
	
	public class UserData extends Thread{
		JSONObject data=null;
		databaseAPI api=new databaseAPI();
		public void run(){
			data=databaseAPI.getTextbookForUsers(email);		
		}
		
		public JSONObject getSearchdata(){
			return data;
		}
	}
}

