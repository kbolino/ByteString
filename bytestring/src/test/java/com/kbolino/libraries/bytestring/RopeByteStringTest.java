package com.kbolino.libraries.bytestring;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RopeByteStringTest {
	ByteString b1, b2;
	RopeByteString r;
	
	@Before
	public void before() {
		b1 = new ArrayByteString(new byte[]{10, 11, 12});
		b2 = new ArrayByteString(new byte[]{13, 14, 12});
		r = new RopeByteString(new ByteString[]{b1, b2});
	}
	
	@Test
	public void testLength() {
		assertEquals(b1.length() + b2.length(), r.length());
	}
	
	@Test
	public void testAt() {
		assertEquals(10, r.at(0));
		assertEquals(11, r.at(1));
		assertEquals(12, r.at(2));
		assertEquals(13, r.at(3));
		assertEquals(14, r.at(4));
		assertEquals(12, r.at(5));
	}
	
	@Test
	public void testSubString() {
		assertEquals(b1, r.subString(0, 3));
		assertEquals(b2, r.subString(3));
		assertEquals(b1.subString(1).concat(b2.subString(0, 2)), r.subString(1, 5));
	}
	
	@Test
	public void testIndexOf() {
		assertEquals(0, r.indexOf(b1));
		assertEquals(3, r.indexOf(b2));
		assertEquals(2, r.indexOf(12));
		assertEquals(5, r.indexOf(12, 3));
		assertEquals(3, r.indexOf(13));
		assertEquals(-1, r.indexOf(16));
		assertEquals(-1, r.indexOf(11, 2));
	}
	
	@Test
	public void testConcat() {
		assertEquals(b1.concat(b2.concat(b1)), r.concat(b1));
	}
}
