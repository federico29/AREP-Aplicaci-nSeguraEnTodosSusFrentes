package co.edu.escuelaing.securespark;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.Objects;
import org.json.JSONObject;
import org.bson.Document;

/**
 * Conexión a la base de datos Mongodb que guarda usuarios y contraseñas
 * @author Federico Barrios Meneses
 */
public class DBConnection {

    private static MongoClient mongoClient;
    private static MongoDatabase dataBase;
    private static MongoCollection<Document> collection;

    /**
     * Establece la conexión, luego referencia la base de datos y la colección
     * requerida
     */
    public DBConnection() {
        mongoClient = new MongoClient(new MongoClientURI("mongodb://172.17.0.1:29000"));
        dataBase = mongoClient.getDatabase("login");
        collection = dataBase.getCollection("users");
    }

    /**
     * Valida que el usuario ingresado esté registrado en la base de datos
     * @param source cadena que contiene las credenciales de ingreso
     * @return true si está registrado
     */
    public boolean logIn(String source) {
        JSONObject credentials = new JSONObject(source);
        Document document = new Document();
        document.append("username", credentials.get("username"));
        document.append("password", credentials.get("password"));
        Long appears = collection.count(document);
        return !Objects.equals(appears, new Long(0));
    }
}
