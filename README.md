# JNI Bindings for Blake3

C Bindings for the API of [Blake 3](https://github.com/BLAKE3-team/BLAKE3) cryptographic hash function

This library works out of the box and has been tested for for Ubuntu and Windows 64. If you want to compile your own binaries you can follow the mini guide below

Generating the JNI header
```
cd src/main/java/org/scash/
javac *.java -h ../../../c/
```
This will generate a file called [org_scash_JNI.h](https://github.com/sken77/BLAKE3jni/blob/master/src/main/c/org_scash_JNI.h) based on the`native` calls inside the private `JNI` class.

The user then needs to implement the methods from the header in its c file [org_scash_JNI.c](https://github.com/sken77/BLAKE3jni/blob/master/src/main/c/org_scash_JNI3.c)

The library is cross compiled from linux

for more platforms go here [here](https://github.com/BLAKE3-team/BLAKE3/tree/master/c)

#### Linux

```
cd src/main/c
gcc -I/usr/lib/jvm/default-java/include -I/usr/lib/jvm/default-java/include/linux -fPIC -shared -O3 -o ../../../natives/linux_64/libblake3.so blake3.c blake3_dispatch.c blake3_portable.c blake3_sse41_x86-64_unix.S blake3_avx2_x86-64_unix.S blake3_avx512_x86-64_unix.S org_scash_JNI.c
sudo execstack -c ../../../natives/linux_64/libblake3.so
```

#### Windows 64

```
cd src/main/c
x86_64-w64-mingw32-gcc -I/usr/lib/jvm/default-java/include -static -shared -o ../../../natives/windows_64/blake3.dll blake3.c blake3_dispatch.c blake3_portable.c blake3_sse41_x86-64_windows_gnu.S blake3_avx2_x86-64_windows_gnu.S blake3_avx512_x86-64_windows_gnu.S org_scash_JNI.c
```

This would result in the native library binaries to be inside the `/natives/` folder. The project will automatically detect the OS
and load the correct binary
