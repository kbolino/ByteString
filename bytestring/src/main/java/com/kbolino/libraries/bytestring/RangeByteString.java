package com.kbolino.libraries.bytestring;

/**
 * A {@link ByteString} formed from a sequence of consecutive byte values.
 */
class RangeByteString extends AbstractByteString {
	private final int lower;
	private final int upper;
	
	/**
	 * Creates a new {@link RangeByteString}.  Constructor preconditions:
	 * <ol>
	 *   <li>{@code lower < upper}; and</li>
	 *   <li><code>lower &gt;= {@link Byte#MIN_VALUE}</code> and
	 *     <code>upper &lt;= {@link Byte#MAX_VALUE} + 1</code>; or</li>
	 *   <li>{@code lower >= 0} and <code>upper &lt;= {@link
	 *     Utils#UNSIGNED_MAX} + 1</code></li>
	 * </ol>
	 * @param lower  The lower bound, inclusive.
	 * @param upper  The upper bound, exclusive.
	 */
	RangeByteString(int lower, int upper) {
		super();
		this.lower = lower;
		this.upper = upper;
	}

	/** {@inheritDoc} */
	public int length() {
		return upper - lower;
	}

	/** {@inheritDoc} */
	public byte at(int index) throws IllegalArgumentException, IndexOutOfBoundsException {
		checkAt(index);
		return (byte)(lower + index);
	}

}
