import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Reducer {

    public static List<Map.Entry<String, Long>> reduce(Map<String, Integer> map, int count) {
        return map.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingLong(Map.Entry::getValue)))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
