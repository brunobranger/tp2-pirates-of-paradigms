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
git clone git@github.com:bruno-branger/tp2-pirates-of-paradigms.git
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

Al iniciar, el programa pedirá:

1. Seleccionar un mapa de los disponibles: `predeterminada.json`, `archipielago.json`,
   `pasillos_actuales.json`, `paso_de_arrecife.json`.
2. Seleccionar una configuración de flotas: `balanced.json`.
3. Cada jugador coloca su puerto inicial en una casilla navegable libre.
4. Cada jugador coloca uno a uno el resto de sus barcos en casillas navegables libres.

Luego comienza la partida. En cada turno el jugador puede:

1. Ver el estado de la partida (tablero, HP de cada barco, recursos, tesoros).
2. Utilizar un barco:
  - **Mover**: ingresá el destino como `fila,columna`. El sistema muestra las
    posiciones alcanzables antes de pedir el destino.
  - **Atacar**: el sistema muestra los barcos enemigos en rango. Elegí uno por número.
  - **Reparar**: gratis en puerto propio, cuesta madera fuera de él.
  - **Comprar puerto**: disponible si el barco está sobre un puerto neutral y hay oro suficiente.
  - **Apagar incendio**: aparece solo si el barco está incendiado.
3. Finalizar turno.
4. Rendirse (pide confirmación).

En cualquier submenú podés ingresar `0` o `c` para cancelar y volver al menú anterior.

---

## Decisiones de diseño

Ver [informe completo](https://docs.google.com/document/d/1vYzimgFAUGqLEJULcRMF2w2TxwtyOVF6g0cL-HISw_I/edit?tab=t.0).

### Patrones utilizados

- **MVC**: separación entre `GameState` (modelo), `GameController` (controlador) y `GameView`/`ConsoleView` (vista).
- **Strategy**: `GameEvent` — cada evento encapsula su propia lógica de aplicación.
- **Strategy**: `GameState` — cada tipo de victoria, sabe como interactuar de cumplirse las condiciones y efectos puntuales.
- **Strategy**: `ShipEffect` — cada efecto de estado sabe cómo modificar las estadísticas del barco.
- **Singleton:** `EventManager` — garantiza una única instancia del gestor de eventos durante toda la ejecución, manteniendo el contador de turnos consistente.
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

---

## Hipótesis tomadas

- Con respecto al caso de cuando un barco logra la condicion de victoria de capturar el tesoro enemigo y lo llevo correctamente hasta su base. No estaba bien definido y queda medio ambiguo cuando era soltar la carga o no por hundimiento del barco con el tesoro o por entregar al puerto el tesoro para ganar por lo que se hizo algo parecido a una sobre carga de metodos, pero en este caso con distinciones sutiles en el nombre para identificar sus acciones, solo cambian un aspecto booleano para indicar el aspecto de que si la carga se suelta o no, para el caso practico del barco queda en false, para hundimiento si cambia.

- Con respecto a la generación del tablero se opta por usar uno genérico y otro personalizado:
  - Board() {} - establece una generación predeterminada.
  - Board(int rows, int cols) {} - establece una generación personalizada.

- Con respecto a la frecuencia de eventos aleatorios, el enunciado no especifica cada cuántos turnos debe ocurrir un evento. Se tomó la hipótesis de que un evento  ocurre cada 3 turnos completados, contando el turno de cada jugador por separado. Este valor está centralizado en `EventManager.EVENT_FREQUENCY_TURNS`.

---

## Funcionalidades implementadas

- [x] Carga de mapa desde archivo
- [x] Carga de flotas desde archivo
- [x] Fase de setup (colocación de puertos y barcos)
- [x] Loop de turnos por consola
- [x] Movimiento en 8 direcciones
- [x] Sistema de combate (ATK - DEF)
- [x] Reparación (en puerto y con madera)
- [x] Compra de puertos
- [x] Condición de victoria: Barco Insignia hundido
- [x] Condición de victoria: Tesoro capturado y entregado
- [x] Condición de victoria: Rendición
- [x] Efectos de terreno (tormenta, niebla, arrecife, corriente, isla)
- [x] Eventos aleatorios (mínimo 4): Vendaval, Motín, Tesoro Flotante, Viento Favorable, Incendio
- [x] Tesoros dinásticos (transporte, ocultamiento, recuperación)
- [x] Recursos (oro y madera)

---

## Conclusiones

El desarrollo de Pirates of Paradigms permitió integrar en la práctica los principios de Programación Orientada a Objetos
y los patrones estudiados durante la materia. La separación en capas —configuración, modelo, controlador y vista— demostró
su valor concreto: modificar la carga de archivos no requirió tocar la lógica de combate, y cambiar el formato de visualización
del tablero no afectó al modelo.

Desde la perspectiva de OOP, el diseño se apoya fuertemente en encapsulamiento, ya que cada entidad del dominio (tablero, barcos,
jugadores y eventos) mantiene su estado interno protegido, exponiendo únicamente comportamientos controlados a través de sus métodos.
Esto permitió evitar dependencias directas sobre estructuras internas y redujo el acoplamiento entre componentes.

Asimismo, el uso de herencia y jerarquías de clases permitió modelar de manera natural elementos con comportamientos relacionados pero
variantes específicas. Por ejemplo, los distintos tipos de efectos sobre los barcos comparten una interfaz común, pero cada implementación
define su propio comportamiento. Esto, combinado con el uso de polimorfismo, permite que el sistema trate a todos los efectos de forma uniforme
sin conocer sus detalles internos. El polimorfismo también se ve reflejado en la ejecución de acciones y eventos, donde el sistema delega el comportamiento concreto en las
implementaciones específicas. Esto reduce la necesidad de condicionales y mejora la extensibilidad del diseño.

El patrón Command fue particularmente valioso para modelar las acciones del jugador. Al encapsular cada acción en una clase con canExecute y
execute, el controlador quedó limpio de lógica condicional dispersa, y agregar una nueva acción se reduce a crear una nueva clase sin modificar
el flujo existente.

El uso del patrón Strategy en tres contextos distintos —efectos de barcos, condiciones de victoria y eventos aleatorios— mostró su versatilidad:
en los tres casos permite extender el comportamiento agregando nuevas implementaciones sin modificar el código que las consume.

Como aprendizaje general, el trabajo reforzó que un buen diseño no solo facilita agregar nuevas funcionalidades, sino que también hace más evidente dónde ubicar cada pieza de lógica nueva. Cuando el diseño es claro, la pregunta “¿dónde va esto?” tiene una respuesta más natural y consistente.
