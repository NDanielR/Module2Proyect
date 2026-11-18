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
        // Scanner para el teclado
        Scanner scanner = new Scanner(System.in);

        // inicializa la clase portafilio manager
        PortfolioManager portfolioManager = new PortfolioManager();

        // Variable del estado de la aplicacion
        PlatformStatus platformStatus = PlatformStatus.OPERATIONAL;

        // iniciacion de la clase evaluacion de creditos
        LoanEvaluationService evaluationService = new LoanEvaluationService();

        // Almacena el credito pendiente
        Loan pendingLoan = null;

        // inicio del programa
        Boolean running = true;

        // menu
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

            // switch 
            switch (option) {
                case "1" -> {

                    // revisa el estado de plarformstatus usa el metodo allows para sacar el boolean
                    if (!platformStatus.allowsApplications()) {
                        System.out.println(
                                "Actualmente no es posible procesar solicitudes.");
                    } else {

                        //borramos el credito pendiente, u lo colocamos null
                        pendingLoan = null;

                        //Solicitud de informacion
                        System.out.print("Documento del cliente: ");
                        String id = scanner.nextLine();

                        System.out.print("Nombre del cliente: ");
                        String name = scanner.nextLine();

                        System.out.print("Puntaje de crédito: ");
                        int creditScore = Integer.parseInt(scanner.nextLine());

                        System.out.print("Ciudad: ");
                        String city = scanner.nextLine();

                        //Crea el objeto cliente
                        Customer customer = new Customer(id, name, creditScore, city);

                        //Tipo de credito
                        System.out.println("""

                                Tipo de crédito:
                                1. Emprendimiento
                                2. Vivienda social
                                """);

                        String loanType = scanner.nextLine();

                        //Valida el tipo de credito
                        if (!loanType.equals("1") && !loanType.equals("2")) {
                            System.out.println("Tipo de crédito no válido.");
                        } else {
                            System.out.print("Identificador del crédito: ");
                            String loanId = scanner.nextLine();

                            System.out.print("Monto solicitado: ");
                            BigDecimal requestedAmount = new BigDecimal(scanner.nextLine());

                            System.out.print("Plazo en meses: ");
                            Integer termMonths = Integer.parseInt(scanner.nextLine());

                            //creamos el objeto dependiendo del tipo de credito escojido
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

                            //usamos el servicio para evaluar al cliente y el prestamos si es viable
                            //el resultado es un objeto del tipos approvedstatus o rejectedstatus que usan
                            //que usan la interface applicationstatus
                            ApplicationStatus result = evaluationService.evaluate(customer, loan);

                            // switch pattern matching paso result al switch y depende del 
                            //objeto realizara el caso
                            switch (result) {

                                //si es approvedstatus 
                                case ApprovedStatus approved -> {
                                    
                                    //el prestamo lo almaceno en el credito pendiente 
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

                                //si es rejectedstatu
                                case RejectedStatus rejected -> {
                                    System.out.println("Solicitud rechazada.");
                                    System.out.println(
                                            "Motivo: " + rejected.rejectionReason());
                                }
                            }
                        }
                    }
                }

                //guardar el credito simulado si fue aprobado
                case "2" -> {

                    //valida la plataforma
                    if (!platformStatus.allowsApplications()) {
                        System.out.println(
                                "Actualmente no es posible registrar créditos.");
                    } 
                    //valida si ya se hizo la simulacion de credito si es igual a null
                    else if (pendingLoan == null) {
                        System.out.println(
                                "No hay una solicitud aprobada pendiente de registro. "
                                        + "Primero realice una simulación en la opción 1.");
                    } 
                    
                    //si pendingloan no es null guardamos el credito usando registerloan
                    else {
                        Boolean registered = portfolioManager.registerLoan(pendingLoan);

                        if (registered) {
                            System.out.println("Crédito registrado exitosamente.");
                            System.out.println(pendingLoan);

                            //deja la variable vacia para la proxima ya que se registro el credito
                            pendingLoan = null;
                        }
                    }
                }

                //muestra los credito que se aprobaron que estan el la varible portfoliomanager
                case "3" -> {
                    portfolioManager.showPortfolio();

                    System.out.println(
                            "Monto total colocado: $"
                                    + portfolioManager
                                            .calculateTotalPlacedAmount()
                                            .toPlainString());
                }

                //muestra el estado de la plataforma 
                case "4" -> System.out.println(
                        "Estado de la plataforma: " + platformStatus);

                //sale del programa
                case "0" -> {
                    running = false;
                    System.out.println("Gracias por utilizar PrestaYa.");
                }

                default -> System.out.println(
                        "Opción no válida. Seleccione una opción del menú.");
            }
        }

        //cerrar el scanner
        scanner.close();
    }
}
