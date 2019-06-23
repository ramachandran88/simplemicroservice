package com.ram.web.request;


import com.ram.util.Constants;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by Ravi on 21-Jun-19.
 */
public class MoneyTransferRequest {
    @NotNull
    private Long sourceAccountId;
    @NotNull
    private Long destinationAccountId;

    @NotNull
    @DecimalMin(value = Constants.MIN_ALLOWED_BALANCE)
    private BigDecimal transferAmount;

    private MoneyTransferRequest() {
    }

    public MoneyTransferRequest(Long sourceAccountId,
                                Long destinationAccountId,
                                BigDecimal transferAmount) {
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.transferAmount = transferAmount;
    }

    public Long getSourceAccountId() {
        return sourceAccountId;
    }

    public Long getDestinationAccountId() {
        return destinationAccountId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }
}
