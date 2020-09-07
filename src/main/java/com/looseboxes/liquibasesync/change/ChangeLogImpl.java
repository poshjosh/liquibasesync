package com.looseboxes.liquibasesync.change;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author hp
 */
public class ChangeLogImpl<NODE_SOURCE> implements ChangeLog<NODE_SOURCE>{
    
    private final List<ChangeLogNode<NODE_SOURCE>> changes;

    public ChangeLogImpl(ChangeLogNode<NODE_SOURCE>... changes) {
        this(Arrays.asList(changes));
    }
    
    public ChangeLogImpl(List<ChangeLogNode<NODE_SOURCE>> changes) {
        this.changes = Collections.unmodifiableList(new ArrayList(changes));
    }

    @Override
    public List<ChangeLogNode<NODE_SOURCE>> getChanges() {
        return changes;
    }
}
