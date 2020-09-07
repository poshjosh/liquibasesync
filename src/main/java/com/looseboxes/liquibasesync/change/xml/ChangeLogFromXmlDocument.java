package com.looseboxes.liquibasesync.change.xml;

import com.looseboxes.liquibasesync.change.ChangeLogImpl;
import com.looseboxes.liquibasesync.NodeFilter;
import com.looseboxes.liquibasesync.change.ChangeLog;
import com.looseboxes.liquibasesync.change.ChangeLogNode;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author hp
 */
public class ChangeLogFromXmlDocument implements ChangeLog<Node>{
    
    private static final Logger LOG = LoggerFactory.getLogger(ChangeLogFromXmlDocument.class);
    
    private final ChangeLogImpl<Node> delegate;
    
    public ChangeLogFromXmlDocument(Document document, String tagName, NodeFilter nodeFilter) {
        
        NodeList nodeList = document.getElementsByTagName(tagName);

        final int len = nodeList.getLength();

        LOG.debug("Found {} nodes", len);
        
        List<ChangeLogNode> changes = new ArrayList<>(len);

        for(int i=0; i<len; i++) {

            Node node = nodeList.item(i);

            if(nodeFilter.accept(node)) {

                changes.add(new ChangeLogNodeFromXmlNode(node));
            }
        }
        
        LOG.debug("Accepted {} nodes", changes.size());
        
        delegate = new ChangeLogImpl(changes);
    }

    @Override
    public List<ChangeLogNode<Node>> getChanges() {
        return delegate.getChanges();
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return delegate.equals(obj);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
