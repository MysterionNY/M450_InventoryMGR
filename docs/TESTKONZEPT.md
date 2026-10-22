# Testkonzept

## 1. Zusammenfassung (Introduction)

Die Tests sichern die Geschäftsregeln des Inventory Managers unabhängig von
der Konsole ab. Fehler sollen bereits beim Push durch die GitLab-Pipeline
erkannt werden. Die Konsole erhält zusätzlich einen Component Test für einen
vollständigen Spielablauf.

## 2. Big Picture – Architektur und Test Items

```text
Benutzer
   |
   v
GameMenu (Konsole)  ------ Component Test: vollständiger Spielablauf
   |
   v
Services             ------ Unit Tests: Shop, Aktivitäten, Reparatur,
   |                         Upgrades, Level, Rewards und Drops
   v
Domänenobjekte        ------ Unit Tests: Player, Inventory und Tool
```

Die Geschäftslogik liegt in Services und Domänenobjekten und ist von der
Ein- und Ausgabe getrennt. Als Test Items gelten `Player`, `Inventory`,
`Tool`, alle Serviceklassen und das Zusammenspiel dieser Klassen über
`GameMenu`. Der Zufallsgenerator ist eine austauschbare Abhängigkeit und kann
deshalb im Test kontrolliert werden.

## 3. Test Features

### Unit Tests

Die Klassen werden isoliert getestet:

- `PlayerTest`: Geld, Startwerte und Validierungen
- `InventoryTest`: Hinzufügen, Entfernen und Kapazitätsgrenze
- `ShopServiceTest`: Kaufen, Verkaufen und Fehlerfälle
- `RepairServiceTest`: vollständige und anteilige Reparaturkosten
- `InventoryUpgradeServiceTest`: Preise, Kapazitätsstufen und Maximalgrösse
- `ToolTest`: Schaden, Level, Haltbarkeit und Werkzeugbruch
- `RewardCalculatorTest`: Multiplikator, Bonus und Rundung
- `LevelServiceTest`: XP-Grenzen und mehrere Level-Ups
- `UpgradeServiceTest`: Gold-Multiplikatoren, Preise und Maximalstufe
- `ActivityServiceTest`: Schaden, Freischaltungen, Belohnungen und Werkzeugtyp
- `DropServiceTest`: normale, seltene und Schatz-Drops

### Component Test

`GameMenuTest` verbindet Player, Shop, Aktivitäten, Belohnungen und Drops. Der
Test simuliert Konsoleneingaben für den Ablauf:

1. Holzaxt kaufen
2. Baum fällen
3. Gold und XP erhalten
4. kontrollierten Schatz-Drop ins Inventar aufnehmen
5. Spiel beenden

### Mocking

Zufall wird über `RandomGenerator` injiziert. `DropServiceTest` verwendet ein
Mockito-Mock, das beispielsweise `0.99` zurückgibt. Dadurch kann der
Schatz-Pfad zuverlässig getestet werden, ohne einen zufälligen Test zu bauen.

Zusätzlich werden Validierungen, Fehlerfälle, Äquivalenzklassen und Grenzwerte
geprüft. Dazu gehören beispielsweise ein volles Inventar, zu wenig Gold,
Levelgrenzen, ein kaputtes Werkzeug und ungültige Zufallswerte.

## 4. Features not to be tested

Nicht automatisiert getestet werden die optische Darstellung und genaue
Formatierung der Konsolenausgaben, das manuelle Spielen über ein echtes
Terminal sowie Maven, GitLab und GitLab Pages selbst. Die Pipeline führt nur
die Projekttests aus und publiziert deren Resultate. Ein kurzer manueller
Smoke-Test prüft vor der Abgabe, ob Menüführung, Eingaben und Ausgaben für den
Benutzer verständlich funktionieren.

## Relevante Äquivalenzklassen und Grenzwerte

| Bereich | Erfolgsfall | Grenz-/Fehlerfall |
|---|---|---|
| Geld | ausreichender Betrag | negativer Betrag, zu wenig Geld |
| Inventar | freier Slot | letzter Slot, volles Inventar |
| Inventar-Upgrade | genügend Gold | zu wenig Gold, maximale 20 Slots |
| Aktivität | passendes Werkzeug | falscher Typ, zu tiefes Level, kaputtes Werkzeug |
| XP | unter Levelgrenze | genau 100, 250 und 500 XP |
| Drop | 0.00–0.89 normal | 0.90 selten, 0.98 Schatz, 1.00 ungültig |
| Reward | positiver Grundwert | null, negative Werte, Rundung |

## 5. Testvorgehen mit TDD

Neue Geschäftsregeln werden nach dem TDD-Zyklus entwickelt:

1. **Red:** Zuerst beschreibt ein Test das erwartete Verhalten und schlägt
   fehl, weil die Funktion noch nicht vorhanden ist.
2. **Green:** Danach wird nur so viel Produktivcode geschrieben, bis der Test
   erfolgreich ist.
3. **Refactor:** Bei weiterhin grünen Tests werden Namen, Struktur und
   Duplikate verbessert.

Dieser Zyklus wurde beim Inventar-Upgrade konkret durchgeführt und im
`TDD_NACHWEIS.md` festgehalten. Nach jeder Änderung laufen zusätzlich alle
vorhandenen Tests als Regressionstest.

## 6. Erfolgs- und Abbruchkriterien

Ein Test ist erfolgreich, wenn das tatsächliche Resultat dem erwarteten Wert
entspricht und keine unerwartete Exception auftritt. Ein Test ist nicht
erfolgreich, wenn eine Assertion fehlschlägt, eine unerwartete Exception
auftritt oder der Build die Coverage-Grenze unterschreitet. Ein Feature gilt
als abgeschlossen, wenn:

- Erfolgsfall und wichtige Fehlerfälle automatisiert getestet sind,
- `mvn clean verify` erfolgreich ist,
- mindestens 70 Prozent Line Coverage erreicht werden,
- die Pipeline die Test- und Coverage-Reports erzeugt.

## 7. Testumgebung und Automatisierung

| Bereich | Werkzeug |
|---|---|
| Laufzeit und Build | Java 21 und Maven |
| Unit-/Component-Tests | JUnit 5 und Maven Surefire |
| Mocking | Mockito |
| Coverage | JaCoCo mit 70-Prozent-Grenze |
| Automatisierung | GitLab CI/CD mit Build-, Test- und Deploy-Stage |
| Reports | Surefire HTML/XML und JaCoCo HTML/XML |

`mvn clean verify` führt die Tests aus und erzeugt:

- Surefire XML für GitLab JUnit Reports
- Surefire HTML für GitLab Pages
- JaCoCo HTML für die Detailanalyse
- JaCoCo XML für Coverage-Markierungen in Merge Requests

Der Maven-Build schlägt fehl, wenn weniger als 70 Prozent der ausführbaren
Zeilen abgedeckt sind. Testartefakte bleiben in GitLab 30 Tage verfügbar.

## 8. Kurze Planung

1. Geschäftsregel und erwartete Grenzfälle festlegen.
2. Fehlgeschlagenen Test schreiben (**Red**).
3. Funktion minimal implementieren (**Green**).
4. Code und Tests verbessern (**Refactor**).
5. Gesamte Testsuite und Coverage mit `mvn clean verify` prüfen.
6. Reports kontrollieren und einen manuellen Smoke-Test durchführen.
