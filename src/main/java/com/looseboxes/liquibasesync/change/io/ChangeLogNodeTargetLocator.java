package com.looseboxes.liquibasesync.change.io;

import com.looseboxes.liquibasesync.change.ChangeLogNode;
import java.util.Optional;

/**
 * @author hp
 */
public interface ChangeLogNodeTargetLocator<T> {

    Optional<T> find(ChangeLogNode node);
}
