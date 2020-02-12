package com.foreignexchange.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.foreignexchange.common.ForeignExchangeEnum;
import com.foreignexchange.dto.TransferRequestDto;
import com.foreignexchange.dto.TransferResponseDto;
import com.foreignexchange.entity.User;
import com.foreignexchange.entity.UserAccount;
import com.foreignexchange.entity.UserTransaction;
import com.foreignexchange.exception.UserAccountNotFoundException;
import com.foreignexchange.exception.UserNotFoundException;
import com.foreignexchange.repository.UserAccountRepository;
import com.foreignexchange.repository.UserRepository;
import com.foreignexchange.repository.UserTransactionRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserAccountTransactionServiceImplTest {

	@InjectMocks
	UserAccountTransactionServiceImpl userAccountTransactionServiceImpl;

	@Mock
	UserRepository userRepository;

	@Mock
	UserAccountRepository userAccountRepository;

	@Mock
	UserTransactionRepository userTransactionRepository;

	User user = new User();
	UserAccount userAccount = new UserAccount();
	UserAccount toUserAccount = new UserAccount();
	TransferRequestDto transferRequestDto = new TransferRequestDto();

	@Before
	public void init() {

		user.setUserId(1);
		user.setPhoneNumber(8675958381L);

		userAccount.setUserId(user);
		userAccount.setAccountNumber(50001L);

		toUserAccount.setAccountNumber(50002L);

		transferRequestDto.setToAccountNumber(50002L);
	}

	@Test
	public void testTransferAmount() throws UserNotFoundException, UserAccountNotFoundException {

		UserTransaction updateTransaction = new UserTransaction();
		updateTransaction.setStatus(ForeignExchangeEnum.TransferStatus.PENDING);

		when(userRepository.findById(1)).thenReturn(Optional.of(user));
		when(userAccountRepository.findById(50001L)).thenReturn(Optional.of(userAccount));
		when(userAccountRepository.findById(transferRequestDto.getToAccountNumber()))
				.thenReturn(Optional.of(toUserAccount));
		when(userTransactionRepository.save(Mockito.any())).thenReturn(updateTransaction);

		TransferResponseDto response = userAccountTransactionServiceImpl.transferAmount(1, 50001L, transferRequestDto);
		assertEquals(ForeignExchangeEnum.TransferStatus.PENDING, response.getStatus());
	}

	@Test(expected = UserNotFoundException.class)
	public void testTransferAmountForUserNotFoundEx() throws UserNotFoundException, UserAccountNotFoundException {

		when(userRepository.findById(1)).thenReturn(Optional.ofNullable(null));
		userAccountTransactionServiceImpl.transferAmount(1, 50001L, transferRequestDto);
	}

	@Test(expected = UserAccountNotFoundException.class)
	public void testTransferAmountForFromUserAccNotFoundEx()
			throws UserNotFoundException, UserAccountNotFoundException {

		when(userRepository.findById(1)).thenReturn(Optional.of(user));
		when(userAccountRepository.findById(50001L)).thenReturn(Optional.ofNullable(null));

		userAccountTransactionServiceImpl.transferAmount(1, 50001L, transferRequestDto);
	}

	@Test(expected = UserAccountNotFoundException.class)
	public void testTransferAmountForToUserAccNotFoundEx() throws UserNotFoundException, UserAccountNotFoundException {

		when(userRepository.findById(1)).thenReturn(Optional.of(user));
		when(userAccountRepository.findById(50001L)).thenReturn(Optional.of(userAccount));
		when(userAccountRepository.findById(transferRequestDto.getToAccountNumber()))
				.thenReturn(Optional.ofNullable(null));

		userAccountTransactionServiceImpl.transferAmount(1, 50001L, transferRequestDto);
	}

}
