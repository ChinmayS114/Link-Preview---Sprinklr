package com.example.linkpreview.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PrimaryDetails {

    public static String fetchTitleTwitter(Document pageSource) {
        String title = TwitterTagGetter.getTitle(pageSource);
        if (title == null) {
            title = OpenGraphTagGetter.getTitle(pageSource);
        }
        return title;
    }

    public static String fetchTitle(Document pageSource) {
        String title = OpenGraphTagGetter.getTitle(pageSource);
        return title;
    }

    public static String fetchTitleTwitter(Document pageSource, WebDriver driver, WebDriverWait wait) {
        String title = MetaTagContentGetter.getMetaTagContent(driver, wait, "twitter:title");
        if (title == null || title.isEmpty()) {
            title = MetaTagContentGetter.getMetaTagContent(driver, wait, "og:title");
        }
        if (title == null || title.isEmpty()) {
            title = MetaTagContentGetter.getTitleContent(driver, wait);
        }
        if (title == null || title.isEmpty() && pageSource != null) {
            title = OpenGraphTagGetter.getTitle(pageSource);
        }
        if (title == null || title.isEmpty()) {
            title = MetaTagContentGetter.getMetaTagContent(driver, wait, "name", "title");
        }
        if (title == null || title.isEmpty()) {
            title = getFirstNonNullTitle(driver);
        }
        return title;
    }

    public static String fetchTitle(Document pageSource, WebDriver driver, WebDriverWait wait) {
        String title = MetaTagContentGetter.getMetaTagContent(driver, wait, "og:title");
        if (title == null || title.isEmpty()) {
            title = MetaTagContentGetter.getTitleContent(driver, wait);
        }
        if (title == null || title.isEmpty() && pageSource != null) {
            title = OpenGraphTagGetter.getTitle(pageSource);
        }
        if (title == null || title.isEmpty()) {
            title = MetaTagContentGetter.getMetaTagContent(driver, wait, "name", "title");
        }
        if (title == null || title.isEmpty()) {
            title = getFirstNonNullTitle(driver);
        }
        if (title == null || title.isEmpty() || title.length() < 3) {
            title = MetaTagContentGetter.getMetaTagContent(driver, wait, "name", "title");
        }
        if (title == null || title.isEmpty() || title.length() < 3) {
            title = MetaTagContentGetter.getMetaTagContent(driver, wait, "og:title");
        }
        if (title == null || title.isEmpty() || title.length() < 3) {
            title = MetaTagContentGetter.getTitleContent(driver, wait);
        }
        if (title.length() < 3 || title == null || title.isEmpty() && pageSource != null) {
            title = TwitterTagGetter.getTitle(pageSource); // Make sure pageSource is of type org.jsoup.nodes.Document
        }
        return title;
    }

    public static String fetchDescription(Document pageSource) {
        String description = TwitterTagGetter.getDescription(pageSource);
        if (description == null) {
            description = OpenGraphTagGetter.getDescription(pageSource);
        }
        return description;
    }

    public static String fetchDescription(Document pageSource, WebDriver driver, WebDriverWait wait) {
        String description = MetaTagContentGetter.getMetaTagContent(driver, wait, "twitter:description");
        if (description == null || description.isEmpty()) {
            description = MetaTagContentGetter.getMetaTagContent(driver, wait, "og:description");
            if (description == null || description.isEmpty()) {
                description = MetaTagContentGetter.getMetaTagContent(driver, wait, "name", "description");
            }
        }
        if (description == null || description.isEmpty() && pageSource != null) {
            description = MetaTagContentGetter.getMetaTagContent(driver, wait, "name", "description");
        }
        if (description == null || description.isEmpty()) {
            description = OpenGraphTagGetter.getDescription(pageSource);
        }
        return description;
    }

    public static String fetchCard(Document pageSource) {
        String twitterCard = TwitterTagGetter.getCard(pageSource);
        if (twitterCard == null || twitterCard.isEmpty()) {
            twitterCard = "default";
        }
        return twitterCard;
    }

    public static String fetchCard(Document pageSource, WebDriver driver, WebDriverWait wait) {
        String twitterCard = MetaTagContentGetter.getMetaTagContent(driver, wait, "name", "twitter:card");

//        if (twitterCard == null || twitterCard.isEmpty() && pageSource != null) {
//            twitterCard = TwitterTagGetter.getCard(pageSource); // Make sure pageSource is of type org.jsoup.nodes.Document
//        }
        if (twitterCard == null || twitterCard.isEmpty()) {
            twitterCard = "default";
        }
        return twitterCard;
    }

    public static String fetchDomain(String urlString) throws MalformedURLException {
        URL url = new URL(urlString);
        String domain = url.getHost();

        return domain;
    }

    public static String fetchImageTwitter(Document pageSource) {
        String imageUrl = TwitterTagGetter.getImageUrl(pageSource); // Make sure pageSource is of type org.jsoup.nodes.Document
        return imageUrl;
    }

    public static String fetchImage(Document pageSource, WebDriver driver, int minW, int minH) {
        String imageUrl = OpenGraphTagGetter.getImageUrl(pageSource);
        if (imageUrl != null && !imageUrl.isEmpty() && imageUtils.isImageTooSmall(driver, imageUrl, minW, minH)) {
            imageUrl = null;
        }
        return imageUrl;
    }

    public static String fetchImage(String url, Document pageSource, WebDriver driver, WebDriverWait wait, int minW, int minH) {
        String imageUrl = MetaTagContentGetter.getMetaTagContent(driver, wait, "og:image");
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageUrl = MetaTagContentGetter.getMetaTagContent(driver, wait, "name", "image");
        }

        if (imageUrl != null) {
            if (minH == 200 && imageUtils.isImageTooSmall(driver, imageUrl, minW, minH)) {
                imageUrl = imageUtils.findFirstLargeImage(driver, minW, minH);
            }
        } else {
            imageUrl = imageUtils.findFirstLargeImage(driver, minW, minH);
        }

        if (imageUrl == null || imageUrl.length() < 3) {
            driver.get(url);
            imageUrl = imageUtils.findFirstLargeImageFallback(driver, minW, minH);
        }
        return imageUrl;
    }

    public static String fetchImageTwitter(Document pageSource, WebDriver driver, WebDriverWait wait) {
        String imageUrl = null;
        if (pageSource != null) {
            imageUrl = OpenGraphTagGetter.getImageUrlLast(pageSource);
        }
        if (imageUrl == null) {
            imageUrl = MetaTagContentGetter.getMetaTagContent(driver, wait, "name", "twitter:image");
        }
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageUrl = MetaTagContentGetter.getMetaTagContent(driver, wait, "name", "twitter:image:src");
            if (imageUrl == null || imageUrl.isEmpty()) {
                imageUrl = MetaTagContentGetter.getLastMetaTagContent(driver, wait, "property", "og:image");
            }
        }

        return imageUrl;
    }

    private static String getFirstNonNullTitle(WebDriver driver) {
        try {

            WebElement titleTag = driver.findElement(By.tagName("title"));
            if (titleTag != null && !titleTag.getText().isEmpty()) {
                return titleTag.getText();
            }


            List<WebElement> h1Tags = driver.findElements(By.tagName("h1"));
            for (WebElement h1Tag : h1Tags) {
                if (!h1Tag.getText().isEmpty()) {
                    return h1Tag.getText();
                }
            }

            return "";
        } catch (Exception e) {
            return "";
        }
    }
}
