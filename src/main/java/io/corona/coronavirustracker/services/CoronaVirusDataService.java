package io.corona.coronavirustracker.services;

import io.corona.coronavirustracker.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronaVirusDataService {
    public static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";

    List<LocationStats> allStats = new ArrayList<>();

    public List<LocationStats> getAllStats() {
        return allStats;
    }

    // Post construct so that Spring can know to run this function when instance of this service class is created
    @PostConstruct
    // Scheduled to run daily so that records can be updated ...
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        List<LocationStats> newStats = new ArrayList<>();
        //Http client is created to make request
        HttpClient client = HttpClient.newHttpClient();
        // HttpRequest module is used which is using builder pattern to build uri out of String so that new req can be made
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(VIRUS_DATA_URL)).build();
        // Http Response
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        //  StringReader for reading  String
        StringReader csvReader = new StringReader(httpResponse.body());
        // Commons CSV Open Source library for parsing csv file in raw format and taking values out of it
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvReader);
        // Enabled auto header picker and fetching records for display  while looping
        for (CSVRecord record : records) {
            LocationStats locationStats = new LocationStats();
            locationStats.setState(record.get("Province/State"));
            locationStats.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
            locationStats.setLatestTotalCases(latestCases);
            locationStats.setDiffFromPrevDay(latestCases - prevDayCases);
            newStats.add(locationStats);
        }
        this.allStats = newStats;
    }
}
