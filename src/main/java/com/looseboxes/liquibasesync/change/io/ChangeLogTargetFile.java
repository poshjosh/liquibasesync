package com.looseboxes.liquibasesync.change.io;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

/**
 * @author hp
 */
public class ChangeLogTargetFile implements ChangeLogTarget{

    private final Path path;
    private final Charset charset;

    public ChangeLogTargetFile(Path path, Charset charset) {
        this.path = Objects.requireNonNull(path);
        this.charset = Objects.requireNonNull(charset);
    }

    @Override
    public String load() throws IOException{
        
        return new String(Files.readAllBytes(path), charset);
    }

    @Override
    public void save(String content) throws IOException{
        
        Files.write(path, content.getBytes(charset),
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.path);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ChangeLogTargetFile other = (ChangeLogTargetFile) obj;
        if (!Objects.equals(this.path, other.path)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ChangeLogTargetFile{path=").append(path);
        sb.append(", charset=").append(charset);
        sb.append('}');
        return sb.toString();
    }
}
