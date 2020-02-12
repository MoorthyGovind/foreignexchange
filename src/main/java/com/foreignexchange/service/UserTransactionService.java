package com.foreignexchange.service;

import com.foreignexchange.dto.UserTransactionResponceDto;
import com.foreignexchange.exception.UserAccountNotFoundException;
import com.foreignexchange.exception.UserNotFoundException;

public interface UserTransactionService {

	UserTransactionResponceDto getTransactionDetailsById(Integer userId, Long accountNumber)
			throws UserNotFoundException, UserAccountNotFoundException;

}
