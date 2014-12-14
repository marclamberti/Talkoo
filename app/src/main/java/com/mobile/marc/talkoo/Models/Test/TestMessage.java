//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243622        mlamberti@ust.hk

package com.mobile.marc.talkoo.Models.Test;

import android.test.InstrumentationTestCase;

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
