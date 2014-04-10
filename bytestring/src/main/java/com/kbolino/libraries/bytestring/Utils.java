package com.kbolino.libraries.bytestring;

import java.nio.ByteBuffer;

/**
 * Core utility methods for {@link ByteString}s.
 */
final class Utils {
	/** The empty string. */
	static final ByteString EMPTY_STRING = new EmptyByteString();
	
	/** Maximum value of an unsigned 8-bit integer. */
	static final int UNSIGNED_MAX = 0xFF;
	
	/**
	 * Checks the given value as a signed or unsigned 8-bit integer.
	 * @param value  The value to check.
	 * @throws IllegalArgumentException  If
	 *   <code>value &lt; {@link Byte#MIN_VALUE}</code> or
	 *   <code>value &gt; {@link #UNSIGNED_MAX}</code>.
	 */
	static void checkByteValue(int value) throws IllegalArgumentException {
		if (value < Byte.MIN_VALUE) {
			throw new IllegalArgumentException(String.format("value (%d) < minimum (%d)", value, Byte.MIN_VALUE));
		} else if (value > UNSIGNED_MAX) {
			throw new IllegalArgumentException(String.format("value (%d) > maximum (%d)", value, UNSIGNED_MAX));
		}
	}
	
	/**
	 * Converts a signed or unsigned 8-bit integer into a byte.
	 * @param value  The value to convert.
	 * @return  A byte value equal to {@code (byte)value}.
	 * @throws IllegalArgumentException  If
	 *   <code>value &lt; {@link Byte#MIN_VALUE}</code> or
	 *   <code>value &gt; {@link #UNSIGNED_MAX}</code>.
	 */
	static byte toByteValue(final int value) throws IllegalArgumentException {
		checkByteValue(value);
		return (byte)value;
	}

	/**
	 * Checks parameters for copying to/from byte arrays.
	 * @param bytes  The array.
	 * @param offset  The first index within the array.
	 * @param length  The number of bytes to copy.
	 */
	static void checkCopyParams(final byte[] bytes, final int offset, final int length) {
		if (bytes == null) {
			throw new NullPointerException("bytes is null");
		} else if (offset < 0) {
			throw new IllegalArgumentException(
					String.format("offset (%d) < 0", offset));
		} else if (length < 0) {
			throw new IllegalArgumentException(
					String.format("length (%d) < 0", length));
		} else if (length > 0 && offset >= bytes.length) {
			throw new IndexOutOfBoundsException(
					String.format("offset (%d) >= bytes.length (%d)", offset, bytes.length));
		} else if (length > bytes.length - offset) {
			throw new IndexOutOfBoundsException(
					String.format("length (%d) > bytes.length (%d) - offset (%d)", length, bytes.length, offset));
		}
	}

	/**
	 * Checks parameters for copying to/from buffers.
	 * @param buffer  The buffer.
	 * @param length  The number of bytes to copy.
	 */
	static void checkCopyParams(final ByteBuffer buffer, final int length) {
		if (buffer == null) {
			throw new NullPointerException("buffer is null");
		} else if (length < 0) {
			throw new IllegalArgumentException(
					String.format("length (%d) < 0", length));
		}
	}
	
	/**
	 * Checks parameters for substring methods.
	 * @param string  The string.
	 * @param beginIndex  The first index, inclusive.
	 * @param endIndex  The last index, exclusive.
	 */
	static void checkSubString(ByteString string, int beginIndex, int endIndex) {
		if (beginIndex < 0) {
			throw new IllegalArgumentException(
					String.format("beginIndex (%d) < 0", beginIndex));
		} else if (endIndex < 0) {
			throw new IllegalArgumentException(
					String.format("endIndex (%d) < 0", endIndex));
		} else if (beginIndex > endIndex) {
			throw new IllegalArgumentException(
					String.format("beginIndex (%d) > endIndex (%d)", beginIndex, endIndex));
		} else {
			final int length = string.length();
			if (length > 0 && beginIndex >= length) {
				throw new IndexOutOfBoundsException(
						String.format("beginIndex (%d) >= length (%d)", beginIndex, length));
			} else if (endIndex > length) {
				throw new IndexOutOfBoundsException(
						String.format("endIndex (%d) > length (%d)", endIndex, length));
			}
		}
	}
	
	private Utils() { }
}
