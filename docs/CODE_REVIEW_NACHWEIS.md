# Code-Review-Nachweis für das Solo-Projekt

Dieses Dokument enthält drei strukturierte Review-Runden. Es ist kein Ersatz
für die im Auftrag verlangten Team-Pull-Requests, solange die Lehrperson diesen
Ersatz nicht ausdrücklich akzeptiert. Die Reviews verwenden aber denselben
Ablauf: Befund, Challenge, Entscheidung, Änderung und Verifikation.

## Review 1: Robuste Eingabewerte

**Befund:** `DropService` lehnte Werte unter 0 und ab 1 ab. `Double.NaN` erfüllt
jedoch keinen dieser Vergleiche und wurde deshalb fälschlicherweise als Schatz
behandelt. Auch `RewardCalculator` akzeptierte nicht-endliche Faktoren.

**Challenge:** Jede externe oder injizierte Abhängigkeit kann ungültige Werte
liefern. Ein Mock darf keine versteckte Lücke in der Validierung aufdecken.

**Änderung:** Beide Services prüfen jetzt zusätzlich `Double.isFinite(...)`.
Neue Tests decken `NaN` und positive Unendlichkeit ab.

**Verifikation:** Die neuen Tests schlagen ohne die Prüfung fehl und laufen mit
der Korrektur grün.

## Review 2: Verantwortlichkeiten der Konsolenschicht

**Befund:** `GameMenu` berechnete selbst, wie viele Treffer eine Aktivität
benötigt. Das ist eine Geschäftsregel und keine Ein-/Ausgabe.

**Challenge:** Dieselbe Regel müsste bei einer späteren anderen Oberfläche
dupliziert werden und wäre isoliert nur schwer testbar.

**Änderung:** `requiredHits(...)` und `hasEnoughDurability(...)` liegen jetzt in
`ActivityService`. `GameMenu` zeigt nur noch das Resultat an.

**Verifikation:** `ActivityServiceTest` prüft ein zu schwaches und ein genügend
haltbares Werkzeug.

## Review 3: Zustands- und Wirtschaftsregeln

**Befund:** Das Inventar blieb dauerhaft bei zehn Slots. Gleichzeitig erzeugen
Aktivitäten Drops, wodurch der Spieler relativ schnell keine Belohnungen mehr
aufnehmen konnte. Ausserdem konnte `Player.addMoney` bei sehr grossen Beträgen
unbemerkt überlaufen.

**Challenge:** Progression muss einen vollen Zustand auflösen können und
Geldoperationen dürfen keine negativen Werte durch Integer-Overflow erzeugen.

**Änderung:** Inventar-Upgrades auf 15 und 20 Slots wurden per TDD ergänzt.
`Player.addMoney` verwendet nun `Math.addExact` und bricht bei Overflow ab.

**Verifikation:** Tests prüfen beide Upgrade-Stufen, fehlendes Geld, die
Maximalstufe und `Integer.MAX_VALUE + 1`.

## Review-Checkliste

- Liegt Geschäftslogik ausserhalb von `Main` und `GameMenu`?
- Bleibt der Zustand bei fehlgeschlagenen Aktionen unverändert?
- Sind Zufall und andere Abhängigkeiten injizierbar?
- Existieren Tests für Erfolg, Grenze und Fehlerfall?
- Bleibt die Coverage-Grenze erfüllt?
- Sind Namen, Preise und Fehlermeldungen verständlich?
