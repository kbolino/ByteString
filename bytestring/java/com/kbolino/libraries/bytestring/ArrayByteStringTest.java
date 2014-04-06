package com.kbolino.libraries.bytestring;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import org.junit.Before;
import org.junit.Test;

public class ArrayByteStringTest {
	final static byte A = 65, B = 66, C = 67;
	ArrayByteString a, ab, aa, aab, abc, highBytes;
	
	@Before
	public void before() {
		a = new ArrayByteString(new byte[]{A});
		ab = new ArrayByteString(new byte[]{A, B});
		aa = new ArrayByteString(new byte[]{A, A});
		aab = new ArrayByteString(new byte[]{A, A, B});
		abc = new ArrayByteString(new byte[]{A, B, C});
		highBytes = new ArrayByteString(new byte[]{-128, -1});
	}
	
	@Test
	public void testLength() {
		assertEquals(1, a.length());
		assertEquals(2, aa.length());
		assertEquals(2, ab.length());
		assertEquals(3, aab.length());
		assertEquals(3, abc.length());
	}
	
	@Test
	public void testAt() {
		assertEquals(A, a.at(0));
		assertEquals(A, ab.at(0));
		assertEquals(B, ab.at(1));
		assertEquals(A, aa.at(0));
		assertEquals(A, aa.at(1));
		assertEquals(A, aab.at(0));
		assertEquals(A, aab.at(1));
		assertEquals(B, aab.at(2));
		assertEquals(A, abc.at(0));
		assertEquals(B, abc.at(1));
		assertEquals(C, abc.at(2));
	}
	
	@Test
	public void testUnsignedAt() {
		assertEquals(0x80, highBytes.unsignedAt(0));
		assertEquals(0xFF, highBytes.unsignedAt(1));
	}
	
	@Test
	public void testSubString() {
		assertEquals(Utils.EMPTY_STRING, aa.subString(0, 0));
		assertEquals(ab, aab.subString(1));
		assertEquals(ab, abc.subString(0, 2));
	}
	
	@Test
	public void testToByteBuffer() {
		ByteBuffer buffer = abc.toByteBuffer();
		assertEquals(0, buffer.position());
		assertEquals(3, buffer.limit());
		assertEquals(A, buffer.get(0));
		assertEquals(B, buffer.get(1));
		assertEquals(C, buffer.get(2));
	}
	
	@Test
	public void testToByteArray() {
		byte[] bytes = abc.toByteArray();
		assertEquals(3, bytes.length);
		assertEquals(A, bytes[0]);
		assertEquals(B, bytes[1]);
		assertEquals(C, bytes[2]);
	}
	
	@Test
	public void testCopyToByteArray() {
		byte[] bytes = new byte[]{C, C, C, C};
		abc.copyTo(bytes, 1, 2);
		assertEquals(C, bytes[0]);
		assertEquals(A, bytes[1]);
		assertEquals(B, bytes[2]);
		assertEquals(C, bytes[3]);
	}
	
	@Test
	public void testCopyToByteBuffer() {
		ByteBuffer buffer = ByteBuffer.allocate(3);
		abc.copyTo(buffer, 2);
		assertEquals(2, buffer.position());
		assertEquals(3, buffer.limit());
		assertEquals(A, buffer.get(0));
		assertEquals(B, buffer.get(1));
	}
	
	@Test
	public void testContains() {
		assertTrue(abc.contains(abc));
		assertTrue(abc.contains(a));
		assertTrue(abc.contains(ab));
		assertTrue(aab.contains(aa));
		assertTrue(aab.contains(ab));
		assertFalse(a.contains(ab));
		assertFalse(aab.contains(abc));
		assertFalse(ab.contains(aab));
	}
	
	@Test
	public void testStartsWith() {
		assertTrue(aab.startsWith(a));
		assertTrue(aab.startsWith(aab));
		assertTrue(aab.startsWith(aa));
		assertTrue(abc.startsWith(ab));
		assertFalse(aab.startsWith(ab));
		assertFalse(aab.startsWith(abc));
		assertFalse(abc.startsWith(aa));
	}
	
	@Test
	public void testEndsWith() {
		assertTrue(aab.endsWith(ab));
		assertTrue(aab.endsWith(aab));
		assertTrue(aa.endsWith(a));
		assertFalse(abc.endsWith(ab));
		assertFalse(aab.endsWith(aa));
		assertFalse(a.endsWith(aa));
	}
	
	@Test
	public void indexOf() {
		assertEquals(0, abc.indexOf(a));
		assertEquals(0, abc.indexOf(ab));
		assertEquals(0, aab.indexOf(aa));
		assertEquals(1, aab.indexOf(ab));
		assertEquals(1, aab.indexOf(ab, 0));
		assertEquals(0, abc.indexOf(abc));
		assertEquals(-1, abc.indexOf(aa));
		assertEquals(-1, aa.indexOf(abc));
		assertEquals(-1, abc.indexOf(a, 1));
	}
	
}
