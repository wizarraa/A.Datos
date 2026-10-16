# A.Datos

Trabajos de la asignatura **A.Datos**.

## Contenido

| Práctica | Carpeta | Descripción |
|---|---|---|
| Práctica Tinder | `PracticaTinder/Src/EjercicioTinder` | Gestión de usuarios y sus aficiones, y búsqueda de parejas compatibles |

---

## Práctica Tinder

Programa de consola en Java que lee un fichero de usuarios con sus aficiones, permite añadir usuarios nuevos y genera un fichero con las parejas que comparten un número mínimo de aficiones.

### Archivos

- `Main.java`: programa principal, con el menú, la lectura y validación del fichero y la generación de concordancias.
- `Usuario.java`: representa un usuario (código y lista de aficiones).
- `Concordancia.java`: representa una pareja de usuarios y sus aficiones comunes.

### Formato del fichero de usuarios

Cada línea contiene un usuario: su código seguido de sus aficiones, separados por espacios.

```
U1 CINE DEPORTE LECTURA
U2 CINE MUSICA
U3 DEPORTE LECTURA VIAJES
```

Reglas que comprueba el programa:

- El código tiene el formato `U` seguido de números (`U1`, `U25`…) y no puede repetirse.
- Cada usuario tiene al menos una afición.
- Las aficiones van en mayúsculas, sin tildes ni espacios (solo letras `A-Z`).
- Las aficiones de cada línea están en orden alfabético.
- El fichero no puede superar los 10 000 bytes.
- Las líneas vacías se ignoran.

### Menú

1. **Añadir usuario**: pide el código y las aficiones, las ordena alfabéticamente y añade la línea al final del fichero.
2. **Mostrar usuarios**: muestra cada usuario con el formato `U1{CINE|DEPORTE|LECTURA}`.
3. **Generar fichero de concordancias**: pide el número mínimo de aficiones comunes y crea `concordancias.txt` con las parejas que lo cumplen, ordenadas de más a menos aficiones en común.
4. **Salir**.

Ejemplo de `concordancias.txt` (mínimo 2) con los usuarios de arriba:

```
U1 U3 DEPORTE LECTURA
```

### Cómo compilar y ejecutar

Requiere Java 8 o superior. Desde la carpeta `PracticaTinder`:

```bash
javac -d out Src/EjercicioTinder/*.java
java -cp out EjercicioTinder.Main
```

Al arrancar, el programa pide el nombre del fichero de usuarios, que debe estar en `Src/EjercicioTinder/` (por ejemplo, `usuarios.txt`). El fichero `concordancias.txt` se crea en la carpeta desde la que se ejecuta el programa.
