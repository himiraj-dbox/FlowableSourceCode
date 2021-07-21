package utilityclasses;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class BiddingUtility {

	public static void SendEmail(final String toEmail,final String subjectLine,final String messageBody){
		  final BiddingUtility ste = new BiddingUtility();
	        Runtime.getRuntime().addShutdownHook(new Thread() {
	            public void run() {
	                ste.shutdowh();
	            }
	        });
	        ste.startScheduleTask(  toEmail,  subjectLine,  messageBody);

		
	}
	
	private final ScheduledExecutorService scheduler = Executors
	        .newScheduledThreadPool(1);

	    public void startScheduleTask(final String toEmail,final String subjectLine,final String messageBody) {
	    /**
	    * not using the taskHandle returned here, but it can be used to cancel
	    * the task, or check if it's done (for recurring tasks, that's not
	    * going to be very useful)
	    */
	    final ScheduledFuture<?> taskHandle = scheduler.scheduleAtFixedRate(
	        new Runnable() {
	            public void run() {
	                try {
	                    getDataFromDatabase(toEmail,subjectLine,messageBody);
	                }catch(Exception ex) {
	                    ex.printStackTrace(); //or loggger would be better
	                }
	            }
	        }, 0, 15, TimeUnit.MINUTES);
	    }

	    private void getDataFromDatabase(String toEmail, String subjectLine, String messageBody) {
	    	Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");

			Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("gamerboy1001@gmail.com","xxxxxxxxxxxxxxxxx");
					}
				});

			try {

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("gamerboy1001@gmail.com"));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(toEmail));
				message.setSubject(subjectLine);
				message.setText(messageBody);

				Transport.send(message);

				//System.out.println("Done");

			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}
	    }

	    public void shutdowh() {
	        System.out.println("shutdown...");
	        if(scheduler != null) {
	            scheduler.shutdown();
	        }
	    }

	  
	
}
