package com.looseboxes.liquibasesync;

import com.bc.xml.XmlUtil;
import java.util.Objects;
import org.w3c.dom.Node;

/**
 * @author hp
 */
public class AddUniqueConstraintToJavaFile implements Formatter<Node>{

    private final XmlUtil xmlUtil;
    private final Formatter<String> delegate;

    public AddUniqueConstraintToJavaFile(XmlUtil xmlUtil, Formatter<String> delegate) {
        this.xmlUtil = Objects.requireNonNull(xmlUtil);
        this.delegate = Objects.requireNonNull(delegate);
    }
    
    @Override
    public String format(Node node, String content) {
        
        String [] columnNames = xmlUtil.getAttributeValues(node, "columnNames", ",");
        
        return delegate.format(columnNames[0], content);
    }
}
