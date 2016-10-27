# Code review

### App
App crasht als ik op create account druk (omdat velden leeg zijn, is makkelijk op te lossen met een if/else blokje).

Weinig user feedback. Vaak niet te zien wanneer je iets gedaan hebt.

Weinig informatie voor de user over hoe de applicatie werkt.

Kan alleen bij je MenuActivity uitloggen.

### Java code

Menu activity alle onClicks kunnen in 1 functie met een switch en meerdere cases of if/else.

Een aparte class for asynctask is netter dan een class in een class (kan in mijn code zien hoe dit gaat).

Kijken naar de gele errors die android studio geeft (bv ongebruikte variabelen of unchecked assignments).

In main activity doet updateUI niks.

Misschien meer comments bij functies maar ook in oncreate om code op te delen en meer overzicht te geven.

Verder is code goed opgedeeld in aparte functies, code is zeer duidelijk.

### Layout XML

Hardcoded strings in menu activity.

Onclicks vervangen met setButtonListener in java code.

Beetje mierenneukerig maar zorg voor consistentie in waar je je tags afsluit en de hoeveelheid witregels tussen tags.
