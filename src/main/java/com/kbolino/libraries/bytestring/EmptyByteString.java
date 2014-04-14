package com.kbolino.libraries.bytestring;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;

/**
 * An empty {@link ByteString}.
 */
final class EmptyByteString extends AbstractByteString {
	EmptyByteString() { }
	
	/** {@inheritDoc} */
	public int length() {
		return 0;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean isEmpty() {
		return true;
	}

	/** {@inheritDoc} */
	public byte at(final int index) throws IllegalArgumentException, IndexOutOfBoundsException {
		checkAt(index);
		throw new Error("unreachable code");
	}

	/** {@inheritDoc} */
	public ByteString subString(final int beginIndex, final int endIndex)
			throws IllegalArgumentException, IndexOutOfBoundsException {
		checkSubString(beginIndex, endIndex);
		return this;
	}

	/** {@inheritDoc} */
	public byte[] toByteArray() {
		return Utils.EMPTY_BYTE_ARRAY;
	}
	
	/** {@inheritDoc} */
	public ByteBuffer toByteBuffer() {
		return Utils.EMPTY_BYTE_BUFFER;
	}

	/** {@inheritDoc} */
	public int copyTo(final byte[] bytes, final int offset, final int length)
			throws NullPointerException, IllegalArgumentException,
			IndexOutOfBoundsException {
		checkCopyTo(bytes, offset, length);
		return 0;
	}

	/** {@inheritDoc} */
	public int copyTo(final ByteBuffer buffer, final int length)
			throws NullPointerException, IllegalArgumentException,
			IndexOutOfBoundsException, BufferUnderflowException,
			ReadOnlyBufferException {
		checkCopyTo(buffer, length);
		return 0;
	}

}
