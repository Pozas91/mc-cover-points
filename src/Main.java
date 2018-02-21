import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class Main {

    private static final Integer NUMBER_OF_MATCHES = 7;
    private static final Integer n = 504;
    private static final Integer R = 1;
    private static final Integer TIMES = 2;
    private static final Integer MAX_MINUTES_EXECUTION_TIME = 30;

    private static final Long INITIAL_TIME = System.nanoTime();
    private static final Long MAX_EXECUTION_TIME = MAX_MINUTES_EXECUTION_TIME * 60 * 1000000000L; // Minutes in nano seconds

    private static final Double TEMPERATURE = 2.0;
    private static final Double COLD = 0.95;

    private static final Random RANDOM = new Random();
    private static final Set<Character> ELEMENTS = new HashSet<>(Arrays.asList('0', '1', '2'));

    private static final Integer[] NUMBER_OF_VARIANTS = {2, 3, 4};
    private static final Integer[] NUMBER_OF_X = {0, 1, 2};
    private static final Integer[] NUMBER_OF_TWOS = {0, 1, 2};

    public static void main(String[] args) {

        // Generamos el espacio total
        Set<String> totalSpace = generateS(ELEMENTS, NUMBER_OF_MATCHES);

        // Aplicamos las condiciones de la quiniela
        Set<String> S = applyVariants(totalSpace, NUMBER_OF_VARIANTS, NUMBER_OF_X, NUMBER_OF_TWOS);

        List<Ball> maxCover = getMaxCoverExhaustiveAlgorithm(S, totalSpace, n);
        System.out.println(maxCover);
    }

    private static List<Ball> getMaxCoverExhaustiveAlgorithm(Set<String> S, Set<String> totalPoints, Integer maximumHeight) {

        List<Ball> maxCover = new ArrayList<>();
        Integer minPoints = Integer.MAX_VALUE;

        for(int i = maximumHeight; i > 0; i--) {

            try {

                System.out.printf("Trying with n = %d ... %n", i);

                List<Ball> currentCover = getMaxCoverSimpleAlgorithm(S, totalPoints, i);

                if(currentCover.size() < minPoints) {

                    System.out.printf("Better cover founded with n = %d! %n", i);

                    maxCover = new ArrayList<>(currentCover);
                    minPoints = maxCover.size();
                }

            } catch (TimeoutException e) {

                /*
                Si se produce una excepción de exceso de tiempo podemos hacer dos cosas:
                 1. Devolver el máximo recubrimiento encontrado hasta la fecha, ya que, entendemos que no vamos a
                 encontrar uno mejor.
                 2. Seguir buscando, ya que puede darse el caso, de que con haya saltado la excepción por otros motivos.

                 Nosotros vamos a optar por devolver el mejor caso encontrado hasta el momento.
                 */

                if(maxCover.isEmpty()) {
                    System.out.printf("Not cover founded! Please try with n > %d. %n", maximumHeight);
                } else {
                    System.out.printf("Timeout! Best cover with n = %d. %n", (i + 1));
                }

                return maxCover;
            }
        }

        return maxCover;
    }

    private static List<Ball> getMaxCoverSimpleAlgorithm(Set<String> S, Set<String> totalPoints, Integer maxCoverPoints) throws TimeoutException {

        // Generamos del total de puntos disponibles un subconjunto C aleatorio.
        List<Ball> C = new ArrayList<>();

        while(C.size() < maxCoverPoints) {

            String center = (String) Utils.getRandomElementFromCollection(totalPoints);
            Ball ball = new Ball(center);
            ball.generatePoints(S, R);
            C.add(ball);
        }

        // Cogemos los puntos del conjunto S que no estén cubiertos por C.
        Set<String> N = uncoveredPoints(C, S);

        return steelCooling(S, C, N, TEMPERATURE, COLD, TIMES);
    }

    private static List<Ball> steelCooling(Set<String> S, List<Ball> C, Set<String> N, Double T, Double cold, Integer times) throws TimeoutException {

        while(N.size() > 0) {

            for(int i = 0; i < C.size(); i++) {

                for(int j = 0; j < times; j++) {

                    // Hacemos una copia por si queremos restaurar la lista original.
                    List<Ball> newC = new ArrayList<>(C);
                    Ball Ci = newC.get(i);

                    // Creamos una nueva bola con centro generado de forma aleatoria.
                    String key = generateRandomBeltWithDistanceFrom(Ci.getCenter(), R);
                    Ball d = new Ball(key);
                    d.generatePoints(S, R);
                    newC.set(i, d);

                    /*
                    Comprobamos cuantas bolas se quedan sin cubrir con el C original y cuantas con la copia de C.
                     */
                    Set<String> uncoveredPointsByC = uncoveredPoints(C, S);
                    Set<String> uncoveredPointsByNewC = uncoveredPoints(newC, S);

                    // Accept the change with certain probability.
                    Double randomNumber = RANDOM.nextDouble();
                    Double probability = Math.exp(-N.size() / T);

                    /*
                    Si el tamaño de los puntos sin recubrir del nuevo C es menor o igual que los puntos sin cubrir
                    del anterior, sustituyo C y sustituyo los puntos.
                     */
                    if(uncoveredPointsByNewC.size() <= uncoveredPointsByC.size()) {

                        C.set(i, d);
                        N = new HashSet<>(uncoveredPointsByNewC);

                    } else if(N.size() > 0 && randomNumber < probability) {

                        C.set(i, d);
                        N = new HashSet<>(uncoveredPointsByNewC);
                    }

                    /*
                    Si todos los puntos han quedado cubiertos, entonces podemos devolver el conjuto de las bolas que
                    recubren el código, ahorrándonos así más iteraciones.
                     */
                    if(N.size() <= 0) {
                        return C;
                    }
                }
            }

            T = T * cold;

            /*
             * Si lleva más de 5 minutos el algoritmo, paramos de ejecutarlo.
             */
            long endTime = System.nanoTime();
            long duration = (endTime - INITIAL_TIME);

            if(duration >= MAX_EXECUTION_TIME) {
                throw new TimeoutException("The execution has taken more than " + MAX_MINUTES_EXECUTION_TIME + " minutes.");
            }
        }

        return C;
    }

    private static Set<String> generateS(Set<Character> elements, int wordSize) {

        Set<String> S = new TreeSet<>();

        generateSRecursive(elements, "", wordSize, S);

        return S;
    }

    private static void generateSRecursive(Set<Character> elements, String prefix, int wordSize, Set<String> S) {

        // Base case: wordSize is 0, save the word
        if (wordSize == 0) {
            S.add(prefix);
            return;
        }

        // One by one add all characters from set and recursively
        // call for k equals to k-1
        for(Character element: elements) {

            String newPrefix = prefix + element;
            generateSRecursive(elements, newPrefix, wordSize - 1, S);
        }
    }

    private static String generateRandomBeltWithDistanceFrom(final String belt, final Integer R) {

        if(belt.length() < R) {
            throw new IllegalArgumentException("The string has lower length that distance required.");
        }

        StringBuilder randomBelt = new StringBuilder(belt);

        while(!Objects.equals(Utils.hammingDistance(belt, randomBelt.toString()), R)) {

            Integer index = RANDOM.nextInt(randomBelt.length());
            Character character = (Character) Utils.getRandomElementFromCollection(ELEMENTS);
            randomBelt.setCharAt(index, character);
        }

        return randomBelt.toString();
    }

    private static Set<String> coveredPoints(Ball ball, Set<String> set) {
        return set.stream().filter(a -> ball.getCenter().equals(a) || ball.getCoveredPoints().contains(a)).collect(Collectors.toCollection(HashSet::new));
    }

    private static Set<String> uncoveredPoints(List<Ball> balls, Set<String> set) {

        Set<String> uncoveredPoints = new HashSet<>(set);
        balls.forEach(ball -> uncoveredPoints.removeAll(coveredPoints(ball, set)));

        return uncoveredPoints;
    }

    private static Set<String> applyVariants(Set<String> total, Integer[] numberOfVariants, Integer[] numberOfXs, Integer[] numberOfTwos) {

        Set<String> S = new HashSet<>();

        for(Integer variant: numberOfVariants) {

            for(Integer numberOfX: numberOfXs) {

                for(Integer numberOfTwo: numberOfTwos) {

                    Integer sum = numberOfX + numberOfTwo;

                    if(sum.equals(variant)) {

                        Set<String> filterSet = total.stream().filter(x -> {

                            Boolean filterX = Utils.numberOfChars(x, '1').equals(numberOfX);
                            Boolean filterTwo = Utils.numberOfChars(x, '2').equals(numberOfTwo);

                            return filterX && filterTwo;
                        }).collect(Collectors.toCollection(HashSet::new));

                        S.addAll(filterSet);
                    }
                }
            }
        }

        return S;
    }
}
