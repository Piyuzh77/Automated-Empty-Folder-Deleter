package com.Piyush.Trials;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FilePath {

    private String init= "C:\\Users\\91945\\OneDrive";
    private Path homePath = Paths.get(init);
    private Path logFile = Paths.get("C:\\Users\\91945\\OneDrive\\deletedFolders.log"); 
    private Path errorLogFile = Paths.get("C:\\Users\\91945\\OneDrive\\errorLog.log"); 
    private BufferedWriter logWriter;
    private BufferedWriter errorLogWriter;

    public void traverseFilePaths(Path init) {

        ExecutorService mt= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        try {

            logWriter = Files.newBufferedWriter(logFile, StandardOpenOption.CREATE,StandardOpenOption.APPEND);

            errorLogWriter = Files.newBufferedWriter(errorLogFile, StandardOpenOption.CREATE,StandardOpenOption.APPEND);

                List<Path> validDirectories= new ArrayList<>();
                Files.walk(init).forEach((folders)->{
                    if(Files.isDirectory(folders))
                        validDirectories.add(folders);
                });
                
                Collections.reverse(validDirectories);

                for(Path fol: validDirectories){
                    mt.submit(()->{
                        try{
                            if(Files.list(fol).count()==0){
                                synchronized(logWriter){
                                    try {
                                        logWriter.write(" ||| Deleted file: "+ fol.toString()+" |||||");
                                        logWriter.flush();
                                    } catch (Exception e) {
                                        System.out.println("Cant write in logWriter!!!???");
                                        e.printStackTrace();
                                    }
                                }
                                Files.delete(fol);
                            }
                        }
                        catch(Exception e){
                                synchronized(errorLogWriter){
                                    try {
                                        errorLogWriter.write("File: "+ fol.toString()+" denied access!");
                                        errorLogWriter.flush();
                                    } catch (IOException e1) {
                                        System.out.println("Cant write in ErrorLogFile!!!!???");
                                        e1.printStackTrace();
                                    }
                                }
                                System.out.println("Cant access folder: "+ fol.toString());
                            }
                    });
                }
            
            }
            catch(Exception e){
                System.out.println("Something went wrong!!");
            }
            
    mt.shutdown();
    
    try {
            if (!mt.awaitTermination(1, TimeUnit.HOURS)) {
                    mt.shutdownNow(); 
                }
        } 
        catch (InterruptedException e) {
                mt.shutdownNow();
        }
    
    System.out.println("Finished!!!");
}

    public Path getHomeDir() {
        return homePath;
    }
}
