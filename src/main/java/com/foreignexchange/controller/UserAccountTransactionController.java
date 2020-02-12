package com.foreignexchange.controller;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foreignexchange.constant.AppConstant;
import com.foreignexchange.dto.TransferRequestDto;
import com.foreignexchange.dto.TransferResponseDto;
import com.foreignexchange.dto.UserTransactionResponceDto;
import com.foreignexchange.exception.UserAccountNotFoundException;
import com.foreignexchange.exception.UserNotFoundException;
import com.foreignexchange.service.UserAccountTransactionService;
import com.foreignexchange.service.UserTransactionService;

import lombok.extern.slf4j.Slf4j;

/**
 * User Account Transaction Controller class, We are implementing the user
 * account transaction like a transfer the amount the currency converted amount
 * to another user account.
 * 
 * @author Govindasamy.C
 * @since 11-02-2020
 * @version V1.1
 *
 */
@RestController
@RequestMapping("users")
@Slf4j
@CrossOrigin
public class UserAccountTransactionController {

	@Autowired
	UserAccountTransactionService userAccountTransactionService;

	@Autowired
	UserTransactionService userTransactionService;
	
	/**
	 * Transfer the currency converted amount to another user account.
	 * 
	 * @param transferRequestDto deatils of the amount, destination account ID and
	 *                           currency Type.
	 * @return transferResponseDto details of the status code and message along with
	 *         transaction status, transaction ID.
	 * @throws UserNotFoundException        - In this exception throws when user not
	 *                                      found based on the input of userId
	 * @throws UserAccountNotFoundException - In this exception throws when user
	 *                                      account not found based on the input of
	 *                                      account number.
	 * @author Govindasamy.C
	 * @since V1.1
	 * 
	 */
	@PostMapping("/{userId}/accounts/{accountNumber}/transactions")
	public ResponseEntity<TransferResponseDto> transferAmount(@PathVariable Integer userId,
			@PathVariable Long accountNumber, @Valid @RequestBody TransferRequestDto transferRequestDto)
			throws UserNotFoundException, UserAccountNotFoundException {
		log.info("transfer amount based on the required transfer details.");
		TransferResponseDto transferResponseDto = userAccountTransactionService.transferAmount(userId, accountNumber,
				transferRequestDto);
		transferResponseDto.setStatusCode(HttpStatus.OK.value());
		transferResponseDto.setMessage(AppConstant.TRANSFER_SCCUESS_MESSAGE);
		log.info("return response for the transfer amount.");
		return new ResponseEntity<>(transferResponseDto, HttpStatus.OK);
	}
	
	@GetMapping("/{userId}/transactions/{accountNumber}")
	public ResponseEntity<UserTransactionResponceDto> getTransactionDetailsById(@PathVariable Integer userId,
			@PathVariable Long accountNumber) throws UserNotFoundException, UserAccountNotFoundException {

		UserTransactionResponceDto transactionDetailsById = userTransactionService.getTransactionDetailsById(userId,
				accountNumber);

		UserTransactionResponceDto userTransactionResponceDto = new UserTransactionResponceDto();
		BeanUtils.copyProperties(transactionDetailsById, userTransactionResponceDto);
		userTransactionResponceDto.setStatusCode(HttpStatus.OK.value());
		userTransactionResponceDto.setMessage(AppConstant.SUCCESS_MESSAGE);
		return new ResponseEntity<>(userTransactionResponceDto, HttpStatus.OK);

	}
}
