package io.terrafino.api.ac.utils;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TimeUtilsTest extends TimeUtils {

    @Test
    public void convertsDateAndTimeToEpoch() throws Exception {
        long timestamp = TimeUtils.getTimestamp(20180101, 0);
        assertThat(timestamp, is(1514764800000L));
    }

    @Test
    public void convertsEpochToDateAndTime() throws Exception {
        long timestamp = 1514764800000L;
        assertThat(TimeUtils.getDate(timestamp), is(20180101));
        assertThat(TimeUtils.getTime(timestamp), is(0));
    }
}