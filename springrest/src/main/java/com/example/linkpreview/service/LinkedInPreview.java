package com.example.linkpreview.service;

import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import com.example.linkpreview.model.LinkPreviewResponse;
import java.net.URL;
import java.util.List;

public class LinkedInPreview {

    public static LinkPreviewResponse fetch(String urlString, WebDriver driver, long startTimeNano, List<String> additionalInfo) {
        try {
        	Document pageSource = PageSourceFetcher.fetchPageSource(urlString, additionalInfo);
           
            
            long fetchStartTimeNano = System.nanoTime();

            if (pageSource != null) {
                String title = PrimaryDetails.fetchTitle(pageSource);
                String description = PrimaryDetails.fetchDescription(pageSource);
                String domain = PrimaryDetails.fetchDomain(urlString);
                String imageUrl = PrimaryDetails.fetchImage(pageSource, driver, 120, 120);

             
                if (title != null && !title.isEmpty() && description != null && !description.isEmpty() &&
                    imageUrl != null) {


                   
                    long elapsedTimeNano = System.nanoTime() - fetchStartTimeNano;
                    double elapsedTimeSeconds = (double) elapsedTimeNano / 1_000_000_000.0;

                    
                    String info = "LinkedIn Response: Elapsed Time (static) - " + elapsedTimeSeconds + " seconds";
                    additionalInfo.add(info);
                    return new LinkPreviewResponse(title, description, imageUrl, domain);
                }
            }

           
            return FBandLiSele.fetchUsingSelenium(urlString, pageSource, driver, 200, 199, startTimeNano, additionalInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}