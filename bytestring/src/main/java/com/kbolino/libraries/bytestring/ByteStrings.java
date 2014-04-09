package com.kbolino.libraries.bytestring;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Methods for obtaining {@code ByteString}s.
 */
public final class ByteStrings {
	/**
	 * The maximum value of an unsigned 8-bit integer.
	 */
	public static final int UNSIGNED_MAX = Utils.UNSIGNED_MAX;
	
	/**
	 * Creates a new string from a byte array.
	 * @param bytes  The array to copy from.
	 * @return  A {@link ByteString} {@code b} such that
	 *   <code>b.{@link ByteString#at(int) at}(i) == bytes[i]</code> for all
	 *   {@code i} from 0 to {@code bytes.length}.
	 * @throws NullPointerException  If {@code bytes} is null.
	 */
	public static ByteString copyFrom(byte[] bytes) throws NullPointerException {
		return copyFrom(bytes, 0, bytes.length);
	}
	
	/**
	 * Creates a new string from a byte array.
	 * @param bytes  The array to copy from.
	 * @param offset  The first index of the array to copy.
	 * @param length  The number of elements from the array to copy.
	 * @return  A {@link ByteString} {@code b} such that
	 *   <code>b.{@link ByteString#at(int) at}(i) == bytes[i]</code> for all
	 *   {@code i} from {@code offset} to {@code offset + length - 1}.
	 * @throws NullPointerException  If {@code bytes} is null.
	 * @throws IllegalArgumentException  If {@code offset < 0} or
	 *   {@code length < 0}.
	 * @throws IndexOutOfBoundsException  If {@code offset >= bytes.length}
	 *   or {@code length > bytes.length - offset}.
	 */
	public static ByteString copyFrom(byte[] bytes, int offset, int length) {
		Utils.checkCopyParams(bytes, offset, length);
		if (length == 0) {
			return empty();
		}
		final byte[] copy = new byte[length];
		System.arraycopy(bytes, offset, copy, 0, length);
		return new ArrayByteString(copy);
	}
	
	/**
	 * Creates a new string from a byte buffer.
	 * Advances the buffer's {@linkplain ByteBuffer#position() position} to
	 * its {@linkplain ByteBuffer#limit() limit}.
	 * @param buffer  The buffer to copy from.
	 * @return  A {@link ByteString} {@code b} such that
	 *   <code>b.{@link ByteString#at(int) at}(i) ==
	 *   buffer.{@link ByteBuffer#get(int) get}(i)</code> for all
	 *   {@code i} from {@code buffer.position()} (initially) to
	 *   {@code buffer.limit()}.
	 */
	public static ByteString copyFrom(ByteBuffer buffer) {
		return copyFrom(buffer, buffer.remaining());
	}
	
	/**
	 * Creates a new string from a byte buffer.
	 * Advances the buffer's {@linkplain ByteBuffer#position() position} by
	 * {@code length}.
	 * @param buffer  The buffer to copy from.
	 * @param length  The number of bytes to copy.
	 * @return  A {@link ByteString} {@code b} such that
	 *   <code>b.{@link ByteString#at(int) at}(i) ==
	 *   buffer.{@link ByteBuffer#get(int) get}(i)</code> for all
	 *   {@code i} from {@code buffer.position()} (initially) to
	 *   {@code buffer.position() - 1} (after).
	 */
	public static ByteString copyFrom(ByteBuffer buffer, int length) {
		Utils.checkCopyParams(buffer, length);
		if (length == 0) {
			return empty();
		}
		final byte[] bytes = new byte[length];
		buffer.get(bytes);
		return new ArrayByteString(bytes);
	}
	
	/**
	 * The empty string.
	 * @return  A singleton {@link ByteString} {@code b} where
	 *   <code>b.{@link ByteString#length() length()} == 0</code>.
	 */
	public static ByteString empty() {
		return Utils.EMPTY_STRING;
	}
	
