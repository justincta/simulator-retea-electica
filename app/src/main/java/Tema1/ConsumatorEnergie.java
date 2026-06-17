package Tema1;

public abstract class ConsumatorEnergie extends ComponentaRetea{
    private double cerereEnergie;
    private int prioritate;
    private boolean esteAlimentat;

    public ConsumatorEnergie(String id,boolean statusOperational, double cerereEnergie,  int prioritate, boolean esteAlimentat) {
        super(id, statusOperational);
        this.cerereEnergie = cerereEnergie;
        this.prioritate = prioritate;
        this.esteAlimentat = esteAlimentat;
    }

    public double getCerereEnergie() {
        return cerereEnergie;
    }
    public int getPrioritate() {
        return prioritate;
    }
    public boolean isAlimentat() {
        return esteAlimentat;
    }
    public void setCerereEnergie(double cerereEnergie) {
        this.cerereEnergie = cerereEnergie;
    }
    public void setPrioritate(int prioritate) {
        this.prioritate = prioritate;
    }
    public void setEsteAlimentat(boolean esteAlimentat) {
        this.esteAlimentat = esteAlimentat;
    }

    public double getCerereCurenta() {
        if (this.esteAlimentat)
            return this.cerereEnergie;
        else
            return 0;
    }

    public void cupleazaLaRetea() {
        this.setEsteAlimentat(true);
    }
    public void decupleazaDeLaRetea() {
        this.setEsteAlimentat(false);
    }
}

class SistemSuportViata extends ConsumatorEnergie {
    public SistemSuportViata(String id, boolean statusOperational, double cerereEnergie, boolean esteAlimentat) {
        super(id, statusOperational, cerereEnergie, 1, esteAlimentat);
    }
}

class LaboratorStiintific extends ConsumatorEnergie {
    public LaboratorStiintific(String id, boolean statusOperational, double cerereEnergie, boolean esteAlimentat) {
        super(id, statusOperational, cerereEnergie, 2, esteAlimentat);
    }
}

class SistemIluminat extends ConsumatorEnergie {
    public SistemIluminat(String id, boolean statusOperational, double cerereEnergie, boolean esteAlimentat) {
        super(id, statusOperational, cerereEnergie, 3, esteAlimentat);
    }
}
