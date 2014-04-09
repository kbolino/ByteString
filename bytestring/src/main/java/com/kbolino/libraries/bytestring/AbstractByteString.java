package com.kbolino.libraries.bytestring;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;

/**
 * Abstract parent of {@link ByteString} implementations.
 */
abstract class AbstractByteString implements ByteString {
	
	/**
	 * Checks parameters to {@link #checkAt(int)}.
	 * @param index  The index.
	 */
	protected void checkAt(final int index) {
		final int length = length();
		if (index < 0) {
			throw new IllegalArgumentException(String.format("index (%d) < 0", index));
		} else if (index >= length) {
			throw new IndexOutOfBoundsException(String.format("index (%d) >= length (%d)", index, length));
		}
	}
	
	/**
	 * Checks parameters to {@link #subString(int)}.
	 * @param beginIndex  The first index.
	 */
	protected void checkSubString(final int beginIndex) {
		if (beginIndex < 0) {
			throw new IllegalArgumentException(
					String.format("beginIndex (%d) < 0", beginIndex));
		} else {
			final int length = length();
			if (beginIndex >= length) {
				throw new IndexOutOfBoundsException(
						String.format("beginIndex (%d) >= length (%d)", beginIndex, length));
			}
		}
	}
	
	/**
	 * Checks parameters to {@link #subString(int, int)}.
	 * @param beginIndex  The first index, inclusive.
	 * @param endIndex  The last index, exclusive.
	 */
	protected void checkSubString(final int beginIndex, final int endIndex) {
		if (beginIndex < 0) {
			throw new IllegalArgumentException(
					String.format("beginIndex (%d) < 0", beginIndex));
		} else if (endIndex < 0) {
			throw new IllegalArgumentException(
					String.format("endIndex (%d) < 0", endIndex));
		} else if (beginIndex > endIndex) {
			throw new IllegalArgumentException(
					String.format("beginIndex (%d) > endIndex (%d)", beginIndex, endIndex));
		} else {
			final int length = length();
			if (length > 0 && beginIndex >= length) {
				throw new IndexOutOfBoundsException(
						String.format("beginIndex (%d) >= length (%d)", beginIndex, length));
			} else if (endIndex > length) {
				throw new IndexOutOfBoundsException(
						String.format("endIndex (%d) > length (%d)", endIndex, length));
			}
		}
	}
	
	/**
	 * Checks parameters to {@link #copyTo(byte[], int, int)}.
	 * @param bytes  The array.
	 * @param offset  The first index.
	 * @param length  The number of bytes to copy.
	 */
	protected void checkCopyTo(final byte[] bytes, final int offset, final int length) {
		Utils.checkCopyParams(bytes, offset, length);
		if (length > length()) {
			throw new IndexOutOfBoundsException(
					String.format("length (%d) > this.length (%d)", length, length()));
		}
	}
	
	/**
	 * Checks parameters to {@link #copyTo(ByteBuffer, int)}.
	 * @param buffer  The buffer.
	 * @param length  The number of bytes to copy.
	 */
	protected void checkCopyTo(final ByteBuffer buffer, final int length) {
		Utils.checkCopyParams(buffer, length);
		if (length > length()) {
			throw new IllegalArgumentException(
					String.format("length (%d) > this.length (%d)", length, length()));
		}
	}
	
	/**
	 * Checks parameters to {@link #indexOf(int)} method.
	 * @param fromIndex  The first index.
	 */
	protected void checkIndexOf(final int fromIndex) {
		if (fromIndex < 0) {
			throw new IllegalArgumentException(
					String.format("fromIndex (%d) < 0", fromIndex));
		} else if (length() > 0 && fromIndex >= length()) {
			throw new IndexOutOfBoundsException(
					String.format("fromIndex (%d) >= length (%d)", fromIndex, length()));
		}
	}
	
	/**
	 * Checks parameters to {@link #indexOf(int, int)} method.
	 * @param value  The byte value.
	 * @param fromIndex  The first index.
	 */
	protected void checkIndexOf(final int value, final int fromIndex) {
		Utils.checkByteValue(value);
		checkIndexOf(fromIndex);
	}
	
	/** {@inheritDoc} */
	public int unsignedAt(final int index) {
		return at(index) & 0xFF;
	}
	
	/** {@inheritDoc} */
	public ByteString subString(int beginIndex) throws IllegalArgumentException,
			IndexOutOfBoundsException {
		checkSubString(beginIndex);
		return subString(beginIndex, length() - beginIndex + 1);
	}
	
	/** {@inheritDoc} */
	public ByteString subString(int beginIndex, int endIndex)
			throws IllegalArgumentException, IndexOutOfBoundsException {
		checkSubString(beginIndex, endIndex);
		return Utils.subString(this, beginIndex, endIndex);
	}
	
