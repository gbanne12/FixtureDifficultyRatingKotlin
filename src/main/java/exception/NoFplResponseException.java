package exception;

import java.io.IOException;

public class NoFplResponseException extends IOException {

    public NoFplResponseException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
