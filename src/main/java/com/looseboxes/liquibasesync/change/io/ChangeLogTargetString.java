package com.looseboxes.liquibasesync.change.io;

import java.util.Objects;

/**
 * @author hp
 */
public class ChangeLogTargetString implements ChangeLogTarget{
    
    private String content;

    public ChangeLogTargetString(String content) {
        this.content = Objects.requireNonNull(content);
    }

    @Override
    public String load() {
        return this.content;
    }

    @Override
    public void save(String content) {
        this.content = content;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.content);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ChangeLogTargetString other = (ChangeLogTargetString) obj;
        if (!Objects.equals(this.content, other.content)) {
            return false;
        }
        return true;
    }
}
