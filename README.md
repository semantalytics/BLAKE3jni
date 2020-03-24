# JNI Bindings for Blake3
WIP

C Bindings for the API of [Blake 3](https://github.com/BLAKE3-team/BLAKE3) cryptographic hash function

Generating the JNI header
```
javac *NativeBlake3.java -h src/main/resources/include/
```

The library is cross compiled from linux

for more platforms go here [here](https://github.com/BLAKE3-team/BLAKE3/tree/master/c)

#### Linux

```
cd src/main/c
gcc -fPIC -I/usr/lib/jvm/default-java/include -I/usr/lib/jvm/default-java/include/linux \
-shared -o ../../../natives/linux_64/libblake3.so blake3.c blake3_dispatch.c blake3_portable.c \ 
blake3_sse41_x86-64_unix.S blake3_avx2_x86-64_unix.S \ 
blake3_avx512_x86-64_unix.S org_scash_NativeBLAKE3.c
```

#### Windows 64

```
cd src/main/c
x86_64-w64-mingw32-gcc -I/usr/lib/jvm/default-java/include -shared -o ../../../natives/windows_64/blake3.dll \
blake3.c blake3_dispatch.c blake3_portable.c blake3_sse41_x86-64_windows_gnu.S blake3_avx512_x86-64_windows_gnu.S \ 
blake3_avx2_x86-64_windows_gnu.S org_scash_NativeBLAKE3.c
```

This would result in the native library binaries to be inside the `/natives/` folder. The project will automatically detect the OS
and load the correct binary
