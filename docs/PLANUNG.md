# Kurze Projektplanung

Die Planung ist bewusst als Meilensteinliste und nicht als Gantt-Diagramm
aufgebaut. Jeder Meilenstein liefert einen vorführbaren Stand.

| Meilenstein | Inhalt | Nachweis | Status |
|---|---|---|---|
| 1. Testbasis | Maven, JUnit 5, Mockito und JaCoCo | erster grüner Maven-Test | Erledigt |
| 2. Kernökonomie | Player, Item, Inventory und Shop | Unit Tests für Kauf und Verkauf | Erledigt |
| 3. Spielmechanik | Tools, Aktivitäten, Schaden und Belohnungen | Unit und Component Tests | Erledigt |
| 4. Progression | XP, Level, Drops und Multiplikatoren | Mockito- und Grenzwerttests | Erledigt |
| 5. Spielbarkeit | Konsolenmenü, Reparaturen und Inventar-Upgrades | automatisierte Menüabläufe | Erledigt |
| 6. Automatisierung | Build-, Test- und Deploy-Pipeline | `.gitlab-ci.yml` und Reports | Lokal erledigt |
| 7. Dokumentation | Testkonzept, Architektur und Reflexion | Dateien im Ordner `docs` | Erledigt |
| 8. Präsentation | Demo, Testing, Reports und Fazit | 10-Minuten-Präsentation | Durch Projektinhaber |

## Arbeitsweise

Neue Geschäftsregeln werden zuerst als kleine Beispiele und Grenzfälle
formuliert. Geeignete Features beginnen mit einem fehlschlagenden Test. Danach
folgt die minimale Implementierung und zuletzt ein Refactoring. Vor einem
Meilensteinabschluss muss `mvn clean verify` erfolgreich sein.

## Risiken und Massnahmen

| Risiko | Massnahme |
|---|---|
| Zufällige Tests schlagen unregelmässig fehl | `RandomGenerator` injizieren und mit Mockito kontrollieren |
| UI und Geschäftslogik vermischen sich | Regeln in Services verschieben |
| Änderungen beschädigen bestehende Features | vollständige Regression mit Maven |
| Coverage sinkt unbemerkt | JaCoCo-Grenze von 70 Prozent im Build |
| Solo-Projekt ermöglicht kein Team-Review | Review-Protokolle führen und Ersatz mit Lehrperson klären |
| Pipeline wird nicht remote ausgeführt | lokale Befehle und Artefakte nachweisen; Ausnahme bestätigen lassen |
