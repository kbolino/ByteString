package com.kbolino.libraries.bytestring;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import org.junit.Before;
import org.junit.Test;

public class ConstantByteStringTest {
	static final byte A = 65, B = 66, C = 67, HIGH = (byte)0xFF;
	ConstantByteString a, a2, a3, b, high;
	
	@Before
	public void before() {
		a = new ConstantByteString(A, 1);
		a2 = new ConstantByteString(A, 2);
		a3 = new ConstantByteString(A, 3);
		b = new ConstantByteString(B, 1);
		high = new ConstantByteString(HIGH, 5);
	}
	
	@Test
	public void testLength() {
		assertEquals(1, a.length());
		assertEquals(2, a2.length());
		assertEquals(3, a3.length());
		assertEquals(1, b.length());
	}
	
	@Test
	public void testAt() {
		assertEquals(A, a3.at(0));
		assertEquals(A, a3.at(1));
		assertEquals(A, a3.at(2));
	}
	
	@Test
	public void testUnsignedAt() {
		assertEquals(0xFF, high.unsignedAt(0));
		assertEquals(0xFF, high.unsignedAt(1));
		assertEquals(0xFF, high.unsignedAt(2));
		assertEquals(0xFF, high.unsignedAt(3));
		assertEquals(0xFF, high.unsignedAt(4));
	}
	
	@Test
	public void testSubString() {
		assertEquals(Utils.EMPTY_STRING, a3.subString(0, 0));
		assertEquals(a2, a3.subString(1));
		assertEquals(a, a2.subString(1, 2));
	}
	
	@Test
	public void testToByteBuffer() {
		ByteBuffer buffer = a3.toByteBuffer();
		assertEquals(0, buffer.position());
		assertEquals(3, buffer.limit());
		assertEquals(A, buffer.get(0));
		assertEquals(A, buffer.get(1));
		assertEquals(A, buffer.get(2));
	}
	
	@Test
	public void testCopyTo() {
		byte[] array = new byte[3];
		a3.copyTo(array);
		assertEquals(A, array[0]);
		assertEquals(A, array[1]);
		assertEquals(A, array[2]);
	}
	
	@Test
	public void testContains() {
		assertTrue(a3.contains(a2));
		assertTrue(a3.contains(a));
		assertFalse(a2.contains(a3));
		assertFalse(a3.contains(b));
	}
	
	@Test
	public void testIndexOf() {
		assertEquals(0, a3.indexOf(a));
		assertEquals(-1, a3.indexOf(b));
		assertEquals(1, a3.indexOf(a, 1));
		assertEquals(-1, a3.indexOf(b, 1));
		assertEquals(-1, a.indexOf(a3));
		assertEquals(-1, a.indexOf(a3, 0));
	}
	
	@Test
	public void testEquals() {
		assertTrue(a.equals(a));
		assertTrue(a2.equals(a2));
		assertTrue(a3.equals(a3));
		assertFalse(a.equals(b));
		assertFalse(a.equals(a2));
		assertFalse(a.equals(a3));
		assertFalse(a2.equals(a));
		assertFalse(a3.equals(a));
	}
	
	@Test
	public void testToString() {
		assertEquals("{41}", a.toString());
		assertEquals("{41 41}", a2.toString());
		assertEquals("{41 41 41}", a3.toString());
		assertEquals("{42}", b.toString());
		assertEquals("{FF FF FF FF FF}", high.toString());
	}
}
