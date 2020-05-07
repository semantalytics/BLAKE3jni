# JNI Bindings for Blake3

C Bindings for the API of [Blake 3](https://github.com/BLAKE3-team/BLAKE3) cryptographic hash function

## Example
The library has the same [c api](https://github.com/BLAKE3-team/BLAKE3/tree/master/c)

```java

  byte[] data = "this is my data".getBytes();
  //Test if the library is properly connected to the c native binaries
  assert(NativeBlake3.isEnabled());
   
  // Initialize the hasher
  NativeBlake3 hasher = new NativeBLAKE3();
  hasher.initDefault();
  
  //read data
  hasher.update(data);
  //more data
  byte[] moredata = "more data".getBytes();
  hasher.update(moredata);
 
  //Finalize the hash. BLAKE3 output lenght defaults to 32 bytes
  byte[] output = hasher.getOutput();
  
  //hasher should be treated as a resource since there is an equivalent object allocated in memory in c.
  hasher.close();
  
```
## API
```java
  //verifies the library is connected
  public static boolean isEnabled() 
  
  //Creates a NativeBlake3 instance and a equivalent one as `blake3_hasher` in c.
  public NativeBLAKE3() throws IllegalStateException 
  
  //initializers
  public void initDefault()
  public void initKeyed(byte[] key)
  public void initDeriveKey(String context)
  
  //Add input to the hasher. This can be called any number of times.
  public void update(byte[] data)
  
  //equivalent to blake3_hasher_finalize in C. you can keep adding data after calling this. 
  public byte[] getOutput() throws InvalidNativeOutput 
  public byte[] getOutput(int outputLength) throws InvalidNativeOutput
  
  //resource handling
  public boolean isValid() //verifies the resource hasnt been closed
  public void close() //close resource and frees up memory in c.
```

## Building from scratch
This library works out of the box and has been tested for for Ubuntu and Windows 64. But if you want to compile your own binaries you can follow the mini guide below

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
