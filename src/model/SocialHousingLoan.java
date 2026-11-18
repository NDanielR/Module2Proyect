package model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SocialHousingLoan extends Loan {

    private final BigDecimal governmentSubsidieAmount;

    public SocialHousingLoan(
            String loanId,
            BigDecimal amount,
            Integer termMonths,
            BigDecimal governmentSubsidieAmount) {
        super(loanId, amount, termMonths);
        this.governmentSubsidieAmount = governmentSubsidieAmount
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getGovernmentSubsidieAmount() {
        return governmentSubsidieAmount;
    }

    @Override
    public BigDecimal calculateMonthlyInstallment() {
        BigDecimal financedAmount = getAmount()
                .subtract(governmentSubsidieAmount);

        return financedAmount.divide(
                BigDecimal.valueOf(getTermMonths()),
                2,
                RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Tipo: Vivienda social"
                + " | Subsidio: $"
                + governmentSubsidieAmount.toPlainString()
                + " | Cuota: $"
                + calculateMonthlyInstallment().toPlainString();
    }
}
