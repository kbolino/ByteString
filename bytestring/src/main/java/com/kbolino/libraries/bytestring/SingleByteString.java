package com.kbolino.libraries.bytestring;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;

/**
 * A single-byte {@link ByteString}.
 */
final class SingleByteString extends AbstractByteString {
	private final byte value;
	
	SingleByteString(byte value) {
		this.value = value;
	}

	/** {@inheritDoc} */
	public int length() {
		return 1;
	}

	/** {@inheritDoc} */
	public byte at(final int index) throws IllegalArgumentException,
			IndexOutOfBoundsException {
		checkAt(index);
		return value;
	}

	/** {@inheritDoc} */
	public ByteString subString(int beginIndex, int endIndex)
			throws IllegalArgumentException, IndexOutOfBoundsException {
		checkSubString(beginIndex, endIndex);
		if (beginIndex == endIndex) {
			return Utils.EMPTY_STRING;
		} else {
			return this;
		}
	}
	
	/** {@inheritDoc} */
	public int copyTo(byte[] bytes, int offset, int length)
			throws NullPointerException, IllegalArgumentException,
			IndexOutOfBoundsException {
		checkCopyTo(bytes, offset, length);
		if (length != 0) {
			bytes[offset] = value;
		}
		return length;
	}

	/** {@inheritDoc} */
	public int copyTo(final ByteBuffer buffer, final int length)
			throws NullPointerException, IllegalArgumentException,
			IndexOutOfBoundsException, BufferUnderflowException,
			ReadOnlyBufferException {
		checkCopyTo(buffer, length);
		if (length != 0) {
			buffer.put(value);
		}
		return length;
	}

}
