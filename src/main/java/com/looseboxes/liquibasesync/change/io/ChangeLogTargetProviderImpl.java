package com.looseboxes.liquibasesync.change.io;

import com.looseboxes.liquibasesync.change.ChangeLogNode;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Objects;

/**
 * @author hp
 */
public class ChangeLogTargetProviderImpl implements ChangeLogTargetProvider{
    
    private final ChangeLogNodeTargetLocator<Path> targetLocator;
    private final Charset charset;

    public ChangeLogTargetProviderImpl(
            ChangeLogNodeTargetLocator<Path> targetLocator, Charset charset) {
        this.targetLocator = Objects.requireNonNull(targetLocator);
        this.charset = Objects.requireNonNull(charset);
    }

    @Override
    public ChangeLogTarget get(ChangeLogNode node) {
        Path path = this.targetLocator.find(node)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Not Found. File with name matching node: " + node));
        return new ChangeLogTargetFile(path, charset);
    }
}
