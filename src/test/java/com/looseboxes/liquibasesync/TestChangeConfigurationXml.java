package com.looseboxes.liquibasesync;

import com.bc.xml.DomReader;
import com.looseboxes.liquibasesync.change.config.JpaUniqueConstraintsChangeConfiguration;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Supplier;
import org.w3c.dom.Document;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hp
 */
public class TestChangeConfigurationXml extends JpaUniqueConstraintsChangeConfiguration{
    
    @Override
    public Iterable<Path> getFilesToApplyChangesTo() {
        
        try{
            
            final URL [] urls = TestUtil.javaFiles();
            final List<Path> paths = new ArrayList<>(urls.length);
            for(URL url : urls) {
                paths.add(Paths.get(url.toURI()));
            }

            return Collections.unmodifiableList(paths);
            
        }catch(URISyntaxException e) {
        
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Supplier<Iterable<Document>> sourceProvider() {
        
        final Document changeLogDocument;
        
        DomReader domReader = this.domReader();
        
        final URL changeLogXmlFile = TestUtil.randomChangeLogXmlFile();
        
        try(InputStream in = changeLogXmlFile.openStream()) {
        
            changeLogDocument = domReader.read(in);
            
        }catch(IOException e) {
        
            throw new RuntimeException(e.getMessage(), e);
        }
        
        return () -> Arrays.asList(changeLogDocument);
    }
}
    /**
     * Read from source, but do not write to source.
     * Suitable for test case where we don't want to update source
     * For example when our source is located in the resources folder
     * and writing to such location will lead to an exception.
     */
/**
 * 
    private static final class ChangeLogTargetFileNoOutput implements ChangeLogTarget{
        private final ChangeLogTarget delegate;
        public ChangeLogTargetFileNoOutput(ChangeLogTarget delegate) {
            this.delegate = Objects.requireNonNull(delegate);
        }
        @Override
        public String load() throws IOException {
            return delegate.load();
        }
        @Override
        public void save(String content) { }
    }

    @Override
    public ChangeLogTargetProvider targetProvider(ChangeLogNodeTargetLocator<Path> converter) {
        return new ChangeLogTargetProviderImpl(converter, this.getCharset()){
            @Override
            public ChangeLogTarget get(ChangeLogNode node) {
                ChangeLogTarget target = super.get(node);
                return new ChangeLogTargetFileNoOutput(target);
            }
        };    
    }
 * 
 */