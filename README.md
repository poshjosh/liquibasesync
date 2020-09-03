# liquibasesync

### After adding liquidbase changelog, run this app to update your java domain/entity types

### Sample code

- Maven pom.xml

```xml
        <dependency>
            <groupId>com.looseboxes</groupId>
            <artifactId>bcxml</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
            <optional>true</optional>
        </dependency>
```

- Java Code

```java
package com.looseboxes.liquibasesync;

import com.bc.xml.DomReader;
import com.bc.xml.DomReaderImpl;
import com.bc.xml.XmlUtil;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author hp
 */
public class ReadMe {
    
    private static final Logger LOG = LoggerFactory.getLogger(ReadMe.class);
    
    public static void main(String... args) {
        
        String fileNameExtension = ".java";
        
        FileChooser folderChooser = new SimpleFolderChooser();
        
        Iterable<Path> paths = new PathsFromUserSelectedFolders(folderChooser, fileNameExtension).get();

        XmlUtil xmlUtil = new XmlUtil();
        
        IO io = new NodeTableNameMatchesFileNameIO(paths, fileNameExtension, xmlUtil, StandardCharsets.UTF_8);

        NodeFilter nodeFilter = new AcceptSingleColumnName(xmlUtil);
        
        Formatter<String> columnAnnotationsFormatter = new AddUniqueConstraintToJpaColumnAnnotations();

        Formatter<Node> formatter = new AddUniqueConstraintToJavaFile(xmlUtil, columnAnnotationsFormatter);
        
        DomNodeProcessor processor = new ApplyLiquibaseChangeLog(xmlUtil, io, nodeFilter, formatter);
        
        FileChooser fileChooser = new SimpleFileChooser("xml");
        
        DomReader domReader = new DomReaderImpl();
        
        Iterable<Document> docs = new LiquibaseChangelogFromUserSelectedFiles(fileChooser, domReader).get();
        
        for(Document doc : docs) {
            
            LOG.info("Processing document: {}", doc.getDocumentURI());
            
            NodeList nodeList = doc.getElementsByTagName("addUniqueConstraint");
            
            LOG.info("Found {} nodes", nodeList.getLength());
        
            List<Node> failedNodes = processor.process(nodeList);
        }
    }
}
```
