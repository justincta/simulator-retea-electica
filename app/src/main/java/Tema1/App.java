package Tema1;

import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class App {
    private Scanner scanner;


    private List<ProducatorEnergie> producatori;
    private List<ConsumatorEnergie> consumatori;
    private ArrayList<Baterie> baterii;

    private GridController controller;
    private List<String> istoricEvenimente;
    private boolean running;

    public App(InputStream input) {
        this.scanner = new Scanner(input);

        this.producatori = new ArrayList<>();
        this.consumatori = new ArrayList<>();
        this.baterii = new ArrayList<>();
        this.istoricEvenimente = new ArrayList<>();

        this.controller = new GridController(producatori, consumatori, baterii);
        this.running = true;
    }

    public void run() {
        while (running && scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            String command = parts[0];

            boolean isAllowedInBlackout =
                            command.equals("5") || command.equals("status_grid") || command.equals("status_retea") ||
                            command.equals("6") || command.equals("istoric_evenimente") ||
                            command.equals("7") || command.equals("exit");

            if (controller.isBlackout() && !isAllowedInBlackout) {
                System.out.println("EROARE: Reteaua este in BLACKOUT. Simulare oprita.");
                continue;
            }

            try {
                switch (command) {
                    case "0": // Adaugare producator
                    case "add_producator":
                        handleAddProducator(parts);
                        break;
                    case "1": // Adaugare consumator
                    case "add_consumator":
                        handleAddConsumator(parts);
                        break;
                    case "2": // Adaugare baterie
                    case "add_baterie":
                        handleAddBaterie(parts);
                        break;
                    case "3": // Simulare tick
                    case "next_tick":
                        handleSimulareTick(parts);
                        break;
                    case "4": // Setare defectiune
                    case "set_defect":
                        handleSetDefect(parts);
                        break;
                    case "5": // Status retea
                    case "status_grid":
                    case "status_retea":
                        handleStatusGrid();
                        break;
                    case "6": // Istoric evenimente
                    case "istoric_evenimente":
                        handleIstoric();
                        break;
                    case "7": // Iesire
                    case "exit":
                        System.out.println("Simulatorul se inchide.");
                        running = false;
                        break;
                    default:
                        System.out.println("EROARE: Comanda necunoscuta.");
                        break;
                }
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
    }


    private void handleAddProducator(String[] parts) {
        if (parts.length < 4) {
            System.out.println("EROARE: Format comanda invalid");
            return;
        }
        String tip = parts[1];
        String id = parts[2];
        String putereStr = parts[3];

        if (existsId(id)) {
            System.out.println("EROARE: Exista deja o componenta cu id-ul " + id);
            return;
        }

        double putere;
        try {
            putere = Double.parseDouble(putereStr);
        } catch (NumberFormatException e) {
            System.out.println("EROARE: Putere invalida");
            return;
        }

        if (putere <= 0) {
            System.out.println("EROARE: Putere invalida");
            return;
        }

        ProducatorEnergie p = null;

        switch (tip) {
            case "solar":
                p = new PanouSolar(id, true, putere);
                break;
            case "turbina":
                p = new TurbinaEoliana(id, true, putere);
                break;
            case "reactor":
                p = new ReactorNuclear(id, true, putere);
                break;
            default:
                System.out.println("EROARE: Tip producator invalid");
                return;
        }

        producatori.add(p);
        System.out.println("S-a adaugat producatorul " + id + " de tip " + tip);
    }

    private void handleAddConsumator(String[] parts) {
        if (parts.length < 4) {
            System.out.println("EROARE: Format comanda invalid");
            return;
        }
        String tip = parts[1];
        String id = parts[2];
        String cerereStr = parts[3];

        if (existsId(id)) {
            System.out.println("EROARE: Exista deja o componenta cu id-ul " + id);
            return;
        }

        double cerere;
        try {
            cerere = Double.parseDouble(cerereStr);
        } catch (NumberFormatException e) {
            System.out.println("EROARE: Cerere putere invalida");
            return;
        }

        if (cerere <= 0) {
            System.out.println("EROARE: Cerere putere invalida");
            return;
        }

        ConsumatorEnergie c = null;
        // Constructorii subclaselor seteaza automat prioritatea
        // statusOperational = true, esteAlimentat = true (la inceput)
        switch (tip) {
            case "suport_viata":
                c = new SistemSuportViata(id, true, cerere, true);
                break;
            case "laborator":
                c = new LaboratorStiintific(id, true, cerere, true);
                break;
            case "iluminat":
                c = new SistemIluminat(id, true, cerere, true);
                break;
            default:
                System.out.println("EROARE: Tip consumator invalid");
                return;
        }

        consumatori.add(c);
        System.out.println("S-a adaugat consumatorul " + id + " de tip " + tip);
    }

    private void handleAddBaterie(String[] parts) {
        if (parts.length < 3) {
            System.out.println("EROARE: Format comanda invalid");
            return;
        }

        String id = parts[1];
        String capStr = parts[2];

        if (existsId(id)) {
            System.out.println("EROARE: Exista deja o componenta cu id-ul " + id);
            return;
        }

        double capacitate;
        try {
            capacitate = Double.parseDouble(capStr);
        } catch (NumberFormatException e) {
            System.out.println("EROARE: Capacitate invalida");
            return;
        }

        if (capacitate <= 0) {
            System.out.println("EROARE: Capacitate invalida");
            return;
        }

        // status=true, stocata=0 la inceput
        Baterie b = new Baterie(id, true, capacitate, 0);
        baterii.add(b);
        System.out.println("S-a adaugat bateria " + id + " cu capacitatea " + capStr);
    }

    private void handleSimulareTick(String[] parts) {
        if (parts.length < 3) {
            System.out.println("EROARE: Format comanda invalid");
            return;
        }

        double soare, vant;
        try {
            soare = Double.parseDouble(parts[1]);
            vant = Double.parseDouble(parts[2]);
        } catch (NumberFormatException e) {
            System.out.println("EROARE: Factori invalizi");
            return;
        }

        controller.simuleazaTick(soare, vant);

        if (controller.isBlackout()) {
            String msg = "BLACKOUT! SIMULARE OPRITA.";
            System.out.println(msg);


            int tickNum = istoricEvenimente.size() + 1;
            istoricEvenimente.add("Tick " + (istoricEvenimente.size() + 1) + ": BLACKOUT! SIMULARE OPRITA.");
        } else {
            // Trebuie recalculate valorile pentru afisare, deoarece simuleazaTick e void
            // si starea s-a schimbat (baterii incarcate/descarcate, consumatori decuplati).

            double productieTotala = 0;
            for (ProducatorEnergie p : producatori) {
                double factor = 0;
                if (p instanceof ReactorNuclear) factor = 1.0;
                else if (p instanceof TurbinaEoliana) factor = vant;
                else if (p instanceof PanouSolar) factor = soare;

                productieTotala += p.calculeazaProductie(factor);
            }

            double cerereTotala = 0;
            List<String> decuplati = new ArrayList<>();

            for (ConsumatorEnergie c : consumatori) {
                if (c.getStatusOperational()) {
                    cerereTotala += c.getCerereEnergie();

                    if (!c.isAlimentat()) {
                        decuplati.add(c.getId());
                    }
                }
            }

            double bateriiTotal = 0;
            for (Baterie b : baterii) {
                bateriiTotal += b.getEnergieStocata();
            }

            DecimalFormat df = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));

            String out = "TICK: Productie " + df.format(productieTotala) +
                    ", Cerere " + df.format(cerereTotala) +
                    ". Baterii: " + df.format(bateriiTotal) + " MW. " +
                    "Decuplati: " + decuplati.toString();

            System.out.println(out);

            if (!decuplati.isEmpty()) {
                istoricEvenimente.add("Tick " + (istoricEvenimente.size() + 1) +
                        ": Deficit - Decuplat " + String.join(", ", decuplati));
            } else {
                istoricEvenimente.add("Tick " + (istoricEvenimente.size() + 1) + ": Stabil");
            }
        }
    }

    private void handleSetDefect(String[] parts) {
        if (parts.length < 3) return;
        String id = parts[1];
        String statusStr = parts[2];

        ComponentaRetea comp = findComponentById(id);
        if (comp == null) {
            System.out.println("EROARE: Nu exista componenta cu id-ul " + id);
            return;
        }

        boolean status;
        if (statusStr.equalsIgnoreCase("true")) {
            status = true; // Operational
        } else if (statusStr.equalsIgnoreCase("false")) {
            status = false; // Defect
        } else {
            System.out.println("EROARE: Status invalid");
            return;
        }

        comp.setStatusOperational(status);
        if (status) {
            System.out.println("Componenta " + id + " este acum operationala.");
        } else {
            System.out.println("Componenta " + id + " este acum defecta.");
        }
    }

    private void handleStatusGrid() {
        if (producatori.isEmpty() && consumatori.isEmpty() && baterii.isEmpty()) {
            System.out.println("Reteaua este goala.");
            return;
        }

        for (ProducatorEnergie p : producatori) {
            System.out.println(String.format("Producator %s (%s) - PutereBaza: %.2f - Status: %s",
                    p.getId(),
                    p.getClass().getSimpleName(),
                    p.getCapacitate(),
                    (p.getStatusOperational() ? "Operational" : "Defect")));
        }

        for (ConsumatorEnergie c : consumatori) {
            System.out.println(String.format("Consumator %s (%s) - Cerere: %.2f - Prioritate: %d - Status: %s",
                    c.getId(),
                    c.getClass().getSimpleName(),
                    c.getCerereEnergie(),
                    c.getPrioritate(),
                    (c.isAlimentat() ? "Alimentat" : "Decuplat")));
        }

        for (Baterie b : baterii) {
            System.out.println(String.format("Baterie %s - Stocare: %.2f/%.2f - Status: Operational",
                    b.getId(),
                    b.getEnergieStocata(),
                    b.getCapacitateMaxima()));
        }

        if (controller.isBlackout()) {
            System.out.println("Stare Retea: BLACKOUT");
        } else {
            System.out.println("Stare Retea: STABILA");
        }
    }

    private void handleIstoric() {
        boolean hasEvents = false;
        for (String ev : istoricEvenimente) {
            if (!ev.contains("Stabil")) {
                System.out.println(ev);
                hasEvents = true;
            }
        }

        if (!hasEvents) {
            System.out.println("Istoric evenimente gol.");
        }
    }

    private boolean existsId(String id) {
        return findComponentById(id) != null;
    }

    private ComponentaRetea findComponentById(String id) {
        for (ProducatorEnergie p : producatori) {
            if (p.getId().equals(id)) return p;
        }
        for (ConsumatorEnergie c : consumatori) {
            if (c.getId().equals(id)) return c;
        }
        for (Baterie b : baterii) {
            if (b.getId().equals(id)) return b;
        }
        return null;
    }

    public static void main(String[] args) {
        App app = new App(System.in);
        app.run();
    }
}