package com.ejemplo.quiz_matematicas;

import java.util.*;

public class GeneradorPreguntas {

    private static final Random random = new Random();

    public static Pregunta generar(String modo, String nivel) {
        // 40% de probabilidad de usar pregunta del JSON
        if (random.nextInt(10) < 4) {
            Pregunta pJSON = LectorJSON.obtener(modo, nivel);
            if (pJSON != null) return pJSON;
        }
        // Si no hay JSON disponible o por probabilidad, usa el generador Java
        return switch (modo) {
            case "matematicas" -> generarMatematica(nivel);
            case "acertijos"   -> generarAcertijo(nivel);
            case "secuencias"  -> generarSecuencia(nivel);
            default            -> generarMixto(nivel);
        };
    }

    private static Pregunta generarMixto(String nivel) {
        return switch (random.nextInt(3)) {
            case 0 -> generarMatematica(nivel);
            case 1 -> generarAcertijo(nivel);
            default -> generarSecuencia(nivel);
        };
    }

    private static Pregunta generarMatematica(String nivel) {
        int tipos = nivel.equals("facil") ? 4 : nivel.equals("intermedio") ? 7 : 12;
        return switch (random.nextInt(tipos)) {
            case 0 -> generarSuma(nivel);
            case 1 -> generarResta(nivel);
            case 2 -> generarMultiplicacion(nivel);
            case 3 -> generarDivision(nivel);
            case 4 -> generarPotencia(nivel);
            case 5 -> generarRaizCuadrada(nivel);
            case 6 -> generarPorcentaje(nivel);
            case 7 -> generarEcuacion(nivel);
            case 8 -> generarPerimetro(nivel);
            case 9 -> generarArea(nivel);
            case 10 -> generarDerivada();
            default -> generarIntegral();
        };
    }

    private static Pregunta generarSuma(String nivel) {
        int max = nivel.equals("facil") ? 20 : nivel.equals("intermedio") ? 200 : 9999;
        int a = random.nextInt(max) + 1;
        int b = random.nextInt(max) + 1;
        return crearPregunta("¿Cuánto es " + a + " + " + b + "?", a + b);
    }

    private static Pregunta generarResta(String nivel) {
        int max = nivel.equals("facil") ? 20 : nivel.equals("intermedio") ? 300 : 5000;
        int b = random.nextInt(max / 2) + 1;
        int a = b + random.nextInt(max / 2) + 1;
        return crearPregunta("¿Cuánto es " + a + " − " + b + "?", a - b);
    }

    private static Pregunta generarMultiplicacion(String nivel) {
        int max = nivel.equals("facil") ? 5 : nivel.equals("intermedio") ? 15 : 30;
        int a = random.nextInt(max) + 2;
        int b = random.nextInt(max) + 2;
        return crearPregunta("¿Cuánto es " + a + " × " + b + "?", a * b);
    }

    private static Pregunta generarDivision(String nivel) {
        int maxB   = nivel.equals("facil") ? 5 : nivel.equals("intermedio") ? 12 : 25;
        int maxRes = nivel.equals("facil") ? 10 : nivel.equals("intermedio") ? 20 : 50;
        int b   = random.nextInt(maxB) + 2;
        int res = random.nextInt(maxRes) + 2;
        return crearPregunta("¿Cuánto es " + (b * res) + " ÷ " + b + "?", res);
    }

    private static Pregunta generarPotencia(String nivel) {
        int maxBase = nivel.equals("facil") ? 5 : nivel.equals("intermedio") ? 10 : 15;
        int maxExp  = nivel.equals("leyenda") ? 4 : 2;
        int base = random.nextInt(maxBase) + 2;
        int exp  = random.nextInt(maxExp) + 2;
        int res  = (int) Math.pow(base, exp);
        String signo = exp == 2 ? "²" : exp == 3 ? "³" : "^" + exp;
        return crearPregunta("¿Cuánto es " + base + signo + "?", res);
    }

    private static Pregunta generarRaizCuadrada(String nivel) {
        int maxRaiz = nivel.equals("facil") ? 10 : nivel.equals("intermedio") ? 20 : 40;
        int raiz = random.nextInt(maxRaiz) + 2;
        return crearPregunta("¿Cuál es la raíz cuadrada de " + (raiz * raiz) + "?", raiz);
    }

