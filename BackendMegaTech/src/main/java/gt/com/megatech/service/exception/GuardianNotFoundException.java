package gt.com.megatech.service.exception;

public class GuardianNotFoundException extends RuntimeException {

    public GuardianNotFoundException(Long id) {
        super("Could not find guardian " + id);
    }
}
