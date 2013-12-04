package com.textchange;
import android.os.Bundle;
import org.json.*;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.Activity;
import java.util.ArrayList;

import org.json.JSONObject;

import com.textchange.utils.databaseAPI;

import android.widget.AdapterView;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.content.Intent;
public class SearchResults extends Activity {
	private ListView list;
	ArrayAdapter<String> adapterresults;
	public static String title;
	public static String author;
	public static String isbn;
	public static String dept;
	public static String course;
	public static String idno;
	CustomListAdapter adapt;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchresults);
		Intent in = getIntent();
		title=in.getStringExtra("title");
		author=in.getStringExtra("author");
		isbn=in.getStringExtra("ISBN");
		dept=in.getStringExtra("department");
		course=in.getStringExtra("course");
		ArrayList<AdListItem> data=getSearchData();
		/*for(AdListItem a:data){
			System.out.println(a.getbookTitle());
		}*/
		
		list=(ListView)findViewById(R.id.search_list);
		adapt=new CustomListAdapter(this,data);
		list.setAdapter(adapt);
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
               Object o = list.getItemAtPosition(position);
               AdListItem adData = (AdListItem) o;
               Intent detailIntent=new Intent(SearchResults.this,AdDetails.class);
               startActivityForResult(detailIntent,1);
               setId(adData.getbookId());
            }
        });
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
        	adapt.notifyDataSetChanged();
        	list.setAdapter(adapt);
        }
	}
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		  super.onRestoreInstanceState(savedInstanceState);
		  adapt.notifyDataSetChanged();
		  list.setAdapter(adapt);
	}
	public void onSaveInstanceState(Bundle savedInstanceState) {
		  super.onSaveInstanceState(savedInstanceState);
		  adapt.notifyDataSetChanged();
		  list.setAdapter(adapt);
	}
        	
	private ArrayList<AdListItem> getSearchData(){
		ArrayList<AdListItem> results = new ArrayList<AdListItem>();
        AdListItem adData;
        SearchData search=new SearchData();
		 search.start();
			try{
				search.join();
				}catch(Exception e){}
			JSONObject temp=search.getSearchdata();
			try{
		    int num=Integer.parseInt(temp.get("NUM").toString());
		    JSONObject eachTextbook;
		    for(int n=1;n<=num;n++){
		    	eachTextbook=(JSONObject)temp.get(""+n);
		    	adData=new AdListItem();
		    	adData.setbookTitle((String)eachTextbook.get("title"));
		    	adData.setbookAuthor((String)eachTextbook.get("author"));
		    	adData.setbookPrice((String)eachTextbook.get("price"));
		    	adData.setbookId((String)eachTextbook.get("id"));
		    	results.add(adData);
		    }
			}
         catch(JSONException e){
        	 System.out.println("Failed due to JSON"); 
         }
        return results;
	}
	
	public static void setId(String id){
		idno=id;
		
	}
	
	public static String getId(){
		return idno;
	}
	
	public class SearchData extends Thread{
		JSONObject data=null;
		databaseAPI api=new databaseAPI();
		public void run(){
			data=databaseAPI.searchTextbookAds(title,author,isbn,dept,course);			
		}
		
		public JSONObject getSearchdata(){
			return data;
		}
	}
}

