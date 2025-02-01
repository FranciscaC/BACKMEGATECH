package gt.com.megatech.service.advice;

import gt.com.megatech.service.exception.GraduatedStudentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GraduatedStudentNotFoundAdvice {

    @ExceptionHandler(
            GraduatedStudentNotFoundException.class
    )
    @ResponseStatus(
            HttpStatus.NOT_FOUND
    )
    public String graduatedStudentNotFoundHandler(
            GraduatedStudentNotFoundException graduatedStudentNotFoundException
    ) {
        return graduatedStudentNotFoundException.getMessage();
    }
}
