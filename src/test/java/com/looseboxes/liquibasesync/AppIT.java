package com.looseboxes.liquibasesync;

import com.bc.xml.DomReaderImpl;
import com.bc.xml.XmlUtil;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author hp
 */
public class AppIT {

    private static final Logger LOG = LoggerFactory.getLogger(AppIT.class);
    
    @Test
    public void test() {
    
         XmlUtil xmlUtil = new XmlUtil();
        
        final String fileNameExtension = ".java";
        
        Path javaFilePath = Paths.get(System.getProperty("user.home"), 
                "Documents\\NetBeansProjects\\webshop-domain\\domain", 
                "CurrencyDetails.java");
        
        Iterable<Path> filesToApplyLiquibaseChangesTo = Collections.singletonList(javaFilePath);

        IO io = new NodeTableNameMatchesFileNameIO(filesToApplyLiquibaseChangesTo, fileNameExtension, xmlUtil, StandardCharsets.UTF_8);

        NodeFilter nodeFilter = new AcceptSingleColumnName(xmlUtil);
        
        Formatter<String> columnAnnotationsFormatter = new AddUniqueConstraintToJpaColumnAnnotations();

        Formatter<Node> formatter = new AddUniqueConstraintToJavaFile(xmlUtil, columnAnnotationsFormatter);
        
        DomNodeProcessor processor = new ApplyLiquibaseChangeLog(xmlUtil, io, nodeFilter, formatter);
        
        Path changeLogFilePath = Paths.get(System.getProperty("user.home"),
                "Documents\\NetBeansProjects\\webshop-domain\\domain", 
                "20200808130700_added_unique_constraints.xml");
        
        Iterable<Document> changeLogDocs = Collections
                .singletonList(new DomReaderImpl().read(changeLogFilePath.toFile()));
        
        for(Document doc : changeLogDocs) {
            
            LOG.info("Processing document: {}", doc.getDocumentURI());
            
            NodeList nodeList = doc.getElementsByTagName("addUniqueConstraint");
            
            LOG.info("Found {} nodes", nodeList.getLength());
        
            List<Node> failedNodes = processor.process(nodeList);
        }
   }
}
