package com.looseboxes.liquibasesync.change;

import com.looseboxes.liquibasesync.Patterns;
import com.looseboxes.liquibasesync.change.io.ChangeLogTarget;
import com.looseboxes.liquibasesync.change.result.ChangeResult;
import com.looseboxes.liquibasesync.change.result.ChangeResults;
import java.io.IOException;
import java.util.regex.Matcher;
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

        // @TODO - process all columns rather than just the first
        // We process only first here because that's all we need to for node
        // type <addUniqueConstraints. However our use case should not be
        // limited to that node type.
        final String columnName = node.getColumnNames().get(0);
        
        final boolean shouldBeUnique = changeLog.isUnique(node);

        ChangeResult result;
        
        try{
            
            final String content = target.load();
            
            final String update = this.format(columnName, shouldBeUnique, content);
            
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

    public String format(String columnName, boolean shouldBeUnique, String content) {
            
        content = this.process(columnName, shouldBeUnique, content);
        
//        When we had: ProductItem.product with @Column(name = "product")
//        Liquibase generated actual column name of 'product_id' and not 'product'
//        So we use use the actual column name here, in this case: 'product_id',
//        even though the entity ProductItem has a field named 'product' and none
//        named 'product_id'
        
        final String idSuffix = "_id";
        
        if(columnName.endsWith(idSuffix)) {
            
            columnName = this.removeSuffix(columnName, idSuffix);
        
            content = this.process(columnName, shouldBeUnique, content);
        }
        
        return content;
    }
    
    private String removeSuffix(String columnName, String suffix) {
        int n = columnName.lastIndexOf(suffix);
        if(n == -1) {
            throw new IllegalArgumentException("Text does not contain expected string: " + suffix);
        }
        return columnName.substring(0, n);
    }

    private String process(String columnName, boolean shouldBeUnique, String content) {
        StringBuffer buff = new StringBuffer();
        this.process(columnName, shouldBeUnique, content, buff);
        return buff.toString();
    }
    
    private int process(String columnName, boolean shouldBeUnique, String content, StringBuffer buff) {
        
        int updateCount = 0;

        Matcher matcher = Patterns.JPA_COLUMN_ANNOTATION.matcher(content);
        
        String quotedColumnName = "\"" + columnName + "\"";
        
        while(matcher.find()) {
        
            final String found = matcher.group();
            
            final String replacement;
            
            if(found.contains(quotedColumnName)) {
                final String update = this.getReplacment(found, shouldBeUnique, null);
                replacement = update == null ? found : update;
                if(update != null) {
                    LOG.info("Updating {} to: {}", found, update);
                    ++updateCount;
                }
            }else{
                replacement = found;
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
}
