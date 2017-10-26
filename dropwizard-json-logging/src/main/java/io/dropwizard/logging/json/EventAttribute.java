package io.dropwizard.logging.json;

import com.google.common.base.CharMatcher;

public enum EventAttribute {
    LEVEL,
    THREAD_NAME,
    MDC,
    LOGGER_NAME,
    MESSAGE,
    EXCEPTION,
    CONTEXT_NAME,
    TIMESTAMP;

    @Override
    public String toString() {
        return CharMatcher.is('_').removeFrom(name());
    }
}
