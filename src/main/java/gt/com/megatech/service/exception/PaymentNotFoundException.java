package gt.com.megatech.service.exception;

public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException(Long id) {
        super("Could not find Payment " + id);
    }
}
