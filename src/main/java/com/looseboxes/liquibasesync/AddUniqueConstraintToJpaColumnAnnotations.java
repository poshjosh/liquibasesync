package com.looseboxes.liquibasesync;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hp
 */
public class AddUniqueConstraintToJpaColumnAnnotations implements Formatter<String>{
    
    private final Pattern pattern = Pattern.compile("@Column\\(.+?\\)");
    
    @Override
    public String format(String columnName, String content) {

        Matcher matcher = pattern.matcher(content);
        
        StringBuffer sb = new StringBuffer();
        
        String quotedColumnName = "\"" + columnName + "\"";
        
        while(matcher.find()) {
        
            final String found = matcher.group();
            
            final String replacement;
            
            if(found.contains(quotedColumnName) && ! found.contains("unique")) {
                replacement = found.replace(")", ", unique = true)");
            }else{
                replacement = found;
            }
            
            matcher.appendReplacement(sb, replacement);
        }
        
        matcher.appendTail(sb);
        
        return sb.toString();
    }
}
