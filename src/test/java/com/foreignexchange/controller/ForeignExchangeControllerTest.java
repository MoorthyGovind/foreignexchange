package com.foreignexchange.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.foreignexchange.common.ForeignExchangeEnum;
import com.foreignexchange.dto.AccountDetailResponse;
import com.foreignexchange.dto.CurrencyResponse;
import com.foreignexchange.entity.Currency;
import com.foreignexchange.entity.UserAccount;
import com.foreignexchange.service.ForeignexchangeServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class ForeignExchangeControllerTest {

	@InjectMocks
	ForeignExchangeController foreignExchangeController;
	
	@Mock
	ForeignexchangeServiceImpl foreignexchangeService;
	
	Currency currency = new Currency();
	UserAccount userAccount = new UserAccount();
	List<Currency> currencies = new ArrayList<>();
	@Before
	public void init() {

		currency.setCurrencyCode("SGD");
		
		currencies.add(currency);
		
		userAccount.setAccountNumber(50001L);
		userAccount.setCurrency(currency);
		userAccount.setAccountType(ForeignExchangeEnum.AccountType.SAVINGS);
	}

	@Test
	public void testGetCurrencies() {
		when(foreignexchangeService.getAllCurrencies()).thenReturn(currencies);
		ResponseEntity<CurrencyResponse> response = foreignExchangeController.getCurrencies();
		assertThat(response.getBody().getCurrencies()).hasSize(1);
	}

	@Test
	public void testGetAccounDetail() {
		
		when(foreignexchangeService.getAccountDetail(50001L)).thenReturn(userAccount);
		ResponseEntity<AccountDetailResponse> response = foreignExchangeController.getAccounDetail(50001L);
		assertEquals(200, response.getBody().getStatusCode());
	}
	
	@Test
	public void testGetAccounDetailForNegative() {
		
		when(foreignexchangeService.getAccountDetail(50001L)).thenReturn(null);
		ResponseEntity<AccountDetailResponse> response = foreignExchangeController.getAccounDetail(50001L);
		assertEquals(400, response.getBody().getStatusCode());
	}
}