    private static Pregunta generarPorcentaje(String nivel) {
        int[] porcs = nivel.equals("facil")
                ? new int[]{10, 25, 50}
                : nivel.equals("intermedio")
                ? new int[]{10, 15, 20, 25, 30, 50, 75}
                : new int[]{5, 12, 15, 18, 22, 35, 40, 60, 65, 80};
        int maxBase = nivel.equals("facil") ? 10 : nivel.equals("intermedio") ? 20 : 50;
        int p   = porcs[random.nextInt(porcs.length)];
        int b   = (random.nextInt(maxBase) + 1) * 10;
        double res = Math.round((p * b / 100.0) * 100.0) / 100.0;
        return crearPreguntaDecimal("¿Cuánto es el " + p + "% de " + b + "?", res);
    }

    private static Pregunta generarEcuacion(String nivel) {
        int maxX = nivel.equals("facil") ? 10 : nivel.equals("intermedio") ? 20 : 50;
        int maxA = nivel.equals("facil") ? 3  : nivel.equals("intermedio") ? 8  : 15;
        int maxB = nivel.equals("facil") ? 10 : nivel.equals("intermedio") ? 30 : 100;
        int x   = random.nextInt(maxX) + 2;
        int a   = random.nextInt(maxA) + 2;
        int b   = random.nextInt(maxB) + 1;
        int res = a * x + b;
        return crearPregunta("Si " + a + "x + " + b + " = " + res + ", ¿cuánto es x?", x);
    }

    private static Pregunta generarPerimetro(String nivel) {
        String[] figuras = nivel.equals("facil")
                ? new String[]{"cuadrado", "triángulo equilátero"}
                : new String[]{"cuadrado", "triángulo equilátero", "pentágono regular", "hexágono regular"};
        int[] lados = {4, 3, 5, 6};
        int maxLado = nivel.equals("facil") ? 10 : nivel.equals("intermedio") ? 25 : 50;
        int idx  = random.nextInt(figuras.length);
        int lado = random.nextInt(maxLado) + 2;
        return crearPregunta(
                "Un " + figuras[idx] + " tiene lados de " + lado + " cm.\n¿Cuánto mide su perímetro?",
                lado * lados[idx]);
    }

    private static Pregunta generarArea(String nivel) {
        int maxLado = nivel.equals("facil") ? 8 : nivel.equals("intermedio") ? 20 : 50;
        int tipo = random.nextInt(nivel.equals("facil") ? 2 : 3);
        if (tipo == 0) {
            int lado = random.nextInt(maxLado) + 2;
            return crearPregunta("¿Cuál es el área de un cuadrado\ncon lado de " + lado + " cm?", lado * lado);
        } else if (tipo == 1) {
            int base = random.nextInt(maxLado) + 2;
            int alto = random.nextInt(maxLado) + 2;
            return crearPregunta("¿Cuál es el área de un rectángulo\nde base " + base + " y alto " + alto + "?", base * alto);
        } else {
            int base = (random.nextInt(maxLado / 2) + 1) * 2;
            int alto = random.nextInt(maxLado) + 2;
            return crearPregunta("¿Cuál es el área de un triángulo\nde base " + base + " y altura " + alto + "?", (base * alto) / 2);
        }
    }
    private static Pregunta generarDerivada() {
        int[][] casos = {
                // {exponente, coeficiente} → derivada de coef*x^exp = coef*exp * x^(exp-1)
                {2, 1},  // x²  → 2x
                {2, 3},  // 3x² → 6x
                {2, 5},  // 5x² → 10x
                {3, 1},  // x³  → 3x²
                {3, 2},  // 2x³ → 6x²
                {4, 1},  // x⁴  → 4x³
                {2, 4},  // 4x² → 8x
                {3, 3},  // 3x³ → 9x²
        };

        int[] caso = casos[random.nextInt(casos.length)];
        int exp  = caso[0];
        int coef = caso[1];
        int nuevoCoef = coef * exp;
        int nuevoExp  = exp - 1;

        String funcion = coef == 1
                ? "x" + (exp == 2 ? "²" : exp == 3 ? "³" : "⁴")
                : coef + "x" + (exp == 2 ? "²" : exp == 3 ? "³" : "⁴");

        String respuesta;
        if (nuevoExp == 1) {
            respuesta = nuevoCoef + "x";
        } else {
            respuesta = nuevoCoef + "x" + (nuevoExp == 2 ? "²" : "³");
        }

        String[] distractores = generarDistractoresDerivada(nuevoCoef, nuevoExp);
        List<String> opciones = new ArrayList<>(Arrays.asList(distractores));
        opciones.add(respuesta);
        Collections.shuffle(opciones);

        return new Pregunta(
                "¿Cuál es la derivada de f(x) = " + funcion + "?",
                opciones,
                respuesta
        );
    }

