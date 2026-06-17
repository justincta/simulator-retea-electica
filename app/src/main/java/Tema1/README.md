Simulator Retea Energetica
Acest proiect reprezinta prima mea tema majora la Programare Orientata pe Obiecte (POO). Scopul aplicatiei este simularea unui sistem energetic simplificat (un "Grid"), format din producatori de energie, consumatori si baterii. Aplicatia primeste comenzi text pentru a adauga componente, a simula trecerea timpului ("ticks") si a gestiona situatiile de criza (deficit de energie).
Structura Proiectului
Am incercat sa organizez codul cat mai logic, folosind conceptele invatate la curs (mostenire, polimorfism, abstractizare).
1. Ierarhia de Clase (Entities)
   La baza sistemului sta clasa abstracta ComponentaRetea. Am facut-o abstracta pentru ca nu are sens sa avem o "componenta" generica in retea, ci doar tipuri specifice. Din ea mostenesc toate entitatile:
   ProducatorEnergie: Clasa abstracta pentru PanouSolar, TurbinaEoliana si ReactorNuclear. Fiecare calculeaza productia diferit (polimorfism), in functie de factorii externi (soare/vant).
   ConsumatorEnergie: Include SistemSuportViata (prioritate maxima), LaboratorStiintific si SistemIluminat.
   Baterie: Stocheaza surplusul de energie si o elibereaza la nevoie.
2. Logica Aplicatiei (GridController)
   Aceasta este clasa "creier". Aici se intampla toata matematica din spatele simularii. In metoda simuleazaTick:
   Se calculeaza productia totala si cererea totala (doar de la componentele operationale!).
   Daca avem surplus, incarcam bateriile.
   Daca avem deficit, descarcam bateriile.
   Daca bateriile sunt goale si tot avem deficit, intra in actiune sistemul de Triage (Load Shedding): decuplez consumatorii cu prioritate mica (iluminat, laboratoare) pentru a salva sistemele critice.
   Daca nici asa nu facem fata, se declanseaza BLACKOUT.
3. Interactiunea cu Utilizatorul (App)
   Clasa App se ocupa de citirea comenzilor de la tastatura si afisarea rezultatelor. Am folosit un switch pentru a interpreta comenzi precum add_producator, next_tick sau set_defect.
   Un aspect important la care a trebuit sa fiu atent a fost gestionarea starii de Blackout. Odata ce reteaua pica, majoritatea comenzilor sunt blocate si se afiseaza un mesaj de eroare, fiind permise doar comenzile de "read-only" (status si istoric).
   Provocari si Solutii
   Cea mai grea parte a fost sa gestionam corect consumatorii defecti vs. cei decuplati.
   Initial, calculam cererea totala doar pentru cei alimentati, dar testele cereau sa afisez cererea potentiala a tuturor aparatelor functionale, chiar daca eu le taiasem curentul.
   Am rezolvat asta filtrand listele: un consumator defect (statusOperational = false) este ignorat complet, pe cand unul decuplat apare in statistici dar nu consuma efectiv.
   De asemenea, am avut grija ca o baterie defecta sa nu poata fi incarcata sau descarcata, comportament implementat direct in clasa Baterie.