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
	private byte[] bytes;
	private int length;
	
	/**
	 * Creates a new {@link ByteStringBuilder}.  Constructor preconditions:
	 * <ol>
	 *   <li>{@code initialCapacity >= 0}</li>
	 * </ol>
	 * @param initialCapacity  The initial capacity.
	 */
	ByteStringBuilder(final int initialCapacity) {
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
	 * Returns the value of a byte.
	 * @param index  The index of the byte to retrieve.
	 * @return  The value of the byte.
	 * @throws IllegalArgumentException  If {@code index < 0}.
	 * throws IndexOutOfBoundsException  If <code>index &gt;=
	 *   {@link #length()}</code>.
	 */
	public byte at(final int index) throws IllegalArgumentException, IndexOutOfBoundsException {
		if (index < 0) {
			throw new IllegalArgumentException(String.format("index (%d) < 0", index));
		} else if (index >= length) {
			throw new IndexOutOfBoundsException(String.format("index (%d) >= length (%d)", index, length));
		}
		return bytes[index];
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
			final int newSize = Math.max(2 * bytes.length + 1, capacity);
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
	
	/**
	 * Shifts bytes downward in the internal array.  Preconditions:
	 * <ol>
	 *   <li>{@code fromIndex >= 0}</li>
	 *   <li>{@code toIndex >= 0}</li>
	 *   <li>{@code fromIndex < length}</li>
	 * </ol>
	 * Adjusts {@link length} appropriately.
	 * @param fromIndex  The index of the first byte to shift.
	 * @param toIndex  The new index to receive the first byte.
	 */
	private void shiftBytes(final int fromIndex, final int toIndex) {
		if (fromIndex == toIndex) {
			return;
		}
		final int shift = toIndex - fromIndex;
		ensureCapacity(length + shift);
		if (shift < 0) {
			for (int i = fromIndex; i < length; i++) {
				bytes[i + shift] = bytes[i];
			}
		} else {
			for (int i = length - 1; i >= fromIndex; i--) {
				bytes[i + shift] = bytes[i];
			}
		}
		length += shift;
	}
	
	/**
	 * Inserts a byte into this builder.
	 * TODO describe postconditions
	 * @param index  The index at which to insert the byte.
	 * @param value  The value of the byte to insert, as a signed or unsigned
	 *   8-bit integer.
	 * @return  {@code this}
	 * @throws IllegalArgumentException  If {@code index < 0},
	 *   <code>value &lt; {@link Byte#MIN_VALUE}</code>, or
	 *   <code>value &gt; {@link ByteStrings#UNSIGNED_MAX}</code>.
	 * @throws IndexOutOfBoundsException  If <code>index &gt;
	 *   {@link #length()}</code>.
	 */
	public ByteStringBuilder insert(final int index, final int value)
			throws IllegalArgumentException, IndexOutOfBoundsException {
		if (index < 0) {
			throw new IllegalArgumentException(String.format("index (%d) < 0", index));
		} else if (index > length) {
			throw new IndexOutOfBoundsException(String.format("index (%d) > length (%d)", index, length));
		} else if (index == length) {
			return append(value);
		}
		final byte b = Utils.toByteValue(value);
		shiftBytes(index, index + 1);
		bytes[index] = b;
		return this;
	}
	
	/**
	 * Inserts a string into this builder.
	 * TODO describe postconditions
	 * @param index  The index at which to insert the string.
	 * @param string  The string to insert.
	 * @return  {@code this}
	 * @throws NullPointerException  If {@code string == null}.
	 * @throws IllegalArgumentException  If {@code index < 0}.
	 * @throws IndexOutOfBoundsException  If <code>index &gt;
	 *   {@link #length()}</code>.
	 */
	public ByteStringBuilder insert(final int index, final ByteString string)
			throws NullPointerException, IllegalArgumentException, IndexOutOfBoundsException {
		if (string == null) {
			throw new NullPointerException("string == null");
		} else if (index < 0) {
			throw new IllegalArgumentException(String.format("index (%d) < 0", index));
		} else if (index > length) {
			throw new IndexOutOfBoundsException(String.format("index (%d) > length (%d)", index, length));
		} else if (index == length) {
			return append(string);
		}
		shiftBytes(index, index + string.length());
		string.copyTo(bytes, index);
		return this;
	}
	
	/**
	 * Inserts bytes into this builder.
	 * TODO describe postconditions
	 * @param index  The index at which to insert the bytes.
	 * @param bytes  The bytes to insert.
	 * @return  {@code this}
	 * @throws NullPointerException  If {@code bytes == null}.
	 * @throws IllegalArgumentException  If {@code index < 0}.
	 * @throws IndexOutOfBoundsException  If
	 *   <code>index &gt; {@link #length()}</code>. 
	 */
	public ByteStringBuilder insert(final int index, final byte[] bytes)
			throws NullPointerException, IllegalArgumentException, IndexOutOfBoundsException {
		// TODO check params
		return insert(index, bytes, 0);
	}
	
	/**
	 * Inserts bytes into this builder.
	 * TODO describe postconditions
	 * @param index  The index at which to insert the bytes.
	 * @param bytes  The bytes to insert.
	 * @param offset  The index of the first byte from {@code bytes} to use.
	 * @return  {@code this}
	 * @throws NullPointerException  If {@code bytes == null}.
	 * @throws IllegalArgumentException  If {@code index < 0} or
	 *   {@code offset < 0}.
	 * @throws IndexOutOfBoundsException  If
	 *   <code>index &gt; {@link #length()}</code>
	 *   {@code offset >= bytes.length}.
	 */
	public ByteStringBuilder insert(final int index, final byte[] bytes, final int offset)
			throws NullPointerException, IllegalArgumentException, IndexOutOfBoundsException {
		// TODO check params
		return insert(index, bytes, offset, bytes.length - offset);
	}
	
	/**
	 * Inserts bytes into this builder.
	 * TODO describe postconditions
	 * @param index  The index at which to insert the bytes.
	 * @param bytes  The bytes to insert.
	 * @param offset  The index of the first byte from {@code bytes} to use.
	 * @param length  THe number of bytes to insert.
	 * @return  {@code this}
	 * @throws NullPointerException  If {@code bytes == null}.
	 * @throws IllegalArgumentException  If {@code index < 0},
	 *   {@code offset < 0}, or {@code length < 0}.
	 * @throws IndexOutOfBoundsException  If
	 *   <code>index &gt; {@link #length()}</code> or
	 *   {@code offset + length >= bytes.length}.
	 */
	public ByteStringBuilder insert(final int index, final byte[] bytes, final int offset, final int length)
			throws NullPointerException, IllegalArgumentException, IndexOutOfBoundsException {
		// TODO check params
		if (index == length) {
			append(bytes, offset, length);
		}
		shiftBytes(index, index + length);
		System.arraycopy(bytes, offset, this.bytes, index, length);
		return this;
	}
	
	/**
	 * Inserts bytes from a buffer into this builder.
	 * @param index  The index at which to insert the bytes.
	 * @param buffer  The buffer of bytes to insert.
	 * @return  {@code this}
	 * @throws NullPointerException  If {@code buffer == null}.
	 * @throws IllegalArgumentException  If {@code index < 0}.
	 * @throws IndexOutOfBoundsException  If
	 *   <code>index &gt; {@link #length()}</code>. 
	 */
	public ByteStringBuilder insert(final int index, final ByteBuffer buffer)
			throws NullPointerException, IllegalArgumentException, IndexOutOfBoundsException {
		// TODO check params
		return insert(index, buffer, buffer.remaining());
	}
	
	/**
	 * Inserts bytes from a buffer into this builder.
	 * @param index  The index at which to insert the bytes.
	 * @param buffer  The buffer of bytes to insert.
	 * @param length  The number of bytes to insert.
	 * @return  {@code this}
	 * @throws NullPointerException  If {@code buffer == null}.
	 * @throws IllegalArgumentException  If {@code index < 0} or
	 *   {@code length < 0}.
	 * @throws BufferUnderflowException  If <code>length &gt;
	 *   buffer.{@link ByteBuffer#remaining() remaining()}</code>.
	 * @throws IndexOutOfBoundsException  If
	 *   <code>index &gt; {@link #length()}</code>. 
	 */
	public ByteStringBuilder insert(final int index, final ByteBuffer buffer, final int length)
			throws NullPointerException, IllegalArgumentException, BufferUnderflowException,
			IndexOutOfBoundsException {
		// TODO check params
		shiftBytes(index, index + length);
		buffer.get(bytes, index, length);
		return this;
	}
	
	/**
	 * Deletes a byte from this builder.
	 * TODO describe postconditions
	 * @param index  The index of the byte to delete.
	 * @return  {@code this}
	 * @throws IllegalArgumentException  If {@code index < 0}.
	 * @throws IndexOutOfBoundsException  If <code>index &gt;=
	 *   {@link #length()}</code>.
	 */
	public ByteStringBuilder deleteByte(final int index)
			throws IllegalArgumentException, IndexOutOfBoundsException {
		Utils.checkIndex("index", length, index);
		if (index == length - 1) {
			return truncate(length - 1);
		}
		shiftBytes(index, index - 1);
		return this;
	}
	
	/**
	 * Truncates the content of this builder.
	 * TODO describe postconditions
	 * @param length  The new length of the builder.
	 * @return  {@code this}
	 * @throws IllegalArgumentException  If {@code length < 0}.
	 * @throws IndexOutOfBoundsException  If <code>length &gt; {@link
	 *   #length()}</code>.
	 */
	public ByteStringBuilder truncate(final int length)
			throws IllegalArgumentException, IndexOutOfBoundsException {
		if (length < 0) {
			throw new IllegalArgumentException(String.format("length (%d) < 0", length));
		} else if (length > this.length) {
			throw new IndexOutOfBoundsException(String.format("length (%d) > this.length (%d)", length, this.length));
		}
		this.length = length;
		return this;
	}
	
	/**
	 * Deletes bytes from this builder.
	 * TODO describe postconditions
	 * @param beginIndex  The index of the first byte to delete, inclusive.
	 * @param endIndex  The index of the last byte to delete, exclusive.
	 * @return  {@code this}
	 * @throws IllegalArgumentException  If {@code beginIndex < 0},
	 *   {@code endIndex < 0}, or {@code beginIndex > endIndex}.
	 * @throws IndexOutOfBoundsException  If
	 *   <code>beginIndex &gt;= {@link #length()}</code> or
	 *   {@code endIndex > length()}.
	 */
	public ByteStringBuilder delete(final int beginIndex, final int endIndex)
			throws IllegalArgumentException, IndexOutOfBoundsException {
		Utils.checkSubString(length, beginIndex, endIndex);
		if (endIndex == length) {
			return truncate(beginIndex);
		}
		shiftBytes(endIndex, beginIndex);
		return this;
	}
	
	/**
	 * Replaces the value of a byte.
	 * Afterwards, <code>{@link #at(int) at}(index) == (byte)value</code>.
	 * @param index  The index of the byte to replace.
	 * @param value  The new value for the byte, as a signed or unsigned
	 *   8-bit integer.
	 * @return  {@code this}
	 * @throws IllegalArgumentException  If {@code index < 0},
	 *   <code>value &lt; {@link Byte#MIN_VALUE}</code>, or
	 *   <code>value &gt; {@link ByteStrings#UNSIGNED_MAX}</code>.
	 * @throws IndexOutOfBoundsException  If <code>index &gt;=
	 *   {@link #length()}</code>.
	 */
	public ByteStringBuilder replace(final int index, final int value)
			throws IllegalArgumentException, IndexOutOfBoundsException {
		if (index < 0) {
			throw new IllegalArgumentException(String.format("index (%d) < 0", index));
		}
		final byte b = Utils.toByteValue(value);
		if (index >= length) {
			throw new IndexOutOfBoundsException(String.format("index (%d) >= length (%d)", index, length));
		}
		bytes[index] = b;
		return this;
	}
	
	/**
	 * Replaces a sequence of bytes with a string.
	 * TODO describe postconditions
	 * @param index  The index of the first byte to replace.
	 * @param string  The string to replace the bytes with.
	 * @return  {@code this}
	 * @throws NullPointerException  If {@code string == null}.
	 * @throws IllegalArgumentException  If {@code index < 0}.
	 * @throws IndexOutOfBoundsException  If <code>index &gt;=
	 *   {@link #length()}</code> or <code>index + string.{@link
	 *   ByteString#length() length()} &gt; length()</code>.
	 */
	public ByteStringBuilder replace(final int index, final ByteString string)
			throws NullPointerException, IllegalArgumentException, IndexOutOfBoundsException {
		if (string == null) {
			throw new NullPointerException("string == null");
		} else if (index < 0) {
			throw new IllegalArgumentException(String.format("index (%d) < 0", index));
		}
		final int strLen = string.length();
		if (index + strLen > length) {
			throw new IndexOutOfBoundsException(String.format("index (%d) + string.length (%d) > length (%d)",
					index, string.length(), length));
		}
		for (int i = 0; i < strLen; i++) {
			bytes[i + index] = string.at(i);
		}
		return this;
	}
	
	/**
	 * Replaces bytes in this builder.
	 * TODO describe postconditions
	 * @param index  The index of the first byte to replace.
	 * @param bytes  The bytes to replace with.
	 * @return  {@code this}
	 * @throws NullPointerException  If {@code bytes == null}.
	 * @throws IllegalArgumentException  If {@code index < 0}.
	 * @throws IndexOutOfBoundsException  If
	 *   <code>index + bytes.length &gt; {@link #length()}</code>.
	 */
	public ByteStringBuilder replace(final int index, final byte[] bytes)
			throws NullPointerException, IllegalArgumentException, IndexOutOfBoundsException {
		// TODO check params
		return replace(index, bytes, 0);
	}
	
	/**
	 * Replaces bytes in this builder.
	 * TODO describe postconditions
	 * @param index  The index of the first byte to replace.
	 * @param bytes  The bytes to replace with.
	 * @param offset  The index of the first byte in {@code bytes} to use.
	 * @return  {@code this}
	 * @throws NullPointerException  If {@code bytes == null}.
	 * @throws IllegalArgumentException  If {@code index < 0} or
	 *   {@code offset < 0}.
	 * @throws IndexOutOfBoundsException  If {@code offset >= bytes.length} or
	 *   <code>index + bytes.length &gt; {@link #length()} + offset</code>.
	 */
	public ByteStringBuilder replace(final int index, final byte[] bytes, final int offset)
			throws NullPointerException, IllegalArgumentException, IndexOutOfBoundsException {
		// TODO check params
		return replace(index, bytes, offset, bytes.length - offset);
	}
	
	/**
	 * Replaces bytes in this builder.
	 * TODO describe postconditions
	 * @param index  The index of the first byte to replace.
	 * @param bytes  The bytes to replace with.
	 * @param offset  The index of the first byte in {@code bytes} to use.
	 * @param length  The number of bytes to replace.
	 * @return  {@code this}
	 * @throws NullPointerException  If {@code bytes == null}.
	 * @throws IllegalArgumentException  If {@code index < 0},
	 *   {@code offset < 0}, or {@code length < 0}.
	 * @throws IndexOutOfBoundsException  If {@code offset >= bytes.length},
	 *   {@code length > bytes.length}, or
	 *   <code>index + length &gt; {@link #length()} + offset</code>.
	 */
	public ByteStringBuilder replace(final int index, final byte[] bytes, final int offset, final int length)
			throws NullPointerException, IllegalArgumentException, IndexOutOfBoundsException {
		// TODO check params
		System.arraycopy(bytes, offset, this.bytes, index, length);
		return this;
	}
	
	/**
	 * Replaces bytes in this builder with bytes from a buffer.
	 * TODO describe postconditions
	 * @param index  The index of the first byte to replace.
	 * @param buffer  The buffer of bytes to replace with.
	 * @return  {@code this}
	 * @throws NullPointerException  If {@code buffer == null}.
	 * @throws IllegalArgumentException  If {@code index < 0}.
	 * @throws IndexOutOfBoundsException  If
	 *   <code>index + buffer.{@link ByteBuffer#remaining() remaining()}
	 *   &gt; {@link #length()}</code>.
	 */
	public ByteStringBuilder replace(final int index, final ByteBuffer buffer)
			throws NullPointerException, IllegalArgumentException, IndexOutOfBoundsException {
		// TODO check params
		return replace(index, buffer, buffer.remaining());
	}
	
	/**
	 * Replaces bytes in this builder with bytes from a buffer.
	 * TODO describe postconditions
	 * @param index  The index of the first byte to replace.
	 * @param buffer  The buffer of bytes to replace.
	 * @param length  The number of bytes to replace.
	 * @return  {@code this}
	 * @throws NullPointerException  IF {@code buffer == null}.
	 * @throws IllegalArgumentException  If {@code index < 0} or
	 *   {@code length < 0}.
	 * @throws BufferUnderflowException  If <code>length &gt;
	 *   buffer.{@link ByteBuffer#remaining() remaining()}</code>.
	 * @throws IndexOutOfBoundsException  If
	 *   <code>index + length &gt; {@link #length()}</code>.
	 */
	public ByteStringBuilder replace(final int index, final ByteBuffer buffer, final int length)
			throws NullPointerException, IllegalArgumentException, BufferUnderflowException,
			IndexOutOfBoundsException {
		// TODO check params
		buffer.get(bytes, index, length);
		return this;
	}
	
	/**
	 * Creates an array from this builder.
	 * @return  A byte array {@code b} such that
	 *   <code>b[i] == this.{@link #at(int) at}(i)</code> for all {@code i}
	 *   from 0 to <code>this.{@link #length()} - 1</code>.
	 */
	public byte[] toByteArray() {
		final byte[] copy = new byte[length];
		if (length > 0) {
			System.arraycopy(bytes, 0, copy, 0, length);
		}
		return copy;
	}
	
	/**
	 * Creates a string from this builder.
	 * @return  A {@link ByteString} {@code b} such that
	 *   <code>b.{@link ByteString#at(int) at}(i) == this.{@link #at(int)
	 *   at}(i)</code> for all {@code i} from 0 to <code>this.{@link
	 *   #length()} - 1</code>.
	 */
	public ByteString toByteString() {
		if (length == 0) {
			return Utils.EMPTY_STRING;
		}
		return new ArrayByteString(toByteArray());
	}
}
