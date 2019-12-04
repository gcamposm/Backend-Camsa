package ventas.backend.Email;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class Report {
    private Integer deskId;
    private String userName;
    private String physicalDesk;
    private Integer totalItems;
    //Esperado
    private BigDecimal efectivoEsperado;
    private BigDecimal debitoEsperado;
    private BigDecimal creditoEsperado;
    private BigDecimal transferenciaEsperado;
    //Capturado
    private BigDecimal efectivoCapturado;
    private BigDecimal debitoCapturado;
    private BigDecimal creditoCapturado;
    private BigDecimal transferenciaCapturado;
    //Diferencia
    private BigDecimal efectivoDiferencia;
    private BigDecimal debitoDiferencia;
    private BigDecimal creditoDiferencia;
    private BigDecimal transferenciaDiferencia;

    //Otros
    private BigDecimal dineroInicial;
    private BigDecimal totalEsperado;

    public void calculateDifference(){
        efectivoDiferencia = efectivoCapturado.subtract(efectivoEsperado);
        debitoDiferencia = debitoCapturado.subtract(debitoEsperado);
        creditoDiferencia = creditoCapturado.subtract(creditoEsperado);
        transferenciaDiferencia = transferenciaCapturado.subtract(transferenciaEsperado);
    }
    public void calculateTotal(){
        totalEsperado = efectivoEsperado.add(debitoEsperado.add(creditoEsperado.add(transferenciaEsperado)));
        totalEsperado = totalEsperado.subtract(dineroInicial);

    }


}
