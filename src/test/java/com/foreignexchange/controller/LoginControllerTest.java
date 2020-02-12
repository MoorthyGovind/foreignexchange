package com.foreignexchange.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.foreignexchange.dto.LoginRequsetDto;
import com.foreignexchange.dto.LoginResponseDto;
import com.foreignexchange.exception.UserNotFoundException;
import com.foreignexchange.service.LoginService;

@RunWith(MockitoJUnitRunner.Silent.class)
public class LoginControllerTest {

	@InjectMocks
	LoginController loginController;
	@Mock
	LoginService loginService;

	LoginResponseDto loginResponseDto = new LoginResponseDto();

	@Before
	public void init() {
		loginResponseDto.setStatusCode(200);
	}
	@Test
	public void testAuthenticateUser() throws UserNotFoundException {
		LoginRequsetDto loginRequestDto = new LoginRequsetDto();

		loginRequestDto.setPassword("start@123");
		loginRequestDto.setPhoneNumber(8675958381l);


		when(loginService.authenticateUser(loginRequestDto)).thenReturn(loginResponseDto);
		ResponseEntity<LoginResponseDto> response = loginController.authenticateUser(loginRequestDto);
		assertEquals(200, response.getBody().getStatusCode());

	}

}
