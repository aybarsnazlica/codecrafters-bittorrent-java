import com.google.gson.Gson;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;


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
                byte[] fileContent = Files.readAllBytes(Path.of(args[1]));
                Torrent torrent = new Torrent(fileContent);

                String announce = String.valueOf(torrent.root.get("announce"));
                Long length = (Long) torrent.info.get("length");

                System.out.println("Tracker URL: " + announce);
                System.out.println("Length: " + length);

                Encoder encoder = new Encoder();
                byte[] bencoded = encoder.encode(torrent.info);

                MessageDigest md = MessageDigest.getInstance("SHA-1");
                md.update(bencoded);
                byte[] digest = md.digest();

                StringBuilder hexString = new StringBuilder();
                for (byte b : digest) {
                    hexString.append(String.format("%02x", b));
                }

                System.out.println("Info Hash: " + hexString);
                System.out.println("Piece Length: " + torrent.info.get("piece length"));
                System.out.println("Piece Hashes: ");

                byte[] pieces = (byte[]) torrent.info.get("pieces");
                StringBuilder pieces_hex = new StringBuilder();

                for (byte b : pieces) {
                    pieces_hex.append(String.format("%02x", b));
                }
                System.out.println(pieces_hex);

            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }

        } else {
            System.out.println("Unknown command: " + command);
        }
    }
}