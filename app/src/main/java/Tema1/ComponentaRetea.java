package Tema1;

public abstract class ComponentaRetea {
    private String id;
    private boolean statusOperational;

    public ComponentaRetea(String id, boolean statusOperational) {
        this.id = id;
        this.statusOperational = statusOperational;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public boolean getStatusOperational() {
        return statusOperational;
    }
    public void setStatusOperational(boolean statusOperational) {
        this.statusOperational = statusOperational;
    }
}