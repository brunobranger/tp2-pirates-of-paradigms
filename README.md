# Pirates of Paradigms — TP2

**Materia:** Paradigmas de Programación — Brasburg, Raik  
**Cuatrimestre:** 1º 2026  
**Grupo:** 1
**Integrantes:**

- Bruno Branger, 112821
- Emanuel Dario Lescano, 113228
- Ramiro Hernandez, 111975
- Javier Nicolas Romani, 92585

---

## Requisitos previos

- Java 21 (cualquier distribución)
- Maven 3.9+

Verificar instalación:

```bash
java -version
mvn -version
```

---

## Cómo compilar y ejecutar

```bash
# Clonar el repositorio
git clone <url-del-repo>
cd tp2-pirates-of-paradigms

# Compilar y ejecutar en un solo comando
mvn compile exec:java
```


---

## Estructura del proyecto

```
src/
├── main/
│   ├── java/ar/fiuba/paradigmas/tp2/
│   │   ├── Main.java                  # Entrada del programa
│   │   ├── model/                     # Modelo (MVC)
│   │   │   ├── GameState.java
│   │   │   ├── VictoryCondition.java
│   │   │   ├── board/                 # Tablero, celdas, posiciones
│   │   │   ├── ship/                  # Barcos, efectos, factory
│   │   │   ├── player/                # Jugador, facción
│   │   │   ├── resource/              # Oro, madera, costos
│   │   │   ├── treasure/              # Tesoros dinásticos y flotantes
│   │   │   ├── combat/                # Sistema de combate
│   │   │   └── event/                 # Eventos aleatorios
│   │   ├── controller/                # Controlador (MVC)
│   │   ├── view/                      # Vista (MVC) — consola
│   │   ├── config/                    # Carga de archivos
│   │   └── util/                      # Utilidades
│   └── resources/
│       ├── maps/                      # Archivos JSON de mapas
│       └── fleets/                    # Archivos JSON de flotas
└── test/
    └── java/...                       # Tests unitarios
```

---

## Cómo jugar

<!-- Completar cuando la jugabilidad esté implementada -->

Al iniciar, el programa pedirá:

1. Seleccionar un mapa.
2. Seleccionar una configuración de flotas.
3. Colocar el puerto inicial de cada jugador.
4. Colocar los barcos de cada flota.

Luego comienza la partida. En cada turno el jugador puede:

1. Ver el estado de la partida.
2. Utilizar un barco (mover, atacar, reparar, comprar puerto, apagar incendio).
3. Rendirse.

---

## Decisiones de diseño

Ver informe PDF adjunto.

### Patrones utilizados

- **MVC**: separación entre `GameState` (modelo), `GameController` (controlador) y `GameView`/`ConsoleView` (vista).
- **Strategy**: `GameEvent` — cada evento encapsula su propia lógica de aplicación.
- **Strategy**: `ShipEffect` — cada efecto de estado sabe cómo modificar las estadísticas del barco.
- **Factory Method**: `ShipFactory` — centraliza la creación de barcos por tipo.
- **Value Object**: `Position`, `AttackResult`, `EventResult` — inmutables, sin identidad.

### Principios SOLID aplicados

- **SRP**: `CombatSystem`, `BoardRenderer`, `EventManager`, `GameLoader` tienen una única razón de cambio.
- **OCP**: agregar un evento = nueva clase que implementa `GameEvent`. Agregar un tipo de barco = nueva subclase de `Ship`.
- **LSP**: todas las subclases de `Ship` pueden usarse donde se espera `Ship`.
- **DIP**: `GameController` depende de `GameView` (interfaz), no de `ConsoleView`.

---

## Hipótesis tomadas

<!-- Completar durante el desarrollo -->

- ...

---

## Funcionalidades implementadas

<!-- Completar antes de la entrega -->

- [ ] Carga de mapa desde archivo
- [ ] Carga de flotas desde archivo
- [ ] Fase de setup (colocación de puertos y barcos)
- [ ] Loop de turnos por consola
- [ ] Movimiento en 8 direcciones
- [ ] Sistema de combate (ATK - DEF)
- [ ] Reparación (en puerto y con madera)
- [ ] Compra de puertos
- [ ] Condición de victoria: Barco Insignia hundido
- [ ] Condición de victoria: Tesoro capturado y entregado
- [ ] Condición de victoria: Rendición
- [ ] Efectos de terreno (tormenta, niebla, arrecife, corriente, isla)
- [ ] Eventos aleatorios (mínimo 4): Vendaval, Motín, Tesoro Flotante, Kraken, Viento Favorable, Incendio
- [ ] Tesoros dinásticos (transporte, ocultamiento, recuperación)
- [ ] Recursos (oro y madera)

---

## Conclusiones

<!-- Completar antes de la entrega -->
