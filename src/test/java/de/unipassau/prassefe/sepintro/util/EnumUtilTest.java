package de.unipassau.prassefe.sepintro.util;

import static org.junit.Assert.*;

import org.junit.Test;

import de.unipassau.prassefe.sepintro.TestEnum;
import de.unipassau.prassefe.sepintro.util.EnumUtil;

public class EnumUtilTest {

	/**
	 * Test method for {@link de.unipassau.prassefe.sepintro.util.EnumUtil#lookup(java.lang.Class, java.lang.String)}.
	 */
	@Test
	public final void testLookup() {
		assertEquals(TestEnum.OK, EnumUtil.lookup(TestEnum.class, "OK"));
	}

}
