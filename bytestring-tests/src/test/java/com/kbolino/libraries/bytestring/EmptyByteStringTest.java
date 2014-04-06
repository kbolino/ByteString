package com.kbolino.libraries.bytestring;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import org.junit.Before;
import org.junit.Test;

public class EmptyByteStringTest {
	EmptyByteString e;
	
	@Before
	public void before() {
		e = new EmptyByteString();
	}
	
	@Test
	public void testLength() {
		assertEquals(0, e.length());
	}
	
	@Test
	public void testAtInvalid() {
		boolean thrown = false;
		try {
			e.at(-1);
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assertTrue(thrown);
	}
	
	@Test
	public void testAt0() {
		boolean thrown = false;
		try {
			e.at(0);
		} catch (IndexOutOfBoundsException e) {
			thrown = true;
		}
		assertTrue(thrown);
	}
	
	@Test
	public void testSubString() {
		assertEquals(e, e.subString(0, 0));
	}
	
	@Test
	public void testToByteBuffer() {
		ByteBuffer buffer = e.toByteBuffer();
		assertEquals(0, buffer.position());
		assertEquals(0, buffer.limit());
	}
	
	@Test
	public void testToByteArray() {
		assertArrayEquals(new byte[0], e.toByteArray());
	}
	
	@Test
	public void testCopyToByteArray() {
		e.copyTo(new byte[0]); // should not throw exceptions
	}
	
	@Test
	public void testCopyToByteBuffer() {
		e.copyTo(ByteBuffer.allocate(0).asReadOnlyBuffer()); // should not throw exceptions
	}
	
	@Test
	public void testConcat() {
		assertEquals(e, e.concat(e));
	}
	
	@Test
	public void testContains() {
		assertTrue(e.contains(e));
	}

}
