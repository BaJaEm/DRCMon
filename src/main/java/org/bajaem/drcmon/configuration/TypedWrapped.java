
package org.bajaem.drcmon.configuration;

/**
 * Functional interface used to execute code.
 *
 */
public interface TypedWrapped<T>
{
    /**
     * Implement this method to execute code via lambda expression
     */
    public T execute();

}