    private static String[] generarDistractoresDerivada(int coef, int exp) {
        Set<String> set = new LinkedHashSet<>();
        // Distractor 1: coeficiente incorrecto
        set.add((coef + 1) + (exp == 1 ? "x" : "x" + (exp == 2 ? "²" : "³")));
        // Distractor 2: exponente incorrecto
        set.add(coef + (exp == 1 ? "x²" : "x" + (exp == 2 ? "³" : "⁴")));
        // Distractor 3: coeficiente menos 1
        int c2 = Math.max(1, coef - 1);
        set.add(c2 + (exp == 1 ? "x" : "x" + (exp == 2 ? "²" : "³")));
        return set.toArray(new String[0]);
    }

    private static Pregunta generarIntegral() {
        // Integrales básicas: ∫x^n dx = x^(n+1)/(n+1) + C
        int[][] casos = {
                {1, 1},  // ∫x dx     = x²/2
                {2, 1},  // ∫x² dx    = x³/3
                {3, 1},  // ∫x³ dx    = x⁴/4
                {1, 2},  // ∫2x dx    = x²
                {1, 3},  // ∫3x dx    = 3x²/2 → simplificado
                {2, 2},  // ∫2x² dx   = 2x³/3
                {2, 3},  // ∫3x² dx   = x³
                {3, 2},  // ∫2x³ dx   = x⁴/2
        };

        String[][] respuestas = {
                {"x²/2 + C",  "x³/3 + C",  "x²   + C",  "2x   + C"},  // ∫x
                {"x³/3 + C",  "x²/2 + C",  "x⁴/4 + C",  "3x²  + C"},  // ∫x²
                {"x⁴/4 + C",  "x³/3 + C",  "x⁵/5 + C",  "4x³  + C"},  // ∫x³
                {"x²   + C",  "x²/2 + C",  "2x²  + C",  "x³   + C"},  // ∫2x
                {"3x²/2 + C", "3x²  + C",  "x²   + C",  "3x   + C"},  // ∫3x
                {"2x³/3 + C", "x³/3 + C",  "2x²  + C",  "x³   + C"},  // ∫2x²
                {"x³    + C", "3x³/3 + C", "x²   + C",  "3x²  + C"},  // ∫3x²
                {"x⁴/2  + C", "x⁴/4 + C",  "2x⁴  + C",  "x³   + C"},  // ∫2x³
        };

        String[] funciones = {
                "x", "x²", "x³", "2x", "3x", "2x²", "3x²", "2x³"
        };

        int idx = random.nextInt(casos.length);
        String funcion   = funciones[idx];
        String[] opts    = respuestas[idx];
        String respuesta = opts[0]; // siempre la primera es la correcta

        List<String> opciones = new ArrayList<>(Arrays.asList(opts));
        Collections.shuffle(opciones);

        return new Pregunta(
                "¿Cuál es la integral de f(x) = " + funcion + "?",
                opciones,
                respuesta
        );
    }

    private static Pregunta generarAcertijo(String nivel) {
        int tipos = nivel.equals("facil") ? 10 : 10;
        return switch (random.nextInt(tipos)) {
            case 0 -> acertijoCajas(nivel);
            case 1 -> acertijoVelocidad(nivel);
            case 2 -> acertijoHoras(nivel);
            case 3 -> acertijoPajaros(nivel);
            case 4 -> acertijoMonedas(nivel);
            case 5 -> acertijoEdad(nivel);
            case 6 -> acertijoGrupos(nivel);
            case 7 -> acertijoVuelto(nivel);
            case 8 -> acertijoCarreras(nivel);
            default -> acertijoRepartos(nivel);
        };
    }

