package Tema1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GridController {
    private List<ProducatorEnergie>  producatori;
    private List<ConsumatorEnergie> consumatori;
    private List<Baterie> baterii;

    private boolean isBlackout = false;

    public GridController(List<ProducatorEnergie> producatori, List<ConsumatorEnergie> consumatori, ArrayList<Baterie> baterii) {
        this.producatori = producatori != null ? producatori : new ArrayList<>();
        this.consumatori = consumatori != null ? consumatori : new ArrayList<>();
        this.baterii = baterii != null ? baterii : new ArrayList<>();
    }

    public void simuleazaTick(double factorSoare, double factorVant) {
        for (ConsumatorEnergie consumator : this.consumatori) {
            if (consumator.getStatusOperational()) {
                consumator.setEsteAlimentat(true);
            } else {
                consumator.setEsteAlimentat(false);
            }
        }

        double productieTotala = 0;
        for (ProducatorEnergie producator : this.producatori) {
            double factor;
            if (producator instanceof ReactorNuclear) {
                factor = 1.0;
            } else if (producator instanceof TurbinaEoliana) {
                factor = factorVant;
            } else {
                factor = factorSoare;
            }
            productieTotala +=  producator.calculeazaProductie(factor);
        }
        double cerereTotala = 0;
        for (ConsumatorEnergie consumator : this.consumatori) {
            if (consumator.getStatusOperational()) {
                cerereTotala += consumator.getCerereCurenta();
            }
        }

        double delta = productieTotala - cerereTotala;

        if (delta > 0) {
            double surplus = delta;
            for (Baterie baterie : this.baterii) {
                    if (surplus == 0)
                        break;
                    surplus = baterie.incarca(surplus);
            }
        } else if (delta < 0) {
            double deficit = -delta;
            for (Baterie baterie : this.baterii) {
                if (deficit <= 0)
                    break;
                deficit -= baterie.descarca(deficit);
            }
            if (deficit > 0) {
                consumatori.sort(Comparator.comparing(ConsumatorEnergie::getPrioritate).reversed());
                for (ConsumatorEnergie consumator : this.consumatori) {
                    if (consumator.getStatusOperational() && consumator.getPrioritate() > 1 && deficit > 0) {
                        deficit -= consumator.getCerereEnergie();
                        consumator.decupleazaDeLaRetea();
                    }
                }
                if (deficit > 0) {
                    this.isBlackout = true;
                }
            }
        }
    }
    public boolean isBlackout() {
        return isBlackout;
    }
}
