package org.scash;

import org.scijava.nativelib.NativeLoader;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.scash.NativeBLAKE3Util.checkState;

public class NativeBLAKE3 {
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();

    private static final boolean enabled;

    private long hasher = -1;

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

    public NativeBLAKE3() throws IllegalStateException {
        checkState(enabled);
        long initHasher = createHasher();
        checkState(initHasher != 0);
        hasher = initHasher;
    }

    private long getHasher() throws IllegalStateException {
        checkState(isValid());
        return hasher;
    }

    public boolean isValid() {
        return hasher != -1;
    }

    public void close() {
        if(isValid()) {
            cleanUp();
        }
    }

    private void cleanUp() {
        w.lock();
        try {
            destroyHasher(getHasher());
        } finally {
            hasher = -1;
            w.unlock();
        }
    }

    private static native long createHasher();

    private static native void destroyHasher(long hasher);
}
