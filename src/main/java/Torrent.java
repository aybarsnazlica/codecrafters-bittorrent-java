import java.util.HashMap;

public class Torrent {
    public HashMap<String, HashMap<String, Object>>  root;
    public HashMap<String, Object> info;

    public Torrent(byte[] bytes) {
        Decoder decoder = new Decoder();
        root = (HashMap<String, HashMap<String, Object>>) decoder.decode(bytes);
        info = root.get("info");
    }
}