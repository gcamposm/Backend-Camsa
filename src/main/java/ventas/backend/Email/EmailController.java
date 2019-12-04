package ventas.backend.Email;

import ventas.backend.Exceptions.EmailSenderException;
import ventas.backend.Exceptions.MongoSaveException;
import ventas.backend.Mongo.dao.DeskStatisticDao;
import ventas.backend.Mongo.models.Statistics.DeskStatistic;
import ventas.backend.SQL.dao.DeskDao;
import ventas.backend.SQL.dao.SaleDao;
import ventas.backend.SQL.dao.SaleDetailDao;
import ventas.backend.SQL.dto.SaleWithPaymentMethodEncapsulatedDto;
import ventas.backend.SQL.models.Desk;
import ventas.backend.SQL.models.Sale;
import ventas.backend.SQL.services.DeskService;
import ventas.backend.SQL.services.SalePaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@SuppressWarnings("Duplicates")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/email")
public class EmailController {


    //@Autowired
    //private ReservationService reservationService;
    @Autowired
    private DeskDao deskDao;

    @Autowired
    private SaleDetailDao saleDetailDao;

    @Autowired
    private SaleDao saleDao;

    @Autowired private DeskService deskService;

    @Autowired private DeskStatisticDao deskStatisticDao;

    @Autowired private SalePaymentMethodService salePaymentMethodService;

    @RequestMapping(value = "/send", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity sendVoucherEmail(@RequestParam("deskId") Integer id,
                                           @RequestParam("efectivoCapturado") int efectivoCapturado,
                                           @RequestParam("debitoCapturado") int debitoCapturado,
                                           @RequestParam("creditoCapturado") int creditoCapturado,
                                           @RequestParam("transferenciaCapturado") int transferenciaCapturado){
        try {
            EmailValidator emailValidator = EmailValidator.getInstance();
            //get un desk por id
            Desk desk = deskDao.findDeskById(id);
            if(desk == null){
                return ResponseEntity.badRequest().body("No existe una caja con esa id");
            }
            List<Sale> saleList = saleDao.findSaleByDeskAndFinalized(desk, true);
            Integer totalQuantity = 0;
            if(saleList.size() > 0) {
                for (Sale sale : saleList) {
                    totalQuantity = totalQuantity + sale.getTotalQuantity();
                }
            }
            Map<String, Integer> deskTotals = deskService.totalByDeskId(id);
            Report report = new Report();
            report.setDeskId(id);
            report.setTotalItems(totalQuantity);
            report.setPhysicalDesk(desk.getPhysicalDesk().getOffice()+ " " + desk.getPhysicalDesk().getDescription() );
            report.setUserName(desk.getUserventas().getUsername());
            report.setDineroInicial(BigDecimal.valueOf(desk.getInitialMoney()));
            report.setEfectivoEsperado(BigDecimal.valueOf(deskTotals.get("efectivo") + desk.getInitialMoney()));
            report.setDebitoEsperado(BigDecimal.valueOf(deskTotals.get("debito")));
            report.setCreditoEsperado(BigDecimal.valueOf(deskTotals.get("credito")));
            report.setTransferenciaEsperado(BigDecimal.valueOf(deskTotals.get("transfer")));
            report.setEfectivoCapturado(BigDecimal.valueOf(efectivoCapturado));
            report.setDebitoCapturado(BigDecimal.valueOf(debitoCapturado));
            report.setCreditoCapturado(BigDecimal.valueOf(creditoCapturado));
            report.setTransferenciaCapturado(BigDecimal.valueOf(transferenciaCapturado));
            report.calculateDifference();
            report.calculateTotal();

            try{
                DeskStatistic deskStatistic = new DeskStatistic(report);
                deskStatisticDao.save(deskStatistic);
            }catch (MongoSaveException ex){
                throw new MongoSaveException("Could not save the report, please try again!", ex);
            }
            boolean isEmailValid = emailValidator.isValidEmailAddress("jasobarzov@gmail.com");
            if (isEmailValid) {
                EmailFactory emailFactory = new EmailFactory();
                Runnable emailSender = emailFactory.getEmailSender("report", "jasobarzov@gmail.com", report);
                emailSender.run();

            } else {
                return ResponseEntity.badRequest().body("Invalid email address");
            }
        }catch (EmailSenderException e){
            throw new EmailSenderException("Can not send the email report, please try again", e);
        }
        return ResponseEntity.ok("Se ha enviado con exito");
    }
    @PostMapping("/sendDetailedStatistics/{deskId}")
    public ResponseEntity sendDetailedStatistics(@PathVariable("deskId") int deskId){
        try {
            EmailValidator emailValidator = EmailValidator.getInstance();
            List<SaleWithPaymentMethodEncapsulatedDto> saleWithPaymentMethodEncapsulatedDtoList =
                    salePaymentMethodService.getPaymentMethodForAllSalesInADesk(deskId);
            boolean isEmailValid = emailValidator.isValidEmailAddress("jasobarzov@gmail.com");
            if (isEmailValid) {
                EmailSenderPaymentAmounth emailSenderPaymentAmounth = new EmailSenderPaymentAmounth();
                emailSenderPaymentAmounth.setSaleWithPaymentMethodsDtos(saleWithPaymentMethodEncapsulatedDtoList);
                emailSenderPaymentAmounth.setEmailTo("jasobarzov@gmail.com");
                emailSenderPaymentAmounth.run();
            } else {
                return ResponseEntity.badRequest().body("Invalid email address");
            }
        }catch (EmailSenderException e){
            throw new EmailSenderException("Can not send the email report, please try again", e);
        }
        return ResponseEntity.ok("Se ha enviado con exito");
    }
}
