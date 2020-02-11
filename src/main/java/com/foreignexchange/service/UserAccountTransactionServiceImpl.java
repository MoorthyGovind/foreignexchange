package com.foreignexchange.service;

import java.time.LocalDate;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foreignexchange.common.ForeignExchangeEnum;
import com.foreignexchange.constant.AppConstant;
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

import lombok.extern.slf4j.Slf4j;

/**
 * User Account Transaction Service Impl class, We are implementing the user
 * account transaction like a transfer the amount the currency converted amount
 * to another user account.
 * 
 * @author Govindasamy.C
 * @since 11-02-2020
 * @version V1.1
 *
 */
@Service
@Slf4j
@Transactional
public class UserAccountTransactionServiceImpl implements UserAccountTransactionService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserAccountRepository userAccountRepository;

	@Autowired
	UserTransactionRepository userTransactionRepository;

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
	@Override
	public TransferResponseDto transferAmount(Integer userId, Long accountNumber, TransferRequestDto transferRequestDto)
			throws UserNotFoundException, UserAccountNotFoundException {
		log.info("transfer amount based on the required transfer details.");
		// Check the User is present or not.
		Optional<User> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			log.error("Error Occured in transferAmount for user not found...");
			throw new UserNotFoundException(AppConstant.USER_NOT_FOUND);
		}

		// Check the From User Account is present or not.
		Optional<UserAccount> userAccount = userAccountRepository.findById(accountNumber);
		if (!userAccount.isPresent()) {
			log.error("Error Occured in transferAmount for From user account not found...");
			throw new UserAccountNotFoundException(AppConstant.USER_ACCOUNT_NOT_FOUND);
		}

		// Check the To User Account is present or not.
		Optional<UserAccount> toUserAccount = userAccountRepository.findById(transferRequestDto.getToAccountNumber());
		if (!toUserAccount.isPresent()) {
			log.error("Error Occured in transferAmount for To user account not found...");
			throw new UserAccountNotFoundException(AppConstant.TO_USER_ACCOUNT_NOT_FOUND);
		}
		log.info("setting the transaction details into transaction entity...");
		UserTransaction userTransaction = new UserTransaction();
		userTransaction.setFromUserAccount(userAccount.get());
		userTransaction.setToUserAccount(toUserAccount.get());
		userTransaction.setToAccountAmount(transferRequestDto.getToAccountAmount());
		userTransaction.setTransactionType(ForeignExchangeEnum.TransactionType.DEBIT);
		userTransaction.setAmount(transferRequestDto.getTransferAmount());
		userTransaction.setTransactionDate(LocalDate.now());
		BeanUtils.copyProperties(transferRequestDto, userTransaction);
		userTransaction.setTransferAmount(transferRequestDto.getTotalAmount());
		userTransaction.setStatus(ForeignExchangeEnum.TransferStatus.PENDING);

		log.debug("save the account transaction details...");
		userTransactionRepository.save(userTransaction);

		log.debug("setting response details of the transaction details...");
		TransferResponseDto responseDto = new TransferResponseDto();
		responseDto.setTransRefNo(userTransaction.getUserTransactionId());
		responseDto.setStatus(userTransaction.getStatus());
		return responseDto;
	}

}