	/**
	 * Creates a new string from zero or more values.
	 * The values are {@code int}s instead of {@code byte}s to allow for
	 * unsigned byte values above 127 to be represented easily.
	 * @param values  The values to copy.
	 * @return  A {@link ByteString} {@code b} such that
	 *   <code>b.{@link ByteString#at(int) at}(i) == (byte)values[i]</code>
	 *   for all {@code i} from 0 to {@code values.length}.
	 * @throws NullPointerException  If {@code values} is null.
	 * @throws IllegalArgumentException  If any of {@code values} is not a
	 *   valid signed or unsigned 8-bit integer.
	 */
	public static ByteString of(final int... values) throws NullPointerException, IllegalArgumentException {
		if (values == null) {
			throw new NullPointerException("values is null");
		}
		final int length = values.length;
		if (length == 0) {
			return empty();
		} else if (length == 1) {
			return new SingleByteString((byte)values[0]);
		}
		final byte[] bytes = new byte[length];
		for (int i = 0; i < length; i++) {
			final byte byteVal;
			try {
				byteVal = Utils.toByteValue(values[i]);
			} catch (final IllegalArgumentException e) {
				throw new IllegalArgumentException(String.format("values[%d] is not valid", i), e);
			}
			bytes[i] = byteVal;
		}
		return new ArrayByteString(bytes);
	}
	
	/**
	 * Creates a new string consisting entirely of zeros.
	 * @param length  The number of bytes to have in the string.
	 * @return  A {@link ByteString} {@code b} such that
	 *   <code>b.{@link ByteString#at(int) at}(i) == 0</code> for all
	 *   {@code i} from 0 to {@code length - 1}.
	 * @throws IllegalArgumentException   If {@code length < 0}.
	 */
	public static ByteString zeros(final int length) {
		return fill(0, length);
	}
	
	/**
	 * Creates a new string consisting entirely of the same value.
	 * @param value  The value to fill the string with.
	 * @param length  The number of bytes to have in the string.
	 * @return  A {@link ByteString} {@code b} such that
	 *   <code>b.{@link ByteString#at(int) at}(i) == (byte)value</code> for
	 *   all {@code i} from 0 to {@code length - 1}.
	 * @throws IllegalArgumentException  If {@code length < 0} or 
	 *   {@code value} is not a valid signed or unsigned 8-bit integer.
	 */
	public static ByteString fill(final int value, final int length) throws IllegalArgumentException {
		if (length < 0) {
			throw new IllegalArgumentException(String.format("length (%d) < 0", length));
		} else if (length == 0) {
			return empty();
		}
		final byte byteVal = Utils.toByteValue(value);
		return new ConstantByteString(byteVal, length);
	}
	
	/**
	 * Creates a new string by concatenating the given strings in order.
	 * @param strings  The strings to concatenate.
	 * @return  A {@link ByteString} equal to <code>strings[0].{@link
	 *   ByteString#concat(ByteString) concat}(strings[1].concat(strings[2]
	 *   ...))</code>.
	 * @throws NullPointerException  If {@code strings} or any of its
	 *   elements are null.
	 */
	public static ByteString concat(final ByteString... strings) throws NullPointerException {
		if (strings == null) {
			throw new NullPointerException("strings is null");
		} else {
			return concat(Arrays.asList(strings));
		}
	}
	
	/**
	 * Creates a new string by concatenating the given strings in order.
	 * @param strings  The strings to concatenate.
	 * @return  A {@link ByteString} equal to the concatenation of the
	 *   elements of {@code strings}.
	 * @throws NullPointerException  If {@code strings} or any of its
	 *   elements are null.
	 */
	public static ByteString concat(final Iterable<ByteString> strings) throws NullPointerException {
		if (strings == null) {
			throw new NullPointerException("strings is null");
		}
		final Iterator<ByteString> it = strings.iterator();
		ByteString string = empty();
		for (int i = 0; it.hasNext(); i++) {
			final ByteString s = it.next();
			if (s == null) {
				throw new NullPointerException(String.format("element %d of strings is null", i));
			}
			string = string.concat(s);
		}
		return string;
	}
	
