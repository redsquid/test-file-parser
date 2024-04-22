import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Application {

    public static void main(String[] args) throws IOException, InterruptedException {

        int minLength = 5;
        Buffer buffer = new Buffer(2);
        List<Path> paths = Files.walk(Paths.get("C:/test"))
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toFile().getName().toLowerCase().endsWith(".txt"))
                .toList();

        //У меня было два файла, по файлу на каждый поток, возможно это снизит эффективность.
        //В случае с SSD, возможно, есть смысл читать каждый файл в несколько потоков, здесь надо отдельно разбираться
        Thread reader0 = new Thread(new Reader(buffer, List.of(paths.get(0))));
        Thread reader1 = new Thread(new Reader(buffer, List.of(paths.get(1))));

        Map<String, Integer> result = Collections.synchronizedMap(new HashMap<>());

        //Нужно подобрать кол-во
        Thread parser0 = new Thread(new Parser(buffer, minLength, result));
        Thread parser1 = new Thread(new Parser(buffer, minLength, result));

        parser0.start();
        parser1.start();
        reader0.start();
        reader1.start();

        reader1.join();
        reader0.join();
        parser1.join();
        parser0.join();

        Reducer.reduce(result, 10).forEach(r -> System.out.println(r.getKey() + " " + r.getValue()));
    }
}
