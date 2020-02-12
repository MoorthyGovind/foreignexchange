package com.foreignexchange.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.foreignexchange.dto.CurrencyExrateDto;
import com.foreignexchange.entity.Currency;
import com.foreignexchange.exception.CurrencyNotFoundException;
import com.foreignexchange.exception.ExchangeRateNotFoundException;
import com.foreignexchange.repository.CurrencyRepository;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyServiceImplTest {

	@InjectMocks
	CurrencyServiceImpl currencyServiceImpl;

	@Mock
	CurrencyRepository currencyRepository;
	
	Currency currency = new Currency();

	@Before
	public void init() {
		currency.setCurrencyCode("SGD");
	}

	@Test
	public void testGetExrateRateByCurrencyCode() throws ExchangeRateNotFoundException, CurrencyNotFoundException {
		when(currencyRepository.findById("SGD")).thenReturn(Optional.of(currency));
		CurrencyExrateDto response = currencyServiceImpl.getExrateRateByCurrencyCode("SGD");
		assertEquals(51.37, response.getExchangeRate());
	}
	
	@Test(expected = CurrencyNotFoundException.class)
	public void testGetExrateRateByCurrencyCodeForCurrencyEx() throws ExchangeRateNotFoundException, CurrencyNotFoundException {
		when(currencyRepository.findById("SGD")).thenReturn(Optional.ofNullable(null));
		currencyServiceImpl.getExrateRateByCurrencyCode("SGD");
	}

}
