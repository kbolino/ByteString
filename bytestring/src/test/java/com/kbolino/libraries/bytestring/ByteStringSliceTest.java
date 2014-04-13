package com.kbolino.libraries.bytestring;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ByteStringSliceTest {
	ByteString b1;
	SlicedByteString s1, s2, s0;
	
	@Before
	public void before() {
		b1 = new ArrayByteString(new byte[]{10, 12, 14, 16, 18, 20, 22, 24});
		s1 = new SlicedByteString(b1, 4, 2);
		s2 = new SlicedByteString(b1, 2, 4);
		s0 = new SlicedByteString(b1, 2, 2);
	}
	
	@Test
	public void testLength() {
		assertEquals(2, s1.length());
		assertEquals(4, s2.length());
	}
	
	@Test
	public void testAt() {
		assertEquals(18, s1.at(0));
		assertEquals(20, s1.at(1));
		assertEquals(14, s2.at(0));
		assertEquals(16, s2.at(1));
		assertEquals(18, s2.at(2));
		assertEquals(20, s2.at(3));
	}
	
	@Test
	public void testSubString() {
		assertEquals(s1, s2.subString(2, 4));
	}
	
	@Test
	public void testIndexOf() {
		assertEquals(2, s2.indexOf(s1));
	}
	
	@Test
	public void testToByteArray() {
		byte[] bytes = s2.toByteArray();
		assertEquals(4, bytes.length);
		assertEquals(14, bytes[0]);
		assertEquals(16, bytes[1]);
		assertEquals(18, bytes[2]);
		assertEquals(20, bytes[3]);
	}
	
}
