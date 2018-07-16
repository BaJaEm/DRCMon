package org.bajaem.netmon.drcmon.probe;

import java.util.HashMap;
import java.util.Map;

/**
 * Data returned from a Probe. The Response consists of a start time, end time,
 * boolean success or failure and optional key value pairs for the response, as
 * well as an error code ( i.e. Error Message or Java {@link Exception} )
 *
 */
public class ProbeResponse
{
    private final Map<String, String> dataMap;

    private final boolean success;

    private final String errorMessage;

    private final Exception error;

    public ProbeResponse(final Map<String, String> _dataMap, final boolean _success)
    {
        this(_dataMap, _success, null, null);
    }

    public ProbeResponse(final boolean _success)
    {
        this(new HashMap<>(), _success, null, null);
    }

    public ProbeResponse(final boolean _success, final String _errorMessage, final Exception _error)
    {
        this(new HashMap<>(), _success, _errorMessage, _error);
    }

    /**
     * Construct a new ProbeResponse object
     * 
     * @param _dataMap
     *            arbitrary key value pairs.
     * @param _success
     *            true if the probe was successful, false otherwise
     * @param _errorMessage
     *            error message returned by probe.
     * @param _error
     *            {@link Exception} that was thrown by probe.
     */
    public ProbeResponse(final Map<String, String> _dataMap, final boolean _success, final String _errorMessage,
            final Exception _error)
    {
        dataMap = _dataMap;
        success = _success;
        errorMessage = _errorMessage;
        error = _error;
    }

    public Map<String, String> getDataMap()
    {
        return dataMap;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public Exception getError()
    {
        return error;
    }

    public String toString()
    {
        return ("Success: " + success);
    }
}
