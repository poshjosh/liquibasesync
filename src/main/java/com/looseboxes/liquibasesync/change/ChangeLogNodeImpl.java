package com.looseboxes.liquibasesync.change;

import java.util.List;
import java.util.Objects;

/**
 * @author hp
 */
public class ChangeLogNodeImpl<S> implements ChangeLogNode {
    
    private S source;
    
    private String nodeName;

    private String tableName;
    
    private List<String> columnNames;

    public S getSource() {
        return source;
    }

    public void setSource(S source) {
        this.source = source;
    }

    @Override
    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.source);
        hash = 59 * hash + Objects.hashCode(this.tableName);
        hash = 59 * hash + Objects.hashCode(this.columnNames);
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
        final ChangeLogNodeImpl other = (ChangeLogNodeImpl) obj;
        if (!Objects.equals(this.tableName, other.tableName)) {
            return false;
        }
        if (!Objects.equals(this.source, other.source)) {
            return false;
        }
        if (!Objects.equals(this.columnNames, other.columnNames)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ChangeLogNodeImpl{tableName=").append(tableName);
        sb.append(", columnNames=").append(columnNames);
        sb.append(", source=").append(source);
        sb.append('}');
        return sb.toString();
    }
}
