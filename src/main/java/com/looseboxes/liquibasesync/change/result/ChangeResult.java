package com.looseboxes.liquibasesync.change.result;

import java.util.Optional;

/**
 * @author hp
 */
public interface ChangeResult {
    
    enum State{SUCCESS, NOCHANGE, FAILED}
    
    State getState();
    
    Optional<Exception> getException();
}
