package gt.com.megatech.service.advice;

import gt.com.megatech.service.exception.StudentNotFoundException;
import gt.com.megatech.service.exception.SuspendedStudentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SuspendedStudentNotFoundAdvice {

    @ExceptionHandler(
            SuspendedStudentNotFoundException.class
    )
    @ResponseStatus(
            HttpStatus.NOT_FOUND
    )
    public String suspendedStudentNotFoundHandler(
            SuspendedStudentNotFoundException suspendedStudentNotFoundException
    ) {
        return suspendedStudentNotFoundException.getMessage();
    }
}
