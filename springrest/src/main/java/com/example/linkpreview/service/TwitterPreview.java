package com.example.linkpreview.service;

import org.openqa.selenium.WebDriver;
import org.jsoup.nodes.Document;

import java.util.List;

public class TwitterPreview {

    public static TwitterPreviewResponse fetch(String url, WebDriver driver, long startTimeNano, List<String> additionalInfo) {
        try {
            Document pageSource = PageSourceFetcher.fetchPageSource(url, additionalInfo);
            
            if (pageSource != null) {
            	long fetchStartTimeNano = System.nanoTime();
                String title = PrimaryDetails.fetchTitleTwitter(pageSource);
                String description = PrimaryDetails.fetchDescription(pageSource);
                String imageUrl = PrimaryDetails.fetchImageTwitter(pageSource);
                String twitterCard = PrimaryDetails.fetchCard(pageSource);
                String domain = PrimaryDetails.fetchDomain(url);
                long elapsedTimeNano1 = System.nanoTime() - fetchStartTimeNano;
                double elapsedTimeSeconds1 = (double) elapsedTimeNano1 / 1_000_000_000.0;
                System.out.println(elapsedTimeSeconds1);
                if (title != null && !title.isEmpty() && description != null && !description.isEmpty() && imageUrl != null && !imageUrl.isEmpty()) {
                    
                    long elapsedTimeNano = System.nanoTime() - fetchStartTimeNano;
                    double elapsedTimeSeconds = (double) elapsedTimeNano / 1_000_000_000.0;

                    String info = "Twitter Response: Elapsed Time (static)- " + elapsedTimeSeconds + " seconds";
                    additionalInfo.add(info);

                    return new TwitterPreviewResponse(title, description, imageUrl, domain, twitterCard);
                }
            }

            return TwitterSelenium.fetch(url, pageSource, driver, startTimeNano, additionalInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
