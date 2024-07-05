package com.example.linkpreview.service;

import java.net.URL;
import java.time.Duration;
import java.util.List;

import org.jsoup.nodes.Document;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.example.linkpreview.model.LinkPreviewResponse;

import demo.PreviewPrinter;

public class FBandLiSele {

    public static LinkPreviewResponse fetchUsingSelenium(String urlString, Document pageSource, WebDriver driver, int minW, int minH, long startTimeNano, List<String> additionalInfo) {
        try {
            
            long fetchStartTimeNano = System.nanoTime();

            driver.get(urlString);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            String title = PrimaryDetails.fetchTitle(pageSource, driver, wait);
            String description = PrimaryDetails.fetchDescription(pageSource, driver, wait);
            String domain = PrimaryDetails.fetchDomain(urlString);
            String imageUrl = PrimaryDetails.fetchImage(urlString, pageSource, driver, wait, minW, minH);

           
            long elapsedTimeNano = System.nanoTime() - fetchStartTimeNano;
            double elapsedTimeSeconds = (double) elapsedTimeNano / 1_000_000_000.0;

            
            if(minH == 200) {
            String info = "Facebook Response: Elapsed Time (dynamic) - " + elapsedTimeSeconds + " seconds";
            additionalInfo.add(info);
            }
            else
            {
            String info = "LinkedIn Response: Elapsed Time (dynamic) - " + elapsedTimeSeconds + " seconds";
            additionalInfo.add(info);
            }


            return new LinkPreviewResponse(title, description, imageUrl, domain);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}