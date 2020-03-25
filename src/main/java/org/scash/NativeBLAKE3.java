package org.scash;

import org.scijava.nativelib.NativeLoader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.scash.NativeBLAKE3Util.*;

public class NativeBLAKE3 {
    public static final int KEY_LEN = 32;
    public static final int OUT_LEN = 32;
    public static final int BLOCK_LEN = 64;
    public static final int CHUNK_LEN = 1024;
    public static final int MAX_DEPTH = 54;
    public static final int MAX_SIMD_DEGREE = 16;

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
        if (isValid()) {
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

    public void update(byte[] data) {
        ByteBuffer byteBuff = nativeByteBuffer.get();

        if (byteBuff == null || byteBuff.capacity() < data.length) {
            byteBuff = ByteBuffer.allocateDirect(data.length);
            byteBuff.order(ByteOrder.nativeOrder());
            nativeByteBuffer.set(byteBuff);
        }
        byteBuff.rewind();
        byteBuff.put(data);
        r.lock();
        try {
            JNI.blake3_hasher_update(getHasher(), byteBuff, data.length);
        } finally {
            r.unlock();
        }
    }

    public byte[] getOutput() throws Exception {
        return getOutput(OUT_LEN);
    }

    public byte[] getOutput(int outputLength) throws Exception {
        byte[] retByteArray;
        r.lock();
        try {
            retByteArray = JNI.blake3_hasher_finalize(getHasher(), outputLength);
        } finally {
            r.unlock();
        }
        checkOutput(retByteArray.length == outputLength, "Output size produced by lib doesnt match:" + retByteArray.length + " expected:" + outputLength);
        return retByteArray;
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
