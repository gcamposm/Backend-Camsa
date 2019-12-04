package tingeso.backend.Email;

import tingeso.backend.SQL.dto.SaleWithPaymentMethodEncapsulatedDto;
import tingeso.backend.SQL.dto.SaleWithPaymentMethodsDto;
import tingeso.backend.SQL.models.Desk;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@SuppressWarnings("Duplicates")
@NoArgsConstructor
@Data
public class EmailSenderPaymentAmounth implements Runnable {

    public EmailSenderPaymentAmounth(String emailTo) {
        this.emailTo = emailTo;
    }
    private List<SaleWithPaymentMethodEncapsulatedDto> saleWithPaymentMethodEncapsulatedDtoList;
    private String emailTo;
    private Desk desk;

    public void setSaleWithPaymentMethodsDtos(List<SaleWithPaymentMethodEncapsulatedDto> saleWithPaymentMethodEncapsulatedDtoList){
        this.saleWithPaymentMethodEncapsulatedDtoList = saleWithPaymentMethodEncapsulatedDtoList;
    }



    public String createReport(){
        String headers = "<html>" +
        "<!-- ######## This is a comment, visible only in the source editor  ######## -->\n" +
                "<p><strong>Informe de ventas para la caja N&ordm;: "+ desk.getId() +"</strong></p>\n" +
                "<p><strong>Fecha de apertura: "+desk.getDateOpen()+"</strong></p>\n" +
                "<table style=\"height: 123px; width: 637px; vertical-align: top; border-style: inset;\"  border=1 frame=void>\n" +
                "<thead>\n" +
                    "<tr style=\"height: 23px;\">\n" +
                        "<td style=\"width: 126px; height: 10px; text-align: center;\">N&uacute;mero de ticket</td>\n" +
                        "<td style=\"width: 187px; height: 10px; text-align: center;\">Total de la venta</td>\n" +
                        "<td style=\"width: 142px; height: 10px; text-align: center;\">Tipo de pago</td>\n" +
                        "<td style=\"width: 182px; height: 10px; text-align: center;\">Monto del tipo de pago</td>\n" +
                    "</tr>\n" +
                "</thead>\n";

        String body = createBody();


        String finish =
                "</table>\n" +
            "<p></p>" + "</html>";
        return headers.concat(body.concat(finish));
    }
    public String createBody(){
        String top = "<tbody>\n" +
                "<tr style=\"height: 25px;\">\n";
        String finish = "</tr> \n" +
                "</tbody>"   ;
        String table = "";
        String totalBody = "";
        String body = "";
        for (SaleWithPaymentMethodEncapsulatedDto saleWithPaymentMethodEncapsulatedDto: saleWithPaymentMethodEncapsulatedDtoList
             ) {
            System.out.println("here");
            List<SaleWithPaymentMethodsDto> saleWithPaymentMethodsDtoList = saleWithPaymentMethodEncapsulatedDto.getSaleWithPaymentMethodsDtoList();
            int rowspan = saleWithPaymentMethodEncapsulatedDto.getSaleWithPaymentMethodsDtoList().size();
            body = "<th rowspan="+ rowspan +" style=\"height: 50px; width: 126px; text-align: center;\">"
                    + saleWithPaymentMethodEncapsulatedDto.getTicketNumber() + "</th>\n" +
                    "<th rowspan="+ rowspan +" style=\"width: 187px; height: 25px; text-align: center;\">" + saleWithPaymentMethodEncapsulatedDto.getTotalWithDiscount() + "</th>";
            table = "";
            for (SaleWithPaymentMethodsDto saleWithPaymentMethodDto: saleWithPaymentMethodsDtoList
                 ) {
                table = table.concat(createTable(saleWithPaymentMethodDto.getTotalWithDiscount(),
                        saleWithPaymentMethodDto.getType(),
                        saleWithPaymentMethodDto.getAmount()));
                table = table.concat( "<tr style=\"height: 25px;\">");

            }
            totalBody = totalBody.concat(body.concat(table));
            System.out.println(totalBody);
            System.out.println("--------------------------------------------");
            System.out.println("--------------------------------------------");
        }
        String completeBody = top.concat(totalBody.concat(finish));
        return completeBody;
    }
    public String createTable(BigDecimal totalVenta, String tipo, Integer monto){
        String table = "<td style=\"width: 142px; height: 25px; text-align: center;\">"+ tipo +"</td>\n" +
                "<td style=\"width: 182px; height: 25px; text-align: center;\">"+ monto +"</td>\n";
        return table;
    }




    public void sendMail() throws AddressException, MessagingException, IOException {
        //List<String> mailData = file.readEmailConfigJSON();
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
        messageBodyPart.setContent(this.createReport(), "text/html");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        msg.setContent(multipart);
        Transport.send(msg);
    }

    @Override
    public void run() {
        try {
            sendMail();
        }
        catch(Exception e) {
            System.out.println("Exception caught while sending message: " + e);
        }
    }
}
