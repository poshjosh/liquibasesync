package com.looseboxes.liquibasesync.change;

import com.looseboxes.liquibasesync.change.io.ChangeLogTarget;
import com.looseboxes.liquibasesync.change.result.ChangeResult;

/**
 * @author hp
 */
public interface ChangeLogNodeProcessor<NODE_SOURCE> {

    ChangeResult process(
            ChangeLog<NODE_SOURCE> changeLog, 
            ChangeLogNode<NODE_SOURCE> node, 
            ChangeLogTarget target);
}
