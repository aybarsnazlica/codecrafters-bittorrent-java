import java.util.ArrayList;
import java.util.HashMap;

public class Decoder {
    private int index = 0;

    public Object decode(byte[] bencodedData) {
        char currentChar = (char) bencodedData[index];

        if (Character.isDigit(currentChar)) {
            return decodeString(bencodedData);
        } else if (currentChar == 'i') {
            return decodeInteger(bencodedData);
        } else if (currentChar == 'l') {
            return decodeList(bencodedData);
        } else if (currentChar == 'd') {
            return decodeMap(bencodedData);
        } else {
            throw new RuntimeException("Invalid bencoded data at index " + index + ": " + currentChar);
        }
    }

    private String decodeString(byte[] bencodedData) {
        int colonIndex = index;
        while (bencodedData[colonIndex] != ':') {
            colonIndex++;
        }

        int length = Integer.parseInt(new String(bencodedData, index, colonIndex - index));
        index = colonIndex + 1;
        String result = new String(bencodedData, index, length);
        index += length;

        return result;
    }

    private Long decodeInteger(byte[] bencodedData) {
        index++; // skip 'i'
        int endIndex = index;
        while (bencodedData[endIndex] != 'e') {
            endIndex++;
        }

        Long number = Long.parseLong(new String(bencodedData, index, endIndex - index));
        index = endIndex + 1; // skip 'e'
        return number;
    }

    private ArrayList<Object> decodeList(byte[] bencodedData) {
        index++; // skip 'l'
        ArrayList<Object> list = new ArrayList<>();
        while (bencodedData[index] != 'e') {
            list.add(decode(bencodedData));
        }
        index++; // skip 'e'
        return list;
    }

    private HashMap<String, Object> decodeMap(byte[] bencodedData) {
        index++; // skip 'd'
        HashMap<String, Object> map = new HashMap<>();
        while (bencodedData[index] != 'e') {
            String key = (String) decode(bencodedData);
            Object value = decode(bencodedData);
            map.put(key, value);
        }
        index++; // skip 'e'
        return map;
    }
}