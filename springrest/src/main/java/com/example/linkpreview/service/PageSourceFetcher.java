
package com.example.linkpreview.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class PageSourceFetcher {

    public static synchronized Document fetchPageSource(String urlString, List<String> additionalInfo) {
        long fetchStartTimeNano = System.nanoTime();
        StringBuilder pageSourceBuilder = new StringBuilder();
//        return null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(4000);
            connection.setReadTimeout(4000);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                pageSourceBuilder.append(inputLine);
            }
            in.close();

            String pageSource = pageSourceBuilder.toString();
            long elapsedTimeNano = System.nanoTime() - fetchStartTimeNano;
            double elapsedTimeSeconds = (double) elapsedTimeNano / 1_000_000_000.0;

            
            if(pageSource == null)
            	return null;
            Document d =  Jsoup.parse(pageSource);
            
            String info = "Page Source Fetch time(HTML) - " + elapsedTimeSeconds + " seconds";
            additionalInfo.add(info);
            
            return d;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


