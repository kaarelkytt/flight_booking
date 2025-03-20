
# Lennu planeerimise ja istekohtade soovitamise rakendus

## Autor: Kaarel Kütt

### Käivitamine

### Eeldused

* Lennu number on unikaalne, kuna lennud võetakse API-st ainult ühe päeva kohta.
* Kuna API-st saab ainult sama päeva lennud, siis lendude kuupäevi on liigutatud edasi suvalise päevade arvu võrra (1-30 päeva).
* Lendude andmete juures pole tihti lennukitüüpi, sellisel juhul lisatakse see vastavalt lennu pikkusele.
* Lennu hind on genereeritakse vastavalt lennu pikkusele, kuid sisaldab ka juhuslikkust.

* Kõrvutikohtade valik ...

### Ajakulu

| ajakulu | tegevus                                                                                                                                                                                                               |
|---------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 3h      | Projekti struktuuri ja klasside planeerimine: Projekti ülesehituse ja vajalike klasside määratlemine, et tagada loogiline ja efektiivne koodistruktuur.                                                               |
| 4h      | Andmeklasside loomine: Andmeklasside defineerimine ja arendamine, et võimaldada andmete korrektne salvestamine ja töötlemine.                                                                                         |
| 6h      | Lennu isendi loomise protsessi väljatöötamine: Protsessi kavandamine ja rakendamine, mis võimaldab lennu isendite loomist ja haldamist süsteemis.                                                                     |
| 16h     | API kaudu lendude andmete lugemine ja andmeklasside täiendamine: API-st andmete lugemise ja töötlemise funktsionaalsuse arendamine ning andmeklasside täiendamine, et tagada andmete õige salvestamine ja kasutamine. |
| 2h      | Lennuteenuste ja -andmete haldamise funktsioonide arendamine: Lennuteenuste, andmete salvestamise ja päringute tegemise funktsioonide loomine ja täiustamine.                                                         |