	/**
	 * Creates a new string from a sequence of consecutive byte values.
	 * @param lower  The lower bound of the byte range, inclusive.
	 * @param upper  The upper bound of the byte range, exclusive.
	 * @return  A {@link ByteString} {@code b} such that
	 *   <code>b.{@link ByteString#at(int) at}(i) == (byte)(lower + i)</code>
	 *   for all {@code i} from 0 to {@code upper - lower}.
	 * @throws IllegalArgumentException  If <code>lower &lt; {@link
	 *   Byte#MIN_VALUE}</code> or <code>upper &gt; {@link Byte#MAX_VALUE}
	 *   + 1</code> or {@code lower > upper}.
	 */
	public static ByteString range(final int lower, final int upper) throws IllegalArgumentException {
		if (lower < Byte.MIN_VALUE) {
			throw new IllegalArgumentException(
					String.format("lower (%d) < Byte.MIN_VALUE (%d)", lower, Byte.MIN_VALUE));
		} else if (upper > Byte.MAX_VALUE + 1) {
			throw new IllegalArgumentException(
					String.format("upper (%d) > Byte.MAX_VALUE (%d) + 1", upper, Byte.MAX_VALUE));
		} else if (lower > upper) {
			throw new IllegalArgumentException(String.format("lower (%d) > upper (%d)", lower, upper));
		}
		return new RangeByteString(lower, upper);
	}
	
	/**
	 * Creates a new string from a sequence of consecutive unsigned 8-bit
	 * integer values.
	 * @param lower  The lower bound of the range, inclusive.
	 * @param upper  The upper bound of the range, exclusive.
	 * @return  A {@link ByteString} {@code b} such that
	 *   <code>b.{@link ByteString#at(int) at}(i) == (byte)(lower + i)</code>
	 *   for all {@code i} from 0 to {@code upper - lower}.
	 * @throws IllegalArgumentException  If <code>lower &lt; 0</code> or
	 *   <code>upper &gt; {@link #UNSIGNED_MAX} + 1</code> or {@code lower
	 *   > upper}.
	 */
	public static ByteString unsignedRange(final int lower, final int upper) throws IllegalArgumentException {
		if (lower < 0) {
			throw new IllegalArgumentException(String.format("lower (%d) < 0", lower));
		} else if (upper > UNSIGNED_MAX + 1) {
			throw new IllegalArgumentException(
					String.format("upper (%d) > UNSIGNED_MAX (%d) + 1", upper, UNSIGNED_MAX));
		} else if (lower > upper) {
			throw new IllegalArgumentException(String.format("lower (%d) > upper (%d)", lower, upper));
		}
		return new RangeByteString(lower, upper);
	}
	
	/**
	 * Creates a new string by repeating another string.
	 * @param string  The string to repeat.
	 * @param times  The number of times to repeat the string.
	 * @return  A {@link ByteString} {@code b} such that
	 *   <code>b.{@link ByteString#at(int) at}(i) == string.at(i
	 *   % string.length())</code> for all {@code i} from 0 to
	 *   {@code string.length() * times}.
	 * @throws NullPointerException  If {@code string == null}.
	 * @throws IllegalArgumentException  If {@code times < 0}.
	 */
	public static ByteString repeat(final ByteString string, final int times)
			throws NullPointerException, IllegalArgumentException {
		if (string == null) {
			throw new NullPointerException("string is null");
		} else if (times < 0) {
			throw new IllegalArgumentException(String.format("times (%d) < 0", times));
		} else if (times == 0) {
			return empty();
		} else {
			return new RepeatedByteString(string, times);
		}
	}
	
	private ByteStrings() { }
}
