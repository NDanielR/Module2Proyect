package model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class Loan {

    private final String loanId;
    private final BigDecimal amount;
    private final int termMonths;

    protected Loan(String loanId, BigDecimal amount, int termMonths) {
        this.loanId = loanId;
        //redondea el amount
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.termMonths = termMonths;
    }

    public String getLoanId() {
        return loanId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getTermMonths() {
        return termMonths;
    }

    public abstract BigDecimal calculateMonthlyInstallment();

    @Override
    public String toString() {
        return "Crédito: " + loanId
                + " | Monto: $" + amount.toPlainString()
                + " | Plazo: " + termMonths + " meses";
    }
}
