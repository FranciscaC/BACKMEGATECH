package gt.com.megatech.service.exception;

public class ProfessorNotFoundException extends RuntimeException {

    public ProfessorNotFoundException(Long id) {
        super("Could not find professor " + id);
    }
}
