package com.kbolino.libraries.bytestring;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RangeByteStringTest {
	RangeByteString small, large, first, last, signed;
	
	@Before
	public void before() {
		small = new RangeByteString(10, 21);
		first = new RangeByteString(0, 128);
		last = new RangeByteString(128, 256);
		large = new RangeByteString(0, 256);
		signed = new RangeByteString(-128, 128);
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
		assertEquals(-128, signed.at(0));
		assertEquals(-1, signed.at(127));
		assertEquals(0, signed.at(128));
		assertEquals(127, signed.at(255));
	}
	
	@Test
	public void testUnsignedAt() {
		assertEquals(10, small.unsignedAt(0));
		assertEquals(20, small.unsignedAt(10));
		assertEquals(0, large.unsignedAt(0));
		assertEquals(255, large.unsignedAt(255));
		assertEquals(128, signed.unsignedAt(0));
		assertEquals(255, signed.unsignedAt(127));
		assertEquals(0, signed.unsignedAt(128));
		assertEquals(127, signed.unsignedAt(255));
	}
	
	@Test
	public void testSubString() {
		assertEquals(small, large.subString(10, 21));
		assertEquals(small, signed.subString(138, 149));
		assertEquals(last, signed.subString(0, 128));
		assertEquals(first, signed.subString(128));
	}
	
	@Test
	public void testIndexOfString() {
		assertEquals(10, large.indexOf(small));
		assertEquals(5, small.indexOf(15));
		assertEquals(5, small.indexOf(15, 5));
		assertEquals(-1, small.indexOf(15, 6));
		assertEquals(0, large.indexOf(Utils.EMPTY_STRING));
		assertEquals(255, large.indexOf(255));
		assertEquals(127, signed.indexOf(-1));
		// TODO --correct behavior?
		// assertEquals(-1, signed.indexOf(255)); 
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
		assertEquals(signed, last.concat(first));
	}
	
	@Test
	public void testToString() {
		assertEquals("{0A 0B 0C 0D 0E 0F 10 11 12 13 14}", small.toString());
	}
}
