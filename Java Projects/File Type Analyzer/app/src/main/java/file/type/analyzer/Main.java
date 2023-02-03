package file.type.analyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import file.type.analyzer.algorithms.RabinKarp;
import file.type.analyzer.tools.*;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        InputParameters params = new InputParameters(args);

        List<File> fileList = new ArrayList<>();
        FileReader.getFilesFromDirectory(new File[]{new File(params.getFilepath())}, fileList);

        RabinKarp kmp = new RabinKarp();

        ExecutorService workers = Executors.newFixedThreadPool(10);

        Callable<String> searcher = () -> {
            while (fileList.size() > 0) {
                var file = fileList.remove(0);
                var content = FileReader.readFile(file.getPath());
                boolean patternFound = false;

                for(var pattern: params.getPatternList()) {
                    if (kmp.applyAlgorithm(content, pattern.getPattern())) {
                        System.out.println(file.getName() + ": " + pattern.getFileType());
                        patternFound = true;
                        break;
                    }
                }

                if (!patternFound) {
                    System.out.println(file.getName() + ": " + "Unknown file type");
                }
            }

            return null;
        };

        List<Callable<String>> searches = new ArrayList<>();

        for (int i = 0; i < fileList.size(); i++) {
            searches.add(searcher);
        }

        workers.invokeAll(searches);
        waitExecution(workers);
        workers.shutdown();
    }

    private static void waitExecution(ExecutorService threadPool) {
        threadPool.shutdown();

        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
