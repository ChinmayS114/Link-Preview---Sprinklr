package com.example.linkpreview.service;

import org.openqa.selenium.WebDriver;
import com.example.linkpreview.model.LinkPreviewResponse;

import java.net.URL;
import java.util.List;
import org.jsoup.nodes.Document;

public class FacebookPreview {

    public static LinkPreviewResponse fetch(String urlString, WebDriver driver, long startTimeNano, List<String> additionalInfo) {
        try {
            Document pageSource = PageSourceFetcher.fetchPageSource(urlString, additionalInfo);
            if (pageSource != null) {
                
                long fetchStartTimeNano = System.nanoTime();

                String title = PrimaryDetails.fetchTitle(pageSource);
                String description = PrimaryDetails.fetchDescription(pageSource);
                String imageUrl = PrimaryDetails.fetchImage(pageSource, driver, 200, 200);
                String domain = PrimaryDetails.fetchDomain(urlString);
                
                long elapsedTimeNano = System.nanoTime() - fetchStartTimeNano;
                double elapsedTimeSeconds = (double) elapsedTimeNano / 1_000_000_000.0;

                if (title != null && !title.isEmpty() && description != null && !description.isEmpty() && imageUrl != null && !imageUrl.isEmpty()) {

                    String info = "Facebook Response: Elapsed Time (static) - " + elapsedTimeSeconds + " seconds";
                    additionalInfo.add(info);
                    return new LinkPreviewResponse(title, description, imageUrl, domain);
                }
            }

           
            return FBandLiSele.fetchUsingSelenium(urlString, pageSource, driver, 200, 200,startTimeNano,additionalInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}