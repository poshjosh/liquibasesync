package com.looseboxes.liquibasesync.change;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hp
 */
public interface ChangeLog<NODE_SOURCE> {
    
    List<ChangeLogNode<NODE_SOURCE>> getChanges();

    default boolean isUnique(ChangeLogNode node) {
        return "addUniqueConstraint".equals(node.getNodeName());
    }
    
    default List<String> getTableNames() {
        return this.stream().map((node) -> node.getTableName()).collect(Collectors.toList());
    }
    
    default List<String> getColumnNames(String tableName) {
        List<String> tableColumnNames = new ArrayList<>();
        this.streamChanges(tableName)
                .map((node) -> node.getColumnNames())
                .forEachOrdered((columnNames) -> tableColumnNames.addAll(columnNames));
        return tableColumnNames;
    }
    
    default Stream<ChangeLogNode<NODE_SOURCE>> streamChanges(String tableName) {
        return this.stream().filter((node) -> Objects.equals(tableName, node.getTableName()));
    }
    
    default Stream<ChangeLogNode<NODE_SOURCE>> stream() {
        return this.getChanges().stream();
    }
}
