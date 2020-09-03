package com.looseboxes.liquibasesync;

import com.bc.xml.XmlUtil;
import java.util.Objects;
import org.w3c.dom.Node;

/**
 * @author hp
 */
public class AcceptSingleColumnName implements NodeFilter{
    
    private final XmlUtil xmlUtil;

    public AcceptSingleColumnName(XmlUtil xmlUtil) {
        this.xmlUtil = Objects.requireNonNull(xmlUtil);
    }
    
    @Override
    public boolean accept(Node node) {
        
        String [] columnNames = xmlUtil.getAttributeValues(node, "columnNames", ",");

        return columnNames.length == 1;
    }
}
