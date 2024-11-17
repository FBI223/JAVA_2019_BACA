package uj.java.pwj2019.kindergarten;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



final class Dziecko extends Child implements Runnable, Comparable<Dziecko> {


    public  ReentrantLock leftFork;
    public  ReentrantLock rightFork;
    public  int prefered_fork;
    public Random rand;
    public int prog_glodu = 60 ;
    public int thinking_time = 100;


    public Dziecko(String name, int hungerSpeedMs, Random random , ReentrantLock leftFork, ReentrantLock rightFork , int prefered_fork) {
        super(name, hungerSpeedMs);

        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.prefered_fork = prefered_fork;
        this.rand = random;

        //thinking_time = 90 + rand.nextInt(21);
        thinking_time = this.hungerSpeed() * 5 ;
        prog_glodu = 60 - rand.nextInt(6);

    }



    public void think() throws InterruptedException {
        Thread.sleep(this.thinking_time);
    }


    @Override
    public int compareTo(Dziecko o) {
        return  Integer.compare(happiness(), o.happiness()) ;
    }

    @Override
    public void run() {

        try{
            while (true)
            {
                if (  !leftFork.isLocked() && !rightFork.isLocked() && happiness() <=  prog_glodu )
                {
                    if ( prefered_fork == 0 )
                    {
                        this.leftFork.lock();
                        this.rightFork.lock();
                    } else
                    {
                        this.rightFork.lock();
                        this.leftFork.lock();
                    }

                    this.eat();

                    this.leftFork.unlock();
                    this.rightFork.unlock();


                } else {
                    try {
                        this.think();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }


        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }
}




public class Kindergarten {


    public static void main(String[] args) throws IOException {
        init();
        final var fileName = args[0];
        Path file_path = Paths.get(fileName);

         List<String> strim = Files.readAllLines(file_path);
         int n = Integer.parseInt(strim.get(0));

        Random random = new Random();
        ReentrantLock[] forks = new ReentrantLock[n];

        for (int i = 0; i < n; i++) {
            forks[i] = new ReentrantLock();
        }


        List<Child> children = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            String temp = strim.get(i+1);
            String[] temp_str = temp.split(" ");
            Dziecko dz = new Dziecko( temp_str[0]  , Integer.parseInt(temp_str[1]) , random , forks[i] , forks[(i+1) % n]  , random.nextInt(2) ) ;
            children.add( dz );
            new Thread(dz).start();
        }



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
            //10100
            Thread.sleep(20100);
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
