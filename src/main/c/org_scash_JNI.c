#include <stdlib.h>
#include <stdint.h>
#include <unistd.h>
#include "blake3.h"
#include "org_scash_JNI.h"

JNIEXPORT jlong JNICALL Java_org_scash_JNI_create_1hasher
  (JNIEnv *env, jclass classObject)
    {
        blake3_hasher *hasher = malloc (sizeof (blake3_hasher));

        (void)classObject;(void)env;

        return (uintptr_t)hasher;
    }

JNIEXPORT void JNICALL Java_org_scash_JNI_destroy_1hasher
  (JNIEnv *env, jclass classObject, jlong hp)
    {
        blake3_hasher *hasher = (blake3_hasher*)(uintptr_t)hp;
        if (hasher != NULL) {
            free(hasher);
        }
        (void)classObject;(void)env;
        return;
    }

JNIEXPORT void JNICALL Java_org_scash_JNI_blake3_1hasher_1init
  (JNIEnv *env, jclass classObject, jlong hp)
  {
        blake3_hasher *hasher = (blake3_hasher*)(uintptr_t)hp;

        blake3_hasher_init(hasher);

        (void)classObject;(void)env;
        return;
  }

JNIEXPORT void JNICALL Java_org_scash_JNI_blake3_1hasher_1init_1keyed
  (JNIEnv *env, jclass classObject, jlong hp, jobject byteBuffer)
  {
        blake3_hasher *hasher = (blake3_hasher*)(uintptr_t)hp;
        uint8_t *key = (uint8_t*) (*env)->GetDirectBufferAddress(env, byteBuffer);

        blake3_hasher_init_keyed(hasher, key);

        (void)classObject;
        return;
  }

JNIEXPORT void JNICALL Java_org_scash_JNI_blake3_1hasher_1init_1derive_1key
  (JNIEnv *env, jclass classObject, jlong hp, jstring ctx)
  {
        blake3_hasher *hasher = (blake3_hasher*)(uintptr_t)hp;

        const char *context = (*env)->GetStringUTFChars(env, ctx, 0);

        blake3_hasher_init_derive_key(hasher, context);

        (*env)->ReleaseStringUTFChars(env, ctx, context);
        (void)classObject;
  }

JNIEXPORT void JNICALL Java_org_scash_JNI_blake3_1hasher_1update
  (JNIEnv *env, jclass classObject, jlong hp, jobject byteBuffer, jint input_len)
  {
        blake3_hasher *hasher = (blake3_hasher*)(uintptr_t)hp;

        void *input = (void*) (*env)->GetDirectBufferAddress(env, byteBuffer);

        blake3_hasher_update(hasher, input, input_len);

        (void)classObject;
        return;
  }

JNIEXPORT void JNICALL Java_org_scash_JNI_blake3_1hasher_1finalize
  (JNIEnv *env, jclass classObject, jlong hp, jobject byteBuffer, jint output_len)
  {
        blake3_hasher *hasher = (blake3_hasher*)(uintptr_t)hp;

        uint8_t *output = (uint8_t*) (*env)->GetDirectBufferAddress(env, byteBuffer);

        blake3_hasher_finalize(hasher, output, output_len);

        (void)classObject;

        return;
  }