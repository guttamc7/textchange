package com.textchange.utils;
import java.sql.*;
import org.json.*;
import java.util.*;

public class DataBase{
  private Connection con;
  
  private static final String URLDEFAULT="jdbc:mysql://mydb.ics.purdue.edu:3306/guttamc";
  private static final String USERNAME_DEFAULT="guttamc";
  private static final String PASSWORD_DEFAULT="boilerslist";
  
  private final String url;
  private final String username;
  private final String password;
  
  public DataBase(String url,String username,String password){
    this.url=url;
    this.username=username;
    this.password=password;
  }
  
  public DataBase(){
    this(DataBase.URLDEFAULT,DataBase.USERNAME_DEFAULT,DataBase.PASSWORD_DEFAULT);
  }
  
  public void establishDataBaseConnection(){
    try{
      Class.forName("com.mysql.jdbc.Driver"); 
      con = DriverManager.getConnection(this.url,this.username,this.password);
    }
    catch(SQLException e){
      System.out.println("Error while establishing connection with mySql database");
      e.printStackTrace();
    }
    catch(ClassNotFoundException e){
      System.out.println("Error while getting the drivers for establishing connection with mySql");  
      e.printStackTrace();
    }
  }
  
  //Get all the departments from the MySQL Database
  public JSONObject getAllDepartments(){
    JSONObject allDepartments=new JSONObject();
    int n=1;
    try{
      PreparedStatement stat = con.prepareStatement("SELECT DISTINCT Major FROM purduesubjects");
      ResultSet r=stat.executeQuery();
      while(r.next()){
        allDepartments.put(Integer.toString(n++),r.getString(1)); 
      }
      allDepartments.put("NUM",--n);
    }
    catch(SQLException e){
      System.out.println("Error while getting all the departments from mySql Database");
      e.printStackTrace();
    }catch(JSONException e){
     System.out.println("Error while converting the getAllDepartments data to JSON Object");
      e.printStackTrace();
    }
    
    return allDepartments;
  }
  
  
  //Get all the Courses from the MySQL Database
  public JSONObject getAllCourses(){
    JSONObject allCourses=new JSONObject();
    int n=1;
    
    try{
      PreparedStatement stat = con.prepareStatement("SELECT * FROM purduesubjects");
      ResultSet r=stat.executeQuery();
      while(r.next()){
        allCourses.put(Integer.toString(n++),r.getString(2));
      }
       allCourses.put("NUM",--n);
    } 
    catch(SQLException e){
      System.out.println("Error while getting all the courses from mySql Database");
      e.printStackTrace();
    }catch(JSONException e){
     System.out.println("Error while converting the getAllCourses data to JSON Object");
      e.printStackTrace();
    }
    return allCourses;
  }
  
  
  //Get all the courses for a Particular department
  public JSONObject getCoursesForDepartment(String department){
    JSONObject coursesForDepartment=new JSONObject();
    int n=1;
    
    try{
      PreparedStatement stat = con.prepareStatement("SELECT * FROM purduesubjects WHERE Major='"+department+"'");
      ResultSet r=stat.executeQuery();
      while(r.next()){
        coursesForDepartment.put(Integer.toString(n++),r.getString(2));
      }
       coursesForDepartment.put("NUM",--n);
    } 
    catch(SQLException e){
      System.out.println("Error while getting courses for a particular major from mySql Database");
      e.printStackTrace();
    }catch(JSONException e){
      System.out.println("Error while converting the getCoursesForDepartment data to JSON Object");
      e.printStackTrace();
    }
    
    return coursesForDepartment;
  }
  
  //Search for a textbook from the database
  public JSONObject searchForTextbook(String title,String author,String ISBN,String department,String course){
  JSONObject textbooks=new JSONObject();
  HashMap<String,String> textbook=new HashMap <String,String>();
  int n=1;
  
  try{
      PreparedStatement stat = con.prepareStatement("SELECT Title,Author,Price,UniqueID FROM Ad WHERE Title LIKE '"+title+
                                                    "%' AND Author LIKE '"+author+"%' AND ISBN LIKE '"+ISBN+"%' AND Course LIKE '"+course+"%' AND Course LIKE '"+department+"%'");
      ResultSet r=stat.executeQuery();
      while(r.next()){ 
        textbook.put("title",r.getString(1));
        textbook.put("author",r.getString(2));
        textbook.put("price",r.getString(3));
        textbook.put("id",""+r.getInt(4));
        textbooks.put(Integer.toString(n++),textbook);
      }
       textbooks.put("NUM",--n);
    } 
    catch(SQLException e){
      System.out.println("Error while searching for a textbook from mySql Database");
      e.printStackTrace();
    }catch(JSONException e){
      System.out.println("Error while converting the searchForTextbook data to JSON Object");
      e.printStackTrace();
    }
  
  return textbooks;
  }
  
