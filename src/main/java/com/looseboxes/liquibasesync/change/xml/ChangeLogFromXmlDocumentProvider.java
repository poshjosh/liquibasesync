package com.looseboxes.liquibasesync.change.xml;

import com.looseboxes.liquibasesync.NodeFilter;
import com.looseboxes.liquibasesync.change.ChangeLog;
import com.looseboxes.liquibasesync.change.ChangeLogProvider;
import java.util.Objects;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author hp
 */
public class ChangeLogFromXmlDocumentProvider implements ChangeLogProvider<Document, Node>{
    
    private final NodeFilter nodeFilter;
    
    private final String tagName;

    public ChangeLogFromXmlDocumentProvider(NodeFilter nodeFilter, String tagName) {
        this.nodeFilter = Objects.requireNonNull(nodeFilter);
        this.tagName = Objects.requireNonNull(tagName);
    }

    @Override
    public ChangeLog<Node> get(Document source) {

        ChangeLog<Node> changeLog = new ChangeLogFromXmlDocument(source, tagName, nodeFilter);
        
        return changeLog;
    }
}
