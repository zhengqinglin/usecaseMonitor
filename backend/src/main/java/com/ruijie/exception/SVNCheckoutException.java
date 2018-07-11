package com.ruijie.exception;

/**
 * Created by zhengqinglin on 2018/6/16.
 */
public class SVNCheckoutException  extends  Exception{
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public SVNCheckoutException(String msg,Throwable e) {
        super(msg,e);
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public SVNCheckoutException(String message) {
        super(message);
    }
}
