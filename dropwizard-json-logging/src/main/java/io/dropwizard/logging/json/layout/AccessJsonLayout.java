package io.dropwizard.logging.json.layout;

import ch.qos.logback.access.spi.IAccessEvent;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.net.HttpHeaders;
import io.dropwizard.logging.json.AccessAttribute;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Builds JSON messages from access log events as {@link IAccessEvent}.
 */
public class AccessJsonLayout extends AbstractJsonLayout<IAccessEvent> {

    private EnumSet<AccessAttribute> includes = EnumSet.of(AccessAttribute.REMOTE_ADDRESS,
        AccessAttribute.REMOTE_USER, AccessAttribute.REQUEST_TIME, AccessAttribute.REQUEST_URI,
        AccessAttribute.STATUS_CODE, AccessAttribute.METHOD, AccessAttribute.PROTOCOL, AccessAttribute.CONTENT_LENGTH,
        AccessAttribute.USER_AGENT, AccessAttribute.TIMESTAMP);

    private Set<String> requestHeaders = ImmutableSet.of();
    private Set<String> responseHeaders = ImmutableSet.of();

    @Nullable
    private String jsonProtocolVersion;

    private final TimestampFormatter timestampFormatter;
    private final Map<String, Object> additionalFields;
    private final Map<String, String> customFieldNames;

    public AccessJsonLayout(JsonFormatter jsonFormatter, TimestampFormatter timestampFormatter,
                            Map<String, String> customFieldNames, Map<String, Object> additionalFields) {
        super(jsonFormatter);
        this.timestampFormatter = timestampFormatter;
        this.additionalFields = additionalFields;
        this.customFieldNames = customFieldNames;
    }

    @Override
    protected Map<String, Object> toJsonMap(IAccessEvent event) {
        return new MapBuilder(timestampFormatter, customFieldNames, additionalFields, 20)
            .add("port", isIncluded(AccessAttribute.LOCAL_PORT), event.getLocalPort())
            .add("contentLength", isIncluded(AccessAttribute.CONTENT_LENGTH), event.getContentLength())
            .addTimestamp("timestamp", isIncluded(AccessAttribute.TIMESTAMP), event.getTimeStamp())
            .add("method", isIncluded(AccessAttribute.METHOD), event.getMethod())
            .add("protocol", isIncluded(AccessAttribute.PROTOCOL), event.getProtocol())
            .add("requestContent", isIncluded(AccessAttribute.REQUEST_CONTENT), event.getRequestContent())
            .add("remoteAddress", isIncluded(AccessAttribute.REMOTE_ADDRESS), event.getRemoteAddr())
            .add("remoteUser", isIncluded(AccessAttribute.REMOTE_USER), event.getRemoteUser())
            .add("headers", !requestHeaders.isEmpty(),
                filterHeaders(event.getRequestHeaderMap(), requestHeaders))
            .add("params", isIncluded(AccessAttribute.REQUEST_PARAMETERS), event.getRequestParameterMap())
            .add("requestTime", isIncluded(AccessAttribute.REQUEST_TIME), event.getElapsedTime())
            .add("uri", isIncluded(AccessAttribute.REQUEST_URI), event.getRequestURI())
            .add("url", isIncluded(AccessAttribute.REQUEST_URL), event.getRequestURL())
            .add("remoteHost", isIncluded(AccessAttribute.REMOTE_HOST), event.getRemoteHost())
            .add("responseContent", isIncluded(AccessAttribute.RESPONSE_CONTENT), event.getResponseContent())
            .add("responseHeaders", !responseHeaders.isEmpty(),
                filterHeaders(event.getResponseHeaderMap(), responseHeaders))
            .add("serverName", isIncluded(AccessAttribute.SERVER_NAME), event.getServerName())
            .add("status", isIncluded(AccessAttribute.STATUS_CODE), event.getStatusCode())
            .add("userAgent", isIncluded(AccessAttribute.USER_AGENT), event.getRequestHeader(HttpHeaders.USER_AGENT))
            .add("version", jsonProtocolVersion != null, jsonProtocolVersion)
            .build();
    }

    private boolean isIncluded(AccessAttribute userAgent) {
        return includes.contains(userAgent);
    }

    private Map<String, String> filterHeaders(Map<String, String> headers, Set<String> filteredHeaderNames) {
        if (filteredHeaderNames.isEmpty()) {
            return ImmutableMap.of();
        }
        return headers.entrySet().stream()
            .filter(e -> filteredHeaderNames.stream().anyMatch(h -> h.equalsIgnoreCase(e.getKey())))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public EnumSet<AccessAttribute> getIncludes() {
        return includes;
    }

    public void setIncludes(EnumSet<AccessAttribute> includes) {
        this.includes = includes;
    }

    @Nullable
    public String getJsonProtocolVersion() {
        return jsonProtocolVersion;
    }

    public void setJsonProtocolVersion(@Nullable String jsonProtocolVersion) {
        this.jsonProtocolVersion = jsonProtocolVersion;
    }

    public Set<String> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(Set<String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public Set<String> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Set<String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }
}
