package com.Piyush.Trials;


public class App {
    public static void main(String[] args) {
        long start= System.currentTimeMillis();
        FilePath fp= new FilePath();
        fp.traverseFilePaths(fp.getHomeDir());
        System.out.println("time: "+ (System.currentTimeMillis()-start));
    }
}
