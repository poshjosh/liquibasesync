package com.looseboxes.liquibasesync.change.io;

import com.looseboxes.liquibasesync.change.ChangeLogNode;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author hp
 */
public class ChangeLogNodeTargetPathLocatorComposite<T> implements ChangeLogNodeTargetLocator<T>{
    
    private final List<ChangeLogNodeTargetLocator<T>> delegates;

    public ChangeLogNodeTargetPathLocatorComposite(ChangeLogNodeTargetLocator<T>... delegates) {
        this(Arrays.asList(delegates));
    }
    
    public ChangeLogNodeTargetPathLocatorComposite(List<ChangeLogNodeTargetLocator<T>> delegates) {
        this.delegates = Objects.requireNonNull(delegates);
    }

    @Override
    public Optional<T> find(ChangeLogNode node) {
        for(ChangeLogNodeTargetLocator delegate : delegates) {
            Optional<T> found = delegate.find(node);
            if(found.isPresent()) {
                return found;
            }
        }
        return Optional.empty();
    }
}
