package org.scash;

import org.scijava.nativelib.NativeLoader;

import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.scash.NativeBLAKE3Util.*;

public class NativeBLAKE3 {
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();
    private static ThreadLocal<ByteBuffer> nativeByteBuffer = new ThreadLocal<ByteBuffer>();

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
        long initHasher = JNI.create_hasher();
        checkState(initHasher != 0);
        hasher = initHasher;
    }

    public boolean isValid() {
        return hasher != -1;
    }

    public void close() {
        if(isValid()) {
            cleanUp();
        }
    }

    public void initDefault() {
        w.lock();
        try {
            JNI.blake3_hasher_init(getHasher());
        } finally {
            w.unlock();
        }
    }

    public void hash(ByteBuffer bytes) {

    }
    public void update(byte[] data) throws AssertFailException {

    }

    private long getHasher() throws IllegalStateException {
        checkState(isValid());
        return hasher;
    }

    private void cleanUp() {
        w.lock();
        try {
            JNI.destroy_hasher(getHasher());
        } finally {
            hasher = -1;
            w.unlock();
        }
    }
}
