import model.PlatformStatus;
import service.PortfolioManager;

import java.util.Scanner;
import model.ApplicationStatus;
import model.ApprovedStatus;
import model.Customer;
import model.EntrepreneurLoan;
import model.Loan;
import model.RejectedStatus;
import model.SocialHousingLoan;
import service.LoanEvaluationService;

import java.math.BigDecimal;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PortfolioManager portfolioManager = new PortfolioManager();
        //Variable del estado de la aplicacion 
        PlatformStatus platformStatus = PlatformStatus.OPERATIONAL;
        LoanEvaluationService evaluationService = new LoanEvaluationService();
        Loan pendingLoan = null;

        boolean running = true;

        while (running) {
            System.out.println("""

                    ===== PRESTAYA =====
                    1. Simular nueva solicitud
                    2. Aprobar y registrar
                    3. Ver cartera total
                    4. Estado de la plataforma
                    0. Salir
                    """);

            System.out.print("Seleccione una opción: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1" -> {
                    if (!platformStatus.allowsApplications()) {
                        System.out.println(
                                "Actualmente no es posible procesar solicitudes.");
                    } else {
                        pendingLoan = null;

                        System.out.print("Documento del cliente: ");
                        String id = scanner.nextLine();

                        System.out.print("Nombre del cliente: ");
                        String name = scanner.nextLine();

                        System.out.print("Puntaje de crédito: ");
                        int creditScore = Integer.parseInt(scanner.nextLine());

                        System.out.print("Ciudad: ");
                        String city = scanner.nextLine();

                        Customer customer = new Customer(id, name, creditScore, city);

                        System.out.println("""

                                Tipo de crédito:
                                1. Emprendimiento
                                2. Vivienda social
                                """);

                        String loanType = scanner.nextLine();

                        if (!loanType.equals("1") && !loanType.equals("2")) {
                            System.out.println("Tipo de crédito no válido.");
                        } else {
                            System.out.print("Identificador del crédito: ");
                            String loanId = scanner.nextLine();

                            System.out.print("Monto solicitado: ");
                            BigDecimal requestedAmount = new BigDecimal(scanner.nextLine());

                            System.out.print("Plazo en meses: ");
                            int termMonths = Integer.parseInt(scanner.nextLine());

                            Loan loan;

                            if (loanType.equals("1")) {
                                loan = new EntrepreneurLoan(
                                        loanId,
                                        requestedAmount,
                                        termMonths);
                            } else {
                                System.out.print("Monto del subsidio: ");
                                BigDecimal subsidy = new BigDecimal(scanner.nextLine());

                                loan = new SocialHousingLoan(
                                        loanId,
                                        requestedAmount,
                                        termMonths,
                                        subsidy);
                            }

                            ApplicationStatus result = evaluationService.evaluate(customer, loan);

                            switch (result) {
                                case ApprovedStatus approved -> {
                                    pendingLoan = loan;

                                    System.out.printf("""

                                            ===== PLAN DE PAGOS RESUMIDO =====
                                            Cliente: %s
                                            Identificador del crédito: %s
                                            Monto aprobado: $%s
                                            Plazo: %d meses
                                            Cuota mensual: $%s

                                            Simulación aprobada.
                                            Seleccione la opción 2 para registrar el crédito.
                                            %n""",
                                            customer.name(),
                                            loan.getLoanId(),
                                            approved.approvedAmount().toPlainString(),
                                            loan.getTermMonths(),
                                            approved.calculatedInstallment().toPlainString());
                                }

                                case RejectedStatus rejected -> {
                                    System.out.println("Solicitud rechazada.");
                                    System.out.println(
                                            "Motivo: " + rejected.rejectionReason());
                                }
                            }
                        }
                    }
                }

                case "2" -> {
                    if (!platformStatus.allowsApplications()) {
                        System.out.println(
                                "Actualmente no es posible registrar créditos.");
                    } else if (pendingLoan == null) {
                        System.out.println(
                                "No hay una solicitud aprobada pendiente de registro. "
                                        + "Primero realice una simulación en la opción 1.");
                    } else {
                        boolean registered = portfolioManager.registerLoan(pendingLoan);

                        if (registered) {
                            System.out.println("Crédito registrado exitosamente.");
                            System.out.println(pendingLoan);

                            pendingLoan = null;
                        }
                    }
                }

                case "3" -> {
                    portfolioManager.showPortfolio();

                    System.out.println(
                            "Monto total colocado: $"
                                    + portfolioManager
                                            .calculateTotalPlacedAmount()
                                            .toPlainString());
                }

                case "4" -> System.out.println(
                        "Estado de la plataforma: " + platformStatus);

                case "0" -> {
                    running = false;
                    System.out.println("Gracias por utilizar PrestaYa.");
                }

                default -> System.out.println(
                        "Opción no válida. Seleccione una opción del menú.");
            }
        }

        scanner.close();
    }
}
