package Tema1;

public abstract class ProducatorEnergie extends ComponentaRetea {
    public ProducatorEnergie(String id, boolean statusOperational) {
        super(id, statusOperational);
    }
    abstract double calculeazaProductie(double factorExtern);

    abstract double getCapacitate();
}

class TurbinaEoliana extends ProducatorEnergie {
    private double putereBaza;

    public TurbinaEoliana(String id, boolean statusOperational, double putereBaza) {
        super(id, statusOperational);
        this.putereBaza = putereBaza;
    }

    @Override
    public double calculeazaProductie(double factorExtern) {
        if (this.getStatusOperational())
            return putereBaza * factorExtern;
        else
            return 0;
    }

    @Override
    double getCapacitate() {
        return putereBaza;
    }
}

class PanouSolar extends ProducatorEnergie {
    private double putereMaxima;

    PanouSolar(String id, boolean statusOperational, double putereMaxima) {
        super(id, statusOperational);
        this.putereMaxima = putereMaxima;
    }

    @Override
    public double calculeazaProductie(double factorExtern) {
        if (this.getStatusOperational())
            return this.putereMaxima*factorExtern;
        else
            return 0;
    }

    @Override
    double getCapacitate() {
        return putereMaxima;
    }
}

class ReactorNuclear extends ProducatorEnergie {
    private double putereConstanta;

    public ReactorNuclear(String id, boolean statusOperational, double putereConstanta) {
        super(id, statusOperational);
        this.putereConstanta = putereConstanta;
    }

    @Override
    public double calculeazaProductie(double factor) {
        if (this.getStatusOperational())
            return putereConstanta;
        else
            return 0;
    }

    @Override
    double getCapacitate() {
        return putereConstanta;
    }
}
