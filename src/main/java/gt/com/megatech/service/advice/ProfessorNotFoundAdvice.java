package gt.com.megatech.service.advice;

import gt.com.megatech.service.exception.ProfessorNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProfessorNotFoundAdvice {

    @ExceptionHandler(
            ProfessorNotFoundException.class
    )
    @ResponseStatus(
            HttpStatus.NOT_FOUND
    )
    public String professorNotFoundHandler(
            ProfessorNotFoundException professorNotFoundException
    ) {
        return professorNotFoundException.getMessage();
    }
}
