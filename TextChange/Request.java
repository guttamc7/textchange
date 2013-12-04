//Class to handle requests

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
  
  private static String emailVerificationLink="http://sac12.cs.purdue.edu:8080/verifyEmail?email=";
  
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
  
  
  //Handle every request accordingly as received from client
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
        outputData.put("response","Unknown Request type");
      }catch(JSONException e){
        System.out.println("Error while converting the Request Unhandled data to JSON object");
        e.printStackTrace();
      }
    }
    
    try{
      //Send a normal string to the client
      if(parse.getRequestData().equals("verifyEmail")){
        if((Boolean)outputData.get("emailVerified")==true){
          writeToOutputStream("Your email is verified. You can now login and use TextChange services. Thank You!");
        }else
          if((Boolean)outputData.get("emailVerified")==false){
          writeToOutputStream("Your email is not verified. Please try clicking on the link sent to you again!");
        }//Send a JSON Object to the client
      }else{
        writeToOutputStream(outputData);
      }
    }catch(JSONException e){
      System.out.println("Error while getting the JSON data from outputData in handleRequest");
      e.printStackTrace();
    }
  }
  
  //Handle the GET HTTP request
  public synchronized JSONObject GETRequestType(HTTPParser request){
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
      outputData=new JSONObject();  
      try{
        if(updateLoginNum==false){      
          outputData.put("emailVerified",updateLoginNum);
        }else{
          outputData=new JSONObject();
          outputData.put("emailVerified",updateLoginNum);
        }
      }catch(JSONException e){
        System.out.println("Error while converting the verifyEmail data inside GET request to JSON object");
        e.printStackTrace();
      }
    }
    else{
      outputData=new JSONObject();
      try{        
        outputData.put("response","Unknown GET Request");
      }catch(JSONException e){
        System.out.println("Error while converting the Request Unhandled data inside GET request to JSON object");
        e.printStackTrace();
      }
    }
    
    return outputData; 
  }
  
  //Handle the POST http request type
  public synchronized JSONObject POSTRequestType(HTTPParser request){
    JSONObject outputData=new JSONObject();
    String requestData=request.getRequestData();    
    HashMap<String,String> nameValuePairs=request.getNameValuePairs();
    
    if(requestData.equals("addUser")){
      outputData=accessDatabase.userExists(nameValuePairs.get("email"));
      try{
        if(outputData.getBoolean("userExists")==false){
          outputData=accessDatabase.addUser(nameValuePairs.get("email"),nameValuePairs.get("password"),nameValuePairs.get("fullname"),nameValuePairs.get("phoneNumber"));
          Request.sendVerificationEmail(nameValuePairs.get("email"),nameValuePairs.get("fullname"),emailVerificationLink+nameValuePairs.get("email"));
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
                                            Request.getTodaysDate(),nameValuePairs.get("price"),nameValuePairs.get("course"),nameValuePairs.get("image"));
    }else
      if(requestData.equals("verifyLogin")){
      outputData=accessDatabase.verifyLogin(nameValuePairs.get("username"),nameValuePairs.get("password"));
    }else
      if(requestData.equals("deleteTextbook")){
      outputData=accessDatabase.deleteTextbook(nameValuePairs.get("id"));
      System.out.println(outputData);
    }else
      if(requestData.equals("updateUserInfo")){
      outputData=accessDatabase.updateUserInfo(nameValuePairs.get("email"),nameValuePairs.get("name"),nameValuePairs.get("phone"));
    }else
      if(requestData.equals("addMessage")){
      outputData=accessDatabase.addMessage(nameValuePairs.get("fromEmail"), nameValuePairs.get("toEmail"),nameValuePairs.get("message"));
    }
    else{
      outputData=new JSONObject();
      try{        
        outputData.put("response","Unknown POST Request");
      }catch(JSONException e){
        System.out.println("Error while converting the Request Unhandled data inside GET request to JSON object");
        e.printStackTrace();
      }
    }
    
    return outputData; 
  }
  
  
  //Send a verification email to the users email 
  public static void sendVerificationEmail(String email,String name,String link){
    String messageBody="Hello "+name+"\nWelcome to TextChange - The Best Place to buy and sell textbooks in Purdue University"+
      ". You will have to click on the following link to verify your account for TextChange: \n"+link+
      " \n Thank You! \n TextChange Executive Board";
    String subject="Greetings from TextChange!";
    ServerMail newEmail= new ServerMail();
    newEmail.sendMail(email,subject,messageBody);
  }
  
  
  //Write the JSONObject to the output-stream
  public synchronized void writeToOutputStream(JSONObject data){
    PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);
    out.print("HTTP/1.1 200 OK\r\n\r\n"); 
    out.println(data.toString());
  }
  
  //Write the string to the output-stream
  public synchronized void writeToOutputStream(String data){
    PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);
    out.print("HTTP/1.1 200 OK\r\n\r\n"); 
    out.println(data);
  }
  
  
  //Read the request received
  public synchronized StringBuilder readRequest(){
    StringBuilder request=new StringBuilder();
    int inputSize=0;
    byte inputData[];
    
    try{
      //Try to read the request for 2.5 seconds. If no request is received, move on. Request will be read every 0.5 seconds 
      for(int n=5;(inputSize=inStream.available())==0 && n!=0;n--){
        putRequestToSleep(500);
      }
      
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
  
  //Put the request to sleep so that it can again be tried for reading its request in some time
  public synchronized void putRequestToSleep(int time){
    try{
      Thread.sleep(time);
    }catch(InterruptedException e){
      System.out.println("Error while putting the request to sleep");
      e.printStackTrace();
    }
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
      inStream.close();
      outStream.close();
      incomingRequest.close();
    }catch(IOException e){
      System.out.println("Error while closing the connection");
      e.printStackTrace();
    }
  }
}
