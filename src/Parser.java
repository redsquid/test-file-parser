import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Parser implements Runnable {

    private final Pattern pattern;

    private final Buffer buffer;

    private final Map<String, Integer> map;

    public Parser(Buffer buffer, int minLength, Map<String, Integer> map) {
        this.pattern = Pattern.compile(String.format("(\\b[А-Яа-яA-Za-z]*[А-Яа-яA-Za-z\\d-]{%d}\\b)", minLength));
        this.buffer = buffer;
        this.map = map;
    }

    public void run() {
        try {
            while (true) {
                String line = buffer.get();
                if (line == null) {
                    return;
                }
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String word = matcher.group(1).toUpperCase();
                    map.put(word, map.getOrDefault(word, 0) + 1);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}