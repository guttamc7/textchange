package com.textchange.utils;
import java.net.*;
import java.io.*;
import org.json.*;


import java.util.*;
import java.text.*;
import java.util.Date;

public class Request implements Runnable{
  
  private final Socket incomingRequest;
  private InputStream inStream;
  private OutputStream outStream;
  private DataBase accessDatabase;
  
  public Request(Socket incomingRequest){
    this.incomingRequest=incomingRequest;
  }
  
  public void run(){
    setInputOutputStream();
    accessDatabase=new DataBase();
    accessDatabase.establishDataBaseConnection();
    
    handleRequest();
    closeConnection(); 
  }
  
  //Set InputOutputSteam for getting and sending the requests
  public synchronized void setInputOutputStream(){
    try{
      this.inStream=this.incomingRequest.getInputStream();
      this.outStream=this.incomingRequest.getOutputStream();
    }catch(IOException e){
      System.out.println("Error while getting input/output Stream");
      e.printStackTrace();
    }
  }
  
  
  
  public synchronized void handleRequest(){
 
    
    JSONObject outputData;
    StringBuilder request=readRequest();
    
    HTTPParser parse=new HTTPParser(request);
    parse.parseRequest();
    
    if(parse.getRequestType().equals("GET")){
      outputData=GETRequestType(parse);
    }
    else
      if(parse.getRequestType().equals("POST")){
      outputData=POSTRequestType(parse);
    }
    else{
      outputData=new JSONObject();
      try{       
        outputData.put("response","Request Unhandled");
      }catch(JSONException e){
        System.out.println("Error while converting the Request Unhandled data to JSON object");
        e.printStackTrace();
      }
    }
  
    writeToOutputStream(outputData);
   
  }
  
  public JSONObject GETRequestType(HTTPParser request){
    JSONObject outputData;
    String requestData=request.getRequestData();    
    HashMap<String,String> nameValuePairs=request.getNameValuePairs();
    
    if(requestData.equals("getAllDepartments")){
      outputData=accessDatabase.getAllDepartments();
    }
    else
      if(requestData.equals("getAllCourses")){
      outputData=accessDatabase.getAllCourses();
    }
    else
      if(requestData.equals("getCoursesForDepartment")){
      outputData=accessDatabase.getCoursesForDepartment(nameValuePairs.get("department"));
    }else
      if(requestData.equals("searchTextbook")){
      outputData=accessDatabase.searchForTextbook(nameValuePairs.get("title"),nameValuePairs.get("author"),nameValuePairs.get("isbn"),
                                                  nameValuePairs.get("department"),nameValuePairs.get("course"));
    }else
      if(requestData.equals("getTextbooksForCourse")){
      outputData=accessDatabase.getTextbooksForCourse(nameValuePairs.get("course"));
    }else
      if(requestData.equals("getTextbookData")){
      outputData=accessDatabase.getTextbookData(nameValuePairs.get("id"));
    }else
      if(requestData.equals("getUserDetails")){
      outputData=accessDatabase.getUserDetails(nameValuePairs.get("email"));
    }else
      if(requestData.equals("getTextbooksForUser")){
      outputData=accessDatabase.getTextbooksForUser(nameValuePairs.get("email"));
    }
    else
      if(requestData.equals("getMessage")){
      outputData=accessDatabase.getMessage(nameValuePairs.get("toEmail"));
    }
    else
      if(requestData.equals("verifyEmail")){
      boolean updateLoginNum=accessDatabase.updateLoginNumber(nameValuePairs.get("email"));
      try{
        if(updateLoginNum==false){
          outputData=new JSONObject();
          outputData.put("Your email is not verified","Please try again!");
        }else{
          outputData=new JSONObject();
          outputData.put("Your email is now verified","You can now Login and enjoy our services\nThank You");
        }
      }catch(JSONException e){
      outputData=new JSONObject();
      }
    }
    else{
      outputData=new JSONObject();
      try{        
        outputData.put("response","Unknown Request");
      }catch(JSONException e){
        System.out.println("Error while converting the Request Unhandled data inside GET request to JSON object");
        e.printStackTrace();
      }
    }
    
    
    return outputData; 
  }
  
