package com.looseboxes.liquibasesync.change;

import com.looseboxes.liquibasesync.util.Patterns;
import com.looseboxes.liquibasesync.change.io.ChangeLogTarget;
import com.looseboxes.liquibasesync.change.result.ChangeResult;
import com.looseboxes.liquibasesync.change.result.ChangeResults;
import com.looseboxes.liquibasesync.util.StringUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hp
 */
public class UpdateJpaColumnAnnotations<NODE_SOURCE> implements ChangeLogNodeProcessor<NODE_SOURCE>{

    private static final Logger LOG = LoggerFactory.getLogger(UpdateJpaColumnAnnotations.class);
    
    @Override
    public ChangeResult process(
            ChangeLog<NODE_SOURCE> changeLog, 
            ChangeLogNode<NODE_SOURCE> node, 
            ChangeLogTarget target) {
        
        ChangeResult result;
        
        try{
            
            final String content = target.load();
            
            final List<String> uniqueColumnNames = getUnqiueColumns(changeLog, node.getTableName());
            
            final String update = this.update(uniqueColumnNames, content);
            
            if( ! update.equals(content)) {
            
                target.save(update);
                
                result = ChangeResults.success();
                
            }else{
            
                result = ChangeResults.nochange();
            }
        }catch(IOException e) {
            
            result = ChangeResults.from(ChangeResult.State.FAILED, e);
        
            throw new RuntimeException(e);
        }
 
        LOG.info("{} node: {}", result, node);
        
        return result;
    }
    
    private List<String> getUnqiueColumns(ChangeLog<NODE_SOURCE> changeLog, String tableName) {
        List<String> result = new ArrayList<>();
        changeLog.streamChanges(tableName)
                .filter((node) -> changeLog.isUnique(node))
                .forEachOrdered((node) -> this.addAllColumnNames(node, result));
        return result;
    }
    
    private void addAllColumnNames(ChangeLogNode<NODE_SOURCE> node, List<String> addTo) {
        
        List<String> cols = node.getColumnNames();
        
//        When we had: ProductItem.product with @Column(name = "product")
//        Liquibase generated actual column name of 'product_id' and not 'product'
//        So we use use the actual column name here, in this case: 'product_id',
//        even though the entity ProductItem has a field named 'product' and none
//        named 'product_id'
        
        final String idSuffix = "_id";
        
        for(String col : cols) {
            
            addTo.add(col);
            
            if(col.endsWith(idSuffix)) {
            
                addTo.add(StringUtil.removeSuffix(col, idSuffix));
            }
        }
    }

    private String update(List<String> uniqueColumns, String content) {
        StringBuffer buff = new StringBuffer();
        this.process(uniqueColumns, content, buff);
        return buff.toString();
    }
    
    
    private int process(List<String> uniqueColumns, String content, StringBuffer buff) {
        
        int updateCount = 0;

        Matcher matcher = Patterns.JPA_COLUMN_ANNOTATION.matcher(content);
        
        uniqueColumns = uniqueColumns.stream().map(StringUtil::format).collect(Collectors.toList());
        
        while(matcher.find()) {
        
            final String columnAnnotation = matcher.group();
            
            String replacement = columnAnnotation;
            
            final String columnName = this.getColumnName(columnAnnotation);
            
            boolean shouldBeUnique = uniqueColumns.contains(StringUtil.format(columnName));
            
            final String update = this.getReplacment(columnAnnotation, shouldBeUnique, null);
            
            replacement = update == null ? columnAnnotation : update;
            if(update != null) {
                LOG.info("Updating {} to: {}", columnAnnotation, update);
                ++updateCount;
            }
            
            matcher.appendReplacement(buff, replacement);
        }
        
        matcher.appendTail(buff);
        
        return updateCount;
    }
    

    private String getReplacment(String found, boolean shouldBeUnique, String resultIfNone) {
        
        String replacement = null;

        //@TODO - Make this work better
        // This will not work well for many reasons
        // - If the unique attribute does not follow this exact format e.g it was
        // not added by this code here
        // - If the user edited 'unique = true' to 'unique=true'
        // 
        final String UNIQUE_ATTR = ", unique = true)";
        
        final boolean currentlyUnique = found.contains(UNIQUE_ATTR);

        if(shouldBeUnique) {

            if( ! currentlyUnique) {

                replacement = found.replace(")", UNIQUE_ATTR);
            }                    
        }else{

            if(currentlyUnique) {

                replacement = found.replace(UNIQUE_ATTR, ")");
            }
        }

        return replacement == null ? resultIfNone : replacement;
    }
    
    private String getColumnName(String columnAnnotation) {
        Matcher matcher = Patterns.JPA_COLUMN_ANNOTATION_NAME.matcher(columnAnnotation);
        if(matcher.find()) {
            return matcher.group(1);
        }else{
            throw new IllegalArgumentException(
                    "Could not extract value of name attribute of Jpa @Column annotation: " + columnAnnotation + ", using pattern: " + Patterns.JPA_COLUMN_ANNOTATION_NAME);
        }
    }
}

