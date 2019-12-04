package ventas.backend.mail;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@SuppressWarnings("Duplicates")
public class MailSender implements Runnable {

    public MailSender(String mailTo, String name, String mail, String msg) {
        this.mailTo = mailTo;
        this.name = name;
        this.mail = mail;
        this.msg = msg;
    }
    private String mailTo;
    private String name;
    private String mail;
    private String msg;

    public String createMessage(){
        String message = "<h1 style=\"color: #5e9ca0;\">&nbsp;Cotizaci&oacute;n&nbsp;</h1>\n" +
                "<p>Nombre: "+ name+"</p>\n" +
                "<p>Correo: "+ mail+"</p>\n" +
                "<p>Comentario: "+ msg+"</p>\n";
        return message;
    }

    public void sendMail() throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.setProperty("mail.smtp.user", "javier@arancam.cl");
        props.setProperty("mail.smtp.password", "cordonroma0401");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("javier@arancam.cl","cordonroma0401");
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("smtp.gmail.com", false));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
        msg.setSubject("Cotización");
        msg.setSentDate(new Date());
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(createMessage(), "text/html");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        msg.setContent(multipart);
        Transport.send(msg);
    }

    public void run() {
        try {
            sendMail();
        }
        catch(Exception e) {
            System.out.println("Exception caught while sending message: " + e);
        }
    }
}