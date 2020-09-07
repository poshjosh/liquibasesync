package com.looseboxes.liquibasesync.change;

import com.looseboxes.liquibasesync.change.result.ChangeResult;
import java.util.List;

/**
 * @author hp
 */
public interface ChangeLogSourceProcessor<CHANGE_LOG_SOURCE> {

    List<ChangeResult> process(CHANGE_LOG_SOURCE source);
}
