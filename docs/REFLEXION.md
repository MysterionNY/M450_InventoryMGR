# Reflexion zu TDD und Code Reviews

## TDD

Der erste Zyklus für `Player` wurde strikt als RED–GREEN–REFACTOR ausgeführt:
Zuerst verlangte der Test einen Spieler mit 100 Gold und schlug wegen der
fehlenden Klasse fehl. Danach wurde nur der Konstruktor mit `getMoney()`
implementiert. Anschliessend kamen Geldänderungen und Validierungen hinzu.

Ein weiterer Zyklus wurde für das Inventar-Upgrade vollständig protokolliert.
Der Test wurde zuerst ohne vorhandenen `InventoryUpgradeService` ausgeführt und
scheiterte wie erwartet beim Kompilieren. Nach der minimalen Implementierung
liefen die drei neuen Tests grün. Im Refactoring wurde das Feature ins Menü
integriert und die Haltbarkeitsberechnung aus der UI in einen Service verlegt.
Der genaue Ablauf steht in `TDD_NACHWEIS.md`.

Besonders nützlich war das testorientierte Vorgehen bei Grenzfällen. Beispiele
sind ein voller Inventarslot, ein Werkzeug mit zu tiefem Level und die exakten
Drop-Grenzen bei 0.90 und 0.98. Diese Regeln wären bei einer rein manuellen
Prüfung leicht übersehen worden.

Nicht jede spätere Klasse entstand in vollständig getrennten RED- und
GREEN-Commits. Ein Teil wurde als kleiner, gemeinsam geprüfter Feature-Block
entwickelt. Für eine sauberere TDD-Historie sollten zukünftige Features in
einem Git-Repository mit getrennten Test-, Implementierungs- und
Refactoring-Commits erstellt werden.

## Mocking

Das Mocking des `RandomGenerator` ist sinnvoll, weil echter Zufall zu
instabilen Tests führen würde. Der Test kann mit `0.99` garantiert einen Schatz
auslösen und zusätzlich prüfen, dass der Generator genau verwendet wurde. Bei
einfachen Klassen ohne externe Abhängigkeit wurde bewusst kein Mock eingesetzt.

## Code Reviews

Ein Review sollte besonders auf folgende Punkte achten:

- Befindet sich Geschäftslogik ausserhalb von `GameMenu` und `Main`?
- Bleiben Geld- und Inventaränderungen bei fehlgeschlagenen Aktionen atomar?
- Sind neue Zufallsquellen injizierbar?
- Decken Tests Erfolg, Grenzwerte und Fehlerfälle ab?
- Bleibt die Coverage über dem festgelegten Grenzwert?
- Sind Namen und Fehlermeldungen verständlich?

Automatisierte Tests ersetzen kein Review. Sie zeigen, ob dokumentiertes
Verhalten eingehalten wird, beurteilen aber beispielsweise keine unnötige
Komplexität oder schlechte Verantwortungsverteilung. Merge Requests mit
Pipeline und Coverage-Diff schaffen dafür eine nachvollziehbare Grundlage.
