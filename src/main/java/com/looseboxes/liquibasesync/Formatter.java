package com.looseboxes.liquibasesync;

/**
 * @author hp
 */
public interface Formatter<K> {
    
    String format(K key, String content);
}
