package com.example.linkpreview.service;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;
import com.example.linkpreview.model.LinkPreviewResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class LinkPreviewService {

    private final WebDriver[] drivers = new WebDriver[3];
    private boolean isDriverInitialized = false;
    private final Map<String, AggregatedResponse> cache = new HashMap<>();
    private long startTimeNano;

    public AggregatedResponse getLinkPreview(String url) {
        startTimeNano = System.nanoTime();
        List<String> additionalInfo = new ArrayList<>();
        if (!isDriverInitialized) {
            long driverInitStartTime = System.nanoTime();
            initializeWebDrivers();
            isDriverInitialized = true;
            long driverInitEndTime = System.nanoTime();
            double driverInitTimeSeconds = (driverInitEndTime - driverInitStartTime) / 1_000_000_000.0;
            String info = "Driver Initialization time - " + driverInitTimeSeconds + " seconds";
            additionalInfo.add(info);
        }

        try {
            if (cache.containsKey(url)) {
                System.out.println("Found Cached preview for URL: " + url);
                return cache.get(url);
            }

            CompletableFuture<TwitterPreviewResponse> twitterFuture = CompletableFuture.supplyAsync(() -> TwitterPreview.fetch(url, drivers[0], startTimeNano, additionalInfo));
            CompletableFuture<LinkPreviewResponse> facebookFuture = CompletableFuture.supplyAsync(() -> FacebookPreview.fetch(url, drivers[1], startTimeNano, additionalInfo));
            CompletableFuture<LinkPreviewResponse> linkedinFuture = CompletableFuture.supplyAsync(() -> LinkedInPreview.fetch(url, drivers[2], startTimeNano, additionalInfo));

            CompletableFuture<Void> allFutures = CompletableFuture.allOf(twitterFuture, facebookFuture, linkedinFuture);

            allFutures.join(); 

            TwitterPreviewResponse twitterResponse = twitterFuture.get();
            LinkPreviewResponse facebookResponse = facebookFuture.get();
            LinkPreviewResponse linkedinResponse = linkedinFuture.get();

            System.out.println("Additional Info:");
            for (String info : additionalInfo) {
                System.out.println(info);
            }

            AggregatedResponse aggregatedResponse = new AggregatedResponse(twitterResponse, facebookResponse, linkedinResponse);

            long elapsedTimeNano = System.nanoTime() - startTimeNano;
            double elapsedTimeSeconds = (double) elapsedTimeNano / 1_000_000_000.0;
            System.out.println("Elapsed time (seconds): " + elapsedTimeSeconds);

            cache.put(url, aggregatedResponse);

            return aggregatedResponse;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to fetch link preview", e);
        }
    }

    private void initializeWebDrivers() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--incognito");
        options.addArguments("--lang=en-US");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-notifications");

        for (int i = 0; i < 3; i++) {
            drivers[i] = new ChromeDriver(options);
            Options driverOptions = drivers[i].manage();
            driverOptions.deleteAllCookies();
        }
    }
}


