package com.looseboxes.liquibasesync;

import java.awt.Component;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.JFileChooser;

/**
 * @author hp
 */
public class SimpleFolderChooser implements FileChooser {
    
    private static File lastSelected;
        
    private final JFileChooser chooser = new JFileChooser();
    
    @Override
    public List<File> promptUser(Component parent, String dialogTitle) {
        
        chooser.setAcceptAllFileFilterUsed(false);
        String approveButtonText = "Select folder(s)";
        chooser.setApproveButtonText(approveButtonText);
        if(lastSelected != null) {
            chooser.setCurrentDirectory(lastSelected);
        }
        chooser.setDialogTitle(dialogTitle);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
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
