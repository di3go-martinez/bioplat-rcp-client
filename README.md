
Pasos para integrar la aplicación

- Sistema operativo para realizar la integración: Linux
  
  Scripts probado en Ubuntun 20+

- Se utiliza Java 8 

  ej: sdk install java 8-open

- Una vez hecho el checkout de bioplat-rcp-client (este repo)

- Descargar el módulo de integración

      git submodule init && git submodule update
      
- Descomprimir el archivo clean-rcp-oxygen.tar.gz que está dentro de bioplat-rcp-integration

      tar xf clean-rcp-oxygen.tar.gz

- Ejecutar el script build

  La versión se calcula según git. Si hay tag se utiliza como versión, sino se utiliza la fecha y el id del commit

- Las bundles generadas quedan en el directorio temporal

  La aplicación se genera para linux (gtk), mac y windows
