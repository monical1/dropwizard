package io.dropwizard.logging.json.layout;

import ch.qos.logback.classic.pattern.ThrowableHandlingConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import io.dropwizard.logging.json.EventAttribute;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Map;

/**
 * Builds JSON messages from logging events of the type {@link ILoggingEvent}.
 */
public class EventJsonLayout extends AbstractJsonLayout<ILoggingEvent> {

    private EnumSet<EventAttribute> includes = EnumSet.of(EventAttribute.LEVEL,
        EventAttribute.THREAD_NAME, EventAttribute.MDC, EventAttribute.LOGGER_NAME, EventAttribute.MESSAGE,
        EventAttribute.EXCEPTION, EventAttribute.TIMESTAMP);

    @Nullable
    private String jsonProtocolVersion;

    private final ThrowableHandlingConverter throwableProxyConverter;
    private final TimestampFormatter timestampFormatter;
    private final Map<String, Object> additionalFields;
    private final Map<String, String> customFieldNames;

    public EventJsonLayout(JsonFormatter jsonFormatter, TimestampFormatter timestampFormatter,
                           ThrowableHandlingConverter throwableProxyConverter, Map<String, String> customFieldNames,
                           Map<String, Object> additionalFields) {
        super(jsonFormatter);
        this.timestampFormatter = timestampFormatter;
        this.additionalFields = additionalFields;
        this.customFieldNames = customFieldNames;
        this.throwableProxyConverter = throwableProxyConverter;
    }

    @Override
    public void start() {
        throwableProxyConverter.start();
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        throwableProxyConverter.stop();
    }

    @Override
    protected Map<String, Object> toJsonMap(ILoggingEvent event) {
        return new MapBuilder(timestampFormatter, customFieldNames, additionalFields, 16)
            .addTimestamp("timestamp", isIncluded(EventAttribute.TIMESTAMP), event.getTimeStamp())
            .add("level", isIncluded(EventAttribute.LEVEL), String.valueOf(event.getLevel()))
            .add("thread", isIncluded(EventAttribute.THREAD_NAME), event.getThreadName())
            .add("mdc", isIncluded(EventAttribute.MDC), event.getMDCPropertyMap())
            .add("logger", isIncluded(EventAttribute.LOGGER_NAME), event.getLoggerName())
            .add("message", isIncluded(EventAttribute.MESSAGE), event.getFormattedMessage())
            .add("context", isIncluded(EventAttribute.CONTEXT_NAME), event.getLoggerContextVO().getName())
            .add("version", jsonProtocolVersion != null, jsonProtocolVersion)
            .add("exception", isIncluded(EventAttribute.EXCEPTION) && event.getThrowableProxy() != null,
                throwableProxyConverter.convert(event))
            .build();
    }

    private boolean isIncluded(EventAttribute exception) {
        return includes.contains(exception);
    }

    public EnumSet<EventAttribute> getIncludes() {
        return includes;
    }

    public void setIncludes(EnumSet<EventAttribute> includes) {
        this.includes = includes;
    }

    @Nullable
    public String getJsonProtocolVersion() {
        return jsonProtocolVersion;
    }

    public void setJsonProtocolVersion(@Nullable String jsonProtocolVersion) {
        this.jsonProtocolVersion = jsonProtocolVersion;
    }
}
