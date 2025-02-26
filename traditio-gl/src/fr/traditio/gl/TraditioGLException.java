package fr.traditio.gl;

/**
 * <code>TraditioGLException</code> is supplied to make exception handling more
 * generic for TraditioGL specific exceptions.
 * 
 * @author GnosticOccultist
 */
public class TraditioGLException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new <code>TraditioGLException</code> with {@code null} as its
	 * detail message. The cause is not initialized, and may subsequently be
	 * initialized by a call to {@link #initCause}.
	 */
	public TraditioGLException() {
		super();
	}

	/**
	 * Constructs a new <code>TraditioGLException</code> with the specified detail
	 * message. The cause is not initialized, and may subsequently be initialized by
	 * a call to {@link #initCause}.
	 *
	 * @param message The detail message. The detail message is saved for later
	 *                retrieval by the {@link #getMessage()} method.
	 */
	public TraditioGLException(String message) {
		super(message);
	}

	/**
	 * Constructs a new <code>TraditioGLException</code> with the specified cause
	 * and a detail message of {@code (cause==null ? null : cause.toString())}
	 * (which typically contains the class and detail message of {@code cause}).
	 * This constructor is useful for exceptions that are little more than wrappers
	 * for other throwables (for example, {@link PrivilegedActionException}).
	 *
	 * @param cause The cause (which is saved for later retrieval by the
	 *              {@link #getCause()} method). (A {@code null} value is permitted,
	 *              and indicates that the cause is nonexistent or unknown).
	 */
	public TraditioGLException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new <code>TraditioGLException</code> with the specified detail
	 * message and cause.
	 * <p>
	 * Note that the detail message associated with {@code cause} is <i>not</i>
	 * automatically incorporated in this exception's detail message.
	 *
	 * @param message The detail message (which is saved for later retrieval by the
	 *                {@link #getMessage()} method).
	 * @param cause   The cause (which is saved for later retrieval by the
	 *                {@link #getCause()} method). (A {@code null} value is
	 *                permitted, and indicates that the cause is nonexistent or
	 *                unknown).
	 */
	public TraditioGLException(String message, Throwable cause) {
		super(message, cause);
	}
}
