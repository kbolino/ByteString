package com.kbolino.libraries.bytestring;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RangeByteStringTest {
	RangeByteString small, large, first, last;
	
	@Before
	public void before() {
		small = new RangeByteString(10, 21);
		first = new RangeByteString(0, 128);
		last = new RangeByteString(128, 256);
		large = new RangeByteString(0, 256);
	}
	
	@Test
	public void testLength() {
		assertEquals(11, small.length());
		assertEquals(256, large.length());
	}
	
	@Test
	public void testAt() {
		assertEquals(10, small.at(0));
		assertEquals(20, small.at(10));
		assertEquals(0, large.at(0));
		assertEquals(-1, large.at(255));
	}
	
	@Test
	public void testUnsignedAt() {
		assertEquals(10, small.unsignedAt(0));
		assertEquals(20, small.unsignedAt(10));
		assertEquals(0, large.unsignedAt(0));
		assertEquals(255, large.unsignedAt(255));
	}
	
	@Test
	public void testSubString() {
		assertEquals(small, large.subString(10, 21));
	}
	
	@Test
	public void testIndexOfString() {
		assertEquals(10, large.indexOf(small));
		assertEquals(5, small.indexOf(15));
		assertEquals(5, small.indexOf(15, 5));
		assertEquals(-1, small.indexOf(15, 6));
		assertEquals(0, large.indexOf(Utils.EMPTY_STRING));
	}
	
	@Test
	public void testToByteArray() {
		byte[] bytes = small.toByteArray();
		assertEquals(11, bytes.length);
		assertEquals(10, bytes[0]);
		assertEquals(20, bytes[10]);
	}
	
	@Test
	public void testConcat() {
		assertEquals(small, small.concat(Utils.EMPTY_STRING));
		assertEquals(large, first.concat(last));
	}
	
	@Test
	public void testToString() {
		assertEquals("{0A 0B 0C 0D 0E 0F 10 11 12 13 14}", small.toString());
	}
}
