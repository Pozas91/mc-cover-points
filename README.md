# Problema recubrimiento Quiniela

## Problema propuesto
1. Fijar un espacio de apuestas condicionadas, de entre los 7 propuestos.
2. Hallar un recubrimiento de dicho espacio según la distancia indicada, mediante una heurística a libre elección (se recomienda enfriamiento del acero).
3. Comparar el resultado con el inducido por el recubrimiento óptimo conocido del espacio total, con esa distancia.

## Como atacar el problema
Para realizar el problema hemos empezado con un ejemplo sencillo de 4 triples, subiendo posteriormente a 5 triples, y finalmente generalizándolo por medios de parámetros en la cabecera del programa, para los diferentes triples.

Hemos utilizado las siguientes variables por defecto:
* TEMPERATURE = 2.0 -> Temperatura usada por la heurística.
* COLD = 0.95 -> Enfriamiento usado por la heurísitica.
* TIMES = 2 -> Veces que se realiza el bucle modificando la bola con centro Ci.
* MAX_EXECUTION_TIME -> tiempo máximo de ejecución para cuando no se encuentre un recubrimiento con n bolas para el conjunto S generado, una vez alcanzado el tiempo límite de ejecución saldremos del algoritmo.

## Estructura de las variables
Para representar las apuestas hemos usado cadenas de texto con los posibles valores {0, 1, 2}, formando cadenas como "10121" que representa a una apuesta de 5 partidos, siendo 0 equivalente a la opción 1, 1 a la opción X y 2 a la opción 2.
Las principales variables han sido:
* __totalSpace__: Es un conjunto de cadenas de texto que representa todas las posibles apuestas.
* __S__: Conjunto de cadenas de texto que representa las apuestas filtradas en caso de que se indiquen variantes, equivaldrá a la variable __totalSpace__, en caso contrario.
* __Ball__: Clase que representa una bola con centro equivalente a una cadena de texto, y tiene asociado un conjunto de puntos que recubre con el radio indicado en los parámetros de la cabecera.
* __C__: Lista de __Ball__ con el que intentaremos recubrir el espacio __S__.
* __N__: Representa a un conjunto de cadenas de texto que representa las apuestas que quedan sin cubrir por __C__ del conjunto __S__. En álgebra sería equivalente a __N = S - C__.

## Funciones relevantes
La clase Main contiene dos métodos importantes, el primero se llama __getMaxCoverSimpleAlgorithm__, este método buscará un recubrimiento para el conjunto S dado, con un tamaño de puntos que recubrirán a S. Devolverá esos puntos en caso de que los encuentre.

Este método lanzará una excepción cuando lleve más de 2 minutos de búsqueda ya que en caso de no encontrar recubrimiento el algoritmo se quedaría bloqueado.
 
El otro método es __getMaxCoverExhaustiveAlgorithm__, su función es igual al anterior sólo que como tercer argumento se le pasará un número inicial de puntos máximos a recubrir, e irá iterando hasta encontrar el mínimo posible dado todos los puntos en S. Si en este método se lanza la excepción de tiempo entonces devolverá el último conjunto mínimo encontrado hasta el momento.

Todos los parámetros se encuentran en la cabecera de la clase __Main__, y en el interior del método __main__, por tanto, es válido tanto para el sistema de 5 triples, como para el sistema de 6 triples.

## Resultados obtenidos
Así, para el sistema de __*5 triples*__ con 2 variantes, 1-2 equis y 1-2 doses ha encontrado un recubrimiento mínimo con __*n = 15*__.
 
Y para el sistema de __*6 triples*__ con 2, 3 o 4 variantes, con 0, 1 o 2 equis y con 0, 1 o 2 doses, encuentra un recubrimiento mínimo con __*n = 42*__.

## Problemas y conclusiones finales
Se ha solucionado el problema del bucle infinito, cuando el algoritmo no encuentra un recubrimiento del conjunto __S__ con la lista __C__, se ha añadido un temporizador del programa, que lanza una excepción controlada cuando supera el límite de tiempo.

Se genera todo el conjunto __S__ de una vez, y se mantiene como referencia para calcular el conjunto __N__, cuando se modifica la lista __C__.

A pesar de un correcto funcionamiento del algoritmo, se detectan situaciones debido a la aleatoriedad donde por ejemplo para __*6 triples*__ con 2, 3 o 4 variantes, se encuentran soluciones inmediatas para __*n = 45*__, pero tardan más para __*n = 50*__. Así que es posible, que si se deja más tiempo o se ejecuta más veces el algoritmo se consiga mejorar los resultados dados, o no se vuelvan a conseguir para esos datos.
