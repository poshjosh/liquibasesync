package com.looseboxes.liquibasesync;

import com.bc.xml.XmlUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.Optional;
import org.w3c.dom.Node;

/**
 * @author hp
 */
public class NodeTableNameMatchesFileNameIO implements IO{

    private final Iterable<Path> paths;
    private final String fileNameExtension;
    private final XmlUtil xmlUtil;
    private final Charset charset;

    public NodeTableNameMatchesFileNameIO(
            Iterable<Path> paths, String fileNameExtension, 
            XmlUtil xmlUtil, Charset charset) {
        this.paths = Objects.requireNonNull(paths);
        this.fileNameExtension = fileNameExtension.startsWith(".") ? 
                fileNameExtension : "." + fileNameExtension;
        this.xmlUtil = Objects.requireNonNull(xmlUtil);
        this.charset = Objects.requireNonNull(charset);
    }
    
    @Override
    public String readContent(Node node) throws IOException{
        
        final Path path = this.findMatchingFile(node);
        
        return new String(Files.readAllBytes(path), charset);
    }

    @Override
    public void saveContent(Node node, String content) throws IOException{
        
        final Path path = this.findMatchingFile(node);
        
        Files.write(path, content.getBytes(charset),
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }
    
    private Path findMatchingFile(Node node) throws FileNotFoundException{
    
        final String tableName = this.getTableName(node);
        
        final Path path = this.findMatchingFile(tableName)
                .orElseThrow(() -> new FileNotFoundException("File with name matching node: " + xmlUtil.toString(node)));

        return path;
    }
    
    private Optional<Path> findMatchingFile(String tableName) {
        Path matching = null;
        String rhs = format(tableName);
        for(Path path : paths) {
            String lhs = format(path.getFileName().toString());
//            System.out.println("LHS: " + lhs + ", RHS: " + rhs);
            if(lhs.equalsIgnoreCase(rhs)) {
                matching = path;
                break;
            }
        }
        return Optional.ofNullable(matching);
    }
    
    private String format(String s) {
        s = s.replaceAll("_", "");
        s = this.removeSuffixIfPresent(s, fileNameExtension);
        return s;
    }
    
    private String removeSuffixIfPresent(String s, String suffix) {
        if(s.endsWith(suffix)) {
            s = s.substring(0, s.length() - suffix.length());
        }
        return s;
    }
    
    private String getTableName(Node node) {
        return xmlUtil.getAttributeValue(node, "tableName");
    }
}