	/** {@inheritDoc} */
	public byte[] toByteArray() {
		final byte[] array = new byte[length()];
		copyTo(array, 0, length());
		return array;
	}
	
	/** {@inheritDoc} */
	public ByteBuffer toByteBuffer() {
		final ByteBuffer buffer = ByteBuffer.allocate(length());
		copyTo(buffer);
		buffer.rewind();
		return buffer;
	}
	
	/** {@inheritDoc} */
	public int copyTo(final byte[] bytes) {
		return copyTo(bytes, 0);
	}
	
	/** {@inheritDoc} */
	public int copyTo(byte[] bytes, int offset) throws NullPointerException,
			IllegalArgumentException, IndexOutOfBoundsException {
		return copyTo(bytes, offset, length());
	}
	
	/** {@inheritDoc} */
	public int copyTo(final byte[] bytes, final int offset, final int length)
			throws NullPointerException, IllegalArgumentException, IndexOutOfBoundsException {
		checkCopyTo(bytes, offset, length);
		final ByteBuffer buffer = ByteBuffer.wrap(bytes);
		buffer.position(offset);
		return copyTo(buffer, length);
	}
	
	/** {@inheritDoc} */
	public int copyTo(final ByteBuffer buffer) {
		return copyTo(buffer, length());
	}
	
	/** {@inheritDoc} */
	public int copyTo(ByteBuffer buffer, int length)
			throws NullPointerException, IllegalArgumentException,
			IndexOutOfBoundsException, BufferOverflowException,
			ReadOnlyBufferException {
		checkCopyTo(buffer, length);
		for (int i = 0; i < length; i++) {
			buffer.put(at(i));
		}
		return length;
	}
	
	/** {@inheritDoc} */
	public int indexOf(final ByteString string, final int fromIndex) {
		if (string == null) {
			throw new NullPointerException("string is null");
		}
		checkIndexOf(fromIndex);
		final int strLen = string.length();
		if (strLen == 0) {
			return fromIndex;
		}
		final int length = length();
		for (int i = fromIndex; i < length; i++) {
			if (i + strLen > length) {
				return -1;
			} else {
				final ByteString sub = subString(i, i + strLen);
				if (string.equals(sub)) {
					return i;
				}
			}
		}
		return -1;
	}

	/** {@inheritDoc} */
	public int indexOf(final ByteString string) {
		return indexOf(string, 0);
	}
	
	/** {@inheritDoc} */
	public int indexOf(int value, final int fromIndex)
			throws IllegalArgumentException, IndexOutOfBoundsException {
		checkIndexOf(fromIndex);
		final byte byteVal = Utils.toByteValue(value);
		final int length = length();
		for (int i = fromIndex; i < length; i++) {
			if (byteVal == at(i)) {
				return i;
			}
		}
		return -1;
	}
	
	/** {@inheritDoc} */
	public int indexOf(int value) {
		return indexOf(value, 0);
	}
	
	/** {@inheritDoc} */
	public boolean contains(final ByteString string) {
		return indexOf(string) != -1;
	}
	
	/** {@inheritDoc} */
	public boolean contains(final int value) {
		return indexOf(value) != -1;
	}
	
	/** {@inheritDoc} */
	public boolean startsWith(final ByteString string) {
		if (string == null) {
			throw new NullPointerException("string is null");
		} else if (string.length() > length()) {
			return false;
		} else {
			final ByteString start = subString(0, string.length());
			return string.equals(start);
		}
	}
	
	/** {@inheritDoc} */
	public boolean endsWith(final ByteString string) {
		if (string == null) {
			throw new NullPointerException("string is null");
		} else if (string.length() > length()) {
			return false;
		} else {
			final ByteString end = subString(length() - string.length(), length());
			return string.equals(end);
		}
	}
	
	/** {@inheritDoc} */
	public ByteString concat(final ByteString string) throws NullPointerException {
		if (string == null) {
			throw new NullPointerException("string is null");
		}
		return Utils.concat(this, string);
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof ByteString) {
			final ByteString other = (ByteString) obj;
			final int length = length();
			if (length != other.length()) { return false; }
			for (int i = 0; i < length; i++) {
				if (at(i) != other.at(i)) { return false; }
			}
			return true;
		} else {
			return false;
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		final int PRIME = 92821;
		int hash = PRIME;
		final int length = length();
		for (int i = 0; i < length; i++) {
			hash += PRIME * at(i);
		}
		return hash;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		final int length = length();
		final StringBuilder builder = new StringBuilder(3 * length + 1);
		builder.append('{');
		for (int i = 0; i < length; i++) {
			if (i != 0) {
				builder.append(" ");
			}
			builder.append(String.format("%02X", at(i)));
		}
		builder.append('}');
		return builder.toString();
	}
}
