package com.kbolino.libraries.bytestring;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RepeatedByteStringTest {
	ByteString b1, b2;
	RepeatedByteString r1, r2, r3;
	
	@Before
	public void before() {
		b1 = new ArrayByteString(new byte[]{10, 11});
		b2 = new ArrayByteString(new byte[]{11, 10});
		r1 = new RepeatedByteString(b1, 2);
		r2 = new RepeatedByteString(b1, 4);
		r3 = new RepeatedByteString(b2, 2);
	}
	
	@Test
	public void testLength() {
		assertEquals(4, r1.length());
		assertEquals(8, r2.length());
		assertEquals(4, r3.length());
	}
	
	@Test
	public void testAt() {
		assertEquals(10, r1.at(0));
		assertEquals(11, r1.at(1));
		assertEquals(10, r1.at(2));
		assertEquals(11, r1.at(3));
	}
	
	@Test
	public void testSubString() {
		assertEquals(r1, r2.subString(2, 6));
		assertEquals(r3, r2.subString(1, 5));
	}
	
	@Test
	public void testIndexOf() {
		assertEquals(0, r2.indexOf(r1));
		assertEquals(2, r2.indexOf(r1, 1));
		assertEquals(1, r2.indexOf(r3));
		assertEquals(1, r3.indexOf(10));
		assertEquals(2, r3.indexOf(11, 2));
	}
	
	@Test
	public void testConcat() {
		assertEquals(r2, r1.concat(r1));
	}
}
