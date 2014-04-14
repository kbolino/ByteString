package com.kbolino.libraries.bytestring;

/**
 * A {@link ByteString} formed by repeating another string.
 */
class RepeatedByteString extends AbstractByteString {
	private final ByteString string;
	private final int times;
	
	/**
	 * Creates a new {@link RepeatedByteString}.  Constructor preconditions:
	 * <ol>
	 *   <li>{@code string != null}</li>
	 *   <li>{@code times >= 0}</li>
	 * </ol>
	 * @param string  The string to repeat.
	 * @param times  The number of times to repeat the string.
	 */
	RepeatedByteString(ByteString string, int times) {
		super();
		this.string = string;
		this.times = times;
	}

	/** {@inheritDoc} */
	public int length() {
		return string.length() * times;
	}

	/** {@inheritDoc} */
	public byte at(int index) throws IllegalArgumentException,
			IndexOutOfBoundsException {
		checkAt(index);
		return string.at(index % string.length());
	}
	
	/** {@inheritDoc} */
	@Override
	public int indexOf(int value, int fromIndex)
			throws IllegalArgumentException, IndexOutOfBoundsException {
		checkIndexOf(value, fromIndex);
		final int shifts = fromIndex / string.length();
		final int shifted = fromIndex % string.length();
		return string.indexOf(value, shifted) + shifts * string.length();
	}

}
