# Medical Application using Spring Boot

### Descriere

MedicalApp este o aplicatie care vine in ajutorul unitatilor si cabinetelor medicale pentru a gestiona toate activitatile din cadrul acestora,
precum medicii angajati, investigatiile pe care acestia le fac, pacienti si programarile lor, rapoartele medicale.

### Entitati
Aceasta este diagrama bazei de date, in care se pot observa 7 entitati (`Patient`, `Doctor`, `Appointment`, `Investigation`, `MedicalReport`, `MedicalUnit` si `Specialization`)
si relatiile dintre acestea:
- intr-un cabinet lucraza mai multi medici, dar un medical nu poate lucra decat intr-un singur cabinet -> `OneToMany`
- un medica poate realiza mai multe investigatii, dar o investigatie nu poate fi facuta decat de un medic
  (chiar daca procedeul ar fi acelasi, id-ul este diferit deoarece pretul poate sa difere in functie de medic) -> `OneToMany`
- un medic poate avea mai multe specializari, iar o anumita specializare o pot avea mai multi medici -> `ManyToMany`
- un pacient poate avea mai multe programari, o programare apartine unui singur pacient -> `OneToMany`
- pentru o investigatie pot exista mai multe programari, dar o programare nu poate fi facuta decat pentru o singura investigatie -> `OneToMany`
- la finalul unei programari, se poate atasa un raport medical, iar un raport medical apartine unei singure programari - `OneToOne`

//TODO INSERT DIAGRAMA     
  
### Functionalitati si endpoint-urile corespunzatoare
1. Pentru specializari:
    - se poate adauga o specializare noua 
    - se poate actualiza descrierea unei specializari
    - se pot vizualiza detaliile unei specializari
    - se pot vizualiza toate specializarile
    
2. Pentru cabinete/unitati medicale:
    - se poate adauga o noua unitate medicala
    - se pot vizualiza toate unitatile medicale dintr-un anumit oras dorit
    
3. Pentru investigatii medicale:
    - se poate adauga o noua investigatie pentru un anumit medic (doar daca medicul exista)
    - se pot vizualiza detaliile unei investigatii
    - se poate actualiza pretul unei investigatii *doar daca* noul pret este mai mic sau mai mare decat pretul actual cu maxim 10%, 
      iar pretul nu poate sa aiba, bineinteles, o valoare negativa
    - se pot filtra investigatiile medicale astfel inca sa fie vizualizate doar cele care au pretul mai mic decat cel dat ca parametru 
    - se pot vizualiza investigatiile medicale realizate de un anumit doctor dat ca parametru
    - se poate sterge o investigatie *doar daca* nu exista vreo programare viitoare pentru aceasta investigatie
   
4. Pentru pacienti:
    - se pot adauga datele unui nou pacient in sistem
    - se pot adauga informatii suplimentare la istoricul medical al unui pacient deja existent
    - se pot vizualiza datele unui pacient
    
5. Pentru doctori:
    - se poate adauga un doctor care sa aiba o anumita specializare (trebuie sa existe deja in sistem)
      si pentru un anumit cabinet medical (care sa existe)
    - se pot vizualiza datele unui doctor
    - se poate vizualiza o lista cu toti doctorii care au o anumita specializare
    - se poate vizualiza o lista cu toti doctorii angajati la un anumit cabinet
    - se poate adauga o noua specializare pentru un doctor

6. Pentru programari:
    - se pot vizualiza o programare
    - se pot vizualiza programarile care s-au incheiat ale unui anumit pacient
    - se pot vizualiza programarile viitoare ale unui anumit pacient
    - se poate adauga o programare pentru o investigatie pentru un anumit pacient
    - se poate sterge o programare *doar daca* aceasta

7. Pentru rapoarte medicale:
    - se poate vizualiza un raport medical
    - se pot actualiza informatiile dintr-un raport medical (diagnostic, investigatii ulterioare necesare, interpretarea rezultatelor)
    - se poate adauga un raport medical pentru o programare

----

### Packages
Toate clasele sunt grupate în mai multe pachete:
- model: conține clasele pentru cele 7 entități, prezentate in diagrama de mai sus
- service: bean pentru definirea serviciilor (implementarea logicii de functionare a aplicatiei), câte unul per entitate
- repository: bean/interfata pentru comunicarea cu baza de date, câte unul pentru fiecare entitate
- exceptions: contine toate exceptiile custom definite, care sunt folosite in clasele Service; gestionarea acestora se face printr-un ControllerAdvice
![img_1.png](img/img_1.png)

----

### Validari
- Există validări la nivel de model pentru câmpurile unei entități:
![img_4.png](img/img_4.png)   
  

- Pentru a prinde cât mai devreme orice excepție, in controller se face validarea pentru entitati, precum și pentru orice alți parametri trimisi de client:
![img_5.png](img/img_5.png)


- De asemenea, exista excepțiile custom definite pentru diferite situații, care sunt gestionate într-un ControllerAdvice:
![img_3.png](img/img_3.png)

----

### Teste unitate
![img_2.png](img/img_2.png)

#### Pentru Services: 
![img_3.png](img/img_service_tests.png)   

#### Pentru Controllers:
![img_1.png](img/img_controller_tests.png)
----

### Documentatie Swagger

După rularea aplicației, aceasta poate fi testata în browser la acest link: `http://localhost:8080/swagger-ui/index.html#/`.

