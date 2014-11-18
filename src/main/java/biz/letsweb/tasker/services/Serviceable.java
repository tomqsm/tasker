package biz.letsweb.tasker.services;

import java.sql.Connection;

/**
 *
 * @author toks
 * @param <T>
 */
public interface Serviceable <T> {

    /**
     *
     * @param con
     */
    void execute(Connection con);
    T getEntry();
    void setEntry(T entry);
}