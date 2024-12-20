package gt.com.megatech.service.advice;

import gt.com.megatech.service.exception.GuardianNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GuardianNotFoundAdvice {

    @ExceptionHandler(GuardianNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String guardianNotFoundHandler(GuardianNotFoundException guardianNotFoundException) {
        return guardianNotFoundException.getMessage();
    }
}
