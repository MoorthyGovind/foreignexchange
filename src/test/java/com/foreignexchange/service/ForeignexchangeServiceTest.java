package com.foreignexchange.service;

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

import com.foreignexchange.common.ForeignExchangeEnum;
import com.foreignexchange.entity.Currency;
import com.foreignexchange.entity.UserAccount;
import com.foreignexchange.repository.CurrencyRepository;
import com.foreignexchange.repository.UserAccountRepository;

@RunWith(MockitoJUnitRunner.class)
public class ForeignexchangeServiceTest {

	@InjectMocks
	ForeignexchangeService foreignexchangeService;

	@Mock
	CurrencyRepository currencyRepository;

	@Mock
	UserAccountRepository userAccountRepository;

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
	public void testGetAllCurrencies() {
		when(currencyRepository.findAll()).thenReturn(currencies);
		List<Currency> response = foreignexchangeService.getAllCurrencies();
		assertThat(response).hasSize(1);
	}

	@Test
	public void testGetAccountDetail() {
		when(userAccountRepository.findByAccountNumber(50001L)).thenReturn(userAccount);
		UserAccount response = foreignexchangeService.getAccountDetail(50001L);
		assertEquals("SAVINGS", response.getAccountType().toString());
	}
}