  //Add a new user to the mySql Database
  public JSONObject addUser(String email,String password,String fullName,String phoneNumber){
    JSONObject userAdded=new JSONObject();
    boolean added=false;
    
    try{
      PreparedStatement stat = con.prepareStatement("INSERT INTO User (User_ID,Password,FullName,Phone,LoginNumber) VALUES (?,?,?,?,?)");
      stat.setString(1, email);
      stat.setString(2, password);
      stat.setString(3, fullName);
      stat.setString(4, phoneNumber);
      stat.setString(5,"0");
      stat.executeUpdate();
      added=true;
    } 
    catch(SQLException e){
      System.out.println("Error while adding username to the mySql Database");
      e.printStackTrace();
    }finally{
      try{
        userAdded.put("userAdded",added);
      }catch(JSONException e){
        System.out.println("Error while converting addUser data to JSON Object");
        e.printStackTrace();
      }
    }
    return userAdded;
  }
  
  //Get the number of logins
  public int getNumLogins(String email){
  int numLogins=0;
  String logins="";
  
  try{
      PreparedStatement stat = con.prepareStatement("SELECT LoginNumber FROM User WHERE User_ID='"+email+"'");
      ResultSet r = stat.executeQuery();
      while(r.next()){
        logins+=r.getString(1);
      }
      numLogins=Integer.parseInt(logins);
    } 
    catch(SQLException e){
      System.out.println("Error while getting the number of logins from the mySql Database");
      e.printStackTrace();
    }catch(NumberFormatException e){
      System.out.println("EError while getting the number of logins from the mySql Database");
      e.printStackTrace();
    }
   System.out.println(numLogins); 
  return numLogins;
  }
  
  
  //Update the number of logins by 1 
  public boolean updateLoginNumber(String email){
  int oldNumLogins=getNumLogins(email);
  int newNumLogins=oldNumLogins+1;
  
  boolean loginUpdated=false; 
    
   try{
      PreparedStatement stat = con.prepareStatement("UPDATE User SET LoginNumber='"+newNumLogins+"' WHERE User_ID='"+email+"'");
      stat.executeUpdate();
      loginUpdated=true;
    } 
    catch(SQLException e){
      System.out.println("Error while updating the number of logins from the mySql Database");
      e.printStackTrace();
    }
    
    return loginUpdated;
  }
  
  
  
  //Verify whether the user already exists in the database
  public JSONObject userExists(String email){
  JSONObject userExists=new JSONObject(); 
  boolean exists=false;
    
    try{
      PreparedStatement stat = con.prepareStatement("SELECT User_ID FROM User WHERE User_ID='"+email+"'");
      ResultSet r = stat.executeQuery(); 
      if(r.next()) exists=true;
    } catch(SQLException e){
      System.out.println("Error while checking is user exists in the mySql Database");
      e.printStackTrace();
    }finally{
      try{
        userExists.put("userExists",exists);
      }catch(JSONException e){
        System.out.println("Error while converting userExists data to JSON Object");
        e.printStackTrace();
      }
    }
    return userExists;
  }
   
  //Verify the username and password sent by the client
  public JSONObject verifyLogin(String userName, String password){
    JSONObject loginVerified=new JSONObject(); 
    boolean loginExists=false;
    
    int numLogins=getNumLogins(userName);
    System.out.println(numLogins);
    
      try{
        PreparedStatement stat = con.prepareStatement("SELECT User_ID FROM User WHERE User_ID='"+userName+"' AND Password='"+password+"'");
        ResultSet r = stat.executeQuery(); 
        if(r.next()) loginExists=true;
      } catch(SQLException e){
        System.out.println("Error while verifying username and password from the mySql Database");
        e.printStackTrace();
      }finally{
        try{    
          if(loginExists==false){
            loginVerified.put("loginVerified",loginExists);
            loginVerified.put("reason","Username or Password is incorrect");
          }else
            if(numLogins<=0){       
            loginVerified.put("loginVerified",false);
            loginVerified.put("reason","Email not verified from the mail sent");
          }else{
            loginVerified.put("loginVerified",loginExists);
            updateLoginNumber(userName);
          }
        }catch(JSONException e){
          System.out.println("Error while converting verifyLogin data to JSON Object");
          e.printStackTrace();
        }
      }
    
    return loginVerified;
  }
  
