package exception

import java.io.IOException

class NoFplResponseException(errorMessage: String?, error: Throwable) : IOException(errorMessage, error)
