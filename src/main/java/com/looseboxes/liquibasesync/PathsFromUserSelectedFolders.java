package com.looseboxes.liquibasesync;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author hp
 */
public class PathsFromUserSelectedFolders implements Supplier<Iterable<Path>>{
    
    private final String fileNameExtension;
    
    private final FileChooser folderChooser;

    public PathsFromUserSelectedFolders(FileChooser folderChooser, String fileNameExtension) {
        this.folderChooser = Objects.requireNonNull(folderChooser);
        this.fileNameExtension = Objects.requireNonNull(fileNameExtension);
    }

    @Override
    public Iterable<Path> get() {
        
        String title = "Select folder(s) containing " + this.fileNameExtension + " files";
        
        List<File> selected = folderChooser.promptUser(null, title);
        
        final List<Path> result;
        
        if(selected.isEmpty()) {

            result = Collections.EMPTY_LIST;

        }else{

            FilenameFilter matchesFileNameExtension = 
                    (dir, name) -> name.toLowerCase().endsWith(this.fileNameExtension);

            List<Path> collectInto = new ArrayList<>();

            for(File dir : selected) {
                Arrays.asList(dir.listFiles(matchesFileNameExtension))
                    .stream().map((file) -> file.toPath())
                    .collect(Collectors.toCollection(() -> collectInto));
            }

            result = Collections.unmodifiableList(collectInto);
        }
        
        return result;
    }
}
