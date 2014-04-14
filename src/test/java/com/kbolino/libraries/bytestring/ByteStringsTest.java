package com.kbolino.libraries.bytestring;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import org.junit.Before;
import org.junit.Test;

public class ByteStringsTest {
	byte[] bytes;
	ByteBuffer buffer;
	
	@Before
	public void before() {
		bytes = new byte[]{0, 10, 20};
		buffer = ByteBuffer.wrap(bytes);
	}
	
	@Test
	public void testCopyFromByteArray1() {
		ByteString string = ByteStrings.copyFrom(bytes);
		assertEquals(3, string.length());
		assertEquals(0, string.at(0));
		assertEquals(10, string.at(1));
		assertEquals(20, string.at(2));
	}
	
	@Test
	public void testCopyFromByteArray2() {
		ByteString string = ByteStrings.copyFrom(bytes, 1, 1);
		assertEquals(1, string.length());
		assertEquals(10, string.at(0));
	}
	
	@Test
	public void testCopyFromByteBuffer1() {
		ByteString string = ByteStrings.copyFrom(buffer);
		assertEquals(3, string.length());
		assertEquals(0, string.at(0));
		assertEquals(10, string.at(1));
		assertEquals(20, string.at(2));
	}
	
	@Test
	public void testCopyFromByteBuffer2() {
		ByteString string = ByteStrings.copyFrom(buffer, 2);
		assertEquals(2, string.length());
		assertEquals(0, string.at(0));
		assertEquals(10, string.at(1));
	}
	
	@Test
	public void testEmpty() {
		assertEquals(0, ByteStrings.empty().length());
	}
	
	@Test
	public void testOf0() {
		assertEquals(0, ByteStrings.of().length());
	}
	
	@Test
	public void testOf1() {
		ByteString string = ByteStrings.of(10);
		assertEquals(1, string.length());
		assertEquals(10, string.at(0));
	}
	
	@Test
	public void testOf3() {
		ByteString string = ByteStrings.of(0, 127, 255);
		assertEquals(3, string.length());
		assertEquals(0, string.at(0));
		assertEquals(127, string.at(1));
		assertEquals(255, string.unsignedAt(2));
	}
	
	@Test
	public void testZeros() {
		ByteString string = ByteStrings.zeros(5);
		assertEquals(5, string.length());
		assertEquals(0, string.at(0));
		assertEquals(0, string.at(1));
		assertEquals(0, string.at(2));
		assertEquals(0, string.at(3));
		assertEquals(0, string.at(4));
	}
	
	@Test
	public void testFill() {
		ByteString string = ByteStrings.fill(127, 5);
		assertEquals(5, string.length());
		assertEquals(127, string.at(0));
		assertEquals(127, string.at(1));
		assertEquals(127, string.at(2));
		assertEquals(127, string.at(3));
		assertEquals(127, string.at(4));
	}
	
	@Test
	public void testConcat0() {
		assertEquals(Utils.EMPTY_STRING, ByteStrings.concat());
	}
	
	@Test
	public void testConcat() {
		ByteString string = ByteStrings.concat(ByteStrings.of(0), ByteStrings.of(10, 20), ByteStrings.of(20));
		assertEquals(4, string.length());
		assertEquals(0, string.at(0));
		assertEquals(10, string.at(1));
		assertEquals(20, string.at(2));
		assertEquals(20, string.at(3));
	}
}
