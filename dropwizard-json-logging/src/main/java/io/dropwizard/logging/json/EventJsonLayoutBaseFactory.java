package io.dropwizard.logging.json;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.pattern.RootCauseFirstThrowableProxyConverter;
import ch.qos.logback.classic.pattern.ThrowableHandlingConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.logging.json.layout.EventJsonLayout;

import java.util.EnumSet;
import java.util.TimeZone;

/**
 * <table>
 * <tr>
 * <th>Name</th>
 * <th>Default</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td >{@code includeLevel}</td>
 * <td >true</td>
 * <td>Whether to include the logging level.</code></td>
 * </tr>
 * <tr>
 * <td>{@code includeThreadName}</td>
 * <td>true</td>
 * <td>Whether to include the thread name.</td>
 * </tr>
 * <tr>
 * <td>{@code includeMDC}</td>
 * <td>true</td>
 * <td>Whether to include the MDC properties.</td>
 * </tr>
 * <tr>
 * <td>{@code includeLoggerName}</td>
 * <td>true</td>
 * <td>Whether to include the logger name.</td>
 * </tr>
 * <tr>
 * <td >{@code includeMessage}</td>
 * <td>true</td>
 * <td>Whether to include the formatted message.
 * </tr>
 * <tr>
 * <td >{@code includeException}</td>
 * <td>true</td>
 * <td>Whether to log exceptions. If there is an exception, it will be formatted to a string added to the JSON map.</td>
 * </tr>
 * <tr>
 * <td >{@code context}</td>
 * <td>false</td>
 * <td>The name of the logger context.</td>
 * </tr>
 * </table>
 */
@JsonTypeName("json")
public class EventJsonLayoutBaseFactory extends AbstractJsonLayoutBaseFactory<ILoggingEvent> {

    private EnumSet<EventAttribute> includes = EnumSet.of(EventAttribute.LEVEL,
        EventAttribute.THREAD_NAME, EventAttribute.MDC, EventAttribute.LOGGER_NAME, EventAttribute.MESSAGE,
        EventAttribute.EXCEPTION, EventAttribute.TIMESTAMP);

    @JsonProperty
    public EnumSet<EventAttribute> getIncludes() {
        return includes;
    }

    @JsonProperty
    public void setInclude(EnumSet<EventAttribute> includes) {
        this.includes = includes;
    }

    @Override
    @SuppressWarnings("unchecked")
    public LayoutBase<ILoggingEvent> build(LoggerContext context, TimeZone timeZone) {
        final EventJsonLayout jsonLayout = new EventJsonLayout(createDropwizardJsonFormatter(),
            createTimestampFormatter(timeZone), createThrowableProxyConverter(), getCustomFieldNames(),
            getAdditionalFields());
        jsonLayout.setContext(context);
        jsonLayout.setIncludes(includes);
        return jsonLayout;
    }

    protected ThrowableHandlingConverter createThrowableProxyConverter() {
        return new RootCauseFirstThrowableProxyConverter();
    }

}
