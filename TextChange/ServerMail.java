//Sending emails to user 

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ServerMail {
  private Properties props;
  private Session session;
  private Message message;
  
  private static final String USERNAME="neeraj.abhi10@gmail.com";
  private static final String PASSWORD="perfectdude17";
  
  public ServerMail(){
    props=new Properties();
  }
  
  
  public void setMailProperties(){
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.socketFactory.port", "465");
    props.put("mail.smtp.socketFactory.class",
              "javax.net.ssl.SSLSocketFactory");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.port", "465");  
  }
  
  public void setSession(){
    this.session = Session.getDefaultInstance(props,
                                              new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(ServerMail.USERNAME,ServerMail.PASSWORD);
      }
    });
  }
  
  public void setMessageForNewAccount(String recipientEmail,String subject,String messageBody){
    try {
      
      this.message = new MimeMessage(this.session);
      message.setFrom(new InternetAddress(ServerMail.USERNAME));
      message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(recipientEmail));
      message.setSubject(subject);
      message.setText(messageBody);
      
    } catch (MessagingException e) {
      System.out.println("Error while creating a mail for new account");
      e.printStackTrace();
    }
  }
  
  public void sendMail(){
    try{
      Transport.send(message);
      System.out.println("Done");
    }catch(MessagingException e){
      System.out.println("Error while sending a new Mail");
      e.printStackTrace();
    }
  }
  
  public void sendMail(String toEmail,String subject, String message){
    this.setMailProperties();
    this.setSession();
    this.setMessageForNewAccount(toEmail,subject,message);
    this.sendMail();
  }
  
}
