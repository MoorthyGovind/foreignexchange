package com.foreignexchange.service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.foreignexchange.constant.AppConstant;
import com.foreignexchange.dto.CurrencyExrateDto;
import com.foreignexchange.dto.ExrateRateDto;
import com.foreignexchange.entity.Currency;
import com.foreignexchange.exception.CurrencyNotFoundException;
import com.foreignexchange.exception.ExchangeRateNotFoundException;
import com.foreignexchange.repository.CurrencyRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Get the all currencies and get the exchange rate based on the currency code.
 * 
 * @author Govindasamy.C
 * @since 11-02-2020
 * @version V1.1
 * 
 *
 */
@Service
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {

	@Autowired
	CurrencyRepository currencyRepository;

	/**
	 * Get the Exchange rate value based on the currencyCode
	 * 
	 * @param currencyCode - ID of the countryCode
	 * @return response of the exchange rate along with status code and message.
	 * @throws ExchangeRateNotFoundException - If throws this exception when no data
	 *                                       found for exchange rate from external
	 *                                       api call.
	 * @throws CurrencyNotFoundException
	 */
	@Override
	public CurrencyExrateDto getExrateRateByCurrencyCode(String currencyCode)
			throws ExchangeRateNotFoundException, CurrencyNotFoundException {
		log.info("getting the exrate rate based on the currency code...");

		// Check the currency Code is present or not.
		Optional<Currency> user = currencyRepository.findById(currencyCode);
		if (!user.isPresent()) {
			log.error("Error Occured in transferAmount for user not found...");
			throw new CurrencyNotFoundException(AppConstant.USER_NOT_FOUND);
		}
		CurrencyExrateDto currencyExrateDto = new CurrencyExrateDto();
		String url = AppConstant.EXRATE_RATE_URL.concat(currencyCode);

		RestTemplate restTemplate = new RestTemplate();
		log.info("getting the exrate rate before calling rest template...");

		ExrateRateDto response = restTemplate.getForEntity(url, ExrateRateDto.class).getBody();

		HashMap<String, Double> exrates = response.getRates();

		Optional<Entry<String, Double>> value = exrates.entrySet().stream()
				.filter(exrate -> exrate.getKey().equals(AppConstant.INR_CURRENCY_CODE)).findFirst();
		log.info("filtering the INR currency based on the rates...");
		if (!value.isPresent()) {
			throw new ExchangeRateNotFoundException(AppConstant.EXRATE_NOT_FOUND);
		} else {
			DecimalFormat df2 = new DecimalFormat(AppConstant.EXRATE_DECIMAL_FORMAT);
			df2.setRoundingMode(RoundingMode.UP);
			currencyExrateDto.setExchangeRate(Double.valueOf(df2.format(value.get().getValue())));
		}
		log.info("return the exchange rate based on the currency code...");
		return currencyExrateDto;
	}

}
