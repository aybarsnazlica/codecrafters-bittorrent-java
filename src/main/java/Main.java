import com.google.gson.Gson;
import java.nio.file.Files;
import java.nio.file.Path;
//import java.util.HashMap;

public class Main {
    public static final Gson gson = new Gson();

    public static void main(String[] args) throws Exception {
        String command = args[0];

        if ("decode".equals(command)) {
            try {
                Decoder decoder = new Decoder();
                String bencodedValue = args[1];
                Object decoded = decoder.decode(bencodedValue.getBytes());
                System.out.println(gson.toJson(decoded));
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }

        } else if ("info".equals(command)) {
            try {
                Torrent torrent = new Torrent(Files.readAllBytes(Path.of(args[1])));

                String announce = String.valueOf(torrent.root.get("announce"));
                Long length = (Long) torrent.info.get("length");

                System.out.println("Tracker URL: " + announce);
                System.out.println("Length: " + length);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }

        } else {
            System.out.println("Unknown command: " + command);
        }
    }
}