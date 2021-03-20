package co.edu.escuelaing.dateservice;

import org.json.JSONObject;
import static spark.Spark.*;

/**
 * Servicio que retorna el nombre de usuario con el que se ingresó y retorna
 * la fecha y hora actual, también indica la máquina desde donde atendió la
 * petición.
 * @author Federico Barrios Meneses
 */
public class SparkService {

    /**
     * Ejecuta el servicio
     * @param args 
     */
    public static void main(String[] args) {
        port(getPort());
        secure("keystores/aws2keystore.p12", "ramona", "keystores/myTrustStore", "ramona");
        get("/", (req, res) -> {
            String usuario = req.queryParams("username");
            JSONObject json = new JSONObject();
            json.put("username", usuario);
            return "Hola " + usuario + ", la hora es: " + java.time.LocalTime.now() + 
                    ". Petición atendida desde https://ec2-54-162-211-19.compute-1.amazonaws.com:5000";
        });
    }

    /**
     * Define el puerto
     * @return el puerto que se va a usar
     */
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 5000;
    }
}
