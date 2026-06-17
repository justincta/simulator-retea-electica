package Tema1;

public class Baterie extends ComponentaRetea {
    private double capacitateMaxima;
    private double energieStocata;

    public Baterie(String id, boolean statusOperational, double capacitateMaxima, double energieStocata) {
        super(id, statusOperational);
        this.capacitateMaxima = capacitateMaxima;
        this.energieStocata = energieStocata;
    }

    public double getCapacitateMaxima() {
        return capacitateMaxima;
    }
    public double getEnergieStocata() {
        return energieStocata;
    }
    public void setCapacitateMaxima(double capacitateMaxima) {
        this.capacitateMaxima = capacitateMaxima;
    }
    public void setEnergieStocata(double energieStocata) {
        this.energieStocata = energieStocata;
    }

    public double incarca(double energieDisponibila) {
        if (!this.getStatusOperational())
            return energieDisponibila; // o returnez ca pe surplus
        if (this.capacitateMaxima - this.energieStocata >= energieDisponibila) {
            this.setEnergieStocata(this.energieStocata + energieDisponibila);
            return 0;
        } else {
            double surplus = (this.energieStocata + energieDisponibila) - this.capacitateMaxima;
            this.setEnergieStocata(this.capacitateMaxima);
            return surplus;
        }
    }

    public double descarca(double energieCeruta) {
        if (!this.getStatusOperational())
            return 0;

        if (this.energieStocata >= energieCeruta) {
            this.setEnergieStocata(this.energieStocata - energieCeruta);
            return energieCeruta;
        } else {
            double energieDisponibila = getEnergieStocata();
            this.setEnergieStocata(0);
            return energieDisponibila;
        }
    }
}