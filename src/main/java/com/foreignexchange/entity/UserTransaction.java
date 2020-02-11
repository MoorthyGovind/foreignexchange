package com.foreignexchange.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;

import com.foreignexchange.common.ForeignExchangeEnum.TransactionType;
import com.foreignexchange.common.ForeignExchangeEnum.TransferStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Govindasamy.C
 * @version V1.1
 * @since 11-02-2020
 *
 */
@Setter
@Getter
@Entity
public class UserTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer userTransactionId;
	@CreatedDate
	private LocalDate transactionDate;
	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;
	@ManyToOne
	@JoinColumn(name = "from_account_number")
	private UserAccount fromUserAccount;
	@ManyToOne
	@JoinColumn(name = "to_account_number")
	private UserAccount toUserAccount;
	private Double amount;
	private Double remitCharge;
	private Double transferAmount;
	private Double toAccountAmount;
	@Enumerated(EnumType.STRING)
	private TransferStatus status;
}
