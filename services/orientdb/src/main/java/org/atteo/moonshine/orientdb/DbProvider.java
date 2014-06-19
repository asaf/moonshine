package org.atteo.moonshine.orientdb;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentPool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;

/**
 * Provide a DB instance
 */
public class DbProvider {
    private String url;
    private String username;
    private String password;

    ODatabaseDocumentTx db;

    public DbProvider(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public ODatabaseDocumentTx get() {
        if (db == null)
            db = ODatabaseDocumentPool.global().acquire(url, username, password);
        return db;
    }

    public void closeIfNeeded() {
        if (db != null && !db.isClosed())
            db.close();
    }

    public boolean isOpen() {
        return db != null && !db.isClosed();
    }
}
