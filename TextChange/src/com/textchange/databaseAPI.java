import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.*;
import java.io.*;
import java.util.*;

public class databaseAPI
{
  
  public static int PORT=8080;
  public static String HOST="sac12.cs.purdue.edu";
  
  public static JSONObject getDepartments()
  {
       
      Socket requestSocket;
      PrintWriter out;
      BufferedReader in; 
      String inputLine;
      JSONObject data=null;
      try
      {
     
            System.out.println("Lets Connect");
            requestSocket= new Socket(HOST,PORT);
            System.out.println("Connected");
            out = new PrintWriter(requestSocket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
            out.printf("GET /getAllDepartments HTTP/1.1");
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
            StringBuilder s = new StringBuilder();

            String line = in.readLine();
            while(line != null)
            {
            s.append(line);
            line = in.readLine();
            }
            String content=s.substring(s.indexOf("{",0),s.length());
            data=new JSONObject(content);
          //  System.out.println(data);
            in.close();
            out.close();
            requestSocket.close();
            
           
       }
            
      
            catch(JSONException e)
            {
         
              System.out.println("Failed due to JSON"); 
            }
            catch(IOException e)
            {
              System.out.println("Failed due to connection");
            }
       return data;

   
     }
    
  
     public static JSONObject getCoursesByDepartments(String dept)
     {
       
      Socket requestSocket;
      PrintWriter out;
      BufferedReader in; 
      String inputLine;
      JSONObject data=null;
      try
      {
     
            System.out.println("Lets Connect");
            requestSocket= new Socket(HOST,PORT);
            System.out.println("Connected");
            out = new PrintWriter(requestSocket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
            out.printf("GET /getCoursesForDepartment?department="+dept+" HTTP/1.1");
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
            StringBuilder s = new StringBuilder();

            String line = in.readLine();
            while(line != null)
            {
            s.append(line);
            line = in.readLine();
            }
           
            String content=s.substring(s.indexOf("{",0),s.length());
            data=new JSONObject(content);
           // System.out.println(data);
            in.close();
            out.close();
            requestSocket.close();      
       }
       catch(JSONException e)
       {
         
        System.out.println("Failed due to JSON"); 
         
       }
       catch(IOException e)
       {
         System.out.println("Failed due to connection");
       }
       return data;

   
     }
     public static JSONObject searchTextbookAds(String title , String author, String isbn , String department, String course )
     {
       
      Socket requestSocket;
      PrintWriter out;
      BufferedReader in; 
      String inputLine;
      JSONObject data=null;
   
      try
      {
     
            System.out.println("Lets Connect");
            requestSocket= new Socket(HOST,PORT);
            System.out.println("Connected");
            out = new PrintWriter(requestSocket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
            out.printf("GET /searchTextbook?title="+title+"&author="+author+"&isbn="+isbn+"&department="+department+"&course="+course+" HTTP/1.1");
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
            StringBuilder s = new StringBuilder();
            
             String line = in.readLine();
             while(line != null)
             {
               s.append(line);
               line = in.readLine();
            }
               String content=s.substring(s.indexOf("{",0),s.length());
               data=new JSONObject(content);
               //System.out.println(data);
               in.close();
               out.close();
               requestSocket.close();
             
               }
             
      catch (JSONException e)
      {
        
         System.out.println("Failed due to  JSON"); 
      }
       
       catch(IOException e)
       {
         System.out.println("Failed due to connection");
       }
      return data;
     }
     
     public static JSONObject getTextbookData(String id)
     {
       
      Socket requestSocket;
      PrintWriter out;
      BufferedReader in; 
      String inputLine;
      JSONObject data=null;
      try
      {
     
            System.out.println("Lets Connect");
            requestSocket= new Socket(HOST,PORT);
            System.out.println("Connected");
            out = new PrintWriter(requestSocket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
            out.printf("GET /getTextbookData?id="+id+" HTTP/1.1");
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
            StringBuilder s = new StringBuilder();
            
             String line = in.readLine();
             while(line != null)
             {
               s.append(line);
               line = in.readLine();
            }
              String content=s.substring(s.indexOf("{",0),s.length());
              data=new JSONObject(content);
               in.close();
               out.close();
               requestSocket.close();
               
             
            
       
      }
      catch (JSONException e)
      {
        
         System.out.println("Failed due to  JSON"); 
      }
       
       catch(IOException e)
       {
         System.out.println("Failed due to connection");
       }
       return data;
     }
      public static JSONObject getTextbookForUsers(String email)
     {
       
      Socket requestSocket;
      PrintWriter out;
      BufferedReader in; 
      String inputLine;
      JSONObject data=null;
      try
      {
     
            System.out.println("Lets Connect");
            requestSocket= new Socket(HOST,PORT);
            System.out.println("Connected");
            out = new PrintWriter(requestSocket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
            out.printf("GET /getTextbooksForUser?email="+email+" HTTP/1.1");
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
            StringBuilder s = new StringBuilder();
            
             String line = in.readLine();
             while(line != null)
             {
               s.append(line);
               line = in.readLine();
            }
               String content=s.substring(s.indexOf("{",0),s.length());
               data=new JSONObject(content);
               in.close();
               out.close();
               requestSocket.close();
            
            
       
      }
      catch (JSONException e)
      {
        
         System.out.println("Failed due to  JSON"); 
      }
       
       catch(IOException e)
       {
         System.out.println("Failed due to connection");
       }
       return data;
     }
     public static JSONObject getUserDetails(String email)
     {
       
       
      Socket requestSocket;
      PrintWriter out;
      BufferedReader in; 
      String inputLine;
      JSONObject data=null;
      try
      {
     
            System.out.println("Lets Connect");
            requestSocket= new Socket(HOST,PORT);
            System.out.println("Connected");
            out = new PrintWriter(requestSocket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
            out.printf("GET /getUserDetails?email="+email+" HTTP/1.1");
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
            StringBuilder s = new StringBuilder();
            
             String line = in.readLine();
             while(line != null)
             {
               s.append(line);
               line = in.readLine();
            }
              String content=s.substring(s.indexOf("{",0),s.length());
              data=new JSONObject(content);
              in.close();
              out.close();
              requestSocket.close();
              
     
       
      }
      
      catch (JSONException e)
      {
        
       System.out.println("Failed due to  JSON"); 
      }
       
       catch(IOException e)
       {
         System.out.println("Failed due to connection");
       }
            return data;
       }
    
     public static JSONObject addUser(String email, String password, String fullname , String phonenumber)
     {
       
       
      Socket requestSocket;
      PrintWriter out;
      BufferedReader in; 
      String inputLine;
      JSONObject data=null;
      String adduser="";
      try
      {
     
            System.out.println("Lets Connect");
            requestSocket= new Socket(HOST,PORT);
            System.out.println("Connected");
            out = new PrintWriter(requestSocket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
            out.printf("POST /addUser HTTP/1.1 Header \nemail="+email+"&password="+password+"&fullname="+fullname+"&phoneNumber="+phonenumber);
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
            StringBuilder s = new StringBuilder();
            
             String line = in.readLine();
             while(line != null)
             {
               s.append(line);
               line = in.readLine();
            }
             String content=s.substring(s.indexOf("{",0),s.length());
             data=new JSONObject(content);
             in.close();
             out.close();
             requestSocket.close();
             
      }
      
      catch (JSONException e)
      {
        
       System.out.println("Failed due to  JSON"); 
      }
       
       catch(IOException e)
       {
         System.out.println("Failed due to connection");
       }
         return data;
       }
    
      public static JSONObject verifyLogin(String email, String password)
     {
       
       
      Socket requestSocket;
      PrintWriter out;
      BufferedReader in; 
      String inputLine;
      JSONObject data=null;
      try
      {
     
            System.out.println("Lets Connect");
            requestSocket= new Socket(HOST,PORT);
            System.out.println("Connected");
            out = new PrintWriter(requestSocket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
            out.printf("POST /verifyLogin HTTP/1.1 Header \nusername="+email+"&password="+password);
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
            StringBuilder s = new StringBuilder();
            
             String line = in.readLine();
             while(line != null)
             {
               s.append(line);
               line = in.readLine();
            }
             
            String content=s.substring(s.indexOf("{",0),s.length());
            data=new JSONObject(content);
          
             in.close();
             out.close();
             requestSocket.close();
             
      }
      
      catch (JSONException e)
      {
        
       System.out.println("Failed due to  JSON"); 
      }
       
       catch(IOException e)
       {
         System.out.println("Failed due to connection");
       }
       
            return data;
       }
    
      
      
       public static JSONObject postTextBook(String email, String title , String author , String isbn ,  String price , String course)
     {
       
       
      Socket requestSocket;
      PrintWriter out;
      BufferedReader in; 
      String inputLine;
      JSONObject data=null;

      try
      {
     
            System.out.println("Lets Connect");
            requestSocket= new Socket(HOST,PORT);
            System.out.println("Connected");
            out = new PrintWriter(requestSocket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
            out.printf("POST /addTextbook HTTP/1.1 Header \nseller="+email+"&title="+title+"&author="+author+"&isbn="+isbn+"&price="+price+"&course="+course);
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
            StringBuilder s = new StringBuilder();
            
             String line = in.readLine();
             while(line != null)
             {
               s.append(line);
               line = in.readLine();
            }
             String content=s.substring(s.indexOf("{",0),s.length());
             data=new JSONObject(content);
        //     System.out.println(data);
             in.close();
             out.close();
             requestSocket.close();
         
      }
      
      catch (JSONException e)
      {
        
       System.out.println("Failed due to  JSON"); 
      }
       
       catch(IOException e)
       {
         System.out.println("Failed due to connection");
       }
            return data;
       }
       
      public static JSONObject deleteAd(String id)
     {
       
       
      Socket requestSocket;
      PrintWriter out;
      BufferedReader in; 
      String inputLine;
      JSONObject data=null;

      try
      {
     
            System.out.println("Lets Connect");
            requestSocket= new Socket(HOST,PORT);
            System.out.println("Connected");
            out = new PrintWriter(requestSocket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
            out.printf("POST /deleteTextbook HTTP/1.1 Header \nid="+id);
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
            StringBuilder s = new StringBuilder();
            
             String line = in.readLine();
             while(line != null)
             {
               s.append(line);
               line = in.readLine();
            }
             String content=s.substring(s.indexOf("{",0),s.length());
             data=new JSONObject(content);
             in.close();
             out.close();
             requestSocket.close();
         
      }
      
      catch (JSONException e)
      {
        
       System.out.println("Failed due to  JSON"); 
      }
       
       catch(IOException e)
       {
         System.out.println("Failed due to connection");
       }
            return data;
       }
      
       public static JSONObject updateUser(String email , String name , String phone)
     {
       
       
      Socket requestSocket;
      PrintWriter out;
      BufferedReader in; 
      String inputLine;
      JSONObject data=null;

      try
      {
     
            System.out.println("Lets Connect");
            requestSocket= new Socket(HOST,PORT);
            System.out.println("Connected");
            out = new PrintWriter(requestSocket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
            out.printf("POST /updateUserInfo HTTP/1.1 Header \nemail="+email+"&name="+name+"&phone="+phone);
            in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
            StringBuilder s = new StringBuilder();
            
             String line = in.readLine();
             while(line != null)
             {
               s.append(line);
               line = in.readLine();
            }
             String content=s.substring(s.indexOf("{",0),s.length());
             data=new JSONObject(content);
             in.close();
             out.close();
             requestSocket.close();
         
      }
      
      catch (JSONException e)
      {
        
       System.out.println("Failed due to  JSON"); 
      }
       
       catch(IOException e)
       {
         System.out.println("Failed due to connection");
       }
            return data;
       }
   
      
           
      
     }
        
      
     
     

