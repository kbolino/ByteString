package com.kbolino.libraries.bytestring;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A rope is a {@link ByteString} made of other strings.
 */
final class RopeByteString extends AbstractByteString {
	// TODO serialization?
	private final ByteString[] strings;
	private final transient int[] offsets;
	private final transient int length;
	
	/**
	 * Creates a new {@code RopeByteString}.  Constructor preconditions:
	 * <ol>
	 *   <li>{@code strings != null}</li>
	 *   <li>{@code strings.length > 0}</li>
	 *   <li>No element of {@code strings} is null or itself a {@link 
	 *     RopeByteString}</li>
	 *   <li>{@code strings} is not accessible to other classes</li>
	 * </ol>
	 * @param strings  The strings.
	 */
	RopeByteString(final ByteString[] strings) {
		this.strings = strings;
		int length = 0;
		offsets = new int[strings.length];
		for (int i = 0; i < strings.length; i++) {
			offsets[i] = length;
			length += strings[i].length();
		}
		this.length = length;
	}
	
	private int findString(final int index) {
		final int i = Arrays.binarySearch(offsets, index);
		if (i < 0) {
			return -i - 2;
		} else {
			return i;
		}
	}

	/** {@inheritDoc} */
	public int length() {
		return length;
	}
	
	/** {@inheritDoc} */
	public byte at(int index) throws IllegalArgumentException, IndexOutOfBoundsException {
		checkAt(index);
		int s = findString(index);
		return strings[s].at(index - offsets[s]);
	}

	/** {@inheritDoc} */
	public int copyTo(final ByteBuffer buffer, final int length)
			throws NullPointerException, IllegalArgumentException,
			IndexOutOfBoundsException, BufferUnderflowException,
			ReadOnlyBufferException {
		checkCopyTo(buffer, length);
		int lengthRemaining = length;
		for (final ByteString string : strings) {
			final int copyLen = Math.min(string.length(), lengthRemaining);
			lengthRemaining -= string.copyTo(buffer, copyLen);
		}
		return length;
	}
	
	@Override
	public int indexOf(int value, int fromIndex)
			throws IllegalArgumentException, IndexOutOfBoundsException {
		checkIndexOf(value, fromIndex);
		final int s = findString(fromIndex);
		for (int i = s; i < strings.length; i++) {
			final ByteString string = strings[s];
			final int f = Math.max(fromIndex - offsets[i], 0);
			final int index = string.indexOf(value, f);
			if (index >= 0) {
				return index + offsets[i];
			}
		}
		return -1;
	}
	
	/**
	 * The strings in this rope.
	 * @return  An immutable list of {@link ByteString}s composing this rope.
	 */
	public List<ByteString> strings() {
		return Collections.unmodifiableList(Arrays.asList(strings));
	}

}
