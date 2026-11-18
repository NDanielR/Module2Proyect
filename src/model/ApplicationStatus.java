package model;

public sealed interface ApplicationStatus //sealed limita quién puede implementar esta interfaz: en este caso ApprovedStatus y RejectedStatus.
        permits ApprovedStatus, RejectedStatus {
}
