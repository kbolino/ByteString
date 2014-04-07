package com.kbolino.libraries.bytestring;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Core utility methods for {@link ByteString}s.
 */
final class Utils {
	/** The empty string. */
	static final ByteString EMPTY_STRING = new EmptyByteString();
	
	/** Maximum value of an unsigned 8-bit integer. */
	static final int UNSIGNED_MAX = 0xFF;
	
	/**
	 * Converts a signed or unsigned 8-bit integer into a byte.
	 * @param value  The value to convert.
	 * @return  A byte value equal to {@code (byte)value}.
	 * @throws IllegalArgumentException  If
	 *   <code>value &lt; {@link Byte#MIN_VALUE}</code> or
	 *   <code>value &gt; {@link #UNSIGNED_MAX}</code>.
	 */
	static byte toByteValue(final int value) {
		if (value < Byte.MIN_VALUE) {
			throw new IllegalArgumentException(String.format("value (%d) < minimum (%d)", value, Byte.MIN_VALUE));
		} else if (value > UNSIGNED_MAX) {
			throw new IllegalArgumentException(String.format("value (%d) > maximum (%d)", value, UNSIGNED_MAX));
		}
		return (byte)value;
	}
	
	/**
	 * The minimum total length at which two or more strings should be roped
	 * together instead of coalesced.
	 * 
	 * TODO performance tuning
	 */
	static final int ROPE_THRESHOLD = 100;
	
	/**
	 * Concatenates two strings.
	 * Coalesces small strings and ropes together large strings at least
	 * {@link #ROPE_THRESHOLD} in length.
	 * Preconditions:
	 * <ol>
	 *   <li>{@code s1 != null}</li>
	 *   <li>{@code s2 != null}</li>
	 * </ol>
	 * @param s1  The first string.
	 * @param s2  The second string.
	 * @return  A {@link ByteString} containing the concatenation of
	 *   {@code s1} and {@code s2}.
	 */
	static ByteString concat(final ByteString s1, final ByteString s2) {
		if (s1.length() == 0) {
			return s2;
		} else if (s2.length() == 0) {
			return s1;
		}
		final int totalLength = s1.length() + s2.length();
		if (totalLength < ROPE_THRESHOLD) {
			byte[] bytes = new byte[totalLength];
			s1.copyTo(bytes);
			s2.copyTo(bytes, s1.length());
			return new ArrayByteString(bytes);
		} else {
			// don't rope ropes
			final List<ByteString> list = new ArrayList<ByteString>();
			if (s1 instanceof RopeByteString) {
				final RopeByteString rope = (RopeByteString) s1;
				list.addAll(rope.strings());
			} else {
				list.add(s1);
			}
			if (s2 instanceof RopeByteString) {
				final RopeByteString rope = (RopeByteString) s2;
				list.addAll(rope.strings());
			} else {
				list.add(s2);
			}
			final ByteString[] strings = new ByteString[list.size()];
			return new RopeByteString(list.toArray(strings));
		}
	}
	
	/**
	 * The minimum length at which a substring should be constructed using
	 * a slice instead of an array.
	 */
	static final int SLICE_THRESHOLD = 5;
	
	/**
	 * Creates a substring of a string.
	 * Uses an array for small substrings and a slice for large strings
	 * at least {@link #SLICE_THRESHOLD} in length.
	 * Preconditions:
	 * <ol>
	 *   <li>{@code string != null}</li>
	 *   <li>{@code beginIndex >= 0}</li>
	 *   <li>{@code endIndex >= beginIndex}</li>
	 *   <li>{@code endIndex <= string.length()}</li>
	 * </ol>
	 * @param string  The string.
	 * @param beginIndex  The first index, inclusive.
	 * @param endIndex  The last index, exclusive.
	 * @return  A substring of {@code string} from {@code beginIndex} to
	 *   {@code endIndex}.
	 */
	static ByteString subString(final ByteString string, final int beginIndex, final int endIndex) {
		if (string.length() == 0 || beginIndex == endIndex) {
			return EMPTY_STRING;
		}
		final int length = endIndex - beginIndex;
		if (length < SLICE_THRESHOLD) {
			final byte[] bytes = new byte[length];
			for (int i = 0; i < length; i++) {
				bytes[i] = string.at(i + beginIndex);
			}
			return new ArrayByteString(bytes);
		} else {
			// don't slice slices
			ByteString delegate = string;
			int offset = beginIndex;
			if (string instanceof ByteStringSlice) {
				final ByteStringSlice slice = (ByteStringSlice) string;
				delegate = slice.delegate();
				offset += slice.offset();
			}
			return new ByteStringSlice(delegate, offset, length);
		}
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
	
	private Utils() { }
}
