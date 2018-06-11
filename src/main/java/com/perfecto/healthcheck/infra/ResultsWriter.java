package com.perfecto.healthcheck.infra;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResultsWriter {


    private static List<String[]> csvLines = new ArrayList<>();
    private static File resultCsvFile = new File("results.csv");
    private static CSVWriter writer;

    static{
        try {
            writer  = new CSVWriter(new FileWriter(resultCsvFile));
        } catch (IOException e) {
            System.out.println("Unable to open results file " + resultCsvFile + " for writing, aborting...");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void init(){

    }

    public static synchronized void addLine(String mcmName,String cradleId,String deviceId,String status){
        csvLines.add(new String[]{mcmName,cradleId,deviceId,status});
    }

    public synchronized static void flush(){
        if (writer == null){
            return;
        }
        try {
            writer.writeAll(csvLines);
            writer.close();
            writer = null;
        } catch (IOException e) {
            System.out.println("Unable to close results file " + resultCsvFile + " for writing, aborting...");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
