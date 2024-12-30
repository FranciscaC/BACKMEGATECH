package gt.com.megatech.service.advice;

import gt.com.megatech.service.exception.EnrollmentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EnrollmentNotFoundAdvice {

    @ExceptionHandler(EnrollmentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String enrollmentNotFoundHandler(EnrollmentNotFoundException enrollmentNotFoundException) {
        return enrollmentNotFoundException.getMessage();
    }
}
