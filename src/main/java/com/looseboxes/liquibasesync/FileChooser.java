package com.looseboxes.liquibasesync;

import java.awt.Component;
import java.io.File;
import java.util.List;

/**
 * @author hp
 */
public interface FileChooser {

    List<File> promptUser(Component parent, String dialogTitle);
}
