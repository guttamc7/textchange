//Class to parse a HTTP GET and POST request

import java.util.*;

public class HTTPParser{
  
  private StringBuilder request;
  private String requestType;
  private String requestData;
  private String requestHeader;
  private HashMap <String,String> nameValuePairs;
  
  public HTTPParser(StringBuilder request){
    this.request=request;
    this.requestType="";
    this.requestData="";
    this.requestHeader="";
    nameValuePairs=new HashMap<String,String>();
  }
  
  //Parse the type of request and process accordingly
  public void parseRequest(){
    int requestEndIndex=request.indexOf(" ");
    if(requestEndIndex==-1){ this.requestType="UNKNOWN"; return;}
    
    this.requestType=request.substring(0,requestEndIndex);
    if(this.requestType.equals("GET")){
      parseGETRequest();
    }else
      if(this.requestType.equals("POST")){
      parsePOSTRequest();
    }
  }
  
  //Parse GET HTTP request type
  public void parseGETRequest(){    
    StringBuilder requestData=new StringBuilder();
    int datalength=request.indexOf("HTTP")-1;
    int n=5;
    
    while(n<datalength){
      if(request.charAt(n)=='?'){
        storeNameValuePairs(++n,datalength);
        break;
      }
      requestData.append(request.charAt(n));
      n++;
    }
    this.requestData=requestData.toString(); 
    this.requestHeader=request.substring(n,request.length());
  }
  
  //Parse POST http request type
  public void parsePOSTRequest(){
    StringBuilder requestData=new StringBuilder();
    int datalength=request.indexOf("HTTP")-1;
    int headerEndingIndex=request.lastIndexOf("\n");
    int n=6;
    
    while(n<datalength){
      requestData.append(request.charAt(n));
      n++;
    }
    this.requestData=requestData.toString();
    
    this.requestHeader=request.substring(datalength+9,headerEndingIndex);
    storeNameValuePairs(headerEndingIndex+1,request.length());
  }
  
  //Store the name value pairs from the data into the hashmap
  private void storeNameValuePairs(int startingIndex,int endIndex){
    String name="";
    String value="";
    int n=0;
    
    while(startingIndex<endIndex){
      n=request.indexOf("=",startingIndex);  
      name=request.substring(startingIndex,n++);
      startingIndex=request.indexOf("&",n);
      if(startingIndex==-1) startingIndex=endIndex;
      value=request.substring(n,startingIndex++);
      this.nameValuePairs.put(name,value);
    }
  }
  
  //Getter methods to get all the data
  public String getRequestType(){
    return requestType; 
  }
  
  public String getRequestData(){
    return requestData;
  }
  
  public String getRequestHeader(){
    return requestHeader;
  }
  
  public HashMap<String,String> getNameValuePairs(){
  return nameValuePairs;
  }
}
