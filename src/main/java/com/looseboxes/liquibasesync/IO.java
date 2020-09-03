package com.looseboxes.liquibasesync;

import java.io.IOException;
import org.w3c.dom.Node;

/**
 * @author hp
 */
public interface IO {
    String readContent(Node node) throws IOException;
    void saveContent(Node node, String content) throws IOException;
}
