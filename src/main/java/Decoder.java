import java.util.ArrayList;

public class Decoder {
    private int index = 0;

    public Object decode(String bencodedString) {
        char firstChar = bencodedString.charAt(index);

        if (Character.isDigit(firstChar)) {
            int colonIndex = bencodedString.indexOf(':', index);
            int length = Integer.parseInt(bencodedString.substring(index, colonIndex));
            int start = colonIndex + 1;

            index = start + length;

            return bencodedString.substring(start, start + length);
        } else if (firstChar == 'i') {
            int endIndex = bencodedString.indexOf('e', index);
            long number = Long.parseLong(bencodedString.substring(index + 1, endIndex));

            index = endIndex + 1;

            return number;
        } else if (firstChar == 'l') {
            index++;

            ArrayList<Object> list = new ArrayList<>();

            while (bencodedString.charAt(index) != 'e') {
                list.add(decode(bencodedString));
            }

            index++;

            return list;
        } else {
            throw new RuntimeException("Invalid bencoded string");
        }
    }
}
