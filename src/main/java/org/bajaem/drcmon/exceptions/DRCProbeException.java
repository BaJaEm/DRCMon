
package org.bajaem.drcmon.exceptions;

/**
 * Checked exception used by Probes to indicate failure but should not prevent
 * continued processing.
 *
 */
public class DRCProbeException extends Exception
{

    private static final long serialVersionUID = -6580782260001088119L;

    public DRCProbeException()
    {
        super();
    }

    public DRCProbeException(final String message)
    {
        super(message);
    }

    public DRCProbeException(final Throwable cause)
    {
        super(cause);
    }

    public DRCProbeException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

}
