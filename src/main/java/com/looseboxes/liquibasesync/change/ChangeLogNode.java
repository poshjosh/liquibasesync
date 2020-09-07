package com.looseboxes.liquibasesync.change;

import java.util.List;

/**
 * @author hp
 */
public interface ChangeLogNode<S> {
    
    String getNodeName();

    String getTableName();

    List<String> getColumnNames();
}
