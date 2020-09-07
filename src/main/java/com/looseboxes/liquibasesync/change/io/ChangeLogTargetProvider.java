package com.looseboxes.liquibasesync.change.io;

import com.looseboxes.liquibasesync.change.ChangeLogNode;

/**
 * @author hp
 */
public interface ChangeLogTargetProvider {
    
    ChangeLogTarget get(ChangeLogNode node);
}
