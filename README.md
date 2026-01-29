# En el paquete  model:  se encontrarán
todas las tablas y relaciones de la base de datos
# Variable nueva: icono en habito y foto de perfil en usuario

Cosas cambiadas: 
En cuestion de relaciones : Habito con usuario = 1:N
Relacions de usuario con Pinguino 1:1
2 nuevas relaciones entre el pinguino y accesorios
Una N:N para poder ver todo el armario ( lo gratis que se puede 
conseguir con puntos) y 1:N para saber que se refiera a esa 
sola prenda 


# LoginScreen.kt 
En este fichero decidi repartir el codigo para que no fuera tan pesado, y algunas funciones 
serán llamadas desde el paquete de components



# Se implementará lógica Firebase :c
Para el login necesitaremos si o si de internet, por lo cual 
utilizaremos firebase, y ya dentro de la app, lo que serian la
parte de hábitos podria funcionar sin internet, algunas funciones con.

# Cambios en el libs.version.toml 
Versiones de firebase y google services 
-  firebaseBom = "33.1.1"
   service = "4.4.2"
- 

# Cambios en la libreria build.gradle.kts (app)
Dependencias de firebase y cambios en plugins