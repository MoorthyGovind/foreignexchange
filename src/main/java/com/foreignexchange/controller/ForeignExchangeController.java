package com.foreignexchange.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.foreignexchange.constant.AppConstant;
import com.foreignexchange.dto.AccountDetailResponse;
import com.foreignexchange.dto.CurrencyResObj;
import com.foreignexchange.dto.CurrencyResponse;
import com.foreignexchange.entity.Currency;
import com.foreignexchange.entity.UserAccount;
import com.foreignexchange.service.ForeignexchangeServiceImpl;

@Component
@RestController
@CrossOrigin
public class ForeignExchangeController {

	@Autowired
	ForeignexchangeServiceImpl foreignexchangeServiceimpl;

	@GetMapping(value = "/currencies")
	public ResponseEntity<CurrencyResponse> getCurrencies() {
		CurrencyResponse currencyResponse = null;

		List<Currency> currencylsitobj = foreignexchangeServiceimpl.getAllCurrencies();
		if (currencylsitobj != null) {
			currencyResponse = new CurrencyResponse();
			currencyResponse.setMessage(AppConstant.SUCCESS_MESSAGE);
			currencyResponse.setStatuscode(AppConstant.SUCCESS_STATUS_CODE);

			List<CurrencyResObj> currencies = new ArrayList<>();
			CurrencyResObj currencyObject = null;
			for (Currency currency : currencylsitobj) {
				currencyObject = new CurrencyResObj();
				currencyObject.setCurrencyCode(currency.getCurrencyCode());
				currencyObject.setCurrencyName(currency.getCurrencyName());
				currencies.add(currencyObject);
			}
			currencyResponse.setCurrencies(currencies);

		}
		return new ResponseEntity<>(currencyResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/accounts/{accountNumber}")
	public ResponseEntity<AccountDetailResponse> getAccounDetail(@PathVariable("accountNumber") Long acctnum) {

		AccountDetailResponse accountDetailResponse = new AccountDetailResponse();
		UserAccount accountobj = foreignexchangeServiceimpl.getAccountDetail(acctnum);

		if (accountobj != null) {
			accountDetailResponse.setStatusCode(AppConstant.SUCCESS_STATUS_CODE);
			accountDetailResponse.setMessage(AppConstant.SUCCESS_MESSAGE);
			accountDetailResponse.setAccountType(accountobj.getAccountType().name());
			accountDetailResponse.setAccountBalance(accountobj.getAvailableBalance());
			accountDetailResponse.setAccountNumber(accountobj.getAccountNumber());
			accountDetailResponse.setCurrencyType(accountobj.getCurrency().getCurrencyCode());

			return new ResponseEntity<>(accountDetailResponse, HttpStatus.OK);
		} else {
			accountDetailResponse.setStatusCode(400);
			accountDetailResponse.setMessage(AppConstant.NO_RECORDS_FOUND);
			return new ResponseEntity<>(accountDetailResponse, HttpStatus.NOT_FOUND);
		}

	}

}
