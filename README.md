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

## Informe

[Informe TP2 - Pirates of Paradigms](https://docs.google.com/document/d/1vYzimgFAUGqLEJULcRMF2w2TxwtyOVF6g0cL-HISw_I/edit?tab=t.0)

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

Ver [informe completo](https://docs.google.com/document/d/1vYzimgFAUGqLEJULcRMF2w2TxwtyOVF6g0cL-HISw_I/edit?tab=t.0).

### Patrones utilizados

- **MVC**: separación entre `GameState` (modelo), `GameController` (controlador) y `GameView`/`ConsoleView` (vista).
- **Strategy**: `GameEvent` — cada evento encapsula su propia lógica de aplicación.
- **Strategy**: `GameState` — cada tipo de victoria, sabe como interactuar de cumplirse las condiciones y efectos puntuales.
- **Strategy**: `ShipEffect` — cada efecto de estado sabe cómo modificar las estadísticas del barco.
- **Singleton:** ...
- **Factory Method**: `ShipFactory` — centraliza la creación de barcos por tipo.
- **Value Object**: `Position`, `AttackResult`, `EventResult` — inmutables, sin identidad.

### Principios SOLID aplicados

- **SRP**: `CombatSystem`, `BoardRenderer`, `EventManager`, `GameLoader` tienen una única razón de cambio.
- **OCP**: agregar un evento = nueva clase que implementa `GameEvent`. Agregar un tipo de barco = nueva subclase de `Ship`.
- **LSP**: todas las subclases de `Ship` pueden usarse donde se espera `Ship`.
- **DIP**: `GameController` depende de `GameView` (interfaz), no de `ConsoleView`.

### Diseño de View y Test

- **MVC y DIP**: `GameController` usa la interfaz `GameView`; `ConsoleView` solo presenta información y captura elecciones.
- **Command**: la vista muestra las acciones, pero no decide si pueden ejecutarse. Las reglas se validan en `MoveCommand`, `AttackCommand`, `RepairCommand`, `BuyPortCommand` y `ExtinguishFireCommand`.
- **SRP**: `BoardRenderer` concentra la representación ASCII del tablero. `ConsoleView` no recorre archivos del sistema.
- **Inyección de dependencias**: las listas de mapas y flotas disponibles se entregan a `ConsoleView` desde el punto de composición. La lectura de archivos corresponde a `GameLoader`.
- **Tests automatizados**: las entradas y salidas de consola se inyectan como streams para probar la vista sin depender de una terminal interactiva.

---

## Hipótesis tomadas

<!-- Completar durante el desarrollo -->

- Con respecto al caso de cuando un barco logra la condicion de victoria de capturar el tesoro enemigo y lo llevo correctamente hasta su base. No estaba bien definido y queda medio ambiguo cuando era soltar la carga o no por hundimiento del barco con el tesoro o por entregar al puerto el tesoro para ganar por lo que se hizo algo parecido a una sobre carga de metodos, pero en este caso con distinciones sutiles en el nombre para identificar sus acciones, solo cambian un aspecto booleano para indicar el aspecto de que si la carga se suelta o no, para el caso practico del barco queda en false, para hundimiento si cambia.

- Con respecto a la generación del tablero se opta por usar uno genérico y otro personalizado:
  - Board() {} - establece una generación predeterminada.
  - Board(int rows, int cols) {} - establece una generación personalizada.

- 

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
