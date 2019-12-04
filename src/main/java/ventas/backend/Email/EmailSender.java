package ventas.backend.Email;


import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@SuppressWarnings("Duplicates")
public class EmailSender implements Runnable {


    public EmailSender(String emailTo) {
        this.emailTo = emailTo;
    }
    private Report report;
    private JSONFileReader file = new JSONFileReader();
    private String emailTo;


    public void setReport(Report report) {
        this.report = report;
    }

    public String createVoucher(){
        String message = "<p>Reporte de caja N&ordm;: "+ report.getDeskId()+"</p>" +
                        "<p>Lugar f&iacute;sico:&nbsp; "+ report.getPhysicalDesk() +"</p>" +
                        "<p>Encargado de caja: &nbsp; "+ report.getUserName() +"</p>" +
                        "<p>Cantidad de items vendidos: "+report.getTotalItems()+"</p>" +
                        "<p><span style=\"text-decoration: underline;\">Ingresos esperados:</span></p>" +
                        "<ul>" +
                        "<li>Efectivo: &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; $"+report.getEfectivoEsperado()+"</li>" +
                        "<li>D&eacute;bito: &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; $"+report.getDebitoEsperado()+"</li>" +
                        "<li>Cr&eacute;dito: &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; $"+report.getCreditoEsperado()+"</li>" +
                        "<li>Transferencias: &nbsp; $"+report.getTransferenciaEsperado()+"</li>" +
                        "</ul>" +
                        "<p><span style=\"text-decoration: underline;\">Ingresos capturados:</span></p>" +
                        "<ul>" +
                        "<li>Efectivo:  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; $"+report.getEfectivoCapturado()+"</li>" +
                        "<li>D&eacute;bito: &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; $"+report.getDebitoCapturado()+"</li>" +
                        "<li>Cr&eacute;dito: &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; $"+report.getCreditoCapturado()+"</li>" +
                        "<li>Transferencias: &nbsp; $"+report.getTransferenciaCapturado()+"</li>" +
                        "</ul>" +
                        "<p><span style=\"text-decoration: underline;\">Diferencia:</span></p>" +
                        "<ul>" +
                        "<li>Efectivo:   &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; $"+report.getEfectivoDiferencia()+"</li>" +
                        "<li>D&eacute;bito: &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; $"+report.getDebitoDiferencia()+"</li>" +
                        "<li>Cr&eacute;dito: &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; $"+report.getCreditoDiferencia()+"</li>" +
                        "<li>Transferencias: &nbsp; $"+report.getTransferenciaDiferencia()+"</li>" +
                        "</ul>" +
                        "<p><span style=\"text-decoration: underline;\">Total:</span></p>" +
                        "<ul>" +
                        "<li>Total esperado:   &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; $"+report.getTotalEsperado()+"</li>" +
                        "</ul>";
        return message;

    }
    /*public String createTableVoucher(){
        String tableVoucher = "";
        if(voucherDto != null) {

            List<VoucherPerRoomDto> voucherPerRoomDtoList = voucherDto.getVoucherPerRoomDtoList();
            for (VoucherPerRoomDto voucherPerRoom : voucherPerRoomDtoList
            ) {
                tableVoucher = tableVoucher + "<tr>\n" +
                        "<td style=\"width: 373px;\">Habitacion: " + voucherPerRoom.getRoom_number() + "</td>\n" +
                        "<td style=\"width: 107.1875px;\">" + voucherPerRoom.getDaysNumber() + "</td>\n" +
                        "<td style=\"width: 107.1875px;\">dias</td>\n" +
                        "<td style=\"width: 103.8125px;\">&nbsp;" + voucherPerRoom.getRoomPrice() + "</td>\n" +
                        "<td style=\"width: 103.8125px;\">" + voucherPerRoom.getSub_total() + "</td>\n" +
                        "</tr>\n";

                List<VoucherPerServiceDto> voucherPerServiceDtoList = voucherPerRoom.getVoucherPerServiceDtoList();
                for (VoucherPerServiceDto voucherPerService: voucherPerServiceDtoList
                     ) {
                    tableVoucher = tableVoucher + "<tr>\n" +
                            "<td style=\"width: 373px;\">Habitacion: " + voucherPerService.getServiceName() + "</td>\n" +
                            "<td style=\"width: 107.1875px;\">1</td>\n" +
                            "<td style=\"width: 107.1875px;\">Unidad</td>\n" +
                            "<td style=\"width: 103.8125px;\">&nbsp;" + voucherPerService.getSub_total() + "</td>\n" +
                            "<td style=\"width: 103.8125px;\">" + voucherPerService.getSub_total() + "</td>\n" +
                            "</tr>\n";
                }
            }
        }
        return tableVoucher;

    }*/
    public void sendMail() throws AddressException, MessagingException, IOException {
        List<String> mailData = file.readEmailConfigJSON();
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.setProperty("mail.smtp.user", "estadisticas.ventas@gmail.com");
        props.setProperty("mail.smtp.password", "ventas123");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("estadisticas.ventas@gmail.com","ventas123");
            }
        });
        Message msg = new MimeMessage(session);
        //msg.setFrom(new InternetAddress(mailData.get(0), false));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
        msg.setSubject("Mail de confirmación");
        msg.setSentDate(new Date());
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(this.createVoucher(), "text/html");
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
