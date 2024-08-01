import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Encoder {

    public static void encode(Object obj, ByteArrayOutputStream encoded) throws IOException {
        switch (obj) {
            case Long l -> encoded.write(("i" + obj + "e").getBytes());
            case String s -> encodeString(s, encoded);
            case byte[] bytes -> encodeByteString(bytes, encoded);
            case Map<?, ?> map -> encodeMap((Map<String, Object>) obj, encoded);
            case List<?> objects -> encodeList((List<Object>) obj, encoded);
            case null, default -> throw new IllegalArgumentException("Unsupported object type for bencoding");
        }
    }

    private static void encodeString(String obj, ByteArrayOutputStream encoded) throws IOException {
        byte[] bytes = obj.getBytes();
        encoded.write((bytes.length + ":").getBytes());
        encoded.write(bytes);
    }

    private static void encodeByteString(byte[] bytes, ByteArrayOutputStream encoded) throws IOException {
        encoded.write((bytes.length + ":").getBytes());
        encoded.write(bytes);
    }

    private static void encodeMap(Map<String, Object> map, ByteArrayOutputStream encoded) throws IOException {
        encoded.write('d');
        // Ensure the keys are sorted
        for (Map.Entry<String, Object> entry : new TreeMap<>(map).entrySet()) {
            encodeString(entry.getKey(), encoded);
            encode(entry.getValue(), encoded);
        }
        encoded.write('e');
    }

    private static void encodeList(List<Object> list, ByteArrayOutputStream encoded) throws IOException {
        encoded.write('l');
        for (Object item : list) {
            encode(item, encoded);
        }
        encoded.write('e');
    }

    public byte[] encode(Object obj) throws IOException {
        ByteArrayOutputStream encoded = new ByteArrayOutputStream();
        encode(obj, encoded);
        return encoded.toByteArray();
    }
}
