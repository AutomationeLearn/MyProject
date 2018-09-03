package driverScripts;


import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.search.*;
import javax.activation.*;

import org.testng.Reporter;





public class Mail {

    public static boolean readMail(String mailSearchString) {
    	if (mailSearchString.contentEquals("")){
    		Reporter.log("Mail subject is not provided");
    		return false;
    	}
    	Properties props = System.getProperties();
    	props.setProperty("mail.store.protocol", "imaps");

        // SUBSTITUTE YOUR ISP's POP3 SERVER HERE!!!
        String host = "imap.googlemail.com";
        // SUBSTITUTE YOUR USERNAME AND PASSWORD TO ACCESS E-MAIL HERE!!!
        String user = "seleniumatfaichi@gmail.com";
        String password = "faichi$1234";
        // SUBSTITUTE YOUR SUBJECT SUBSTRING TO SEARCH HERE!!!
        //String subjectSubstringToSearch = "Getting started on Google+";

        // Get a session.  Use a blank Properties object.
        Session session = Session.getInstance(new Properties());

        try {

            // Get a Store object
            Store store = session.getStore("imaps");
            store.connect(host, user, password);

            // Get "INBOX"
            Folder fldr = store.getFolder("INBOX");
            fldr.open(Folder.READ_WRITE);
            int count = fldr.getMessageCount();
            System.out.println(count  + " total messages");

            // Message numebers start at 1
          /*  for(int i = 1; i <= count; i++) {
								// Get  a message by its sequence number
                Message m = fldr.getMessage(i);

                // Get some headers
                Date date = m.getSentDate();
                Address [] from = m.getFrom();
                String subj = m.getSubject();
                String mimeType = m.getContentType();
                System.out.println(date + "\t" + from[0] + "\t" +
                                    subj + "\t" + mimeType);
            }*/

            // Search for e-mails by some subject substring
            String pattern = mailSearchString;
            SubjectTerm st = new SubjectTerm(pattern);
            // Get some message references
            Message [] found = fldr.search(st);

            System.out.println(found.length +
                                " messages matched Subject pattern \"" +
                                pattern + "\"");
            if (found.length==0){
            	Reporter.log ("Email with subject "+ mailSearchString +" is not recieved");
                return false;	
            }
            
            for (int i = 0; i < found.length; i++) {
                Message m = found[i];
                // Get some headers
                Date date = m.getSentDate();
                Address [] from = m.getFrom();
                String subj = m.getSubject();
                String mimeType = m.getContentType();
                System.out.println(date + "\t" + from[0] + "\t" +
                                    subj + "\t" + mimeType);

                Object o = m.getContent();

                

                if (o instanceof String) {
                    System.out.println("**This is a String Message**");
                    System.out.println((String)o);
                }
                else if (o instanceof Multipart) {
                    System.out.print("**This is a Multipart Message.  ");
                    Multipart mp = (Multipart)o;
                    int count3 = mp.getCount();
                    System.out.println("It has " + count3 +
                        " BodyParts in it**");
                    for (int j = 0; j < count3; j++) {
                        // Part are numbered starting at 0
                        BodyPart b = mp.getBodyPart(j);
                       // String mimeType2 = b.getContentType();
                        System.out.println( "BodyPart " + (j + 1) +
                                            " is of MimeType " + mimeType);

                        Object o2 = b.getContent();
                        if (o2 instanceof String) {
                            System.out.println("**This is a String BodyPart**");
                            System.out.println((String)o2);
                        }
                        else if (o2 instanceof Multipart) {
                            System.out.print(
                                "**This BodyPart is a nested Multipart.  ");
                            Multipart mp2 = (Multipart)o2;
                            int count2 = mp2.getCount();
                            System.out.println("It has " + count2 +
                                "further BodyParts in it**");
                        }
                        else if (o2 instanceof InputStream) {
                            System.out.println(
                                "**This is an InputStream BodyPart**");
                        }
                    } //End of for
                }
                else if (o instanceof InputStream) {
                    System.out.println("**This is an InputStream message**");
                    InputStream is = (InputStream)o;
                    // Assumes character content (not binary images)
                    int c;
                    while ((c = is.read()) != -1) {
                        System.out.write(c);
                    }
                    
                }

                // Uncomment to set "delete" flag on the message
                //m.setFlag(Flags.Flag.DELETED,true);
                m.setFlag(Flags.Flag.DELETED,true);
                Reporter.log("Found email message and archived it.");
                
            } //End of for

            // "true" actually deletes flagged messages from folder
            fldr.close(true);
            store.close();

        }
        catch (MessagingException mex) {
            // Prints all nested (chained) exceptions as well
            mex.printStackTrace();
        }
        catch (IOException ioex) {
            ioex.printStackTrace();
        }
		return true;

    }
    
    
    private static final String SMTP_HOST_NAME = "smtp.gmail.com";
    private static final int SMTP_HOST_PORT = 465;
    private static final String SMTP_AUTH_USER = "seleniumatfaichi@gmail.com"; // provide user
	private static final String SMTP_AUTH_PWD = "faichi$1234"; // provide password

    public static void sendMail(String replyTo, String toEmail,String subjectEmail,String bodyEmail,String pathToAttachmentEmail) throws Exception{

        Properties props = new Properties();
 
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", "smtp.googlemail.com");
        props.put("mail.smtps.auth", "true");
        // props.put("mail.smtps.quitwait", "false");
 
        Session mailSession = Session.getDefaultInstance(props);
        mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();
 
        MimeMessage message = new MimeMessage(mailSession);
        
        // Set From: header field of the header.
        message.setFrom(new InternetAddress(SMTP_AUTH_USER));
        
        // set To: recipents
        String [] mailRecipents = toEmail.split(",");
        for (int i=0;i<mailRecipents.length;i++)
        {
        message.addRecipient(Message.RecipientType.TO,
                new InternetAddress(mailRecipents[i]));
        }
       
        // set To: recipents
        String [] mailReplyTo = replyTo.split(",");
        InternetAddress[] replyToAddress = new InternetAddress[mailReplyTo.length];
        for (int i=0;i<mailReplyTo.length;i++)
        {
        		replyToAddress[i] = new InternetAddress(mailReplyTo[i]);
        }
        message.setReplyTo(replyToAddress);

        // Set Subject: header field
		message.setSubject(subjectEmail);
		
		// Create the message part 
		BodyPart messageBodyPart = new MimeBodyPart();
		
		// Fill the message
		messageBodyPart.setText(bodyEmail);
		
		// Create a multipart message
		Multipart multipart = new MimeMultipart();
		
		// Set text message part
		multipart.addBodyPart(messageBodyPart);
		
		// Part two is attachment
		messageBodyPart = new MimeBodyPart();
		//String [] filepath = pathToAttachmentEmail.split("\\\\");
		//String filename = filepath[filepath.length-1];
		String filename = "Test report.html";
		DataSource source = new FileDataSource(pathToAttachmentEmail);
		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(filename);
		multipart.addBodyPart(messageBodyPart);
		// Send the complete message parts
		message.setContent(multipart);
		        
        transport.connect
          (SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
 
        transport.sendMessage(message,
            message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }

} //End of class