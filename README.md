\# Sistema de Agenda de Citas — Proyecto Maven Multi-Módulo (Variante C)

# 🏥 Sistema de Gestión y Agenda de Citas Médicas (Java Swing + MySQL)

Aplicación de escritorio desarrollada en **Java Swing** con arquitectura multimódulo en capas (**Core** / **UI**) e integración a **MySQL** vía JDBC. Diseñada para la programación de citas en clínica, resolución automática de conflictos de horario por salas y actualización de estados en tiempo real.

---

## 🚀 Características Principales

* **Control de Horarios y Bloques:** Programación en bloques fijos de 1 hora (08:00 a 17:00).
* **Detección de Conflictos & Sugerencias:** Previene solapamientos en la misma sala e idéntico rango de tiempo, ofreciendo el siguiente bloque libre disponible de forma automática.
* **Actualización Automática de Estados:**
  * Citas en estado `PENDIENTE` cuya `hora_fin` transcurre pasan automáticamente a `EXPIRADA`.
  * Citas en estado `EN_CONSULTA` cuya `hora_fin` concluye pasan automáticamente a `COMPLETADA`.
* **Auto-Refresco:** Temporizador en la interfaz (`javax.swing.Timer`) que actualiza la tabla de consultas cada 30 segundos sin intervención del usuario.
* **Diseño e Interfaz Pastel:** Estilizado mediante `EstiloUtil` con tarjetas elevadas, botones redondeados y resaltado dinámico de filas por estado de la cita.

---

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 11 (JavaSE-11)
* **Interfaz Gráfica:** Java Swing / AWT
* **Base de Datos:** MySQL / MariaDB
* **Conectividad:** JDBC (MySQL Connector/J)
* **Arquitectura:** Proyecto Multimódulo Maven (Core y UI separados)

---

## 📂 Estructura del Proyecto

```
agenda-citas/
├── agenda-citas-core/
│   └── src/main/java/
│       └── edu.umg.programacion2.proyecto/
│           ├── dao/
│           │   └── CitaDAO.java
│           ├── modelo/
│           │   ├── Cita.java
│           │   └── EstadoCita.java
│           └── util/
│               └── ConexionBD.java
│
├── agenda-citas-ui/
│   └── src/main/java/
│       └── edu.umg.programacion2.proyecto.ui/
│           ├── MainUI.java
│           ├── util/
│           │   └── EstiloUtil.java
│           └── vista/
│               ├── BienvenidaFrame.java
│               ├── FormularioCitaFrame.java
│               └── TablaConsultasFrame.java
│
└── pom.xml
```

---

## 🔑 Configuración de la Variable de Entorno (`DB_PASSWORD`)

Para mantener la seguridad de las credenciales, la conexión JDBC lee la contraseña desde la variable de entorno `DB_PASSWORD`.

### Opcion 1: Desde Eclipse IDE (Recomendado para desarrollo)
1. Haz clic derecho sobre la clase principal `MainUI.java` (o el proyecto `agenda-citas-ui`).
2. Selecciona **Run As** > **Run Configurations...**
3. Ve a la pestaña **Environment**.
4. Haz clic en **New...**
5. En **Name** escribe: `DB_PASSWORD`
6. En **Value** escribe la contraseña de tu base de datos MySQL (ej. `admin123`).
7. Haz clic en **Apply** y luego en **Run**.

### Opción 2: En Windows (A nivel de Sistema Operativo)
1. Presiona `Win + R`, escribe `sysdm.cpl` y presiona **Enter**.
2. Ve a la pestaña **Opciones avanzadas** y haz clic en **Variables de entorno...**
3. En la sección **Variables de usuario**, haz clic en **Nueva...**
4. Nombre de la variable: `DB_PASSWORD`
5. Valor de la variable: `tu_contraseña_mysql`
6. Haz clic en **Aceptar** y reinicia tu IDE para que reconozca el cambio.

*O por consola (PowerShell / CMD):*
```cmd
setx DB_PASSWORD "tu_contraseña_mysql"
```

### Opción 3: En Linux / macOS
Añade la siguiente línea en tu archivo `~/.bashrc`, `~/.zshrc` o ejecuta en la terminal:
```bash
export DB_PASSWORD="tu_contraseña_mysql"
```
---

## ⚙️ Ejecución del Proyecto

1. Asegúrate de tener ejecutando tu servicio de MySQL y haber creado la base de datos con la tabla `citas`.
2. Configura la variable `DB_PASSWORD` según los pasos descritos.
3. Ejecuta el proyecto desde el módulo de interfaz de usuario (`agenda-citas-ui`) corriendo la clase principal `MainUI.java`.