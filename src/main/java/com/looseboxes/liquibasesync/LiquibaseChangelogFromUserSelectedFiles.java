package com.looseboxes.liquibasesync;

import com.bc.xml.DomReader;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.w3c.dom.Document;

/**
 * @author hp
 */
public class LiquibaseChangelogFromUserSelectedFiles implements Supplier<Iterable<Document>>{

    private final FileChooser chooser;
    
    private final DomReader domReader;

    public LiquibaseChangelogFromUserSelectedFiles(FileChooser chooser, DomReader domReader) {
        this.chooser = Objects.requireNonNull(chooser);
        this.domReader = Objects.requireNonNull(domReader);
    }
    
    @Override
    public Iterable<Document> get() {
        
        final String dialogTitle = "Select Liquibase ChangeLog xml file(s)";
        
        List<File> selected = chooser.promptUser(null, dialogTitle);
        
        return selected.stream().map((file) -> domReader.read(file)).collect(Collectors.toList());
    }
}
