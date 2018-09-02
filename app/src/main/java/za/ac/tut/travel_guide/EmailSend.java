package za.ac.tut.travel_guide;


import java.sql.Connection;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class EmailSend extends Object{

    private static boolean flag = false;
    public static boolean verifyEmail(String recieptient, String OTP, String firstname, Connection connect)
    {

        try{

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); // for gmail use smtp.gmail.com
            props.put("mail.smtp.auth", "true");
            props.put("mail.debug", "true"); 
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");

            Session mailSession = Session.getInstance(props, new javax.mail.Authenticator() {

                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("touristtravelguide@gmail.com", "touristtravelguide12345");
                }
            });

            mailSession.setDebug(true); // Enable the debug mode

            Message msg = new MimeMessage( mailSession );

            //--[ Set the FROM, TO, DATE and SUBJECT fields
            msg.setFrom( new InternetAddress( "touristtravelguide@gmail.com" ) );
            msg.setRecipients( Message.RecipientType.TO, InternetAddress.parse(recieptient) );
            msg.setSentDate( new Date());
            msg.setSubject( "Account Verification" );

            //--[ Create the body of the mail
            msg.setText( "Dear " + firstname +"\n" +
            "\n" +
            "\n" +

            "Here is your one time pin: "+ OTP + "\n"
            + "\n"
            + "\n"
            + "Kind Regards\n"
            + "Tourist Travel Guide Team");

            //--[ Ask the Transport class to send our mail message
            Transport.send( msg );
            flag = true;
   
        }catch(MessagingException E){
            //JOptionPane.showMessageDialog(null, "Unable to send email. Check if there's internet connection");
            System.out.println( E );
            flag = false;
            
        }
        
        return flag;
    }
    
    
}