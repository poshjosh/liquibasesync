package com.looseboxes.liquibasesync.change;

/**
 * @author hp
 */
public interface ChangeLogProvider<CHANGE_LOG_SOURCE, NODE_SOURCE> {
    
    ChangeLog<NODE_SOURCE> get(CHANGE_LOG_SOURCE source);
}
