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

import com.foreignexchange.common.ForeignExchangeEnum;
import com.foreignexchange.dto.LoginRequsetDto;
import com.foreignexchange.dto.LoginResponseDto;
import com.foreignexchange.entity.User;
import com.foreignexchange.entity.UserAccount;
import com.foreignexchange.exception.UserNotFoundException;
import com.foreignexchange.repository.UserAccountRepository;
import com.foreignexchange.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceImplTest {

	@InjectMocks
	LoginServiceImpl loginServiceImpl;

	@Mock
	UserRepository userRepository;

	@Mock
	UserAccountRepository userAccountRepository;

	User user = new User();
	UserAccount userAccount = new UserAccount();
	LoginRequsetDto loginRequsetDto = new LoginRequsetDto();

	@Before
	public void init() {
		user.setUserId(1);

		userAccount.setUserId(user);
		userAccount.setAccountNumber(50001L);

		loginRequsetDto.setPhoneNumber(8675958381L);
		loginRequsetDto.setPassword("start@123");
		;
	}

	@Test
	public void testAuthenticateUser() throws UserNotFoundException {
		// Given
		when(userRepository.findByPhoneNumberAndPassword(loginRequsetDto.getPhoneNumber(),
				loginRequsetDto.getPassword())).thenReturn(Optional.of(user));
		when(userAccountRepository.findByUserIdAndAccountType(user, ForeignExchangeEnum.AccountType.SAVINGS))
				.thenReturn(userAccount);
		// When
		LoginResponseDto response = loginServiceImpl.authenticateUser(loginRequsetDto);
		// Then
		assertEquals(50001L, response.getAccountNumber());
	}

	@Test(expected = UserNotFoundException.class)
	public void testAuthenticateUserForUserNotFoundEx() throws UserNotFoundException {
		// Given
		when(userRepository.findByPhoneNumberAndPassword(loginRequsetDto.getPhoneNumber(),
				loginRequsetDto.getPassword())).thenReturn(Optional.ofNullable(null));
		// When
		loginServiceImpl.authenticateUser(loginRequsetDto);
	}
}
