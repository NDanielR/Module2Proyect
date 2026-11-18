package model;

//implementacion de la interfaz
//rechazo del credito
public record RejectedStatus(
        String rejectionReason) implements ApplicationStatus {

}
