package com.looseboxes.liquibasesync.change.io;

import com.looseboxes.liquibasesync.change.ChangeLogNode;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hp
 */
public class ChangeLogNodeTargetPathLocatorByFilename implements ChangeLogNodeTargetLocator<Path>{
    
    private static final Logger LOG = LoggerFactory.getLogger(ChangeLogNodeTargetPathLocatorByFilename.class);
    
    private final Iterable<Path> paths;
    private final String fileNameExtension;

    public ChangeLogNodeTargetPathLocatorByFilename(Iterable<Path> paths, String fileNameExtension) {
        this.paths = Objects.requireNonNull(paths);
        LOG.debug("{}", paths);
        this.fileNameExtension = fileNameExtension.startsWith(".") ? 
                fileNameExtension : "." + fileNameExtension;
    }

    @Override
    public Optional<Path> find(ChangeLogNode node) {
    
        final String tableName = node.getTableName();
        
        return findFileWithNameMatching(tableName);
    }
    
    private Optional<Path> findFileWithNameMatching(String tableName) {
        Path matching = null;
        String rhs = StringUtil.format(tableName);
        for(Path path : paths) {
            String lhs = StringUtil.format(path.getFileName().toString(), this.fileNameExtension);
            boolean matches = lhs.equalsIgnoreCase(rhs);
            LOG.trace("Matches: {}, LHS: {}, RHS: {}", matches, lhs, rhs);
            if(matches) {
                matching = path;
                break;
            }
        }
        return Optional.ofNullable(matching);
    }
}
