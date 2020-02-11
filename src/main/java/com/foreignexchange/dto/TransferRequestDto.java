package com.foreignexchange.dto;

import javax.validation.constraints.Min;

import com.foreignexchange.constant.AppConstant;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransferRequestDto {

	private Long toAccountNumber;
	@Min(value = 1, message = AppConstant.TRANSFER_AMOUNT_SHOULD_NOT_LESS_1)
	private Double transferAmount;
	@Min(value = 1, message = AppConstant.REMIT_CHARGES_SHOULD_NOT_LESS_1)
	private Double remitCharge;
	@Min(value = 1, message = AppConstant.TOTAL_AMOUNT_SHOULD_NOT_LESS_1)
	private Double totalAmount;
	@Min(value = 1, message = AppConstant.TRANSFER_AMOUNT_SHOULD_NOT_LESS_1)
	private Double toAccountAmount;
}
