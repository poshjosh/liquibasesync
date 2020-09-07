package com.looseboxes.liquibasesync;

import com.looseboxes.liquibasesync.change.ChangeLogSourceProcessor;
import com.looseboxes.liquibasesync.change.config.ChangeConfiguration;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author hp
 */
public class AppIT {

    private static final Logger LOG = LoggerFactory.getLogger(AppIT.class);
    
    @Test
    public void test() throws URISyntaxException, IOException {
    
        ChangeConfiguration<Path, Document, Node> config = new TestChangeConfigurationXml();
        
        Iterable<Document> changeLogDocuments = config.sourceProvider().get();
        
        ChangeLogSourceProcessor<Document> processor = config.sourceProcessor();
        
        for(Document document : changeLogDocuments) {

            LOG.info("Processing document: {}", document);
            
            processor.process(document);
        }
   }
}
