package com.looseboxes.liquibasesync.change;

import com.looseboxes.liquibasesync.change.io.ChangeLogTarget;
import com.looseboxes.liquibasesync.change.result.ChangeResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.looseboxes.liquibasesync.change.io.ChangeLogTargetProvider;

/**
 * @author hp
 */
public class ApplyChangeLog<NODE_SOURCE> implements ChangeLogProcessor<NODE_SOURCE>{
    
    private final ChangeLogTargetProvider changeTargetProvider;
    
    private final ChangeLogNodeProcessor<NODE_SOURCE> changeNodeProcessor;

    public ApplyChangeLog(
            ChangeLogTargetProvider changeTargetFactory, 
            ChangeLogNodeProcessor<NODE_SOURCE> changeNodeProcessor) {
        this.changeTargetProvider = Objects.requireNonNull(changeTargetFactory);
        this.changeNodeProcessor = Objects.requireNonNull(changeNodeProcessor);
    }

    @Override
    public List<ChangeResult> process(ChangeLog<NODE_SOURCE> changeLog) {

        List<ChangeLogNode<NODE_SOURCE>> changes = changeLog.getChanges();
        
        List<ChangeResult> results = new ArrayList<>(changes.size());
        
        for(ChangeLogNode<NODE_SOURCE> change : changes) {
        
            ChangeLogTarget target = changeTargetProvider.get(change);
            
            ChangeResult result = changeNodeProcessor.process(changeLog, change, target);
            
            results.add(result);
        }
        
        return results;
    }
}
