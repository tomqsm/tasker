package biz.letsweb.tasker;

import javax.sql.PooledConnection;

/**
 *
 * @author toks
 */
public interface PooledConnectionProduceable {

    /**
     * Delivers PooledConnection.
     * @return 
     */
    public PooledConnection getPooledConnection();
}
