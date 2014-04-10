package com.kbolino.libraries.bytestring;


/**
 * A slice of a {@link ByteString}.
 * A slice is composed from another string and appears to be a substring of
 * that string, without copying it.
 */
final class SlicedByteString extends AbstractByteString {
	private final ByteString delegate;
	private final int offset;
	private final int length;
	
	/**
	 * Creates a new {@link SlicedByteString}.  Constructor preconditions:
	 * <ol>
	 *   <li>{@code delegate != null}</li>
	 *   <li>{@code delegate} is not itself a {@link SlicedByteString}</li>
	 *   <li>{@code offset >= 0}</li>
	 *   <li>{@code offset < delegate.length()}</li>
	 *   <li>{@code length >= 0}</li>
	 *   <li>{@code length <= delegate.length() - offset}</li>
	 * </ol>
	 * @param delegate  The underlying string.
	 * @param offset  The index of the first accessible byte from
	 *   {@code delegate}.
	 * @param length  The number of contiguous bytes accessible from
	 *   {@code delegate}, starting with the byte at {@code offset}.
	 */
	SlicedByteString(final ByteString delegate, final int offset, final int length) {
		this.delegate = delegate;
		this.offset = offset;
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
		return delegate.at(index + offset);
	}
	
	/** {@inheritDoc} */
	@Override
	public int indexOf(int value, int fromIndex)
			throws IllegalArgumentException, IndexOutOfBoundsException {
		checkIndexOf(value, fromIndex);
		return delegate.indexOf(value, fromIndex + offset);
	}
	
	/** {@inheritDoc} */
	public ByteString subString(final int beginIndex, final int endIndex)
			throws IllegalArgumentException, IndexOutOfBoundsException {
		checkSubString(beginIndex, endIndex);
		return delegate.subString(beginIndex + offset, endIndex + offset);
	}
	
	/**
	 * The string underlying this slice.
	 * @return  An unsliced {@link ByteString}.
	 */
	public ByteString delegate() {
		return delegate;
	}

	/**
	 * The index of the first accessible byte from {@link #delegate()}.
	 * The index of the last accessible byte is <code>offset() + length()
	 * - 1</code>.
	 * @return  A nonnegative integer less than {@code delegate.length()}.
	 */
	public int offset() {
		return offset;
	}

}
