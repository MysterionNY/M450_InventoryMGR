# TDD-Nachweis: Inventar erweitern

Dieser Zyklus wurde am 20. Oktober 2026 in der Reihenfolge RED, GREEN und
REFACTOR ausgeführt. Das Feature erweitert das Inventar von 10 auf 15 und
anschliessend auf maximal 20 Slots.

## RED

Zuerst entstand nur `InventoryUpgradeServiceTest` mit drei Erwartungen:

1. Das erste Upgrade kostet 200 Gold und ergibt 15 Slots.
2. Bei 199 Gold bleiben Geld und Kapazität unverändert.
3. Nach dem zweiten Upgrade ist bei 20 Slots Schluss.

Ausgeführter Befehl:

```shell
mvn -Dtest=InventoryUpgradeServiceTest test
```

Erwartetes und beobachtetes Resultat:

```text
COMPILATION ERROR
Symbol nicht gefunden: Klasse InventoryUpgradeService
Tests konnten nicht grün sein, weil die Produktionsklasse noch nicht existierte.
```

## GREEN

Danach wurde die kleinste benötigte Lösung implementiert:

- `Inventory.expandCapacity(int)`
- `InventoryUpgradeService` mit den Stufen 10, 15 und 20
- Preise von 200 und 500 Gold
- keine Geldänderung bei fehlgeschlagenem Kauf

Der identische Testbefehl ergab danach:

```text
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## REFACTOR

Im Refactoring wurde das Feature in den Shop integriert. Die Anzeige liest
Preis und nächste Kapazität aus dem Service, statt diese Werte nochmals im Menü
zu definieren. Zusätzlich wurde die Trefferberechnung für ausreichende
Werkzeughaltbarkeit aus `GameMenu` in `ActivityService` verschoben.

Der abschliessende Regressionstest wird mit folgendem Befehl ausgeführt:

```shell
mvn clean verify
```

Beobachtetes Endresultat nach dem Refactoring:

```text
Tests run: 64, Failures: 0, Errors: 0, Skipped: 0
All coverage checks have been met.
BUILD SUCCESS
```

Damit prüft der Zyklus nicht nur den Erfolgsfall, sondern auch fehlendes Geld,
die Maximalstufe, unveränderten Zustand bei Fehlern und die Einbindung ins
Konsolenmenü.
