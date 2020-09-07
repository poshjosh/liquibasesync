/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looseboxes.liquibasesync.change.config;

import com.looseboxes.liquibasesync.change.ChangeLogNodeProcessor;
import com.looseboxes.liquibasesync.change.ChangeLogProcessor;
import com.looseboxes.liquibasesync.change.ChangeLogProvider;
import com.looseboxes.liquibasesync.change.ChangeLogSourceProcessor;
import com.looseboxes.liquibasesync.change.io.ChangeLogTargetProvider;
import java.util.function.Supplier;
import com.looseboxes.liquibasesync.change.io.ChangeLogNodeTargetLocator;

/**
 *
 * @author hp
 */
public interface ChangeConfiguration<CHANGE_LOG_SOURCE, CHANGE_LOG, CHANGE_LOG_NODE> {

    ChangeLogSourceProcessor<CHANGE_LOG> sourceProcessor();
    
    ChangeLogNodeProcessor<CHANGE_LOG_NODE> nodeProcessor();

    ChangeLogProcessor<CHANGE_LOG_NODE> processor();

    ChangeLogProcessor<CHANGE_LOG_NODE> processor(
            ChangeLogTargetProvider targetFactory, ChangeLogNodeProcessor<CHANGE_LOG_NODE> nodeProcessor);

    ChangeLogProvider<CHANGE_LOG, CHANGE_LOG_NODE> changeLogProvider();

    ChangeLogNodeTargetLocator<CHANGE_LOG_SOURCE> nodeTargetLocator();
    
    Supplier<Iterable<CHANGE_LOG>> sourceProvider();

    ChangeLogTargetProvider targetProvider(ChangeLogNodeTargetLocator<CHANGE_LOG_SOURCE> converter);
}
