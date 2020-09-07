package com.looseboxes.liquibasesync.change.config;

import com.looseboxes.liquibasesync.AcceptSingleColumnName;
import com.looseboxes.liquibasesync.NodeFilter;
import java.nio.charset.StandardCharsets;

/**
 * @author hp
 */
public class JpaUniqueConstraintsChangeConfiguration extends ChangeConfigurationForXmlFile{
 
    public JpaUniqueConstraintsChangeConfiguration() {
        super(".java", StandardCharsets.UTF_8, "addUniqueConstraint");
    }

    @Override
    public NodeFilter nodeFilter() {
        return new AcceptSingleColumnName();
    }
}
