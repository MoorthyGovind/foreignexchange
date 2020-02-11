package com.foreignexchange.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foreignexchange.constant.AppConstant;
import com.foreignexchange.dto.UserAccountDto;
import com.foreignexchange.dto.UserTransactionResponceDto;
import com.foreignexchange.entity.User;
import com.foreignexchange.entity.UserAccount;
import com.foreignexchange.entity.UserTransaction;
import com.foreignexchange.exception.UserNotFoundException;
import com.foreignexchange.repository.UserAccountRepository;
import com.foreignexchange.repository.UserRepository;
import com.foreignexchange.repository.UserTransactionRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserTransactionServiceImpl implements UserTransactionService {
	@Autowired
	UserTransactionRepository userTransactionRepository;

	@Autowired
	UserAccountRepository userAccountrepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public UserTransactionResponceDto getTransactionDetailsById(Integer userId, Long accountNumber)
			throws UserNotFoundException {

		// Check the User is present or not.
		Optional<User> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			log.error("Error Occured in transferAmount for user not found...");
			throw new UserNotFoundException(AppConstant.USER_NOT_FOUND);
		}

		Optional<UserAccount> userAccount = userAccountrepository.findById(accountNumber);
		if (!userAccount.isPresent()) {
			log.error("Error Occured in transferAmount for user not found...");
			throw new UserNotFoundException(AppConstant.NO_RECORDS_FOUND);
		}

		List<UserTransaction> userTransactions = userTransactionRepository.findAllByFromUserAccount(userAccount.get());

		List<UserAccountDto> userAccountDtos = userTransactions.stream()
				.map(userTransaction -> convertAccountEntityToDto(userTransaction, userAccount.get()))
				.collect(Collectors.toList());
		UserTransactionResponceDto responseDto = new UserTransactionResponceDto();
		responseDto.setList(userAccountDtos);

		return responseDto;
	}

	private UserAccountDto convertAccountEntityToDto(UserTransaction userTransaction, UserAccount userAccount) {
		UserAccountDto userAccountDto = new UserAccountDto();
		BeanUtils.copyProperties(userTransaction, userAccountDto);
		BeanUtils.copyProperties(userAccount, userAccountDto);
		userAccountDto.setToAccountNumber(userTransaction.getToUserAccount().getAccountNumber());
		userAccountDto.setCurrencyType(userAccount.getCurrency().getCurrencyCode());
		userAccountDto.setRemitCharge(userTransaction.getRemitCharge());
		userAccountDto.setTotalAmount(userTransaction.getTransferAmount());
		userAccountDto.setConvertedType(userTransaction.getToUserAccount().getCurrency().getCurrencyCode());
		userAccountDto.setConvertedAmount(userTransaction.getToAccountAmount());
		return userAccountDto;
	}

}
