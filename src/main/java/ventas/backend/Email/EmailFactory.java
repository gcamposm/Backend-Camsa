package ventas.backend.Email;



public class EmailFactory {

    public Runnable getEmailSender(String type, String emailTo, Report report){
        if(type.equals("report")){
            EmailSender emailSender = new EmailSender(emailTo);
            emailSender.setReport(report);
            return emailSender;
        }
        else if(type.equals("statistics")){
            EmailSenderPaymentAmounth emailSender = new EmailSenderPaymentAmounth(emailTo);
            return emailSender;
        }
        return null;
    }
}
