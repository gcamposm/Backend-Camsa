package ventas.backend.SQL.dao;

import ventas.backend.SQL.models.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodDao extends JpaRepository<PaymentMethod, Integer> {
    PaymentMethod findPaymentMethodById(Integer id);
}