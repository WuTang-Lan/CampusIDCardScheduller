package com.riara.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailService {

    // 🔐 CONFIGURE THESE BEFORE RUNNING
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;
    private static final String SMTP_USERNAME = "your-university@gmail.com";  // 👈 Replace with your email
    private static final String SMTP_PASSWORD = "your-app-password";         // 👈 Use Gmail App Password

    /**
     * Sends an email notification to a student when their ID card is ready.
     *
     * @param toEmail    Student's email address
     * @param fullName   Student's full name
     */
    public static void sendIdReadyEmail(String toEmail, String fullName) {
        // Set up mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS

        // Create session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        });

        try {
            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_USERNAME, "Riara University Admin"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("✅ Your ID Card is Ready for Pickup!");

            String emailBody = "Dear " + fullName + ",\n\n" +
                    "Great news! Your student ID card has been processed and is now ready for pickup.\n\n" +
                    "📍 Please visit the Admin Office during working hours.\n" +
                    "📄 Bring your registration number for verification.\n\n" +
                    "Thank you for using the Campus ID Scheduler!\n\n" +
                    "Best regards,\n" +
                    "Riara University Administration Team";

            message.setText(emailBody);

            // Send email
            Transport.send(message);
            System.out.println("📧 Email successfully sent to: " + toEmail);

        } catch (Exception e) {
            System.err.println("❌ Failed to send email to: " + toEmail);
            e.printStackTrace();
        }
    }
}