package com.looseboxes.liquibasesync.util;

import java.util.regex.Pattern;

/**
 * @author hp
 */
public class Patterns {
    
    // @TODO add support for fully qualified annotation name e.g javax.persistence.Table
    public static final Pattern JPA_TABLE_ANNOTATION = Pattern.compile("@Table\\(.+?\\)");
    
    // @TODO add support for fully qualified annotation name e.g javax.persistence.Column
    public static final Pattern JPA_COLUMN_ANNOTATION = Pattern.compile("@Column\\(.+?\\)");
    
    public static final Pattern JPA_COLUMN_ANNOTATION_NAME = Pattern.compile("name\\s*?=\\s*?\"(.+?)\"");
}