    private static Pregunta acertijoCajas(String nivel) {
        String[] objetos = {"manzanas", "naranjas", "pelotas", "libros", "caramelos"};
        int maxCajas = nivel.equals("facil") ? 5 : nivel.equals("intermedio") ? 12 : 25;
        int maxItems = nivel.equals("facil") ? 5 : nivel.equals("intermedio") ? 15 : 30;
        String obj = objetos[random.nextInt(objetos.length)];
        int cajas  = random.nextInt(maxCajas) + 2;
        int items  = random.nextInt(maxItems) + 2;
        return crearPregunta(
                "Tengo " + cajas + " cajas con " + items + " " + obj + " cada una.\n¿Cuántos " + obj + " tengo en total?",
                cajas * items);
    }

    private static Pregunta acertijoVelocidad(String nivel) {
        String[] vehiculos = {"tren", "auto", "moto", "autobús"};
        int maxVel = nivel.equals("facil") ? 5 : nivel.equals("intermedio") ? 12 : 25;
        int maxHrs = nivel.equals("facil") ? 3 : nivel.equals("intermedio") ? 6  : 12;
        String v   = vehiculos[random.nextInt(vehiculos.length)];
        int vel    = (random.nextInt(maxVel) + 1) * 10;
        int horas  = random.nextInt(maxHrs) + 1;
        return crearPregunta(
                "Un " + v + " viaja a " + vel + " km/h durante " + horas + " hora(s).\n¿Cuántos km recorre?",
                vel * horas);
    }

    private static Pregunta acertijoHoras(String nivel) {
        int maxDias    = nivel.equals("facil") ? 5 : nivel.equals("intermedio") ? 14 : 30;
        int maxSemanas = nivel.equals("facil") ? 2 : nivel.equals("intermedio") ? 6  : 12;
        if (random.nextBoolean()) {
            int dias = random.nextInt(maxDias) + 2;
            return crearPregunta("¿Cuántas horas hay en " + dias + " días?", dias * 24);
        } else {
            int semanas = random.nextInt(maxSemanas) + 1;
            return crearPregunta("¿Cuántos días hay en " + semanas + " semana(s)?", semanas * 7);
        }
    }

    private static Pregunta acertijoPajaros(String nivel) {
        String[] animales = {"pájaros", "palomas", "gorriones"};
        int maxIni  = nivel.equals("facil") ? 10 : nivel.equals("intermedio") ? 25 : 50;
        int maxLleg = nivel.equals("facil") ? 5  : nivel.equals("intermedio") ? 15 : 30;
        String animal = animales[random.nextInt(animales.length)];
        int inicial   = random.nextInt(maxIni)  + 5;
        int llegan    = random.nextInt(maxLleg) + 1;
        int sevan     = random.nextInt(inicial - 1) + 1;
        return crearPregunta(
                "Hay " + inicial + " " + animal + " en un árbol.\n" +
                        "Llegan " + llegan + " más y se van " + sevan + ".\n¿Cuántos quedan?",
                inicial + llegan - sevan);
    }

    private static Pregunta acertijoMonedas(String nivel) {
        int maxMon = nivel.equals("facil") ? 10 : nivel.equals("intermedio") ? 25 : 50;
        int[] vals = nivel.equals("facil") ? new int[]{5, 10} : nivel.equals("intermedio") ? new int[]{5, 10, 25, 50} : new int[]{25, 50, 100, 200};
        int monedas = random.nextInt(maxMon) + 2;
        int val     = vals[random.nextInt(vals.length)];
        return crearPregunta(
                "Tienes " + monedas + " monedas de $" + val + " cada una.\n¿Cuánto dinero tienes en total?",
                monedas * val);
    }

    private static Pregunta acertijoEdad(String nivel) {
        int maxEdad  = nivel.equals("facil") ? 5 : nivel.equals("intermedio") ? 15 : 30;
        int multiple = nivel.equals("facil") ? 2 : random.nextInt(3) + 2;
        int edadB    = random.nextInt(maxEdad) + 3;
        String[] rel = {"el doble", "el triple", "el cuádruple"};
        return crearPregunta(
                "Ana tiene " + rel[multiple - 2] + " de la edad de Luis.\n" +
                        "Si Ana tiene " + (edadB * multiple) + " años, ¿cuántos años tiene Luis?",
                edadB);
    }

