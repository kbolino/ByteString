package com.kbolino.libraries.bytestring;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UtilsTest {
	final byte A = 10, B = 20;
	ByteString s;
	ByteString a, b, a2, b2;
	
	@Before
	public void before() {
		s = new ConstantByteString(A, 100);
		
		a = new ConstantByteString(A, Utils.ROPE_THRESHOLD / 2 - 1);
		b = new ConstantByteString(B, Utils.ROPE_THRESHOLD / 2 - 1);
		
		a2 = new ConstantByteString(A, Utils.ROPE_THRESHOLD);
		b2 = new ConstantByteString(B, Utils.ROPE_THRESHOLD);
	}
	
	@Test
	public void testSubStringSmall() {
		ByteString s1 = s.subString(0, (int)(s.length() * Utils.SLICE_THRESHOLD) - 1);
		for (int i = 0; i < s1.length(); i++) {
			assertEquals(s.at(i), s1.at(i));
		}
	}
	
	@Test
	public void testSubStringLarge() {
		ByteString s2 = s.subString(0, (int)(s.length() * Utils.SLICE_THRESHOLD) + 1);
		for (int i = 0; i < s2.length(); i++) {
			assertEquals(s.at(i), s2.at(i));
		}
	}
	
	@Test
	public void testConcatSmall() {
		ByteString cat = Utils.concat(a, b);
		assertEquals(a.length() + b.length(), cat.length());
		for (int i = 0; i < a.length(); i++) {
			assertEquals(a.at(i), cat.at(i));
		}
		for (int i = 0; i < b.length(); i++) {
			assertEquals(b.at(i), cat.at(i + a.length()));
		}
	}
	
	@Test
	public void testConcatLarge() {
		ByteString cat = Utils.concat(a2, b2);
		assertEquals(a2.length() + b2.length(), cat.length());
		for (int i = 0; i < a2.length(); i++) {
			assertEquals(a2.at(i), cat.at(i));
		}
		for (int i = 0; i < b2.length(); i++) {
			assertEquals(b2.at(i), cat.at(i + a2.length()));
		}
	}
}
