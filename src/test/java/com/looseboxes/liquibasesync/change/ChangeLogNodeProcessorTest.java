package com.looseboxes.liquibasesync.change;

import com.looseboxes.liquibasesync.TestChangeConfigurationXml;
import com.looseboxes.liquibasesync.TestUtil;
import com.looseboxes.liquibasesync.change.config.ChangeConfiguration;
import com.looseboxes.liquibasesync.change.io.ChangeLogTarget;
import com.looseboxes.liquibasesync.change.io.ChangeLogTargetString;
import com.looseboxes.liquibasesync.change.result.ChangeResult;
import com.looseboxes.liquibasesync.change.result.ChangeResults;
import java.util.Arrays;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

/**
 * @author hp
 */
public class ChangeLogNodeProcessorTest {
    
    private final ChangeConfiguration changeConfig = new TestChangeConfigurationXml();
    
    public ChangeLogNodeProcessorTest() { }

    /**
     * Test of process method, of class ChangeLogNodeProcessor.
     */
    @Test
    public void testProcess() {
        System.out.println("process");
        ChangeLogNodeProcessor<Node> instance = this.getInstance();
        final String table = "sample_table";
        final String [] columns = {"sample_column0", "sample_column1"};
        ChangeLogNode<Node> node = this.getChangeLogNode(table, columns);
        ChangeLog<Node> changeLog = this.getChangeLog(node);
        ChangeLogTarget changeTarget = this.getChangeLogTarget(columns);
        ChangeResult expResult = ChangeResults.success();
        ChangeResult result = instance.process(changeLog, node, changeTarget);
        assertThat(result, is(expResult));
    }
    
    public ChangeLogNodeProcessor<Node> getInstance() {
        return changeConfig.nodeProcessor();
    }
    
    public ChangeLogNode<Node> getChangeLogNode(String table, String... columns) {
        ChangeLogNodeImpl<Node> node = new ChangeLogNodeImpl<>();
        node.setTableName(table);
        node.setColumnNames(Arrays.asList(columns));
        return node;
    }
    
    public ChangeLog<Node> getChangeLog(ChangeLogNode<Node>... nodes) {
        return new ChangeLogImpl(nodes);
    }
    
    public ChangeLogTarget getChangeLogTarget(String... columns) {
        final String content = TestUtil.randomChangeLogContent(columns);
        return new ChangeLogTargetString(content);
    } 
}
