package model;

import java.math.BigDecimal;
import java.math.RoundingMode;

//aplicamos la interface a record
public record ApprovedStatus(
        BigDecimal approvedAmount,
        BigDecimal calculatedInstallment) implements ApplicationStatus {

    public ApprovedStatus {
        approvedAmount = approvedAmount
                .setScale(2, RoundingMode.HALF_UP);

        calculatedInstallment = calculatedInstallment
                .setScale(2, RoundingMode.HALF_UP);
    }
}