  public JSONObject POSTRequestType(HTTPParser request){
    JSONObject outputData=new JSONObject();
    String requestData=request.getRequestData();    
    HashMap<String,String> nameValuePairs=request.getNameValuePairs();
    
    if(requestData.equals("addUser")){
      System.out.println("in add user");
      outputData=accessDatabase.userExists(nameValuePairs.get("email"));
      try{
        if(outputData.getBoolean("userExists")==false){
          outputData=accessDatabase.addUser(nameValuePairs.get("email"),nameValuePairs.get("password"),nameValuePairs.get("fullname"),nameValuePairs.get("phoneNumber"));
          Request.sendVerificationEmail(nameValuePairs.get("email"),nameValuePairs.get("fullname"),"http://sac12.cs.purdue.edu:8080/verifyEmail?email="+nameValuePairs.get("email"));
        }else{
        outputData.put("userAdded",false);
        }
      }catch(JSONException e){
        System.out.println("Error while checking is user exists in the database inside POST request");
        e.printStackTrace();
      }
    }else
      if(requestData.equals("addTextbook")){
      outputData=accessDatabase.addTextbook(nameValuePairs.get("seller"),nameValuePairs.get("title"),nameValuePairs.get("author"),nameValuePairs.get("isbn"),
                                             Request.getTodaysDate(),nameValuePairs.get("price"),nameValuePairs.get("course"));
    }else
      if(requestData.equals("verifyLogin")){
      outputData=accessDatabase.verifyLogin(nameValuePairs.get("username"),nameValuePairs.get("password"));
    }else
      if(requestData.equals("deleteTextbook")){
      outputData=accessDatabase.deleteTextbook(nameValuePairs.get("id"));
      System.out.println(outputData);
    }
    else
      if(requestData.equals("addMessage")){
      outputData=accessDatabase.addMessage(nameValuePairs.get("fromEmail"), nameValuePairs.get("toEmail"),nameValuePairs.get("message"));
    } else{
      outputData=new JSONObject();
      try{        
        outputData.put("response","Unknown Request");
      }catch(JSONException e){
        System.out.println("Error while converting the Request Unhandled data inside GET request to JSON object");
        e.printStackTrace();
      }
    }
    
    return outputData; 
  }

  //Generate a random password to be mailed to the user while creating a new account
  private synchronized String generateRandomPassword(){
    String randomPassword = UUID.randomUUID().toString();
    randomPassword=randomPassword.substring(0,8);
    System.out.println(randomPassword);
    return randomPassword;
  }
  
  //Send a verification email to the users email 
  public static void sendVerificationEmail(String email,String name,String link){
  String messageBody="Hello "+name+"\nWelcome to BoilerLists - The Best Place to buy and sell textbooks in Purdue University"+
      ".You will have to click on the following link to verify your account for BoilerLists: \n"+link+
      "\nThank You!\nBoilersList Executive Board";
  String subject="Greetings from BoilersList!";
  ServerMail newEmail= new ServerMail();
  newEmail.sendMail(email,subject,messageBody);
  }
  
  
  //Write the JSONObject to the output-stream
  public synchronized void writeToOutputStream(JSONObject data){
    PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);
    out.print("HTTP/1.1 200 OK\r\n\r\n"); 
    out.println(data.toString());
  }
  
  //Read the request received
  public synchronized StringBuilder readRequest(){
    StringBuilder request=new StringBuilder();
    int inputSize=0;
    byte inputData[];
    
    try{
      inputSize=inStream.available();
      inputData=new byte[inputSize]; 
      inStream.read(inputData);
      request.append(new String(inputData,"UTF-8"));
    }
    catch(IOException e){
      System.out.println("Error while reading the request in readRequest");
      e.printStackTrace();
    }
    decodeSpaces(request);
    return request;
  }
  
  //Replace all the %20 in the request with spaces
  public synchronized void decodeSpaces(StringBuilder request){
   int index=0;
   
   while(index<request.length()){
   index=request.indexOf("%20",index);
   if(index==-1) break;
   request.replace(index,index+3," ");
   }
  }
  
  //Get todays date
  public static String getTodaysDate() {
    return new SimpleDateFormat("MMMM dd, yyyy").format(new Date());
  }
  
  //Close the connnection from the client
  public synchronized void closeConnection(){
    try{
      incomingRequest.close();
    }catch(IOException e){
      System.out.println("Error while closing the connection");
    }
  }
}