package com.mobile.marc.talkoo.MessageManagement.Test;

import android.test.InstrumentationTestCase;

import junit.framework.TestCase;

/**
 * Created by Dyjor on 14/12/14.
 */
public class TestMessage extends InstrumentationTestCase {
    public void test() throws Exception {
        final int expected = 1;
        final int reality = 5;
        assertEquals(expected, reality);
    }
}