  //Get the course ID associated with a course in the database
  public int getCourseID(String course){
    int courseID=-1;
    String courseIdData="";
    try{
      PreparedStatement stat = con.prepareStatement("SELECT * FROM purduesubjects WHERE Course='"+course+"'");
      ResultSet r = stat.executeQuery();
      while(r.next()){
        courseIdData+=r.getString(1);
      }
      courseID=Integer.parseInt(courseIdData);
    } catch(SQLException e){
      System.out.println("Error while getting the course id from the mySql Database");
      e.printStackTrace();
    } catch(NumberFormatException e){
    System.out.println("Error in course while getting course id from the mySql Database");
    e.printStackTrace();
    }
    return courseID;
  }
  
  //Add a new textbook to the database
  public JSONObject addTextbook(String sellerEmail,String title,String author,String isbn,String date,String price,String course){
    JSONObject textbookAdded=new JSONObject();
    boolean added=false;
    int courseID=getCourseID(course);
    
    try{
      PreparedStatement stat = con.prepareStatement("INSERT INTO Ad (SellerEmail, Title, Author, ISBN, Date,Price,Course,Course_ID) VALUES (?,?,?,?,?,?,?,?)");
      stat.setString(1,sellerEmail);
      stat.setString(2,title);
      stat.setString(3,author);
      stat.setString(4,isbn);
      stat.setString(5,date);
      stat.setString(6,price);
      stat.setString(7,course);
      stat.setInt(8,courseID);
      stat.executeUpdate();
      added=true;
    } 
    catch(SQLException e){
      System.out.println("Error while adding a new textbook to the mySql Database");
      e.printStackTrace();
    }finally{
      try{
        textbookAdded.put("textbookAdded",added);
      }catch(JSONException e){
        System.out.println("Error while converting addTextbook data to JSON Object");
        e.printStackTrace();
      }
    }
    return textbookAdded;
  }
  
  //Get textbook title, authors and price for a particular courses 
  public JSONObject getTextbooksForCourse(String course){
    System.out.println(course);
    JSONObject textbooksForCourse=new JSONObject();
    HashMap<String,String> textbook=new HashMap <String,String>();
    int n=1;
    
    try{
      PreparedStatement stat = con.prepareStatement("SELECT Title,Author,Price FROM Ad WHERE Course='"+course+"'");
      ResultSet r=stat.executeQuery();
      
      while(r.next()){
        textbook.put("title",r.getString(1));
        textbook.put("author",r.getString(2));
        textbook.put("price",r.getString(3));      
        textbooksForCourse.put(Integer.toString(n++),textbook);
      } 
      textbooksForCourse.put("NUM",--n);
    } 
    catch(SQLException e){
      System.out.println("Error while getting textbooks for a particular course from mySql Database");
      e.printStackTrace();
    }catch(JSONException e){
      System.out.println("Error while converting the getTextbooksForCourse data to JSON format");
      e.printStackTrace();
    }
    return textbooksForCourse;
  }
  
  
  //Get textbook data with the textbook id
  public JSONObject getTextbookData(String id){
    JSONObject textbookData=new JSONObject();
    
    try{
      PreparedStatement stat = con.prepareStatement("SELECT * FROM Ad WHERE UniqueID='"+id+"'");
      ResultSet r=stat.executeQuery();
      while(r.next()){
        textbookData.put("email",r.getString(1));
        textbookData.put("title",r.getString(2));
        textbookData.put("author",r.getString(3));
        textbookData.put("isbn",r.getString(4));
        textbookData.put("price",r.getString(5));
        textbookData.put("course",r.getString(6));
        textbookData.put("date",r.getString(7));
        textbookData.put("id",id);
      }
    JSONObject userDetails=getUserDetails(textbookData.getString("email"));
    textbookData.put("name",userDetails.get("name"));
    textbookData.put("phone",userDetails.get("phone"));
    } 
    catch(SQLException e){
      System.out.println("Error while getting textbooks for a particular course from mySql Database");
      e.printStackTrace();
    }catch(JSONException e){
      System.out.println("Error while converting the getTextbookData data to JSON format");
      e.printStackTrace();
    } 
    return textbookData;
  }
  
