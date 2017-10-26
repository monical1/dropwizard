package io.dropwizard.logging.json.layout;

import javax.annotation.Nullable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * A faster timestamp formatter than the default one in Logback.
 * Also produces timestamps as numbers if the timestamp formatting is disabled.
 */
public class TimestampFormatter {

    @Nullable
    private final DateTimeFormatter dateTimeFormatter;

    public TimestampFormatter(@Nullable String timestampFormat, ZoneId zoneId) {
        this.dateTimeFormatter = timestampFormat != null ?
            DateTimeFormatter.ofPattern(timestampFormat).withZone(zoneId) : null;
    }

    public Object format(long timestamp) {
        return dateTimeFormatter == null ? timestamp : dateTimeFormatter.format(Instant.ofEpochMilli(timestamp));
    }
}
