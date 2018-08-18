
package org.bajaem.drcmon.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bajaem.drcmon.exceptions.DRCProbeException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class JsonTools
{

    private static final Logger LOG = LogManager.getLogger();

    private JsonTools()
    {
        // This is a utility class and should not be instantiated
    }

    /**
     * return the end value of a for a json "object"
     *
     * Examples:
     *
     * <pre>
     * node = {
     *  'a' : { 'a1' : 'a1val' },
     *  'b' : [ 'b1', 'b2', 'b3'],
     *  'c' : 'cval'
     * }
     *
     * getValue(node, "a.a1" ) returns "a1val";
     * getValue(node, "b.0" ) returns "b1";
     * getValue(node, "c" ) returns "cval";
     * </pre>
     *
     * @param node
     *            root node to start
     * @param path
     *            a period separated String of the path to terminal value
     * @return the String value of the "path" in the json object.
     */
    public static String getValue(final JsonNode node, final String path) throws DRCProbeException
    {
        final String[] split = path.split("\\.");
        final JsonNode last = getNext(node, split, 0);
        return last.asText();
    }

    private static JsonNode getNext(final JsonNode node, final String[] path, final int counter)
            throws DRCProbeException
    {
        final int len = path.length;
        final String currPath = path[counter];
        final JsonNode current;
        if (node instanceof ArrayNode)
        {
            try
            {
                current = node.path(Integer.parseInt(currPath));
            }
            catch (final NumberFormatException e)
            {
                LOG.debug(e);
                throw new DRCProbeException(e);
            }
        }
        else
        {
            current = node.path(currPath);
        }

        if (counter == len - 1)
        {
            return current;
        }
        else
        {
            return (getNext(current, path, counter + 1));
        }
    }
}
