package de.unipassau.prassefe.sepintro.jsf;

import static org.junit.Assert.*;

import org.junit.Test;

import de.unipassau.prassefe.sepintro.TestEnum;

public class JsfEnumAdapterTest {

	private static class TestAdapter extends JsfEnumAdapter<TestEnum> {
		public TestAdapter() {
			super(TestEnum.class);
		}
	}
	
	/**
	 * Test method for {@link de.unipassau.prassefe.sepintro.jsf.JsfEnumAdapter#lookup(java.lang.String)}.
	 */
	@Test
	public final void testLookup() {
		TestAdapter test = new TestAdapter();
		assertEquals(TestEnum.OK ,test.lookup("OK"));
	}

}
