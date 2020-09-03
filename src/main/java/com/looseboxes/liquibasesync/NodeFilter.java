package com.looseboxes.liquibasesync;

import org.w3c.dom.Node;

/**
 * @author hp
 */
public interface NodeFilter {
    boolean accept(Node node);
}
