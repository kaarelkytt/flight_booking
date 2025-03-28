
# Lennu planeerimise ja istekohtade soovitamise rakendus

### Autor: Kaarel Kütt

Lennu planeerimise ja istekohtade soovitamise rakendus, mille eesmärgiks on kuvada lende ning nendele istekohti kasutaja 
valitud väljumise ja sihtkoha järgi. Rakendus kasutab API-t lendude andmete hankimiseks ja React/TypeScripti frontendi.

## Seadistamine ja käivitamine

1. **Node.js ja npm**:
   Veendu, et on installitud **Node.js** (v15 või kõrgem) ja **npm** (v7 või kõrgem). Kui need pole veel installitud, 
   siis saad neid [siit](https://nodejs.org/en/) alla laadida ja paigaldada.

2. **Sõltuvuste installimine**:
   Laadi alla kõik projektis vajalikud sõltuvused:
   ```bash
   cd frontend
   npm install
   ```

3. **API võtme lisamine**:
   Hangi endale API võti lehelt: https://aviationstack.com/.
   Lisa oma API võti faili `application.properties`:
   ```properties
   api.aviationstack.key=your_api_key
   ```
   Lisaks API võtme lisamisele tuleb ka uuendamine sisse lülitada failist `application.properties`:
   ```properties
   flightdata.update.enabled=true
   ```
   Kui uuendamine on välja lülitatud, siis kasutatakse lennud andmeid, mis on varasemalt salvestatud andmebaasi 
   `./db/flightsdb`. Sellisel juhul pole vaja ka API võtit.
4. **Rakenduse käivitamine**:
   
  * Backend:

    Ava ja käivita FlightBookingApplication.java

  * Frontend:
    ```bash
    cd frontend
    npm run dev
    ```
    
  Rakendus käivitub vaikimisi aadressil `http://localhost:5173`.

## Kirjeldus

### Lendude töötlemine

Lendude andmete hankimiseks kasutatakse API-t, milleks on [Aviationstack](https://aviationstack.com/). 
Kuna tasuta API võimaldab hankida ainult hetkel toimuvate lennuliinide andmeid, siis tuleb neid andmeid natukene 
töödelda, et saada andmestik, mis sobib rakenduse testimiseks. API võimaldab hankida ka tuleviku lennuplaane, kuid see 
on tasuline teenus.

* API päringud tehakse lendude uuendamiseks iga tunni tagant. Uuendamise sagedus on määratud failis
  `application.properties`:
   ```properties
   flightdata.update.rate=3600000
   ```
* Päringud lendude saamiseks tehakse kõikidele lennujaamadele, mis on määratud failis
  `application.properties`:
   ```properties
   api.aviationstack.departure.iata=TLL,RIX,HEL,AMS,CDG,FRA
   ```
* Lennu number on unikaalne, kuna lennud võetakse API-st ainult ühe päeva kohta. Sama lennu numbriga lendu uuesti ei lisata.
* Kuna API-st saab ainult sama päeva lennud, siis lendude kuupäevi on liigutatud edasi suvalise päevade arvu võrra (1-45). 
  Ehk lennud jaotatakse ühtlaselt selle ajaperioodi peale.
  Päevade arv on määratud failis `application.properties`:
   ```properties
   flight.schedule.days=45
   ```
* Lennuki tüübi järgi leitakse lennule vastav istmeteplaan. Istemeteplaanid on käsitsi loodud vastavalt levinud
  lennukitüüpidele ja need asuvad kaustas `seatplans`. 
* Lennuki tüüpide ja istmeteplaanide vaheline seos on failis `aircraft-data.json`.
* Lendude andmete juures pole tihti lennukitüüpi, sellisel juhul lisatakse see vastavalt lennu pikkusele. Sobiva
  lennukitüübi leidmiseks on kasutatud andmeid failist `aircraft-data.json`.
* Kuna lendude juures on ainult IATA lennujaamade koodid, siis linnade nimede leidmiseks on eraldi tabel, mis on 
  salvestatud faili `iata-cities.json`.
* API-st ei saa ka piletihinda, seega genereeritakse alghind vastavalt lennu pikkusele, kuid sisaldab ka juhuslikkust. 
  Andmed, mille alusel hind genereeritakse on failis `application.properties`:
   ```properties
   flight.price.base=40.0
   flight.price.minute=0.8
   flight.price.variation=0.2
   ```
* Kindal koha hind arvutakse vastvalt selle istme omadustele ja asukohale lennukis. Hindade arvutamiseks on kasutatud 
  järgmisi andmeid failist `application.properties`:
   ```properties
   flight.price.extraLegroomMultiplier=1.2
   flight.price.nearExitMuliplier=1.15
   ```
* Lisaks on võimalik lennule lisada reisijaid, et saada reaalsem pilt lennukist. Minimaalne ja maksimaalne täituvuse 
  protsent on määratud failis `application.properties`:
   ```properties
   flight.occupancy.min=0.2
   flight.occupancy.max=0.9
   ```
  Iga lennu puhul broneeritakse istmeid nii, et täituvus jääks vahemikku `min` ja `max`, arvestades, kui palju aega on 
  veel lennuni jäänud. Mida rohkem aega on lennuni, seda vähem istmeid on broneeritud.

### Lendude otsimine

  Veebirakendus avaneb aadressil `http://localhost:5173/`. Alguses kuvatakse kõik lennud, mis on salvestatud andmebaasi.
  
* Lendude otsimine toimub väljumise ja sihtkoha ning kuupäeva vahemiku järgi. Lisaks on võimalik vastuseid filtreerida
  lennu kestuse ja hinna järgi.
* Kuvatud lende saab sorteerida väljumisaja ja hinna järgi.
* Lennud kuvatakse tabelina, kus on näha lennu andmed: lähte- ja sihtpunkt, lennu number, väljumise ja saabumise aeg, 
  kestus ning hind. Saabumise kuupäev on kuvatud ainult siis, kui see erineb väljumise kuupäevast.
* Võimalik on ka lehekülje suurust muuta, et kuvada rohkem või vähem lendusid korraga.
* Lentude tabelis on võimalik valida lend, et näha selle istmeplaani ja valida istekohti.

### Istekohtade soovitamine

Lennu valimisel kuvatakse lennu istmeplaan, kus on näha kõik istmed. Istmed on värvitud vastavalt nende omadustele.
* Valge iste on vaba tavakoht.
* Kollane iste on vaba koht, mis on suurema jalaruumiga.
* Helepunane iste on võetud tavakoht.
* Tumepunane iste on võetud koht, mis on suurema jalaruumiga.
* Roheline iste on valitud/soovitatud koht.

Istekohtade soovitamisel arvestatakse järgmiste aspektidega:
* Kasutaja saab valida, kas soovib istekohta akna või vahekäigu kõrval.
* Kasutaja saab valida, kas soovib istekohta väljapääsu lähedal.
* Kasutaja saab valida, kas soovib istekohta suurema jalaruumiga.
* Kasutaja saab valida, kas soovib istekohti, mis asuvad koos.

Soovitusi saab küsida korduvalt ning istekohti saab ka ise valida, klõpsates neid. Kui kasutaja on valinud istekohad, 
siis need kuvatakse eraldi tabelis koos kokku pandud hinnaga.


### Ajakulu

| ajakulu | tegevus                                                                                                                                                                                                               |
|---------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 3h      | Projekti struktuuri ja klasside planeerimine: Projekti ülesehituse ja vajalike klasside määratlemine, et tagada loogiline ja efektiivne koodistruktuur.                                                               |
| 4h      | Andmeklasside loomine: Andmeklasside defineerimine ja arendamine, et võimaldada andmete korrektne salvestamine ja töötlemine.                                                                                         |
| 6h      | Lennu isendi loomise protsessi väljatöötamine: Protsessi kavandamine ja rakendamine, mis võimaldab lennu isendite loomist ja haldamist süsteemis.                                                                     |
| 16h     | API kaudu lendude andmete lugemine ja andmeklasside täiendamine: API-st andmete lugemise ja töötlemise funktsionaalsuse arendamine ning andmeklasside täiendamine, et tagada andmete õige salvestamine ja kasutamine. |
| 6h      | Lennuteenuste ja -andmete haldamise funktsioonide arendamine: Lennuteenuste, andmete salvestamise ja päringute tegemise funktsioonide loomine ja täiustamine.                                                         |
| 16h     | Reacti ja TypeScriptiga frontendi arendamine, mis võimaldab kasutajatel vaadata lende ja nende isendiplaane.                                                                                                          |



