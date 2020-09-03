package com.looseboxes.liquibasesync;

import java.awt.Component;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * @author hp
 */
public class SimpleFileChooser implements FileChooser{
    
    private static File lastSelected;
    
    private static class FilenameExtensionFilter extends FileFilter{
        
        private final String fileNameExtension;

        public FilenameExtensionFilter(String fileNameExtension) {
            this.fileNameExtension = Objects.requireNonNull(fileNameExtension);
        }
        
        @Override
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(fileNameExtension.toLowerCase());
        }

        @Override
        public String getDescription() {
            return fileNameExtension + " files";
        }
    }
    
    private final JFileChooser chooser = new JFileChooser();

    private final String fileNameExtension;

    public SimpleFileChooser(String fileNameExtension) {
        this.fileNameExtension = Objects.requireNonNull(fileNameExtension);
    }
    
    @Override
    public List<File> promptUser(Component parent, String dialogTitle) {
        
        FileFilter filter = new FilenameExtensionFilter(fileNameExtension);
                
        String approveButtonText = "Select " + fileNameExtension + " files(s)";
        
        return this.promptUser(parent, dialogTitle, filter, approveButtonText);
    }
    
    protected List<File> promptUser(Component parent, String dialogTitle, 
            FileFilter filter, String approveButtonText) {

        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setApproveButtonText(approveButtonText);
        if(lastSelected != null) {
            chooser.setCurrentDirectory(lastSelected);
        }
        chooser.setDialogTitle(dialogTitle);
        chooser.setFileFilter(filter);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(true);
        
        final int state = chooser.showDialog(parent, approveButtonText);
        
        final List<File> result;
        
        if(state == JFileChooser.APPROVE_OPTION) {
        
            File [] selected = chooser.getSelectedFiles();
            
            if(selected == null || selected.length == 0) {
            
                result = Collections.EMPTY_LIST;
                
            }else{
                
                lastSelected = selected[selected.length - 1];
                
                result = Arrays.asList(selected);
            }
        }else{
        
            result = Collections.EMPTY_LIST;
        }
        
        return result;
    }
}
