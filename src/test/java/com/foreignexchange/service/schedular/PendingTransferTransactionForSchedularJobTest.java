package com.foreignexchange.service.schedular;

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

import com.foreignexchange.common.ForeignExchangeEnum.TransferStatus;
import com.foreignexchange.entity.Currency;
import com.foreignexchange.entity.User;
import com.foreignexchange.entity.UserAccount;
import com.foreignexchange.entity.UserTransaction;
import com.foreignexchange.repository.UserAccountRepository;
import com.foreignexchange.repository.UserTransactionRepository;

@RunWith(MockitoJUnitRunner.class)
public class PendingTransferTransactionForSchedularJobTest {

	@InjectMocks
	PendingTransferTransactionForSchedularJob job;

	@Mock
	UserTransactionRepository userTransactionRepository;

	@Mock
	UserAccountRepository userAccountRepository;

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
		userAccount.setAvailableBalance(1500.00);

		toUserAccount.setAccountNumber(50002L);
		toUserAccount.setCurrency(currency);
		toUserAccount.setAvailableBalance(2500.00);
		userTransaction.setToUserAccount(toUserAccount);
		userTransaction.setTransferAmount(100.00);
		
		userTransaction.setFromUserAccount(userAccount);
		userTransaction.setAmount(200.00);
		userTransaction.setStatus(TransferStatus.PENDING);
		userTransaction.setUserTransactionId(1);

	}

	@Test
	public void testExecuteTask() {

		userTransactions.add(userTransaction);
		when(userTransactionRepository.findAllByStatus(TransferStatus.PENDING)).thenReturn(userTransactions);
		job.executeTask();
		assertEquals(100.00, userTransaction.getTransferAmount());
	}
	
	@Test
	public void testExecuteTaskForAvaialbleBalLessThanTransferAmt() {
		userTransaction.setTransferAmount(3500.00);
		userTransaction.setRemitCharge(150.00);
		userTransactions.add(userTransaction);
		when(userTransactionRepository.findAllByStatus(TransferStatus.PENDING)).thenReturn(userTransactions);
		job.executeTask();
		assertEquals(3500.00, userTransaction.getTransferAmount());
	}

}
