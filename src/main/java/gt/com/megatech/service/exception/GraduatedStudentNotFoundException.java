package gt.com.megatech.service.exception;

public class GraduatedStudentNotFoundException extends RuntimeException {

    public GraduatedStudentNotFoundException(Long id) {
        super("Could not find graduated student " + id);
    }
}
