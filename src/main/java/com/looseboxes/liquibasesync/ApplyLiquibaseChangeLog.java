package com.looseboxes.liquibasesync;

import com.bc.xml.XmlUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author hp
 */
public class ApplyLiquibaseChangeLog implements DomNodeProcessor {
    
    private static final Logger LOG = LoggerFactory.getLogger(ApplyLiquibaseChangeLog.class);
    
    private final XmlUtil xmlUtil;
    
    private final IO io;
    
    private final NodeFilter nodeFilter;
    
    private final Formatter<Node> formatter;

    public ApplyLiquibaseChangeLog(XmlUtil xmlUtil, IO io, 
            NodeFilter nodeFilter, Formatter<Node> formatter) {
        this.xmlUtil = Objects.requireNonNull(xmlUtil);
        this.io = Objects.requireNonNull(io);
        this.nodeFilter = Objects.requireNonNull(nodeFilter);
        this.formatter = Objects.requireNonNull(formatter);
    }
    
    @Override
    public List<Node> process(NodeList nodes) {
        
        final int len = nodes.getLength();
        
        final List<Node> failedNodes = new ArrayList(len);
        
        for(int i=0; i<len; i++) {
            
            Node node = nodes.item(i);
            
            if(nodeFilter.accept(node)) {
                
                final int updateCount = this.process(node);
                
                final String state = updateCount > 0 ? " SUCCESS" : 
                        updateCount == 0 ? "NOCHANGE" : "  FAILED";
                
                LOG.info("{} node: {}", state, xmlUtil.toString(node));
                
                if(updateCount < 0) {
                
                    failedNodes.add(node);
                }
            }else{
            
                LOG.info("REJECTED node: {}", xmlUtil.toString(node));
            }
        }
        
        return failedNodes.isEmpty() ? Collections.EMPTY_LIST : Collections.unmodifiableList(failedNodes);
    }
    
    @Override
    public int process(Node node) {
    
        try{
            
            String content = io.readContent(node);

            String update = this.formatter.format(node, content);
            
            if(update.equals(content)) {
                
                return 0;
                
            }else{
                
                io.saveContent(node, update);
                
                return 1;
            }
            
        }catch(IOException e) {
        
            LOG.warn("Failed to process node: " + xmlUtil.toString(node) + ", reason: " + e);
            
            return -1;
        }
    }
}
