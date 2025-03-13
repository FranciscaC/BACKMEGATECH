package gt.com.megatech.service.advice;

import gt.com.megatech.service.exception.PaymentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PaymentNotFoundAdvice {

    @ExceptionHandler(
            PaymentNotFoundException.class
    )
    @ResponseStatus(
            HttpStatus.NOT_FOUND
    )
    public String paymentNotFoundHandler(
            PaymentNotFoundException paymentNotFoundException
    ) {
        return paymentNotFoundException.getMessage();
    }
}
