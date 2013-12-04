//Main server class which starts running the server

import java.net.*;
import java.io.*;

public class Server{
  private static final int DEFAULTPORT=8080;
  private final int port; 
  private ServerSocket serverSocket;
  
  public Server(){
    this(Server.DEFAULTPORT);
  }
  
  private Server(int port){
    this.port=port;
    try{
      this.serverSocket=new ServerSocket(port);
    }catch(IOException e){
      System.out.println("Error while establishing server at port number:"+this.port);
    }
  }
  
  public static void main(String []args){
    Server myServer=new Server();
    myServer.startServer(); 
  }
  
  
  public void startServer(){
    while(true){
      try{
        Socket incoming=serverSocket.accept();
        Request handleRequest=new Request(incoming);
        Thread t=new Thread(handleRequest);
        t.start();
      }catch(IOException e){
        System.out.println("Error while accepting Requests");
        e.printStackTrace();
      }
    }
  }
  
} 
