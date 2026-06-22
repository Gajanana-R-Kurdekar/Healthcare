package com.hms.util;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtil {

    // SMTP Configuration Constants (Editable by user or read from Environment/System properties)
    private static final String SMTP_HOST = System.getProperty("smtp.host", "smtp.gmail.com");
    private static final String SMTP_PORT = System.getProperty("smtp.port", "587");
    
    // REPLACE THESE WITH YOUR SMTP CREDENTIALS OR SET THEM AS ENVIRONMENT/SYSTEM PROPERTIES
    private static final String SMTP_USERNAME = System.getProperty("smtp.username", "finalprojectteam74@gmail.com");
    private static final String SMTP_PASSWORD = System.getProperty("smtp.password", "fbgz oqmp ctxs nylj");
    private static final String SENDER_EMAIL = System.getProperty("smtp.sender", SMTP_USERNAME);

    // Thread pool for sending emails asynchronously to prevent UI freeze
    private static final ExecutorService emailExecutor = Executors.newFixedThreadPool(5);

    /**
     * Send an email asynchronously.
     */
    public static void sendEmailAsync(final String recipient, final String subject, final String htmlContent) {
        emailExecutor.submit(new Runnable() {
            @Override
            public void run() {
                sendEmailSync(recipient, subject, htmlContent);
            }
        });
    }

    /**
     * Synchronous email sending implementation.
     */
    private static void sendEmailSync(String recipient, String subject, String htmlContent) {
        // Validation check for empty configurations to prevent crashes
        if (SMTP_USERNAME.equals("YOUR_EMAIL@gmail.com") || SMTP_PASSWORD.equals("YOUR_APP_PASSWORD")) {
            System.out.println("==================================================================");
            System.out.println("[EMAIL SIMULATION] SMTP Credentials not configured yet in EmailUtil.java!");
            System.out.println("To: " + recipient);
            System.out.println("Subject: " + subject);
            System.out.println("Content:\n" + htmlContent);
            System.out.println("==================================================================");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        try {
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("[EMAIL SUCCESS] Email sent to " + recipient + " successfully.");

        } catch (Exception e) {
            System.err.println("[EMAIL ERROR] Failed to send email to " + recipient);
            e.printStackTrace();
        }
    }

    /**
     * Sends an appointment confirmation email to the patient.
     */
    public static void sendAppointmentEmail(String recipientEmail, String patientName, String date, String doctorName, String diseases) {
        String subject = "Appointment Booked Successfully - MediCare";
        
        String htmlBody = "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; border: 1px solid #e0e0e0; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.05);\">"
                + "  <div style=\"background: linear-gradient(135deg, #1976D2, #0D47A1); color: white; padding: 20px; text-align: center;\">"
                + "    <h2 style=\"margin: 0; font-size: 24px;\">MediCare Appointment Confirmation</h2>"
                + "  </div>"
                + "  <div style=\"padding: 24px; color: #333333; line-height: 1.6;\">"
                + "    <p>Dear <strong>" + patientName + "</strong>,</p>"
                + "    <p>Thank you for choosing MediCare. Your appointment has been recorded successfully. Here are the details:</p>"
                + "    <table style=\"width: 100%; border-collapse: collapse; margin: 20px 0;\">"
                + "      <tr>"
                + "        <td style=\"padding: 8px 0; font-weight: bold; border-bottom: 1px solid #f0f0f0; width: 35%;\">Appointment Date:</td>"
                + "        <td style=\"padding: 8px 0; border-bottom: 1px solid #f0f0f0;\">" + date + "</td>"
                + "      </tr>"
                + "      <tr>"
                + "        <td style=\"padding: 8px 0; font-weight: bold; border-bottom: 1px solid #f0f0f0;\">Assigned Doctor:</td>"
                + "        <td style=\"padding: 8px 0; border-bottom: 1px solid #f0f0f0;\">Dr. " + doctorName + "</td>"
                + "      </tr>"
                + "      <tr>"
                + "        <td style=\"padding: 8px 0; font-weight: bold; border-bottom: 1px solid #f0f0f0;\">Reported Symptoms:</td>"
                + "        <td style=\"padding: 8px 0; border-bottom: 1px solid #f0f0f0;\">" + diseases + "</td>"
                + "      </tr>"
                + "      <tr>"
                + "        <td style=\"padding: 8px 0; font-weight: bold; border-bottom: 1px solid #f0f0f0;\">Status:</td>"
                + "        <td style=\"padding: 8px 0; border-bottom: 1px solid #f0f0f0;\"><span style=\"background-color: #ffeeb5; color: #856404; padding: 4px 8px; border-radius: 4px; font-size: 14px;\">Pending doctor review</span></td>"
                + "      </tr>"
                + "    </table>"
                + "    <p style=\"margin-top: 24px;\">If you need to change your appointment or have any questions, please contact our support team.</p>"
                + "    <p>Best regards,<br/><strong>MediCare Team</strong></p>"
                + "  </div>"
                + "  <div style=\"background-color: #f8f9fa; padding: 15px; text-align: center; font-size: 12px; color: #777777; border-top: 1px solid #e0e0e0;\">"
                + "    This is an automated message, please do not reply directly to this email."
                + "  </div>"
                + "</div>";

        sendEmailAsync(recipientEmail, subject, htmlBody);
    }

    /**
     * Sends the doctor's prescription details to the patient.
     */
    public static void sendPrescriptionEmail(String recipientEmail, String patientName, String doctorName, String prescriptionDetails) {
        String subject = "New Medical Prescription Available - MediCare";
        
        // Escape line breaks in the prescription text to format properly in HTML
        String formattedPrescription = prescriptionDetails.replace("\n", "<br/>");

        String htmlBody = "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; border: 1px solid #e0e0e0; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.05);\">"
                + "  <div style=\"background: linear-gradient(135deg, #2E7D32, #1B5E20); color: white; padding: 20px; text-align: center;\">"
                + "    <h2 style=\"margin: 0; font-size: 24px;\">MediCare Prescription</h2>"
                + "  </div>"
                + "  <div style=\"padding: 24px; color: #333333; line-height: 1.6;\">"
                + "    <p>Dear <strong>" + patientName + "</strong>,</p>"
                + "    <p>Dr. <strong>" + doctorName + "</strong> has reviewed your appointment and issued the following treatment details/prescription:</p>"
                + "    <div style=\"background-color: #f1f8e9; border-left: 4px solid #4caf50; padding: 15px; margin: 20px 0; font-family: Courier New, Courier, monospace; font-size: 15px; color: #1b5e20; border-radius: 0 4px 4px 0;\">"
                + "      " + formattedPrescription
                + "    </div>"
                + "    <p style=\"margin-top: 24px;\">Please follow the dosage instructions carefully. If you experience any side effects, reach out to the clinic immediately.</p>"
                + "    <p>Best regards,<br/><strong>MediCare Clinic</strong></p>"
                + "  </div>"
                + "  <div style=\"background-color: #f8f9fa; padding: 15px; text-align: center; font-size: 12px; color: #777777; border-top: 1px solid #e0e0e0;\">"
                + "    This is an automated message, please do not reply directly to this email."
                + "  </div>"
                + "</div>";

        sendEmailAsync(recipientEmail, subject, htmlBody);
    }
}
