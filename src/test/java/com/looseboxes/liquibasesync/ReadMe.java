package com.looseboxes.liquibasesync;

import com.looseboxes.liquibasesync.change.config.JpaUniqueConstraintsChangeConfiguration;
import com.looseboxes.liquibasesync.change.config.ChangeConfiguration;
import com.looseboxes.liquibasesync.change.ChangeLogSourceProcessor;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author hp
 */
public class ReadMe {
    
    private static final Logger LOG = LoggerFactory.getLogger(ReadMe.class);
    
    public static void main(String... args) {
        
        ChangeConfiguration<Path, Document, Node> config = new JpaUniqueConstraintsChangeConfiguration();
        
        Iterable<Document> changeLogDocuments = config.sourceProvider().get();
        
        ChangeLogSourceProcessor<Document> processor = config.sourceProcessor();
        
        for(Document document : changeLogDocuments) {

            LOG.info("Processing document: {}", document.getDocumentURI());
            
            processor.process(document);
        }
    }
}
