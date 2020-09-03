package com.looseboxes.liquibasesync;

import java.awt.Component;
import java.io.File;
import java.util.List;

/**
 * @author hp
 */
public class FileChooserDemo {
    
    public static void main(String... args) {
    
        FileChooserDemo demo = new FileChooserDemo();
        
        demo.promptUserToSelectFiles(".java");
        
        demo.promptUserToSelectFolders();
    }
    
    public void promptUserToSelectFiles(String fileNameExtension) {
        System.out.println("promptUserToSelectFiles");
        Component parent = null;
        String dialogTitle = "Select " + fileNameExtension + " file(s)";
        FileChooser instance = new SimpleFileChooser(fileNameExtension);
        List<File> result = instance.promptUser(parent, dialogTitle);
        System.out.println("Selected: " + result);
    }

    public void promptUserToSelectFolders() {
        System.out.println("promptUserToSelectFolders");
        Component parent = null;
        String dialogTitle = "Select folder(s)";
        FileChooser instance = new SimpleFolderChooser();
        List<File> result = instance.promptUser(parent, dialogTitle);
        System.out.println("Selected: " + result);
    }
}
