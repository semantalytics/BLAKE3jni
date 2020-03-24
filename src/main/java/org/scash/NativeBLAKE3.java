package org.scash;
import org.scijava.nativelib.NativeLoader;

public class NativeBLAKE3 {
    private static final boolean enabled;

    static {
        boolean isEnabled = false;
        try {
            NativeLoader.loadLibrary("blake3");
            isEnabled = true;
        } catch (java.io.IOException e) {
            System.out.println("UnsatisfiedLinkError: " + e.toString());
        } finally {
            enabled = isEnabled;
        }
    }

    public static boolean isEnabled() {
        return enabled;
    }

    private static native long createHasher();

    private static native void destroyHasher(long hasher);
}
