package com.kbolino.libraries.bytestring;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;

/**
 * An immutable string of bytes.
 * Analogous to {@link String} but for {@code byte}s instead of {@code char}s.
 * @see ByteStrings
 */
public interface ByteString {
	/**
	 * Returns the length of this string.
	 * @return  The number of bytes in this {@link ByteString}.
	 */
	public int length();

	/**
	 * Returns the value at the given index.
	 * @param index  The index of the byte to retrieve.
	 * @return  The value of the byte at {@code index}.
	 * @throws IllegalArgumentException  If {@code index < 0}.
	 * @throws IndexOutOfBoundsException  If 
	 *   <code>index &gt;= {@link #length()}</code>
	 */
	public byte at(int index) throws IllegalArgumentException, IndexOutOfBoundsException;
	
	/**
	 * Returns the unsigned value at the given index.
	 * @param index  The index of the byte to retrieve.
	 * @return  The unsigned 8-bit value of the byte at {@code index}.
	 * @throws IllegalArgumentException  If {@code index < 0}.
	 * @throws IndexOutOfBoundsException  If 
	 *   <code>index &gt;= {@link #length()}</code>
	 */
	public int unsignedAt(int index) throws IllegalArgumentException, IndexOutOfBoundsException;
	
	/**
	 * Creates a substring of this string.
	 * @param beginIndex  The index of the first byte in the substring,
	 *   inclusive.
	 * @return  A {@link ByteString} {@code b} such that
	 *   <code>b.{@link #at(int) at}(i) == this.at(i)</code> for all
	 *   {@code i} from {@code beginIndex} to
	 *   <code>{@link #length()} - 1</code>.
	 * @throws IllegalArgumentException  If {@code beginIndex < 0}.
	 * @throws IndexOutOfBoundsException  If
	 * <code>beginIndex &gt;= {@link #length()}</code>.
	 */
	public ByteString subString(int beginIndex)
			throws IllegalArgumentException, IndexOutOfBoundsException;
	
	/**
	 * Creates a substring of this string.
	 * @param beginIndex  The index of the first byte in the substring,
	 *   inclusive.
	 * @param endIndex  The index of the last byte in the substring,
	 *   exclusive.
	 * @return  A {@link ByteString} {@code b} such that
	 *   <code>b.{@link #at(int) at}(i) == this.at(i)</code> for all
	 *   {@code i} from {@code beginIndex} to {@code endIndex - 1}.
	 * @throws IllegalArgumentException  If {@code beginIndex < 0},
	 *   {@code endIndex < 0}, or {@code beginIndex > endIndex}.
	 * @throws IndexOutOfBoundsException  If
	 * <code>beginIndex &gt;= {@link #length()}</code> or
	 * <code>endIndex &gt; length()</code>.
	 */
	public ByteString subString(int beginIndex, int endIndex)
			throws IllegalArgumentException, IndexOutOfBoundsException;
	
	/**
	 * Creates a buffer from this string.
	 * The buffer's {@linkplain ByteBuffer#position() position} will be 0,
	 * its capacity will be {@link #length()}, and its limit will be equal
	 * to its capacity.
	 * 
	 * @return  A {@link ByteBuffer} {@code b} such that
	 *   <code>b.{@link ByteBuffer#get() get}(i) ==
	 *   this.{@link #at(int) at}(i)</code> for all {@code i} from 0
	 *   to {@code this.length() - 1}.
	 *   
	 */
	public ByteBuffer toByteBuffer();
	
	/**
	 * Creates an array from this string.
	 * @return  A byte array {@code b} such that
	 *   <code>b[i] == this.{@link #at(int) at}(i)</code> for all
	 *   {@code i} from 0 to <code>this.{@link #length()} - 1</code>.
	 */
	public byte[] toByteArray();
	
	/**
	 * Copies bytes from this string into an array.
	 * 
	 * <p>Afterwards, <code>bytes[i] == {@link #at(int) at}(i)</code> for all
	 * {@code i} from 0 to <code>{@link #length()} - 1</code>
	 * 
	 * @param bytes  The array to receive the bytes.
	 * @return  The number of bytes copied.
	 * @throws NullPointerException  If {@code bytes == null}.
	 * @throws IndexOutOfBoundsException  If {@code length() > bytes.length}.
	 */
	public int copyTo(byte[] bytes) throws NullPointerException, IndexOutOfBoundsException;
	
