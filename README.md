# MC - Cover Points

La clase Main contiene dos métodos importantes, el primero
se llama __getMaxCoverSimpleAlgorithm__, este método buscará
un recubrimiento para el conjunto S dado, con un tamaño de puntos
que recubrirán a S. Devolverá esos puntos en caso de que los encuentre.

Este método lanzará una excepción cuando lleve más de 2 minutos de búsqueda
 ya que en caso de no encontrar recubrimiento el algoritmo se quedaría bloqueado.
 
El otro método es __getMaxCoverExhaustiveAlgorithm__, su función es
igual al anterior sólo que como tercer argumento se le pasará un número inicial
de puntos máximos a recubrir, e irá iterando hasta encontrar el mínimo posible
dado todos los puntos en S. Si en este método se lanza la excepción de tiempo
entonces devolverá el último conjunto mínimo encontrado hasta el momento.

Todos los parámetros se encuentran en la cabecera de la clase __Main__,
 y en el interior del método __main__, por tanto, es válido tanto
 para el sistema de 5 triples, como
 para el sistema de 6 triples.
 
 Así, para el sistema de __*5 triples*__ con 2 variantes, 1-2 equis y 1-2 doses
 ha encontrado un recubrimiento mínimo con __*n = 15*__.
 
 Y para el sistema de __*6 triples*__ con 2, 3 o 4 variantes,
 con 0, 1 o 2 equis y con 0, 1 o 2 doses, encuentra un recubrimiento mínimo
 con __*n = 42*__.
