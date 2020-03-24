package org.scash;

import org.junit.Test;

import static org.scash.NativeBLAKE3Util.assertEquals;
import static org.scash.NativeBLAKE3Util.AssertFailException;

public class NativeBLAKE3Test {
    @Test
    public void testLibrary() throws AssertFailException {
        assertEquals(NativeBLAKE3.isEnabled(), true, "isEnabled");
    }
}
