import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

class Reader implements Runnable {

    private final Buffer buffer;

    private final List<Path> paths;

    public Reader(Buffer buffer, List<Path> paths) {
        this.buffer = buffer;
        this.paths = paths;
    }

    public void run() {
        try {
            for (Path p : paths) {
                Scanner scanner = new Scanner(p.toFile());
                while (scanner.hasNextLine()) {
                    buffer.put(scanner.nextLine());
                }
                scanner.close();
            }
            buffer.finish();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
