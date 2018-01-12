# Naviniño
Marcos Muñoz Núñez 
José Miguel Fernández Sáenz de Navarrete
Manuel Adrián Castillo Mejías

Esta aplicación permite consultar para cada boleto del sorteo de la Lotería de Navidad y de la Lotería del Niño.
Para cada boleto muestra el número de boleto jugado, el sorteo al que pertenece (previamente introducido por el usuario),
 si el sorteo ha sido celebrado recientemente, el premio recibido para dicho boleto y una imagen (de forma opcional) realizada por el usuario del boleto.
Además, se permite introducir un nuevo boleto con los campos especificados anteriormente (con excepción del premio recibido).
El usuario podrá además seleccionar que boletos desea eliminar de la lista desplazando cada boleto hacia el margen izquierdo de la pantalla.
Además, si lo desea, puede recuperar dicho boleto deshaciendo el último cambio.
Por último, el usuario puede refrescar la lista de los boletos para consultar el valor del premio durante el sorteo o tras el sorteo o si lo prefiere en ajustes,
 puede seleccionar el valor en minutos del tiempo para cada actualización automática de la lista.
Como trabajo pendiente, hubiera resultado muy interesante añadir una funcionalidad que nos permitiese compartir
 (por ejemplo, a través de una red social) un boleto premiado junto con la cantidad.
Comentar, que existe un defecto en la lógica de la actualización, y es que cada vez que un elemento es añadido a la lista por primera vez,
  el valor del premio no es actualizado hasta que un usuario decida su forzado o se produzca una actualización automática.
Esto se debe a la disposición de las consultas que se realizan a la base de datos para obtener el último valor disponible para el premio.
Leemos los elementos, antes de que el servicio que consulta el valor del premio actualice su valor en la base de datos.
Hubiera resultado muy interesante plantear otro tipo de esquema lógico para resolver este problema.
