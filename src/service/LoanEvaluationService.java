package service;

import model.ApplicationStatus;
import model.ApprovedStatus;
import model.Customer;
import model.Loan;
import model.RejectedStatus;

public class LoanEvaluationService {

    // score minimo fijo
    private static final int MINIMUM_CREDIT_SCORE = 600;

    public ApplicationStatus evaluate(Customer customer, Loan loan) {
        
        //si el score del objeto es menor que el minimo de la constante 
        if (customer.creditScore() < MINIMUM_CREDIT_SCORE) {
            return new RejectedStatus("Puntaje de crédito insuficiente");
        }

        return new ApprovedStatus(
                    loan.getAmount(),
                    loan.calculateMonthlyInstallment());
    }
}