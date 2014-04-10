package com.kbolino.libraries.bytestring;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * A builder for {@link ByteString}s.
 * Builders are used to construct strings incrementally from many pieces.
 * Most methods return a reference to the builder object, so that method
 * calls can be chained easily.
 */
public final class ByteStringBuilder {
	// TODO exceptions
	private byte[] bytes;
	private int length;
	
	/**
	 * Creates a new {@link ByteStringBuilder}.  Constructor preconditions:
	 * <ol>
	 *   <li>{@code initialCapacity >= 0}</li>
	 * </ol>
	 * @param initialCapacity  The initial capacity.
	 */
	ByteStringBuilder(int initialCapacity) {
		bytes = new byte[initialCapacity];
	}
	
	/**
	 * The length of this builder.
	 * @return  The number of bytes that have been added to this builder.
	 */
	public int length() {
		return length;
	}
	
	/**
	 * The capacity of this builder.
	 * @return  The maximum value of {@link #length()} for this builder
	 *   before its internal array must be resized.
	 */
	public int capacity() {
		return bytes.length;
	}
	
	/**
	 * Increases the capacity of this builder, if necessary.
	 * @param capacity  The minimum capacity this builder must have.
	 * @return  {@code this}
	 * @throws IllegalArgumentException If {@code capacity < 0}.
	 */
	public ByteStringBuilder ensureCapacity(final int capacity) throws IllegalArgumentException {
		if (capacity < 0) {
			throw new IllegalArgumentException(String.format("capacity (%d) < 0", capacity));
		}
		if (capacity > bytes.length) {
			int newSize = 2 * bytes.length + 1;
			if (capacity > newSize) {
				newSize = capacity;
			}
			final byte[] copy = new byte[newSize];
			System.arraycopy(bytes, 0, copy, 0, length);
			bytes = copy;
		}
		return this;
	}
	
	/**
	 * Appends a single byte to this builder.
	 * @param value  The value of the byte, as a signed or unsigned 8-bit
	 *   integer.
	 * @return  {@code this}
	 * @throws IllegalArgumentException  If <code>value &lt; {@link
	 *   Byte#MIN_VALUE}</code> or <code>value &gt; {@link
	 *   ByteStrings#UNSIGNED_MAX}</code>.
	 */
	public ByteStringBuilder append(final int value) throws IllegalArgumentException {
		final byte b = Utils.toByteValue(value);
		ensureCapacity(length + 1);
		bytes[length] = b;
		length += 1;
		return this;
	}
	
	/**
	 * Appends an array of bytes to this builder.
	 * @param bytes  The array of byte values to append.
	 * @return  {@code this}
	 * @throws NullPointerException  If {@code bytes == null}.
	 */
	public ByteStringBuilder append(final byte[] bytes) throws NullPointerException {
		if (bytes == null) {
			throw new NullPointerException("bytes is null");
		}
		return append(bytes, 0, bytes.length);
	}
	
	/**
	 * Appends an array of bytes to this builder.
	 * @param bytes  The array of byte values to append.
	 * @param offset  The index of the first byte from the array to append.
	 * @param length  The total number of bytes from the array to append.
	 * @return  {@code this}
	 * @throws NullPointerException  If {@code bytes == null}.
	 * @throws IllegalArgumentException  If {@code offset < 0} or
	 *   {@code length < 0}.
	 * @throws IndexOutOfBoundsException  If {@code offset >= bytes.length}
	 *   or {@code length > bytes.length - offset}.
	 */
	public ByteStringBuilder append(final byte[] bytes, final int offset, final int length)
			throws NullPointerException, IllegalArgumentException, IndexOutOfBoundsException {
		Utils.checkCopyParams(bytes, offset, length);
		if (length != 0) {
			ensureCapacity(this.length + length);
			System.arraycopy(bytes, offset, this.bytes, this.length, length);
			this.length += length;
		}
		return this;
	}
	
	/**
	 * Appends the contents of a byte buffer to this builder.
	 * Afterwards, the buffer's {@linkplain ByteBuffer#position()} will be
	 * equal to its {@linkplain ByteBuffer#limit() limit}.
	 * @param buffer  The buffer to append.
	 * @return  {@code this}
	 * @throws NullPointerException  If {@code buffer == null}.
	 */
	public ByteStringBuilder append(final ByteBuffer buffer) throws NullPointerException {
		if (buffer == null) {
			throw new NullPointerException("buffer is null");
		}
		return append(buffer, buffer.remaining());
	}
	
	/**
	 * Appends the contents a byte buffer to this builder.
	 * Afterwards, the buffer's {@linkplain ByteBuffer#position()} will
	 * be advanced by {@code length}.
	 * @param buffer  The buffer to append.
	 * @param length  The number of bytes from the buffer to append.
	 * @return  {@code this}
	 * @throws NullPointerException  If {@code buffer == null}.
	 * @throws IllegalArgumentException  If {@code length < 0}.
	 * @throws BufferUnderflowException  If {@code length > buffer.remaining()}.
	 */
	public ByteStringBuilder append(final ByteBuffer buffer, final int length)
			throws NullPointerException, IllegalArgumentException, BufferUnderflowException {
		Utils.checkCopyParams(buffer, length);
		if (length != 0) {
			ensureCapacity(this.length + length);
			buffer.get(bytes, this.length, length);
			this.length += length;
		}
		return this;
	}
	
	/**
	 * Appends a string to this builder.
	 * @param string  The string to append.
	 * @return  {@code this}
	 * @throws NullPointerException  If {@code string == null}.
	 */
	public ByteStringBuilder append(final ByteString string) throws NullPointerException {
		if (string == null) {
			throw new NullPointerException("string is null");
		} else if (string.length() > 0) {
			ensureCapacity(length + string.length());
			string.copyTo(bytes, length);
			length += string.length();
		}
		return this;
	}
	
	// TODO at, unsignedAt
	// TODO insert methods
	// TODO replace methods
	// TODO delete methods
	
	// TODO read from InputStream, ReadableByteChannel
	
	public byte[] toByteArray() {
		final byte[] copy = new byte[length];
		if (length > 0) {
			System.arraycopy(bytes, 0, copy, 0, length);
		}
		return copy;
	}
	
	public ByteString toByteString() {
		if (length == 0) {
			return Utils.EMPTY_STRING;
		}
		return new ArrayByteString(toByteArray());
	}
}
