package model;

//implementacion de la interfaz
public record RejectedStatus(
        String rejectionReason) implements ApplicationStatus {

}
