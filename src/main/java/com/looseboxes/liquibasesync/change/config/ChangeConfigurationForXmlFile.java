package com.looseboxes.liquibasesync.change.config;

import com.bc.xml.DomReader;
import com.bc.xml.DomReaderImpl;
import com.looseboxes.liquibasesync.FileChooser;
import com.looseboxes.liquibasesync.LiquibaseChangelogFromUserSelectedFiles;
import com.looseboxes.liquibasesync.NodeFilter;
import com.looseboxes.liquibasesync.PathsFromUserSelectedFolders;
import com.looseboxes.liquibasesync.SimpleFileChooser;
import com.looseboxes.liquibasesync.SimpleFolderChooser;
import com.looseboxes.liquibasesync.change.ApplyChangeLog;
import com.looseboxes.liquibasesync.change.ChangeLogNodeProcessor;
import com.looseboxes.liquibasesync.change.ChangeLogProcessor;
import com.looseboxes.liquibasesync.change.ChangeLogProvider;
import com.looseboxes.liquibasesync.change.ChangeLogSourceProcessor;
import com.looseboxes.liquibasesync.change.UpdateJpaColumnAnnotations;
import com.looseboxes.liquibasesync.change.io.ChangeLogTargetProviderImpl;
import java.nio.charset.Charset;
import java.nio.file.Path;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import com.looseboxes.liquibasesync.change.io.ChangeLogTargetProvider;
import com.looseboxes.liquibasesync.change.xml.ChangeLogFromXmlDocumentProvider;
import java.util.Objects;
import java.util.function.Supplier;
import com.looseboxes.liquibasesync.change.io.ChangeLogNodeTargetLocator;
import com.looseboxes.liquibasesync.change.io.ChangeLogNodeTargetPathLocatorByJpaTableAnnotation;
import com.looseboxes.liquibasesync.change.io.ChangeLogNodeTargetPathLocatorByFilename;
import com.looseboxes.liquibasesync.change.io.ChangeLogNodeTargetPathLocatorComposite;

/**
 * @author hp
 */
public class ChangeConfigurationForXmlFile implements ChangeConfiguration<Path, Document, Node> {
    
    private final String fileNameExtension;
    
    private final Charset charset;
    
    private final String tagName;

    public ChangeConfigurationForXmlFile(String fileNameExtension, Charset charset, String tagName) {
        this.fileNameExtension = Objects.requireNonNull(fileNameExtension);
        this.charset = Objects.requireNonNull(charset);
        this.tagName = Objects.requireNonNull(tagName);
    }
    
    @Override
    public ChangeLogSourceProcessor<Document> sourceProcessor() {
        
        ChangeLogProcessor<Node> processor = this.processor();

        ChangeLogProvider<Document, Node> changeLogProvider = this.changeLogProvider();
        
        return (doc) -> processor.process(changeLogProvider.get(doc));
    }
    
    @Override
    public ChangeLogProvider<Document, Node> changeLogProvider() {
        return new ChangeLogFromXmlDocumentProvider(this.nodeFilter(), tagName);
    }
    
    public NodeFilter nodeFilter() {
        return (node) -> true;
    }

    @Override
    public Supplier<Iterable<Document>> sourceProvider() {
        
        FileChooser fileChooser = new SimpleFileChooser("xml");
        
        DomReader domReader = this.domReader();
        
        return new LiquibaseChangelogFromUserSelectedFiles(fileChooser, domReader);
    }
    
    public DomReader domReader() {
        return new DomReaderImpl();
    }
    
    @Override
    public ChangeLogProcessor<Node> processor() {

        ChangeLogNodeTargetLocator<Path> nodeTargetLocator = this.nodeTargetLocator();

        ChangeLogTargetProvider targetProvider = this.targetProvider(nodeTargetLocator);
        
        ChangeLogNodeProcessor<Node> nodeProcessor = this.nodeProcessor();
        
        return new ApplyChangeLog(targetProvider, nodeProcessor);    
    }
    
    @Override
    public ChangeLogProcessor<Node> processor(
            ChangeLogTargetProvider targetFactory, 
            ChangeLogNodeProcessor<Node> nodeProcessor) {
        return new ApplyChangeLog(targetFactory, nodeProcessor);    
    }
    
    @Override
    public ChangeLogNodeProcessor<Node> nodeProcessor() {
        return new UpdateJpaColumnAnnotations();
    }
    
    @Override
    public ChangeLogTargetProvider targetProvider(ChangeLogNodeTargetLocator<Path> converter) {
        return new ChangeLogTargetProviderImpl(converter, charset);    
    }
    
    @Override
    public ChangeLogNodeTargetLocator<Path> nodeTargetLocator() {

        Iterable<Path> filesToApplyLiquibaseChangesTo = this.getFilesToApplyChangesTo();
        
        return new ChangeLogNodeTargetPathLocatorComposite(
                new ChangeLogNodeTargetPathLocatorByFilename(filesToApplyLiquibaseChangesTo, fileNameExtension),
                new ChangeLogNodeTargetPathLocatorByJpaTableAnnotation(filesToApplyLiquibaseChangesTo)
        );
    }

    public Iterable<Path> getFilesToApplyChangesTo() {
        FileChooser folderChooser = new SimpleFolderChooser();
        return new PathsFromUserSelectedFolders(folderChooser, fileNameExtension).get();
    }
    
    public String getFileNameExtension() {
        return fileNameExtension;
    }

    public Charset getCharset() {
        return charset;
    }

    public String getTagName() {
        return tagName;
    }
}
