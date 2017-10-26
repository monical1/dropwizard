package io.dropwizard.logging.json.layout;

import org.junit.Test;

import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

public class TimestampFormatterTest {

    @Test
    public void testFormatTimestampAsString() {
        TimestampFormatter timestampFormatter = new TimestampFormatter("yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            ZoneId.of("GMT+01:00"));
        assertThat(timestampFormatter.format(1513956631000L)).isEqualTo("2017-12-22T16:30:31.000+0100");
    }

    @Test
    public void testDoNotFormatTimestamp() {
        TimestampFormatter timestampFormatter = new TimestampFormatter(null,
            ZoneId.of("GMT+01:00"));
        assertThat(timestampFormatter.format(1513956631000L)).isEqualTo(1513956631000L);
    }
}
