package com.foreignexchange.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.foreignexchange.dto.UserTransactionResponceDto;
import com.foreignexchange.entity.Currency;
import com.foreignexchange.entity.User;
import com.foreignexchange.entity.UserAccount;
import com.foreignexchange.entity.UserTransaction;
import com.foreignexchange.exception.UserAccountNotFoundException;
import com.foreignexchange.exception.UserNotFoundException;
import com.foreignexchange.repository.UserAccountRepository;
import com.foreignexchange.repository.UserRepository;
import com.foreignexchange.repository.UserTransactionRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserTransactionServiceImplTest {

	@InjectMocks
	UserTransactionServiceImpl userTransactionServiceImpl;

	@Mock
	UserTransactionRepository userTransactionRepository;

	@Mock
	UserAccountRepository userAccountRepository;

	@Mock
	UserRepository userRepository;

	User user = new User();
	Currency currency = new Currency();
	UserAccount userAccount = new UserAccount();
	UserAccount toUserAccount = new UserAccount();
	UserTransaction userTransaction = new UserTransaction();
	List<UserTransaction> userTransactions = new ArrayList<>();

	@Before
	public void init() {

		currency.setCurrencyCode("INR");
		user.setUserId(1);
		user.setPhoneNumber(8675958381L);

		userAccount.setUserId(user);
		userAccount.setAccountNumber(50001L);
		userAccount.setCurrency(currency);

		toUserAccount.setAccountNumber(50002L);
		toUserAccount.setCurrency(currency);
		userTransaction.setToUserAccount(toUserAccount);

		userTransaction.setFromUserAccount(userAccount);
		userTransaction.setUserTransactionId(1);
		userTransactions.add(userTransaction);

	}

	@Test
	public void testGetTransactionDetailsById() throws UserNotFoundException, UserAccountNotFoundException {
		when(userRepository.findById(1)).thenReturn(Optional.of(user));
		when(userAccountRepository.findById(50001L)).thenReturn(Optional.of(userAccount));
		when(userTransactionRepository.findAllByFromUserAccount(userAccount)).thenReturn(userTransactions);
		UserTransactionResponceDto response = userTransactionServiceImpl.getTransactionDetailsById(1, 50001L);
		assertThat(response.getList()).hasSize(1);
	}

	@Test(expected = UserNotFoundException.class)
	public void testGetTransactionDetailsByIdForUserNotFoundEx()
			throws UserNotFoundException, UserAccountNotFoundException {
		when(userRepository.findById(1)).thenReturn(Optional.ofNullable(null));
		userTransactionServiceImpl.getTransactionDetailsById(1, 50001L);
	}
	
	@Test(expected = UserAccountNotFoundException.class)
	public void testGetTransactionDetailsByIdForUserAccountNotFoundEx()
			throws UserNotFoundException, UserAccountNotFoundException {
		when(userRepository.findById(1)).thenReturn(Optional.of(user));
		when(userAccountRepository.findById(50001L)).thenReturn(Optional.ofNullable(null));
		userTransactionServiceImpl.getTransactionDetailsById(1, 50001L);
	}

}
