package com.kbolino.libraries.bytestring;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;

/**
 * A {@link ByteString} backed by an array of bytes.
 */
final class ArrayByteString extends AbstractByteString {
	private final byte[] bytes;
	
	static ArrayByteString concat(ByteString b1, ByteString b2) {
		final int totalLength = b1.length() + b2.length();
		final byte[] bytes = new byte[totalLength];
		b1.copyTo(bytes);
		b2.copyTo(bytes, b1.length());
		return new ArrayByteString(bytes);
	}
	
	static ArrayByteString subString(ByteString string, int beginIndex, int endIndex) {
		final int length = endIndex - beginIndex;
		final byte[] bytes = new byte[length];
		string.copyTo(bytes, beginIndex, length);
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
