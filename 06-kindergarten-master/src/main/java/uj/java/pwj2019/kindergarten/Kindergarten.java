package uj.java.pwj2019.kindergarten;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;




final class Gowniak extends Child{

    public Gowniak(String name, int hungerSpeedMs) {
        super(name, hungerSpeedMs);
    }
}

public class Kindergarten {


    public static void main(String[] args) throws IOException {
        init();
        final var fileName = args[0];
        Path file_path = Paths.get(fileName);

         List<String> strim = Files.readAllLines(file_path);
         Integer n = Integer.parseInt(strim.get(0));

        List<Child> children = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            String temp = strim.get(i+1);
            String[] temp_str = temp.split(" ");
            Gowniak g = new Gowniak( temp_str[0]  , Integer.parseInt(temp_str[1]) ) ;
            children.add( g );
        }

        children.get(0).eat();
        children.get(1).eat();
        children.get(2).eat();


        System.out.println("File name: " + fileName);
        //TODO: read children file, and keep children NOT hungry!
    }

    private static void init() throws IOException {
        Files.deleteIfExists(Path.of("out.txt"));
        System.setErr(new PrintStream(new FileOutputStream("out.txt")));
        new Thread(Kindergarten::runKindergarden).start();
    }

    private static void runKindergarden() {
        try {
            Thread.sleep(10100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            List<String> errLines = Files.readAllLines(Path.of("out.txt"));
            System.out.println("Children cries count: " + errLines.size());
            errLines.forEach(System.out::println);
            System.exit(errLines.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
