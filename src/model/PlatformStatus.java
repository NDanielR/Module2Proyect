package model;

public enum PlatformStatus {

    //tipos
    OPERATIONAL("Operativa", true),
    UNDER_MAINTENANCE("En mantenimiento", false),
    OUT_OF_SERVICE("Fuera de servicio", false);

    //
    private final String description;
    private final boolean applicationsAllowed;

    //contructor del enum
    PlatformStatus(String description, boolean applicationsAllowed) {
        this.description = description;
        this.applicationsAllowed = applicationsAllowed;
    }

    public String getDescription() {
        return description;
    }

    public boolean allowsApplications() {
        return applicationsAllowed;
    }

    //por si necesito cambiar la descripcion
    @Override
    public String toString() {
        return description;
    }
}