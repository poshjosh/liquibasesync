package com.looseboxes.liquibasesync.change.result;

import java.util.Objects;
import java.util.Optional;

/**
 * @author hp
 */
class ChangeResultImpl implements ChangeResult{
    
    private final ChangeResult.State state;
    
    private final Exception exception;

    ChangeResultImpl(State state, Exception exception) {
        this.state = Objects.requireNonNull(state);
        this.exception = exception;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public Optional<Exception> getException() {
        return Optional.ofNullable(exception);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ChangeResultImpl{").append(state);
        if(exception != null) {
            sb.append(", exception=").append(exception);
        }
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.state);
        hash = 89 * hash + Objects.hashCode(this.exception);
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
        final ChangeResultImpl other = (ChangeResultImpl) obj;
        if (this.state != other.state) {
            return false;
        }
        if (!Objects.equals(this.exception, other.exception)) {
            return false;
        }
        return true;
    }
}
