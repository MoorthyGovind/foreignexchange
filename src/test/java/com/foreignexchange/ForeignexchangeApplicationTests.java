package com.foreignexchange;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ForeignexchangeApplicationTests {

	@Test
	public void applicationTest() {
		ForeignexchangeApplication.main(new String[] {});
		assertTrue(true);
	}
}
