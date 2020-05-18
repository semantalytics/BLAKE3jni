[![Build Status](https://travis-ci.org/sken77/BLAKE3jni.svg?branch=master)](https://travis-ci.org/sken77/BLAKE3jni)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.lktk/blake3jni/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.lktk/blake3jni)

# JNI Bindings for Blake3

C Bindings for the API of [Blake 3](https://github.com/BLAKE3-team/BLAKE3) cryptographic hash function

## Add to project

The library works out of the box in windows 64 and linux 64 just add this to your build system

#### Maven (Java)

```xml
<dependency>
  <groupId>io.lktk</groupId>
  <artifactId>blake3jni</artifactId>
  <version>0.2.2</version>
</dependency>
```

#### sbt (Scala)

```sbt
libraryDependencies += "io.lktk" % "blake3jni" % "0.2.2"
```

#### Gradle (Kotlin, Groovy)

```groovy
implementation("io.lktk:blake3jni:0.2.2")
```

## Example

```java

  //Test if the library is properly connected to the c binaries
  assert(NativeBlake3.isEnabled());
   
  // Initialize the hasher
  NativeBlake3 hasher = new NativeBLAKE3();
  hasher.initDefault();
  
  //read data
  byte[] data = "this is my data".getBytes();
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

The library has the same [c api](https://github.com/BLAKE3-team/BLAKE3/tree/master/c)

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
  public void close() //close resource and frees up memory in c.
  public boolean isValid() //returns true if the resource for this instance hasnt been closed

```

## Building from scratch
This library works out of the box and has been tested for for Ubuntu and Windows 64. But if you want to compile your own binaries you can follow the mini guide below

Generating the JNI header
```
cd src/main/java/io/lktk/
javac JNI.java -h ../../../c/
```
This will generate a file called [io_lktk_JNI.h](https://github.com/sken77/BLAKE3jni/blob/master/src/main/c/io_lktk_JNI.h) based on the`native` calls inside the private `JNI` class.

The user then needs to implement the methods from the header in its c file [io_lktk_JNI.c](https://github.com/sken77/BLAKE3jni/blob/master/src/main/c/io_lktk_JNI.c)

The library is cross compiled from linux

for more platforms go here [here](https://github.com/BLAKE3-team/BLAKE3/tree/master/c)

#### Linux

```
cd src/main/c
gcc -I/usr/lib/jvm/default-java/include -I/usr/lib/jvm/default-java/include/linux -fPIC -shared -O3 -o ../../../natives/linux_64/libblake3.so blake3.c blake3_dispatch.c blake3_portable.c blake3_sse41_x86-64_unix.S blake3_avx2_x86-64_unix.S blake3_avx512_x86-64_unix.S io_lktk_JNI.c
sudo execstack -c ../../../natives/linux_64/libblake3.so
```

#### Windows 64

```
cd src/main/c
x86_64-w64-mingw32-gcc -I/usr/lib/jvm/default-java/include -static -shared -o ../../../natives/windows_64/blake3.dll blake3.c blake3_dispatch.c blake3_portable.c blake3_sse41_x86-64_windows_gnu.S blake3_avx2_x86-64_windows_gnu.S blake3_avx512_x86-64_windows_gnu.S io_lktk_JNI.c
```

This would result in the native library binaries to be inside the `/natives/` folder. The project will automatically detect the OS
and load the correct binary
