package ventas.backend.SQL.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class VoucherDto {
    private String guestName;
    //private List<VoucherPerRoomDto> voucherPerRoomDtoList;
    private BigDecimal total;
    private LocalDateTime timeStamp;
    private String dateTimeStamp;

    @Override
    public String toString() {
        return "VoucherDto{" +
                "guestName='" + guestName + '\'' +
                ", total=" + total +
                '}';
    }
}
