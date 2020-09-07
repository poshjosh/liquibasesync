package com.looseboxes.liquibasesync.change;

import com.looseboxes.liquibasesync.change.result.ChangeResult;
import java.util.List;

/**
 * @author hp
 */
public interface ChangeLogProcessor<NODE_SOURCE> {

    List<ChangeResult> process(ChangeLog<NODE_SOURCE> changeLog);
}
