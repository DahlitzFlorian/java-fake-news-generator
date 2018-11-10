import org.json.JSONObject;

public interface NachrichtenBeschaffung {
     JSONObject artikelBeschaffung(String keyword);
     String artikelCutter(String artikel);
     JSONObject artikelJSONBuilder(JSONObject artikelJson, String News, String titel);
}
