package com.looseboxes.liquibasesync.change.xml;

import com.bc.xml.XmlUtil;
import com.looseboxes.liquibasesync.change.ChangeLogNode;
import com.looseboxes.liquibasesync.change.ChangeLogNodeImpl;
import java.util.Arrays;
import java.util.List;
import org.w3c.dom.Node;

/**
 * @author hp
 */
public class ChangeLogNodeFromXmlNode implements ChangeLogNode<Node>{
    
    private final ChangeLogNodeImpl<Node> delegate;

    public ChangeLogNodeFromXmlNode(Node node) {
        XmlUtil xmlUtil = new XmlUtil();
        String tableName = xmlUtil.getAttributeValue(node, "tableName");
        String [] columnNames = xmlUtil.getAttributeValues(node, "columnNames", ",");
        delegate = new ChangeLogNodeImpl<>();
        delegate.setNodeName(node.getNodeName());
        delegate.setTableName(tableName);
        delegate.setColumnNames(Arrays.asList(columnNames));
        delegate.setSource(node);
    }

    @Override
    public String getNodeName() {
        return delegate.getNodeName();
    }

    @Override
    public List<String> getColumnNames() {
        return delegate.getColumnNames();
    }

    @Override
    public String getTableName() {
        return delegate.getTableName();
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
