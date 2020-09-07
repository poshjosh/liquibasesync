package com.looseboxes.liquibasesync;

import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author hp
 */
public final class TestUtil {
    
    public static final String JAVA_FILE_NAME = "SampleJavaFile";
    public static final String LIQUIBASE_CHANGELOG_FILE_NAME = "sample_changelog.xml";
    
    private TestUtil() { }

    public static URL [] javaFiles() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final int len = 3;
        URL [] result = new URL[len];
        for(int i=0; i<len; i++) {
            result[i] = classLoader.getResource(JAVA_FILE_NAME + i);
        }
        return result;
    }
    
    public static URL randomJavaFile() {
        final int randomInt = ThreadLocalRandom.current().nextInt(0, 3);
        return Thread.currentThread().getContextClassLoader()
                .getResource(JAVA_FILE_NAME + randomInt);
    }
    
    public static URL randomChangeLogXmlFile() {
        return Thread.currentThread().getContextClassLoader()
                .getResource(LIQUIBASE_CHANGELOG_FILE_NAME);
    }
    
    public static String randomChangeLogContent(String... columnNames) {
        StringBuilder sb = new StringBuilder();
        for(String col : columnNames) {
            sb.append(UUID.randomUUID());
            sb.append(" ");
            sb.append("@Column(name = \"");
            sb.append(col);
            sb.append("\", length = 64, nullable = false, unique = true)");
            sb.append(" ");
            sb.append(UUID.randomUUID());
        }
        return sb.toString();
    }
}
