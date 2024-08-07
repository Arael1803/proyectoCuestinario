package custionario;
import java.io.*;
import java.util.logging.*;
import java.util.*;
public class Cuestionario {
    private List<String> preguntas = new ArrayList<>();
    public static void main(String[] args) {
        Cuestionario cuestionario = new Cuestionario();
        cuestionario.iniciarCuestionario("C:\\Users\\Arami\\Documents\\x.txt", "C:\\Users\\Arami\\Documents\\y.txt");
    }
    private void iniciarCuestionario(String archivoPreguntas, String archivoRespuestas) {
        cargarPreguntas(archivoPreguntas);
        realizarCuestionario(archivoRespuestas);
    }
    private void cargarPreguntas(String archivoPreguntas) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoPreguntas))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                preguntas.add(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de preguntas: " + e.getMessage());
        }
    }
    private void realizarCuestionario(String archivoRespuestas) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        Set<String> preguntasHechas = new HashSet<>();
        String continuar;
        do {
            if (preguntasHechas.size() >= preguntas.size()) {
                System.out.println("No hay más preguntas disponibles.");
                break;
            }
            int indice;
            String preguntaCompleta, id, pregunta;
            String[] partes;
            do {
                indice = random.nextInt(preguntas.size());
                preguntaCompleta = preguntas.get(indice);
                partes = preguntaCompleta.split("\\|");
                id = partes[0];
                pregunta = partes[1];
            } while (partes.length != 2 || preguntasHechas.contains(id));
            preguntasHechas.add(id);
            System.out.println(pregunta);
            System.out.print("Tu respuesta: ");
            String respuestaUsuario = scanner.nextLine().trim();

            String respuestaCorrecta = buscarRespuestaCorrecta(id, archivoRespuestas);
            if (respuestaCorrecta != null && respuestaCorrecta.equalsIgnoreCase(respuestaUsuario)) {
                System.out.println("¡Correcto!");
            } else {
                System.out.println("Incorrecto. La respuesta correcta es: " + respuestaCorrecta);
            }

            if (preguntasHechas.size() < preguntas.size()) {
                System.out.print("¿Deseas continuar? (s/n): ");
                continuar = scanner.nextLine().trim().toLowerCase();
            } else {
                continuar = "n";
            }

        } while (continuar.equals("s"));

        scanner.close();
        System.out.println("Cuestionario finalizado.");
    }

    private String buscarRespuestaCorrecta(String id, String archivoRespuestas) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoRespuestas))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 2 && partes[0].equals(id)) {
                    return partes[1];
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de respuestas: " + e.getMessage());
        }
        return null;
    }
}
