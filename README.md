\# Sistema de Agenda de Citas — Proyecto Maven Multi-Módulo (Variante C)

# Sistema de Gestión de Citas Médicas

Aplicación de escritorio en Java desarrollada con Swing, JDBC y Maven para la administración, agendamiento y seguimiento de citas médicas en tiempo real.

---

## Estructura del Proyecto

proyecto-citas-medicas/
├── src/
│   └── main/
│       └── java/
│           └── edu/
│               └── umg/
│                   └── programacion2/
│                       └── proyecto/
│                           ├── conexion/
│                           │   └── ConexionBD.java
│                           ├── dao/
│                           │   └── CitaDAO.java
│                           ├── modelo/
│                           │   ├── Cita.java
│                           │   └── EstadoCita.java
│                           └── vista/
│                               ├── BienvenidaFrame.java
│                               ├── FormularioCitaFrame.java
│                               └── TablaConsultasFrame.java
├── pom.xml
└── README.md

---

## Configuración de Variables de Entorno

Para proteger las credenciales de la base de datos y evitar dejarlas expuestas en el código fuente, la aplicación lee los accesos mediante Variables de Entorno del sistema.

### Pasos para crear la variable en Windows (Variables de Entorno de Usuario):

1. Presiona la tecla Windows + R, escribe sysdm.cpl y presiona Enter.
2. Dirígete a la pestaña Opciones avanzadas y haz clic en el botón Variables de entorno...
3. En la sección superior (Variables de usuario para TuUsuario), haz clic en Nueva...
4. Ingresa las siguientes variables según tu entorno:

- DB_URL: jdbc:mysql://localhost:3306/citas_db
- DB_USER: root
- DB_PASSWORD: tu_contraseña_aqui

5. Haz clic en Aceptar en todas las ventanas abiertas para guardar los cambios.
6. Importante: Si tienes abierto Eclipse, NetBeans o la Terminal, debes cerrarlos y volverlos a abrir para que reconozcan las nuevas variables de usuario.

---

## Validaciones y Reglas de Negocio

### 1. Agendamiento y Rangos de Horario
- Bloques fijos: Cada cita se agenda en bloques automáticos de 1 hora (ejemplo: 08:00 - 09:00).
- Prevención de Traslapes: El sistema valida que no exista otra cita agendada en la misma fecha, misma hora de inicio/fin y misma sala.
- Sugerencia Automática: Si la sala seleccionada está ocupada en el horario deseado, el sistema busca en la jornada (08:00 a 18:00) el siguiente bloque libre y ofrece al usuario la opción de reagendar automáticamente.

### 2. Modificación de Citas
- Permite reasignar hora y sala a citas registradas.
- Durante la edición, la validación de choque excluye el identificador (ID) de la cita actual para no generar un falso conflicto consigo misma.

### 3. Estados de Citas y Limpieza Automática
- Estados Disponibles: PENDIENTE, EN_CONSULTA, COMPLETADA, CANCELADA, EXPIRADA.
- Transición a Expirada: Cada vez que se consulta la lista de citas, el sistema ejecuta una verificación automática que cambia a estado EXPIRADA todas aquellas citas en estado PENDIENTE cuya fecha u hora de fin sean anteriores al momento actual.

### 4. Visualización y Ordenamiento
- Ordenamiento Estricto: La lista de consultas se organiza por Sala, luego por Fecha y finalmente por Hora, facilitando la lectura simultánea de múltiples salas a la misma hora.
- Resaltado por Color:
  * Azul Claro: Citas en estado EN_CONSULTA.
  * Verde Suave: Citas COMPLETADA.
  * Rojo Suave: Citas EXPIRADA.
  * Blanco / Gris: Citas PENDIENTE y CANCELADA.

---

## Compilación y Ejecución

- Compilar y Empaquetar JAR:
  mvn clean package

- Actualizar Cambios en Módulos (si aplica):
  Si realizas cambios en el módulo de modelo/DAO, ejecuta `mvn install` en el proyecto Core y luego realiza un Update Project (Alt + F5) en el UI.