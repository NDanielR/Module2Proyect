package service;

import model.Loan;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PortfolioManager {

    //creamos un array con 10 espacios de prestamos 
    private final Loan[] loans = new Loan[10];
    //contador para saber cuantos prestamos tenemos
    private Integer loanCount = 0;

    //mtd de registrar prestamos
    public boolean registerLoan(Loan loan) {
        //si el contador de prestamos es mayor igual que el tamaño del array
        if (loanCount >= loans.length) {
            System.out.println(
                    "No fue posible registrar el crédito: "
                            + "la cartera alcanzó su capacidad máxima "
                            + "de 10 créditos."
            );

            return false;
        }

        //guardo el credito en la posicion del contador
        loans[loanCount] = loan;
        loanCount++;
        return true;
    }

    //mostrar los prestamos
    public void showPortfolio() {
        if (loanCount == 0) {
            System.out.println(
                    "La cartera no tiene créditos registrados."
            );
            return;
        }

        //si tiene imprime el array
        for (int i = 0; i < loanCount; i++) {
            System.out.println(loans[i]);
        }
    }

    public BigDecimal calculateTotalPlacedAmount() {
        //inicializamos variable
        BigDecimal total = BigDecimal.ZERO;


        for (int i = 0; i < loanCount; i++) {
            //suma los montos de los creditos
            total = total.add(loans[i].getAmount());
        }

        //retorna el total redondeado
        return total.setScale(2, RoundingMode.HALF_UP);
    }
}