	/**
	 * Copies bytes from this string into an array.
	 * 
	 * <p>Afterwards, <code>bytes[i + offset] == {@link #at(int) at}(i)</code>
	 * for all {@code i} from 0 to <code>{@link #length()} - 1</code>.
	 * 
	 * @param bytes  The array to receive the bytes.
	 * @param offset  The first index of the array to receive a byte.
	 * @return  The number of bytes copied.
	 * @throws NullPointerException  If {@code bytes} is null.
	 * @throws IllegalArgumentException  If {@code offset < 0}.
	 * @throws IndexOutOfBoundsException  If {@code offset >= bytes.length}
	 *   or {@code length() > bytes.length}.
	 */
	public int copyTo(byte[] bytes, int offset) throws NullPointerException,
			IllegalArgumentException, IndexOutOfBoundsException;
	
	/**
	 * Copies bytes from this string into an array.
	 * 
	 * <p>Afterwards, <code>bytes[i + offset] == {@link #at(int) at}(i)</code>
	 * for all {@code i} from 0 to {@code length}.
	 * 
	 * @param bytes  The array to receive the bytes.
	 * @param offset  The first index of the array to receive a byte.
	 * @param length  The number of bytes to copy into the array.
	 * @return  The number of bytes copied.
	 * @throws NullPointerException  If {@code bytes} is null.
	 * @throws IllegalArgumentException  If {@code offset < 0} or
	 *   {@code length < 0}.
	 * @throws IndexOutOfBoundsException  If {@code offset >= bytes.length},
	 *   {@code length > bytes.length - offset}, or
	 *   <code>length &gt; {@link #length()}</code>.
	 */
	public int copyTo(byte[] bytes, int offset, int length)
			throws NullPointerException, IllegalArgumentException, IndexOutOfBoundsException;
	
	/**
	 * Copies bytes from this string into a buffer.
	 * Advances the buffer's position by the number of bytes copied.
	 * 
	 * <p>Let <code>offset = buffer.position()</code> at the start.
	 * Then afterwards, <code>buffer.{@link ByteBuffer#get() get}(i + offset)
	 * == {@link #at(int) at}(i)</code> for all {@code i} from 0 to
	 * <code>{@link #length()} - 1</code>.
	 * 
	 * @param buffer  The buffer to receive the bytes.
	 * @return  The number of bytes copied.
	 * @throws NullPointerException  If {@code buffer == null}.
	 * @throws BufferOverflowException  If <code>buffer.{@link
	 *   ByteBuffer#remaining() remaining()} &lt; length()</code>.
	 * @throws ReadOnlyBufferException  If the buffer is read-only.
	 */
	public int copyTo(ByteBuffer buffer) throws NullPointerException,
			BufferOverflowException, ReadOnlyBufferException;
	
	/**
	 * Copies bytes from this string into a buffer.
	 * Advances the buffer's position by the number of bytes copied.
	 * 
	 * <p>Let <code>offset = buffer.position()</code> at the start.
	 * Then afterwards, <code>buffer.{@link ByteBuffer#get() get}(i + offset)
	 * == {@link #at(int) at}(i)</code> for all {@code i} from 0 to
	 * {@code length - 1}.
	 * 
	 * @param buffer  The buffer to receive the bytes.
	 * @param length  The number of bytes to copy.
	 * @return  The number of bytes copied.
	 * @throws NullPointerException  If {@code buffer} is null.
	 * @throws IllegalArgumentException  If {@code length < 0}.
	 * @throws IndexOutOfBoundsException  If <code>length &gt;
	 *   {@link #length()}</code>.
	 * @throws BufferOverflowException  If
	 *   <code>buffer.{@link ByteBuffer#remaining() remaining()}
	 *   &lt; length</code>.
	 * @throws ReadOnlyBufferException  If the buffer is read-only.
	 */
	public int copyTo(ByteBuffer buffer, int length) throws NullPointerException,
			IllegalArgumentException, IndexOutOfBoundsException, BufferOverflowException,
			ReadOnlyBufferException;
	
