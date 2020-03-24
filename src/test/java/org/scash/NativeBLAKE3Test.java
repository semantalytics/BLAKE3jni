package org.scash;

import org.junit.Test;

import static org.scash.NativeBLAKE3Util.assertEquals;
import static org.scash.NativeBLAKE3Util.AssertFailException;

public class NativeBLAKE3Test {
    @Test
    public void testLibrary() throws AssertFailException {
        assertEquals(NativeBLAKE3.isEnabled(), true, "isEnabled");
    }

    @Test
    public void testHasher() throws AssertFailException {
        NativeBLAKE3 h = new NativeBLAKE3();
        assertEquals(h.isValid(), true, "isValidHasher");
        h.close();
        assertEquals(h.isValid(), false, "hasherDestroyed");
    }

    @Test
    public void multipleHashers() throws AssertFailException {
        NativeBLAKE3 h1 = new NativeBLAKE3();
        NativeBLAKE3 h2 = new NativeBLAKE3();
        h1.close();
        assertEquals(!h1.isValid() && h2.isValid(), true, "differentHashers");
        h2.close();
        assertEquals(!h1.isValid() && !h2.isValid(), true, "all deleted");
    }
}
