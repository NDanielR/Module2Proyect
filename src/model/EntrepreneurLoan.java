package model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class EntrepreneurLoan extends Loan {

    private static final BigDecimal INTEREST_RATE =
            new BigDecimal("0.012"); //tasa de interés preferencial fija del 1.2% mensual.

    public EntrepreneurLoan(
            String loanId,
            BigDecimal amount,
            int termMonths
    ) {
        super(loanId, amount, termMonths);
    }

    @Override
    public BigDecimal calculateMonthlyInstallment() {
        BigDecimal growthFactor = BigDecimal.ONE
                .add(INTEREST_RATE)
                .pow(getTermMonths());

        BigDecimal numerator = getAmount()
                .multiply(INTEREST_RATE)
                .multiply(growthFactor);

        BigDecimal denominator = growthFactor
                .subtract(BigDecimal.ONE);

        return numerator.divide(
                denominator,
                2,
                RoundingMode.HALF_UP
        );
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Tipo: Emprendimiento"
                + " | Tasa mensual: 1.2%"
                + " | Cuota: $"
                + calculateMonthlyInstallment().toPlainString();
    }
}
