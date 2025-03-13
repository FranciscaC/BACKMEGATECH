package gt.com.megatech.service.exception;

public class SuspendedStudentNotFoundException extends RuntimeException {

    public SuspendedStudentNotFoundException(Long id) {
        super("Could not find suspended student " + id);
    }
}
