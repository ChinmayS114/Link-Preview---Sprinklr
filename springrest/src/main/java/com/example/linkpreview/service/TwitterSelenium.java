package com.example.linkpreview.service;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import org.jsoup.nodes.Document;

public class TwitterSelenium {
    public static TwitterPreviewResponse fetch(String urlString, Document pageSource, WebDriver driver, long startTimeNano, List<String> additionalInfo) {
        try {
            
            driver.get(urlString);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));

            
            long fetchStartTimeNano = System.nanoTime();
           
            String title = PrimaryDetails.fetchTitleTwitter(pageSource, driver, wait);
            String description = PrimaryDetails.fetchDescription(pageSource, driver, wait);
            String twitterCard = PrimaryDetails.fetchCard(pageSource, driver, wait);
            String domain = PrimaryDetails.fetchDomain(urlString);
            String imageUrl = PrimaryDetails.fetchImageTwitter(pageSource, driver, wait);
            
            long elapsedTimeNano = System.nanoTime() - fetchStartTimeNano;
            double elapsedTimeSeconds = (double) elapsedTimeNano / 1_000_000_000.0;

           
            String info = "Twitter Response: Elapsed Time (dynamic) - " + elapsedTimeSeconds + " seconds";
            additionalInfo.add(info);
            

            return new TwitterPreviewResponse(title, description, imageUrl, domain, twitterCard);
        } catch (MalformedURLException e) {
            System.out.println("Invalid URL");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
           
        }
    }
}