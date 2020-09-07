package com.looseboxes.liquibasesync.change.result;

/**
 * @author hp
 */
public final class ChangeResults {

    private ChangeResults() { }
    
    public static ChangeResult success() {
        return from(ChangeResult.State.SUCCESS);
    }
    
    public static ChangeResult nochange() {
        return from(ChangeResult.State.NOCHANGE);
    }

    public static ChangeResult from(ChangeResult.State state){
        return from(state, null);
    }
    
    public static ChangeResult from(ChangeResult.State state, Exception exception){
        return new ChangeResultImpl(state, exception);
    }
}
