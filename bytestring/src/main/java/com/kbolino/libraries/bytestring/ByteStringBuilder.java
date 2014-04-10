package com.kbolino.libraries.bytestring;

import java.nio.ByteBuffer;

public final class ByteStringBuilder {
	private byte[] bytes;
	private int length;
	
	ByteStringBuilder(int initialCapacity) {
		bytes = new byte[initialCapacity];
	}
	
	public ByteStringBuilder ensureCapacity(int capacity) {
		if (capacity < bytes.length) {
			int newSize = 2 * bytes.length + 1;
			if (capacity > newSize) {
				newSize = capacity;
			}
			final byte[] copy = new byte[newSize];
			System.arraycopy(bytes, 0, copy, 0, length);
			bytes = copy;
		}
		return this;
	}
	
	public ByteStringBuilder append(int value) throws IllegalArgumentException {
		final byte b = Utils.toByteValue(value);
		ensureCapacity(length + 1);
		bytes[length] = b;
		length += 1;
		return this;
	}
	
	public ByteStringBuilder append(byte[] bytes) {
		return append(bytes, 0, bytes.length);
	}
	
	public ByteStringBuilder append(byte[] bytes, int offset, int length) {
		ensureCapacity(this.length + length);
		System.arraycopy(bytes, offset, this.bytes, this.length, length);
		this.length += length;
		return this;
	}
	
	public ByteStringBuilder append(ByteBuffer buffer) {
		return append(buffer, buffer.remaining());
	}
	
	public ByteStringBuilder append(ByteBuffer buffer, int length) {
		ensureCapacity(this.length + length);
		buffer.get(bytes, this.length, length);
		this.length += length;
		return this;
	}
	
	public ByteStringBuilder append(ByteString string) {
		ensureCapacity(length + string.length());
		string.copyTo(bytes, length);
		length += string.length();
		return this;
	}
	
	// TODO read from InputStream, ReadableByteChannel
	
	public byte[] toByteArray() {
		final byte[] copy = new byte[length];
		if (length > 0) {
			System.arraycopy(bytes, 0, copy, 0, length);
		}
		return copy;
	}
	
	public ByteString toByteString() {
		if (length == 0) {
			return Utils.EMPTY_STRING;
		}
		return new ArrayByteString(toByteArray());
	}
}