	/**
	 * Concatenates this string with another.
	 * @param string  The string to append.
	 * @return  A {@link ByteString} {@code b} such that
	 *   <code>b.{@link #at(int) at}(i) == this.at(i)</code> for all
	 *   {@code i} from 0 to <code>this.{@link #length()} - 1</code>, and
	 *   <code>b.at(i) == string.at(i)</code> for all {@code i} from
	 *   <code>this.length()</code> to <code>this.length() +
	 *   string.length() - 1</code>.
	 * @throws NullPointerException  If {@code string} is null.
	 */
	public ByteString concat(ByteString string) throws NullPointerException;
	
	/**
	 * Finds a substring within this string.
	 * @param string  The string to find.
	 * @param fromIndex  The first index to check.
	 * @return  The index of the first substring equal to {@code string}
	 *   at or after {@code fromIndex}, or {@code -1} if this string does
	 *   not contain a substring equal to {@code string}.
	 * @throws NullPointerException  If {@code string} is null.
	 * @throws IllegalArgumentException  If {@code fromIndex < 0}.
	 * @throws IndexOutOfBoundsException  If <code>fromIndex &gt;=
	 *   {@link #length()}</code>.
	 */
	public int indexOf(ByteString string, int fromIndex)
			throws NullPointerException, IllegalArgumentException, IndexOutOfBoundsException;
	
	/**
	 * Finds a substring within this string.
	 * @param string  The string to find.
	 * @return  The index of the first substring equal to {@code string},
	 *   or {@code -1} if the this string does not contain a substring
	 *   equal to {@code string}.
	 * @throws NullPointerException  If {@code string} is null.
	 */
	public int indexOf(ByteString string) throws NullPointerException;
	
	/**
	 * Finds a byte value within this string.
	 * @param value  The value to find.
	 * @param fromIndex  The first index to check.
	 * @return  The index of the first byte equal to {@code value} at or
	 *   after {@code fromIndex}, or {@code -1} if this string does not
	 *   contain such a byte.
	 * @throws IllegalArgumentException  If {@code fromIndex < 0} or
	 *   {@code value} is not in the range of a signed or unsigned 8-bit
	 *   integer.
	 * @throws IndexOutOfBoundsException  If <code>fromIndex &gt;=
	 *   {@link #length()}</code>.
	 */
	public int indexOf(int value, int fromIndex) throws IllegalArgumentException,
			IndexOutOfBoundsException;
	
	/**
	 * Finds a byte value within this string.
	 * @param value  The value to find.
	 * @return  The index of the first byte equal to {@code value}, or
	 *   {@code -1} if this string does not contain such a byte.
	 * @throws IllegalArgumentException  If value is not in the range
	 *   of a signed or unsigned 8-bit integer.
	 */
	public int indexOf(int value) throws IllegalArgumentException;
	
	
	/**
	 * Does this string contain a certain string?
	 * @param string  The string to find.
	 * @return  True if and only if <code>{@link #indexOf(ByteString)
	 *   indexOf}(string) != -1</code>.
	 * @throws NullPointerException  If {@code string} is null.
	 */
	public boolean contains(ByteString string) throws NullPointerException;
	
	/**
	 * Does this string contain a certain value?
	 * @param value  The value to find.
	 * @return  True if and only <code>{@link #indexOf(int) indexOf}(value)
	 *   != -1</code>.
	 * @throws IllegalArgumentException  If value is not in the range
	 *   of a signed or unsigned 8-bit integer.
	 */
	public boolean contains(int value) throws IllegalArgumentException;
	
	/**
	 * Does this string start with a certain string?
	 * @param string  The initial string.
	 * @return  True if and only if <code>{@link #indexOf(ByteString)
	 *   indexOf}(string) == 0</code>.
	 * @throws NullPointerException  If {@code string} is null.
	 */
	public boolean startsWith(ByteString string) throws NullPointerException;
	
	/**
	 * Does this string end with a certain string?
	 * @param string  The terminal string.
	 * @return  True if and only if <code>{@link #indexOf(ByteString)
	 *   indexOf}(string) == {@link #length()} - string.length() - 1</code>.
	 * @throws NullPointerException  If {@code string} is null.
	 */
	public boolean endsWith(ByteString string) throws NullPointerException;
	
}
