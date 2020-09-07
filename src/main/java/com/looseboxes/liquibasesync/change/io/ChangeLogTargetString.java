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
}
