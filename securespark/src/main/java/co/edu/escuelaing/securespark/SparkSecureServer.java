package co.edu.escuelaing.securespark;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import static spark.Spark.*;

/**
 * Servicio seguro de login, realiza una petición a otro servicio sólo si el
 * usuario se autenticó correctamente
 * @author Federico Barrios Meneses
 */
public class SparkSecureServer {

    /**
     * Ejecuta el servicio
     * @param args 
     */
    public static void main(String[] args) {
        port(getPort());
        secure("keystores/awskeystore.p12", "ramona", "keystores/myTrustStore", "ramona");
        staticFileLocation("/static");
        DBConnection connection = new DBConnection();
        post("/login", (req, res) -> {
            return connection.logIn(req.body());
        });
        get("/service", (req, res) -> {
            String url = "https://ec2-54-162-211-19.compute-1.amazonaws.com:5000/?username=" + req.queryParams("username");
            File trustStoreFile = new File("keystores/myTrustStore");
            char[] trustStorePassword = "ramona".toCharArray();
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(new FileInputStream(trustStoreFile), trustStorePassword);
            TrustManagerFactory tmf = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            SSLContext.setDefault(sslContext);
            return readURL(url);
        });
    }
     
    /**
     * Cliente que obtiene la información del servicio de fecha y hora
     * @param site url del servicio
     * @return la información devuelta por el servicio
     */
    private static String readURL(String site) {
        URL siteURL = null;
        try {
            siteURL = new URL(site);
        } catch (MalformedURLException ex) {}
        URLConnection urlConnection = null;
        try {
            urlConnection = siteURL.openConnection();
        } catch (IOException ex) {}
        String body = "";
        try {
            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine = null;
            while ((inputLine = reader.readLine()) != null) {
                body = body + inputLine;
            }
        } catch (IOException ex) {}
        return body;
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
