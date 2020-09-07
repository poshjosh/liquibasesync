package com.looseboxes.liquibasesync.change.io;

import com.looseboxes.liquibasesync.Patterns;
import com.looseboxes.liquibasesync.change.ChangeLogNode;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hp
 */
public class ChangeLogNodeTargetPathLocatorByJpaTableAnnotation 
        implements ChangeLogNodeTargetLocator<Path>{

    private static final Logger LOG = LoggerFactory.getLogger(ChangeLogNodeTargetPathLocatorByJpaTableAnnotation.class);
    
    private final Iterable<Path> paths;

    public ChangeLogNodeTargetPathLocatorByJpaTableAnnotation(Iterable<Path> paths) {
        this.paths = Objects.requireNonNull(paths);
        LOG.debug("{}", paths);
    }

    @Override
    public Optional<Path> find(ChangeLogNode node) {
    
        final String tableName = node.getTableName();
        
        return this.findFileContainingMatchingJpaTableAnnotation(tableName);
    }

    private Optional<Path> findFileContainingMatchingJpaTableAnnotation(String tableName) {
        Path matching = null;
        final String rhs = "\"" + StringUtil.format(tableName) + "\"";
        for(Path path : paths) {
            if(pathContainsMatchingJpaTableAnnotatiion(path, rhs)) {
                matching = path;
                break;
            }
        }
        return Optional.ofNullable(matching);
    }

    private boolean pathContainsMatchingJpaTableAnnotatiion(Path path, String rhs) {
        List<String> lines = this.getLines(path);
        for(String line : lines) {
            if(this.containsMatchingJpaTableAnnotation(line, rhs)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean containsMatchingJpaTableAnnotation(String line, String rhs) {
        Matcher matcher = Patterns.JPA_TABLE_ANNOTATION.matcher(line);
        if(matcher.find()) {
            final String found = matcher.group();
            if( ! StringUtil.isJavaComment(line)) {
                final String lhs = StringUtil.format(found);
                boolean matches = lhs.contains(rhs);
                LOG.trace("Matches: {}, LHS: {}, RHS: {}", matches, lhs, rhs);
                if(matches) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<String> getLines(Path path) {
        try{
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        }catch(IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}
