package ventas.backend.SQL.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ventas.backend.mail.MailSender;
import ventas.backend.mail.MailValidator;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/mail")
public class MailController {

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity sendMail (@RequestParam("name") String name , @RequestParam("mail") String mail, @RequestParam("msg") String msg){
        MailValidator mailValidator = MailValidator.getInstance();
        if(mailValidator.isValidEmailAddress("javier@arancam.cl")) {
            Runnable emailSender = new MailSender("contacto@arancam.cl", name, mail, msg);
            emailSender.run();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Mensaje enviado con exito.");
        } else{
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("La dirección de correo ingresada no es válida.");
        }
    }
}