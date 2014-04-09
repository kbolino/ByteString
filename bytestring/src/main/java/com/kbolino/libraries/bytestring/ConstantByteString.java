package com.kbolino.libraries.bytestring;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;

/**
 * A {@link ByteString} that contains repetitions of a single byte value.
 */
final class ConstantByteString extends AbstractByteString {
	private final byte value;
	private final int length;
	
	/**
	 * Creates a new {@link ConstantByteString}.  Constructor preconditions:
	 * <ol>
	 *   <li>{@code length >= 0}</li>
	 * </ol>
	 * @param value  The byte value.
	 * @param length  The number of repetitions of the value.
	 */
	ConstantByteString(byte value, int length) {
		super();
		this.value = value;
		this.length = length;
	}

	/** {@inheritDoc} */
	public int length() {
		return length;
	}

	/** {@inheritDoc} */
	public byte at(final int index) throws IllegalArgumentException,
			IndexOutOfBoundsException {
		checkAt(index);
		return value;
	}
	
	/** {@inheritDoc} */
	@Override
	public int indexOf(int value, int fromIndex)
			throws IllegalArgumentException, IndexOutOfBoundsException {
		checkIndexOf(fromIndex);
		final byte val = Utils.toByteValue(value);
		if (val == this.value) {
			return fromIndex;
		} else {
			return -1;
		}
	}

	/** {@inheritDoc} */
	public ByteString subString(int beginIndex, int endIndex)
			throws IllegalArgumentException, IndexOutOfBoundsException {
		checkSubString(beginIndex, endIndex);
		if (beginIndex == endIndex) {
			return Utils.EMPTY_STRING;
		} else {
			return new ConstantByteString(value, endIndex - beginIndex);
		}
	}

	/** {@inheritDoc} */
	public int copyTo(final ByteBuffer buffer, final int length)
			throws NullPointerException, IllegalArgumentException,
			IndexOutOfBoundsException, BufferUnderflowException,
			ReadOnlyBufferException {
		checkCopyTo(buffer, length);
		for (int i = 0; i < length; i++) {
			buffer.put(value);
		}
		return length;
	}

}