    private static Pregunta acertijoGrupos(String nivel) {
        String[] cosas = {"estudiantes", "jugadores", "personas", "niños"};
        int maxGrupos  = nivel.equals("facil") ? 5 : nivel.equals("intermedio") ? 12 : 30;
        int maxPorGrup = nivel.equals("facil") ? 5 : nivel.equals("intermedio") ? 15 : 25;
        String cosa   = cosas[random.nextInt(cosas.length)];
        int grupos    = random.nextInt(maxGrupos)  + 2;
        int porGrupo  = random.nextInt(maxPorGrup) + 2;
        return crearPregunta(
                "Hay " + grupos + " grupos de " + porGrupo + " " + cosa + ".\n¿Cuántos " + cosa + " hay en total?",
                grupos * porGrupo);
    }

    private static Pregunta acertijoVuelto(String nivel) {
        int maxPrecio = nivel.equals("facil") ? 5 : nivel.equals("intermedio") ? 20 : 50;
        int precio = (random.nextInt(maxPrecio) + 1) * 5;
        int pago   = precio + (random.nextInt(nivel.equals("leyenda") ? 10 : 5) + 1) * 5;
        return crearPregunta(
                "Un producto cuesta $" + precio + ".\nPagas con $" + pago + ".\n¿Cuánto es el vuelto?",
                pago - precio);
    }

    private static Pregunta acertijoCarreras(String nivel) {
        int maxCorr    = nivel.equals("facil") ? 8 : nivel.equals("intermedio") ? 15 : 30;
        int corredores = random.nextInt(maxCorr) + 3;
        int posicion   = random.nextInt(corredores - 1) + 2;
        return crearPregunta(
                "En una carrera con " + corredores + " participantes,\n" +
                        "vas en la posición " + posicion + ".\n¿Cuántos corredores van delante de ti?",
                posicion - 1);
    }

    private static Pregunta acertijoRepartos(String nivel) {
        String[] cosas = {"dulces", "monedas", "libros", "pelotas"};
        int maxPers  = nivel.equals("facil") ? 5 : nivel.equals("intermedio") ? 12 : 25;
        int maxPorP  = nivel.equals("facil") ? 5 : nivel.equals("intermedio") ? 15 : 30;
        String cosa  = cosas[random.nextInt(cosas.length)];
        int personas = random.nextInt(maxPers) + 2;
        int porPers  = random.nextInt(maxPorP) + 2;
        return crearPregunta(
                "Repartes " + (personas * porPers) + " " + cosa + " entre " + personas + " personas.\n" +
                        "¿Cuántos " + cosa + " recibe cada uno?",
                porPers);
    }

    private static Pregunta generarSecuencia(String nivel) {
        int tipos = nivel.equals("facil") ? 2 : nivel.equals("intermedio") ? 4 : 5;
        return switch (random.nextInt(tipos)) {
            case 0 -> secuenciaAritmetica(nivel);
            case 1 -> secuenciaDecreciente(nivel);
            case 2 -> secuenciaGeometrica(nivel);
            case 3 -> secuenciaCuadrados(nivel);
            default -> secuenciaFibonacci(nivel);
        };
    }

    private static Pregunta secuenciaAritmetica(String nivel) {
        int maxIni  = nivel.equals("facil") ? 20  : nivel.equals("intermedio") ? 100 : 500;
        int maxPaso = nivel.equals("facil") ? 5   : nivel.equals("intermedio") ? 15  : 50;
        int inicio  = random.nextInt(maxIni)  + 1;
        int paso    = random.nextInt(maxPaso) + 2;
        int[] s     = {inicio, inicio+paso, inicio+2*paso, inicio+3*paso, inicio+4*paso};
        return crearPregunta("¿Cuál es el siguiente número?\n" + s[0]+", "+s[1]+", "+s[2]+", "+s[3]+", __", s[4]);
    }

    private static Pregunta secuenciaDecreciente(String nivel) {
        int maxIni  = nivel.equals("facil") ? 50  : nivel.equals("intermedio") ? 200 : 1000;
        int maxPaso = nivel.equals("facil") ? 5   : nivel.equals("intermedio") ? 15  : 40;
        int inicio  = random.nextInt(maxIni)  + 30;
        int paso    = random.nextInt(maxPaso) + 2;
        int[] s     = {inicio, inicio-paso, inicio-2*paso, inicio-3*paso, inicio-4*paso};
        return crearPregunta("¿Cuál es el siguiente número?\n" + s[0]+", "+s[1]+", "+s[2]+", "+s[3]+", __", s[4]);
    }

