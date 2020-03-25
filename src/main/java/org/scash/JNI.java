package org.scash;

import java.nio.ByteBuffer;

class JNI {
    static native long create_hasher();

    static native void destroy_hasher(long hasher);

    static native void blake3_hasher_init(long hasher);

    static native void blake3_hasher_update(long hasher, ByteBuffer byteBuff, long input_len);

    static native byte[] blake3_hasher_finalize(long hasher, long out_len);
}
