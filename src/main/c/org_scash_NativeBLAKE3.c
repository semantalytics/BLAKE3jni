#include <stdlib.h>
#include <stdint.h>
#include "blake3.h"
#include "org_scash_NativeBLAKE3.h"

JNIEXPORT jlong JNICALL Java_org_scash_NativeBLAKE3_createHasher
  (JNIEnv *env, jclass classObject)
    {
        blake3_hasher *hasher = malloc (sizeof (blake3_hasher));

        (void)classObject;(void)env;

        return (uintptr_t)hasher;
    }

JNIEXPORT void JNICALL Java_org_scash_NativeBLAKE3_destroyHasher
  (JNIEnv *env, jclass classObject, jlong lp)
    {
        blake3_hasher *hasher = (blake3_hasher*)(uintptr_t)lp;
        if (hasher != NULL) {
            free(hasher);
        }
        (void)classObject;(void)env;
        return;
    }