    private static Pregunta secuenciaGeometrica(String nivel) {
        int maxIni    = nivel.equals("leyenda") ? 5 : 3;
        int maxFactor = nivel.equals("facil") ? 2 : nivel.equals("intermedio") ? 3 : 4;
        int inicio    = random.nextInt(maxIni) + 1;
        int factor    = random.nextInt(maxFactor) + 2;
        int[] s       = {inicio, inicio*factor, inicio*factor*factor, inicio*factor*factor*factor};
        return crearPregunta("¿Cuál es el siguiente número?\n" + s[0]+", "+s[1]+", "+s[2]+", "+s[3]+", __", s[3]*factor);
    }

    private static Pregunta secuenciaCuadrados(String nivel) {
        int maxIni = nivel.equals("facil") ? 5 : nivel.equals("intermedio") ? 10 : 20;
        int inicio = random.nextInt(maxIni) + 1;
        int[] s    = {inicio*inicio, (inicio+1)*(inicio+1), (inicio+2)*(inicio+2),
                (inicio+3)*(inicio+3), (inicio+4)*(inicio+4)};
        return crearPregunta("¿Cuál es el siguiente número?\n"
                + s[0]+", "+s[1]+", "+s[2]+", "+s[3]+", __", s[4]);
    }

    private static Pregunta secuenciaFibonacci(String nivel) {
        int maxIni = nivel.equals("facil") ? 3 : nivel.equals("intermedio") ? 8 : 15;
        int a = random.nextInt(maxIni) + 1;
        int b = random.nextInt(maxIni) + 1;
        int c = a+b, d = b+c, e = c+d;
        return crearPregunta(
                "¿Cuál es el siguiente número?\n"
                        + a+", "+b+", "+c+", "+d+", __", e);
    }
    private static Pregunta crearPreguntaDecimal(String enunciado, double correcta) {
        // Formatea el número — si es entero no muestra decimales
        String respuestaStr = correcta == Math.floor(correcta)
                ? String.valueOf((int) correcta)
                : String.format("%.2f", correcta).replace(",", ".");

        Set<String> set = new LinkedHashSet<>();
        set.add(respuestaStr);

        int intentos = 0;
        while (set.size() < 4 && intentos < 200) {
            double delta = Math.max(0.5, correcta * 0.15);
            double d = correcta + (random.nextDouble() * delta * 2) - delta;
            d = Math.round(d * 100.0) / 100.0; // redondea a 2 decimales
            if (d > 0 && d != correcta) {
                String ds = d == Math.floor(d)
                        ? String.valueOf((int) d)
                        : String.format("%.2f", d).replace(",", ".");
                set.add(ds);
            }
            intentos++;
        }

        // Rellena si faltan opciones
        int extra = 1;
        while (set.size() < 4) {
            double val = correcta + extra;
            String vs = val == Math.floor(val)
                    ? String.valueOf((int) val)
                    : String.format("%.2f", val).replace(",", ".");
            if (!set.contains(vs)) set.add(vs);
            extra++;
        }

        List<String> opciones = new ArrayList<>(set);
        Collections.shuffle(opciones);
        return new Pregunta(enunciado, opciones, respuestaStr);
    }

    private static Pregunta crearPregunta(String enunciado, int correcta) {
        Set<Integer> set = new LinkedHashSet<>();
        set.add(correcta);
        int intentos = 0;
        while (set.size() < 4 && intentos < 200) {
            int delta = Math.max(5, correcta / 4);
            int d = correcta + random.nextInt(delta * 2 + 1) - delta;
            if (d > 0 && d != correcta) set.add(d);
            intentos++;
        }
        int extra = 1;
        while (set.size() < 4) {
            if (!set.contains(correcta + extra)) set.add(correcta + extra);
            extra++;
        }
        List<String> opciones = new ArrayList<>();
        for (int n : set) opciones.add(String.valueOf(n));
        Collections.shuffle(opciones);
        return new Pregunta(enunciado, opciones, String.valueOf(correcta));
    }
}