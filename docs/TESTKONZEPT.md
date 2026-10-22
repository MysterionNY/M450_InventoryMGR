# Testkonzept

## Ziel

Die Tests sichern die Geschäftsregeln des Inventory Managers unabhängig von
der Konsole ab. Fehler sollen bereits beim Push durch die GitLab-Pipeline
erkannt werden. Die Konsole erhält zusätzlich einen Component Test für einen
vollständigen Spielablauf.

## Teststufen

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

## Automatisierung und Reports

`mvn clean verify` führt die Tests aus und erzeugt:

- Surefire XML für GitLab JUnit Reports
- Surefire HTML für GitLab Pages
- JaCoCo HTML für die Detailanalyse
- JaCoCo XML für Coverage-Markierungen in Merge Requests

Der Maven-Build schlägt fehl, wenn weniger als 70 Prozent der ausführbaren
Zeilen abgedeckt sind. Testartefakte bleiben in GitLab 30 Tage verfügbar.

## Definition of Done

Ein Feature gilt als abgeschlossen, wenn:

- die Geschäftsregel ausserhalb der Konsolenklasse implementiert ist,
- Erfolgsfall und wichtige Fehlerfälle automatisiert getestet sind,
- `mvn clean verify` erfolgreich ist,
- die Coverage-Grenze eingehalten wird,
- die Pipeline auf dem Default-Branch die Reports publiziert.
