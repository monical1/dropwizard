package io.dropwizard.logging.json;

import com.google.common.base.CharMatcher;

public enum AccessAttribute {

    CONTENT_LENGTH,
    METHOD,
    REMOTE_ADDRESS,
    REMOTE_USER,
    REQUEST_TIME,
    REQUEST_URI,
    REQUEST_URL,
    STATUS_CODE,
    PROTOCOL,
    REMOTE_HOST,
    SERVER_NAME,
    REQUEST_PARAMETERS,
    USER_AGENT,
    LOCAL_PORT,
    REQUEST_CONTENT,
    RESPONSE_CONTENT,
    TIMESTAMP;

    @Override
    public String toString() {
        return CharMatcher.is('_').removeFrom(name());
    }
}
