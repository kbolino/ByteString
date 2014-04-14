package com.kbolino.libraries.bytestring;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;

/**
 * A {@link ByteString} backed by an array of bytes.
 */
final class ArrayByteString extends AbstractByteString {
	private final byte[] bytes;
	
	/**
	 * Concatenate two strings into a new {@link ArrayByteString}.  Preconditions:
	 * <ol>
	 *   <li>{@code b1 != null}</li>
	 *   <li>{@code b2 != null}</li>
	 *   <li>Preferably, neither {@code b1.length() == 0} nor 
	 *     {@code b2.length() == 0}</li>
	 * </ol>
	 * @param b1  The first string.
	 * @param b2  The second string.
	 * @return  An {@link ArrayByteString} equal to {@code b1 + b2}.
	 */
	static ArrayByteString concat(final ByteString b1, final ByteString b2) {
		final int totalLength = b1.length() + b2.length();
		final byte[] bytes = new byte[totalLength];
		b1.copyTo(bytes);
		b2.copyTo(bytes, b1.length());
		return new ArrayByteString(bytes);
	}
	
	/**
	 * Create a substring in a new {@link ArrayByteString}.  Preconditions:
	 * <ol>
	 *   <li>{@code string != null}</li>
	 *   <li>{@code beginIndex >= 0}</li>
	 *   <li>{@code endIndex >= beginIndex}</li>
	 *   <li>{@code beginIndex < string.length()}</li>
	 *   <li>{@code endIndex <= string.length()}</li>
	 *   <li>Preferably, {@code endIndex - beginIndex > 0}</li>
	 * </ol>
	 * @param string  The string.
	 * @param beginIndex  The first index, inclusive.
	 * @param endIndex  The last index, exclusive.
	 * @return  An {@link ArrayByteString} equal to
	 *   {@code string[beginIndex:endIndex]}.
	 */
	static ArrayByteString subString(final ByteString string, final int beginIndex, final int endIndex) {
		final int length = endIndex - beginIndex;
		final byte[] bytes = new byte[length];
		for (int i = 0; i < length; i++) {
			bytes[i] = string.at(i + beginIndex);
		}
		return new ArrayByteString(bytes);
	}
	
	/**
	 * Creates a new {@link ArrayByteString}. Constructor preconditions:
	 * <ol>
	 *   <li>{@code bytes != null}</li>
	 *   <li>{@code bytes} is exclusively held by this object</li>
	 * </ol>
	 * @param bytes  The backing array of bytes.
	 */
	ArrayByteString(final byte[] bytes) {
		super();
		this.bytes = bytes;
	}

	/** {@inheritDoc} */
	public int length() {
		return bytes.length;
	}

	/** {@inheritDoc} */
	public byte at(final int index) throws IllegalArgumentException,
			IndexOutOfBoundsException {
		checkAt(index);
		return bytes[index];
	}
	
	/** {@inheritDoc} */
	@Override
	public ByteBuffer toReadOnlyByteBuffer() {
		return ByteBuffer.wrap(bytes).asReadOnlyBuffer();
	}

	/** {@inheritDoc} */
	public int copyTo(final byte[] bytes, final int offset, final int length)
			throws NullPointerException, IllegalArgumentException,
			IndexOutOfBoundsException {
		checkCopyTo(bytes, offset, length);
		System.arraycopy(this.bytes, 0, bytes, offset, length);
		return length;
	}
	
	/** {@inheritDoc} */
	public int copyTo(final ByteBuffer buffer, final int length)
			throws NullPointerException, IllegalArgumentException,
			IndexOutOfBoundsException, BufferUnderflowException,
			ReadOnlyBufferException {
		checkCopyTo(buffer, length);
		buffer.put(bytes, 0, length);
		return length;
	}

	/** {@inheritDoc} */
	public ByteString subString(final int beginIndex, final int endIndex)
			throws IllegalArgumentException, IndexOutOfBoundsException {
		checkSubString(beginIndex, endIndex);
		final int length = endIndex - beginIndex;
		if (length == 0) {
			return Utils.EMPTY_STRING;
		} else {
			final byte[] copy = new byte[length];
			System.arraycopy(bytes, beginIndex, copy, 0, length);
			return new ArrayByteString(copy);
		}
	}

}
