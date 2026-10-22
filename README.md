# M450 Inventory Manager

Konsolenbasiertes Java-Spiel für das Modul M450 mit Schwerpunkt auf Unit Tests,
Component Tests, TDD, Mockito, JaCoCo und GitLab CI/CD.

Zum Spiel gehören ein Shop, Werkzeuge mit reparierbarer Haltbarkeit, mehrere
Aktivitäten und Werkzeugstufen, XP und Level, zufällige Drops sowie kaufbare
Gold-Multiplikatoren und Inventar-Upgrades.

## Voraussetzungen

- Java 21
- Maven 3.9 oder neuer

## Lokal ausführen

```shell
mvn clean verify
java -cp target/classes ch.tbz.inventorymgr.Main
```

`mvn clean verify` führt alle Tests aus, erzeugt die Reports und kontrolliert,
dass die Line Coverage mindestens 70 Prozent beträgt.

## Lokale Reports

- JUnit XML: `target/surefire-reports/`
- JUnit HTML: `target/reports/surefire.html`
- JaCoCo HTML: `target/site/jacoco/index.html`
- JaCoCo XML: `target/site/jacoco/jacoco.xml`

Der HTML-Testreport wird mit folgendem Befehl ergänzt:

```shell
mvn clean verify surefire-report:report-only
```

## GitLab-Pipeline

Die Datei `.gitlab-ci.yml` definiert drei Stages:

| Stage | Aufgabe | Ergebnis |
|---|---|---|
| Build | Maven kompiliert und paketiert das Projekt | JAR-Artefakt |
| Test | JUnit, Mockito und JaCoCo laufen automatisiert | JUnit- und Coverage-Reports |
| Deploy | Die HTML-Reports werden veröffentlicht | GitLab-Pages-Webseite |

JUnit-Ergebnisse erscheinen im Tests-Tab der GitLab-Pipeline. JaCoCo wird als
HTML-Artefakt, als GitLab-Coverage-Report und als Prozentwert in der Pipeline
bereitgestellt. GitLab Pages wird nur vom Default-Branch veröffentlicht.

Nach dem ersten erfolgreichen Lauf steht die URL unter **Deploy > Pages** im
GitLab-Projekt. Diese URL und der Repository-Link können anschliessend im
Miro-Board eingetragen werden.

## Dokumentation

- [Testkonzept](docs/TESTKONZEPT.md)
- [Architektur](docs/ARCHITEKTUR.md)
- [Reflexion](docs/REFLEXION.md)
- [Kurze Planung](docs/PLANUNG.md)
- [TDD-Nachweis](docs/TDD_NACHWEIS.md)
- [Solo-Code-Review-Nachweis](docs/CODE_REVIEW_NACHWEIS.md)
