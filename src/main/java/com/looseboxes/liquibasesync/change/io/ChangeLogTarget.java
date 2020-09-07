package com.looseboxes.liquibasesync.change.io;

import java.io.IOException;

/**
 * @author hp
 */
public interface ChangeLogTarget {

    String load() throws IOException;
    
    void save(String content) throws IOException;
}
