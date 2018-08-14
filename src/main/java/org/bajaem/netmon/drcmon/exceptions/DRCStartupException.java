package org.bajaem.netmon.drcmon.exceptions;

public class DRCStartupException extends RuntimeException
{

    /**
     * 
     */
    private static final long serialVersionUID = -2800984559004417693L;

    public DRCStartupException()
    {
        super();
    }

    public DRCStartupException(final String message)
    {
        super(message);
    }

    public DRCStartupException(final Throwable cause)
    {
        super(cause);
    }

    public DRCStartupException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

}
