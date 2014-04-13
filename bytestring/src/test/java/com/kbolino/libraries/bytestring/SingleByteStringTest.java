package com.kbolino.libraries.bytestring;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SingleByteStringTest {
	SingleByteString b1, b2, b3;
	
	@Before
	public void before() {
		b1 = new SingleByteString((byte)60);
		b2 = new SingleByteString((byte)120);
		b3 = new SingleByteString((byte)180);
	}
	
	@Test
	public void testLength() {
		assertEquals(1, b1.length());
	}
	
	@Test
	public void testAt() {
		assertEquals(60, b1.at(0));
		assertEquals(120, b2.at(0));
		assertEquals(-76, b3.at(0));
	}
	
	@Test
	public void testUnsignedAt() {
		assertEquals(60, b1.unsignedAt(0));
		assertEquals(120, b2.unsignedAt(0));
		assertEquals(180, b3.unsignedAt(0));
	}
	
	@Test
	public void testConcat() {
		assertArrayEquals(new byte[]{60, 120}, b1.concat(b2).toByteArray());
	}
	
	@Test
	public void testSubString() {
		assertEquals(Utils.EMPTY_STRING, b1.subString(0, 0));
		assertEquals(b1, b1.subString(0, 1));
	}
	
	@Test
	public void testIndexOf() {
		assertEquals(0, b1.indexOf(60));
		assertEquals(0, b1.indexOf(b1));
		assertEquals(-1, b1.indexOf(120));
		assertEquals(-1, b1.indexOf(b2));
	}
}