  //Get all the textbooks for a user from email
  public JSONObject getTextbooksForUser(String email){
    JSONObject textbooksForUser=new JSONObject();
    HashMap<String,String> textbook=new HashMap <String,String>();
    int n=1;
    
    try{
      PreparedStatement stat = con.prepareStatement("SELECT Title,Author,Price,UniqueID FROM Ad WHERE SellerEmail='"+email+"'");
      ResultSet r=stat.executeQuery();
      while(r.next()){
        textbook.put("title",r.getString(1));
        textbook.put("author",r.getString(2));
        textbook.put("price",r.getString(3));
        textbook.put("id",""+r.getInt(4));
        textbooksForUser.put(Integer.toString(n++),textbook);
      }
       textbooksForUser.put("NUM",--n);
    } 
    catch(SQLException e){
      System.out.println("Error while getting textbooks for a particular user from mySql Database");
      e.printStackTrace();
    }catch(JSONException e){
      System.out.println("Error while converting the getTextbooksForUser data to JSON format");
      e.printStackTrace();
    }
    
    return textbooksForUser;
  }
  
  //Delete the textbook from mysql database
  public JSONObject deleteTextbook(String id){
   JSONObject textbookDeleted=new JSONObject();
    boolean deleted=false;
    
    try{
      PreparedStatement stat = con.prepareStatement("DELETE FROM Ad WHERE UniqueID='"+id+"'");
      stat.executeUpdate();
      deleted=true;
    } 
    catch(SQLException e){
      System.out.println("Error while deleting the textbook from the mySql Database");
      e.printStackTrace();
    }finally{
      try{
        textbookDeleted.put("textbookDeleted",deleted);
      }catch(JSONException e){
        System.out.println("Error while converting deleteTextbook data to JSON Object");
        e.printStackTrace();
      }
    }
    return textbookDeleted;
  }

  //Get all the contact details of a user from email address
  public JSONObject getUserDetails(String email){
    JSONObject userDetails=new JSONObject();
    
    try{
      PreparedStatement stat = con.prepareStatement("SELECT * FROM User WHERE User_ID='"+email+"'");
      ResultSet r = stat.executeQuery(); 
      while(r.next()){
        userDetails.put("email",r.getString(1));
        userDetails.put("password",r.getString(2));
        userDetails.put("name",r.getString(3));
        userDetails.put("phone",r.getString(4));
      }
    } catch(SQLException e){
      System.out.println("Error while getting the user details from the mySql Database");
      e.printStackTrace();
    }catch(JSONException e){
      System.out.println("Error while converting the getUserDetails data to JSON format");
      e.printStackTrace();
    }
    
  return userDetails; 
  }
  
  //Chat Client: Add message to the sql-database
  public JSONObject addMessage(String fromEmail, String toEmail,String message){
    JSONObject messageAdded=new JSONObject();
    boolean added=false;
    
    try{
      PreparedStatement stat = con.prepareStatement("INSERT INTO Messages (FromUser,ToUser,Message) VALUES (?,?,?)");
      stat.setString(1, fromEmail);
      stat.setString(2, toEmail);
      stat.setString(3, message);
      stat.executeUpdate();
      added=true;
    } 
    catch(SQLException e){
      System.out.println("Error while adding the chat message to the mySql Database");
      e.printStackTrace();
    }finally{
      try{
        messageAdded.put("messageAdded",added);
      }catch(JSONException e){
        System.out.println("Error while converting addMessage data to JSON Object");
        e.printStackTrace();
      }
    }
    return messageAdded;
  }
  
  //Get all the messages to a particular user
  public JSONObject getMessage(String toEmail){
    JSONObject messages=new JSONObject();
    HashMap <String,String> message=new HashMap<String,String>();
    int n=1;
    
    try{
      PreparedStatement stat = con.prepareStatement("SELECT FromUser,Message FROM Messages WHERE ToUser='"+toEmail+"'");
      ResultSet r = stat.executeQuery(); 
      while(r.next()){
        message.put("From",r.getString(1));
        message.put("Message",r.getString(2));
        
        messages.put(Integer.toString(n++),message);                      
      }
      messages.put("NUM",--n);
    } 
    catch(SQLException e){
      System.out.println("Error while adding the chat message to the mySql Database");
      e.printStackTrace();
    }catch(JSONException e){
      System.out.println("Error while converting the sendMessage data to JSON format");
      e.printStackTrace();
    }
    System.out.println(messages.toString());
    
    deleteAllMessagesToEmail(toEmail);
    return messages;
  }

  //Delete all the messages related to a particular email
  private void deleteAllMessagesToEmail(String toEmail){
   try{
      PreparedStatement stat = con.prepareStatement("DELETE FROM Messages WHERE ToUser='"+toEmail+"'");
      stat.executeUpdate();
    } 
    catch(SQLException e){
      System.out.println("Error while deleting the chat message from the mySql Database");
      e.printStackTrace();
    }
  }
  
  
  
}