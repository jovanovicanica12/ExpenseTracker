# Jetpack Compose Tutorijal: Expense Tracker
### (Room + MVVM + Charts)

> Android aplikacija za praćenje ličnih troškova razvijena korišćenjem **Jetpack Compose** framework-a.

---

## Uvod

Razvoj korisničkog interfejsa (UI) je jedan od najvažnijih aspekata Android aplikacija.
Android UI je razvijen korišćenjem XML layout fajlova, dok se logika aplikacije pisala u Javi ili Kotlinu. Sa razvojem modernih tehnologija i potrebom za bržim i fleksibilnijim razvojem, Google je predstavio Jetpack Compose – moderan, deklarativni UI framework.

Ovaj projekat predstavlja praktičan primer razvoja moderne Android aplikacije za praćenje ličnih troškova (**Expense Tracker**) korišćenjem **Jetpack Compose**-a.
Cilj ovog projekta je detaljna analiza i poređenje ova dva pristupa, sa osvrtom na arhitekturu, način rada, prednosti, mane i primenu u realnim projektima.

Aplikacija omogućava unos, izmenu, brisanje i pregled troškova, kao i vizuelni prikaz statistike kroz grafikone.

---
## XML
XML se koristi za definisanje izgleda Android ekrana. Svaki ekran se opisuje posebnim XML fajlom, dok se ponašanje komponenti kontroliše iz Java ili Kotlin koda.

### Osnovne karakteristike
- UI i logika su strogo odvojeni
- Svaki element ima sopstveni XML tag
- Hijerarhijska struktura layouta
- Često korišćenje findViewById (ili ViewBinding/DataBinding)

### Prednosti
- Dugogodišnja stabilnost i dokumentacija
- Velika količina postojećih projekata i primera
- Lakše razumevanje za početnike
- Jasna separacija dizajna i logike

### Mane
- Veliki i nepregledni layout fajlovi
- Teško upravljanje dinamičkim UI-em
- Boilerplate kod (binding, adapteri, listeneri)
- Kompleksne hijerarhije smanjuju performanse

---

## Jetpack Compose 
Jetpack Compose predstavlja potpuno novi način razmišljanja o UI-u. Umesto opisivanja kako da se UI izgradi, Compose opisuje kako UI treba da izgleda u datom stanju.

### Osnovne karakteristike
- UI se piše direktno u Kotlinu
- Deklarativni pristup
- Automatski reaguje na promenu stanja (state)
- Bez XML fajlova

### Prednosti
- Manje koda (manje boilerplate-a)
- Lakše upravljanje dinamičkim UI-em
- Bolja čitljivost i održavanje koda
- Jednostavne animacije i teme
- Brži razvoj i iteracije (Preview, Hot Reload)

### Mane
- Novija tehnologija (manje legacy resursa)
- Veća početna kriva učenja
- Migracija postojećih XML projekata zahteva vreme
- Potrebno razumevanje stanja i recomposition-a

---

## XML vs Jetpack Compose
| Osobina | XML | Jetpack Compose |
|--------|-----|-----------------|
| Način pisanja | Imperativni | Deklarativni |
| Jezik | XML + Kotlin/Java | Kotlin |
| Upravljanje stanjem | Ručno | Automatsko |
| Animacije | Kompleksne | Jednostavne |
| Performanse | Zavise od hijerarhije | Optimizovana recomposition |
| Testiranje | Teže UI testiranje | Lakše UI testiranje |
| Brzina razvoja | Sporija | Brža |

---

## Arhitektura i upravljanje stanjem
### Kod XML-a, promene UI-a zahtevaju ručne izmene:
- Menjanje vidljivosti
- Ponovno bindovanje podataka
- Pozivanje metoda nad view-ovima

### U Jetpack Compose-u:
- UI je funkcija stanja
- Promena stanja automatski izaziva recomposition
- Manja šansa za greške i „nekonzistentan UI“

---

## Performanse i održavanje
### XML:
- Može imati duboke view hijerarhije
- Teže održavanje velikih layout fajlova
- Veći rizik od grešaka pri promenama

### Jetpack Compose:
- Koristi pametnu recomposition strategiju
- Izbegava duboke hijerarhije
- Olakšava refaktorisanje i ponovno korišćenje komponenti

--- 

## Primena u realnim projektima
- XML je i dalje čest u legacy projektima
- Jetpack Compose je preporučeni pristup za nove projekte
- Moguća je postepena migracija (Compose + XML zajedno)

--- 

## Funkcionalnosti Expense Tracker aplikacije

### Home ekran
- Prikaz ukupne potrošnje
- Broj unetih troškova
- Filteri po kategorijama
- Lista troškova:
    - Opis
    - Kategorija
    - Iznos (€)
    - Ikonica za brisanje
- Empty state ako nema podataka

### Dodavanje troška
- Unos iznosa (€)
- Opis (opciono)
- Izbor kategorije (dropdown)
- Validacija (iznos > 0)
- Snackbar potvrda

### Izmena troška
- Unapred popunjena forma
- Izmena podataka
- Brisanje uz dijalog potvrde

### Statistika
- Izbor vremenskog opsega:
    - Svi troškovi
    - Poslednjih 7 dana
    - Poslednjih 30 dana
    - Tekući mesec
- Pie chart
- Bar chart
- Legenda (kategorija + procenat + iznos)
- Empty state ako nema podataka

