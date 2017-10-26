package io.dropwizard.logging.json;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.LayoutBase;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.ImmutableSet;
import io.dropwizard.logging.json.layout.AccessJsonLayout;

import java.util.EnumSet;
import java.util.Set;
import java.util.TimeZone;

/**
 * <table>
 * <tr>
 * <th>Name</th>
 * <th>Default</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>{@code includeRemoteAddress}</td>
 * <td>true</td>
 * <td>Whether to include the IP address of the client or last proxy that sent the request.</td>
 * </tr>
 * <tr>
 * <td>{@code includeRemoteUser}</td>
 * <td>true</td>
 * <td>Whether to include information about the remote user.</td>
 * </tr>
 * <tr>
 * <td>{@code includeRequestTime}</td>
 * <td>true</td>
 * <td>Whether to include the time elapsed between receiving the request and logging it. Time is in ms.</td>
 * </tr>
 * <tr>
 * <td>{@code includeUri}</td>
 * <td>true</td>
 * <td>Whether to include the URI of the request.</td>
 * </tr>
 * <tr>
 * <td>{@code includeStatusCode}</td>
 * <td>true</td>
 * <td>Whether to include the status code of the response.</td>
 * </tr>
 * <tr>
 * <td>{@code includeMethod}</td>
 * <td>true</td>
 * <td>Whether to include the request HTTP method.</td>
 * </tr>
 * <tr>
 * <td>{@code includeProtocol}</td>
 * <td>true</td>
 * <td>Whether to include the request HTTP protocol.</td>
 * </tr>
 * <tr>
 * <td>{@code includeContentLength}</td>
 * <td>true</td>
 * <td>Whether to include the response content length, if it's known.</td>
 * </tr>
 * <tr>
 * <td>{@code includeUserAgent}</td>
 * <td>true</td>
 * <td>Whether to include the user agent of the request.</td>
 * </tr>
 * <tr>
 * <td>{@code includeRequestURL}</td>
 * <td>false</td>
 * <td>Whether to include the request URL (method, URI, query parameters, protocol).</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>{@code includeRemoteHost}</td>
 * <td>false</td>
 * <td>Whether to include the fully qualified name of the client or the last proxy that sent the request.</td>
 * </tr>
 * <tr>
 * <td>{@code includeServerName}</td>
 * <td>false</td>
 * <td>Whether to include the name of the server to which the request was sent.</td>
 * </tr>
 * <tr>
 * <td>{@code includeRequestHeaders}</td>
 * <td>false</td>
 * <td>Whether to include the request headers.</td>
 * </tr>
 * <tr>
 * <td>{@code includeRequestParameters}</td>
 * <td>true</td>
 * <td>Whether to include the request parameters.</td>
 * </tr>
 * <tr>
 * <td>{@code includeRequestContent}</td>
 * <td>false</td>
 * <td>Whether to include the body of the request.</td>
 * </tr>
 * <tr>
 * <td>{@code responseHeaders}</td>
 * <td>false</td>
 * <td>Whether to include the response headers.</td>
 * </tr>
 * <tr>
 * <td>{@code includeResponseContent}</td>
 * <td>false</td>
 * <td>Whether to include the response body.</td>
 * </tr>
 * <tr>
 * <td>{@code includeLocalPort}</td>
 * <td>false</td>
 * <td>Whether to include the port number of the interface on which the request was received.</code></td>
 * </tr>
 * </table>
 */
@JsonTypeName("access-json")
public class AccessJsonLayoutBaseFactory extends AbstractJsonLayoutBaseFactory<IAccessEvent> {

    private EnumSet<AccessAttribute> includes = EnumSet.of(AccessAttribute.REMOTE_ADDRESS,
        AccessAttribute.REMOTE_USER, AccessAttribute.REQUEST_TIME, AccessAttribute.REQUEST_URI,
        AccessAttribute.STATUS_CODE, AccessAttribute.METHOD, AccessAttribute.PROTOCOL, AccessAttribute.CONTENT_LENGTH,
        AccessAttribute.USER_AGENT, AccessAttribute.TIMESTAMP);

    private Set<String> responseHeaders = ImmutableSet.of();
    private Set<String> requestHeaders = ImmutableSet.of();

    @JsonProperty
    public Set<String> getResponseHeaders() {
        return responseHeaders;
    }

    @JsonProperty
    public void setResponseHeaders(Set<String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    @JsonProperty
    public Set<String> getRequestHeaders() {
        return requestHeaders;
    }

    @JsonProperty
    public void setRequestHeaders(Set<String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    @JsonProperty
    public EnumSet<AccessAttribute> getIncludes() {
        return includes;
    }

    @JsonProperty
    public void setInclude(EnumSet<AccessAttribute> includes) {
        this.includes = includes;
    }

    @Override
    public LayoutBase<IAccessEvent> build(LoggerContext context, TimeZone timeZone) {
        final AccessJsonLayout jsonLayout = new AccessJsonLayout(createDropwizardJsonFormatter(),
            createTimestampFormatter(timeZone), getCustomFieldNames(), getAdditionalFields());
        jsonLayout.setContext(context);
        jsonLayout.setIncludes(includes);
        jsonLayout.setRequestHeaders(ImmutableSet.copyOf(requestHeaders));
        jsonLayout.setResponseHeaders(ImmutableSet.copyOf(responseHeaders));
        return jsonLayout;
    }
}
