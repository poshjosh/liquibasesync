package com.looseboxes.liquibasesync;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author hp
 */
public class AddUniqueConstraintToJpaColumnAnnotationsTest {
    
    private final String SAMPLE_FILE = "SampleJavaFileWithColumnAnnotations";

    @Test
    public void format_givenInputWithJpaColumnAnnotation_shouldAddUniqueAttributeIfNone() {
        
        System.out.println("format_givenInputWithJpaColumnAnnotation_shouldAddUniqueAttributeIfNone");
        
        String columnName = "symbol";
        
        String columnAnnotation = "@Column(name = \"" + columnName + "\", length = 4, nullable = false)";
        
        String content = UUID.randomUUID() + columnAnnotation + UUID.randomUUID();
        
        Formatter<String> instance = this.getInstance();
        
        String expResult = content.replace(")", ", unique = true)");
        
        String result = instance.format(columnName, content);
        
        assertEquals(expResult.replaceAll("\\s", ""), result.replaceAll("\\s", ""));
    }
    
    @Test
    public void format_givenInputWithJpaColumnAnnotationHavingUniqueAttribute_shouldReturnNoChange() {
        
        System.out.println("format_givenInputWithJpaColumnAnnotationHavingUniqueAttribute_shouldReturnNoChange");
        
        String columnName = "symbol";
        
        String columnAnnotation = "@Column(name = \"" + columnName + "\", length = 4, nullable = false, unique = true)";
        
        String content = UUID.randomUUID() + columnAnnotation + UUID.randomUUID();
        
        Formatter<String> instance = this.getInstance();
        
        String expResult = content;
        
        String result = instance.format(columnName, content);
        
        assertEquals(expResult, result);
    }

    @Test
    public void format_givenValidInput_isIdempotent() {
        System.out.println("format_givenValidInput_isIdempotent");
        
        String columnName = this.getColumnNameForSampleContent();
        String content = this.getSampleContent();
        final String UNIQUE = "unique";
        if(content.contains("unique")) {
            throw new RuntimeException("Sample input should not contain text: " + 
                    UNIQUE + ", source: src/test/resources/" + SAMPLE_FILE);
        }
        
        Formatter<String> instance = this.getInstance();
        
        String expResult = instance.format(columnName, content);
        assertTrue(expResult.contains(UNIQUE));
        
        String result = instance.format(columnName, content);
        assertTrue(result.contains(UNIQUE));

        assertEquals(expResult, result);
    }

    private String getColumnNameForSampleContent() {
        return "currency";
    }
    
    private String getSampleContent() {
        try(InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(SAMPLE_FILE)) {
            
            int bufferSize = 8 * 1024;
            
            byte [] buff = new byte[bufferSize];
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream(bufferSize);
            
            while(in.read(buff) != -1) {
            
                baos.write(buff);
            }
            
            return baos.toString(StandardCharsets.UTF_8.name());
        
        }catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public Formatter<String> getInstance() {
        return new AddUniqueConstraintToJpaColumnAnnotations();
    }
}
