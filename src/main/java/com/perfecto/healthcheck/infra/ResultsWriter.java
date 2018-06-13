package com.perfecto.healthcheck.infra;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResultsWriter {


    private static List<String[]> resultsCsvLines = new ArrayList<>();
    private static CSVWriter resultsCsvWriter;

    private static List<String[]> devicesUsedCsvLines = new ArrayList<>();
    private static CSVWriter devicesUsedCsvWriter;


    public static void init(String mcmName){


        try {
            File resultCsvFile = new File("results_" + mcmName+ ".csv");
            resultsCsvWriter = new CSVWriter(new FileWriter(resultCsvFile));

            File devicesUsedCsvFile = new File("devices_used_" + mcmName + ".csv");
            devicesUsedCsvWriter = new CSVWriter(new FileWriter(devicesUsedCsvFile));
        } catch (IOException e) {
            System.out.println("Unable to open some or all results files for writing, aborting. See exception below");
            e.printStackTrace();
            System.exit(1);
        }

    }

    public static synchronized void addLineToResultsCsv(String mcmName, String cradleId, String deviceId, String status){
        resultsCsvLines.add(new String[]{mcmName,cradleId,deviceId,status});
    }

    public static synchronized void addLineToDevicesUsedCsv(String deviceId){
        devicesUsedCsvLines.add(new String[]{deviceId});
    }

    public synchronized static void flush(){
        if (resultsCsvWriter == null){
            return;
        }
        try {
            resultsCsvWriter.writeAll(resultsCsvLines);
            resultsCsvWriter.close();
            resultsCsvWriter = null;
        } catch (IOException e) {
            System.out.println("Unable to close results file  for writing, aborting...");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            devicesUsedCsvWriter.writeAll(devicesUsedCsvLines);
            devicesUsedCsvWriter.close();
            devicesUsedCsvWriter = null;
        } catch (IOException e) {
            System.out.println("Unable to close devices used csv file for writing, aborting...");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
