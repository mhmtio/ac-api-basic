package io.terrafino.api.ac;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AcExceptionTest {

    @Test
    public void canCreateWithMessage() throws Exception {
        AcException exception = new AcException("message");
        assertThat(exception.getMessage(), is("message"));
    }

    @Test
    public void canCreateWithMessageAndCause() throws Exception {
        NullPointerException cause = new NullPointerException();
        AcException exception = new AcException("message", cause);
        assertThat(exception.getMessage(), is("message"));
        assertThat(exception.getCause(), is(cause));
    }
}