ByteString
==========

A Java library for working with immutable sequences of bytes.

The central interface is ```com.kbolino.libraries.bytestring.ByteString```.
Instances and builders are obtained from ```com.kbolino.libraries.bytestring.ByteStrings```.

The primary design goals of this library are:
* Separation of concerns, with Google's
  [Guava library](https://code.google.com/p/guava-libraries/)
  as a model.
* Consistent and predictable behavior, with space and time
  performance guarantees and well defined error conditions.
* Descriptive error messages, including the nature of the error
  and the offending parameters.
* Broad range of features.
* Interopability with byte arrays and ```java.nio.ByteBuffer```.
