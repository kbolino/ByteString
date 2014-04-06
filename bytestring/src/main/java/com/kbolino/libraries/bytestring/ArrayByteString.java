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

}