---

## Nefunkcionalni zahtevi

- Čista i čitljiva arhitektura (MVVM)
- Trajno čuvanje podataka (Room)
- Konzistentan UI
- Jednostavno pokretanje projekta

---

## Korišćene tehnologije

- **Kotlin**
- **Jetpack Compose**
- **Material 3**
- **Navigation Compose**
- **Room**
- **ViewModel**
- **Coroutines**
- **Flow / StateFlow**

---

## Arhitektura (MVVM)

Aplikacija koristi **MVVM** arhitekturu.

### Slojevi:
- **UI (Compose)** – Prikazuje podatke i reaguje na stanje
- **ViewModel** – Čuva UI stanje i poslovnu logiku
- **Repository** – Jedinstvena tačka pristupa podacima
- **Room** – Lokalna baza podataka

### Zašto MVVM + Compose?

Compose prirodno radi sa **Flow / StateFlow**:
- ViewModel emituje stanje
- UI automatski reaguje
- Nema manuelnih UI ažuriranja

---

## Model podataka (Room)

Svaki trošak se čuva u Room bazi.

### Polja:
- `id: Long`
- `amount: Double`
- `description: String`
- `category: ExpenseCategory`
- `createdAtMillis: Long`

### DAO operacije:
- Observe sve troškove
- Insert / Update / Delete
- Observe trošak po ID-ju

---

## Upravljanje stanjem u Compose-u

Compose koristi **state-driven UI**:
- UI čita stanje
- Promena stanja → recomposition

U projektu:
- `ExpenseViewModel` izlaže:
    - `allExpenses`
    - filtrirano stanje za Home ekran
- Stats ekran koristi sve troškove, nezavisno od filtera

---

## Struktura projekta

```text
app/src/main/java/com/example/expensetracker/
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt
│   │   ├── DbProvider.kt
│   │   ├── ExpenseDao.kt
│   │   ├── ExpenseEntity.kt
│   │   └── ExpenseConverters.kt
│   │
│   ├── repository/
│   │   └── ExpenseRepository.kt
│   │
│   └── ExpenseCategory.kt
│
├── ui/
│   ├── components/
│   │   ├── PrimaryOrangeButton.kt
│   │   ├── ExpenseOutlinedTextField.kt
│   │   ├── ExpenseTextField.kt
│   │   ├── CategoryDropdown.kt
│   │   ├── ExpenseBarChart.kt
│   │   ├── PieChart.kt
│   │   ├── HomeComponents.kt
│   │   └── StatsComponents.kt
│   │
│   ├── screens/
│   │   ├── HomeScreen.kt
│   │   ├── AddExpenseScreen.kt
│   │   ├── EditExpenseScreen.kt
│   │   └── StatsScreen.kt
│   │
│   ├── navigation/
│   │   └── Routes.kt
│   │
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
│
├── util/
│   ├── Currency.kt
│   └── MoneyFormatter.kt
│   └── PieColors.kt
│
├── ExpenseUiState.kt
├── ExpenseViewModel.kt
├── ExpenseVmFactory.kt
├── ExpenseTrackerApp.kt
└── MainActivity.kt
```

---

## Reusable UI komponente

Jetpack Compose omogućava lako izdvajanje i ponovnu upotrebu UI elemenata.
U ovom projektu izdvojene su sledeće komponente:

- **PrimaryOrangeButton**  
  Jedinstven stil dugmeta korišćen kroz celu aplikaciju.

- **ExpenseOutlinedTextField**  
  Centralizovan stil input polja (boje, ivice, fokus).

- **CategoryDropdown**  
  Wrapper oko Material 3 dropdown-a koji uklanja boilerplate kod.

- **Charts (Canvas)**
    - Pie chart – raspodela potrošnje po kategorijama
    - Bar chart – uporedni prikaz potrošnje
---


## Stringovi i lokalizacija

Svi stringovi korišćeni u aplikaciji nalaze se u:
```kotlin
app/src/main/res/values/strings.xml
```
Primer korišćenja u Jetpack Compose-u:

```kotlin
Text(text = stringResource(R.string.stats))
```
---

## Pokretanje projekta

### Zahtevi
- Android Studio
- JDK 17
- Emulator ili fizički Android uređaj (**API 24+**)

### Koraci

1. Klonirati repozitorijum:
   ```bash
   git clone https://github.com/USERNAME/ExpenseTracker.git

2. Otvoriti projekat u Android Studio

3. Sačekati da se Gradle sinhronizuje

4. Pokrenuti aplikaciju na emulatoru ili fizičkom uređaju
---

## Screenshot aplikacije

### Početni ekran (Home)
![Home Screen](screenshots/home.png)

---

### Dodavanje troška
![Add Expense](screenshots/add.png)

---

### Statistika potrošnje
![Statistics](screenshots/stats.png)

---

### Izmena troška
![Edit Expense](screenshots/edit.png)

---

## Zaključak

Jetpack Compose predstavlja značajan iskorak u razvoju Android aplikacija, nudeći moderniji, fleksibilniji i produktivniji način izrade korisničkog interfejsa. Iako XML i dalje ima svoje mesto, posebno u postojećim projektima, Compose se nameće kao budući standard Android UI razvoja.
Izbor tehnologije zavisi od:
- Tipa projekta
- Zahteva tima
- Vremena i kompleksnosti aplikacije
