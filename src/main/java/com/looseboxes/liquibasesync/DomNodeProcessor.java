package com.looseboxes.liquibasesync;

import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author hp
 */
public interface DomNodeProcessor {

    List<Node> process(NodeList nodes);

    /**
     * @param node The DOM node to use in implementing the change
     * @return The update count or -1 if an error occurred
     */
    int process(Node node);
}
