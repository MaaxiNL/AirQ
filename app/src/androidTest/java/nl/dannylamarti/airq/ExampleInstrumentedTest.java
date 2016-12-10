package nl.dannylamarti.airq;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation AIRQ_MAP, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under AIRQ_MAP.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("nl.dannylamarti.airq", appContext.getPackageName());
    }
}
