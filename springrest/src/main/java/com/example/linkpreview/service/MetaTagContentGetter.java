package com.example.linkpreview.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;



public class MetaTagContentGetter {
    public static String getMetaTagContent(Document doc, String property) {

        String content = doc.select("meta[property=" + property + "]").attr("content");
        if (content.isEmpty()) {
            content = doc.select("meta[name=" + property + "]").attr("content");
        }
        return content;
    }

    public static String getMetaTagContent(WebDriver driver, WebDriverWait wait, String property) {
        try {
            WebElement metaTag = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//meta[@property='" + property + "'])[1] | (//meta[@name='" + property + "'])[1]")));
            if (metaTag == null) {
                return null;
            }
            String content = metaTag.getAttribute("content");
            if (content == null || content.isEmpty()) {
                content = metaTag.getAttribute("value");
            }
            return content;
        } catch (Exception e) {
            return null;
        }
    }
   
    public static String getTitleContent(WebDriver driver, WebDriverWait wait) {
        try {
        WebElement titleElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//title)[1]")));
            if (titleElement == null) {
                return null;
            }
            String title = titleElement.getText();
            return title;
        } catch (Exception e) {
            return null;
        }
    }
   
    public static String getMetaTagContent(Document doc, String attribute, String value) {
//        Document doc = Jsoup.parse(pageSource);
        String content = doc.select("meta[" + attribute + "=" + value + "]").attr("content");
        return content;
    }

    public static String getMetaTagContent(WebDriver driver, WebDriverWait wait, String attribute, String value) {
        try {
            WebElement metaTag = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//meta[@" + attribute + "='" + value + "'])[1]")));
            if (metaTag == null) {
                return null;
            }
            String content = metaTag.getAttribute("content");
            if (content == null || content.isEmpty()) {
                content = metaTag.getAttribute("value");
            }
            return content;
        } catch (Exception e) {
            return null;
        }
    }
   
    public static String extractTitleFromHtml(Document doc) {
        try {
//            Document doc = Jsoup.parse(html);
            Element titleElement = doc.selectFirst("title");
            if (titleElement != null) {
                return titleElement.text();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
   
    public static String getLastMetaTagContent(Document doc, String property) {
//        Document doc = Jsoup.parse(pageSource);
        Elements metaTags = doc.select("meta[property=" + property + "]");
        if (!metaTags.isEmpty()) {
            Element lastMetaTag = metaTags.last();
            return lastMetaTag.attr("content");
        }
        return null;
    }
   
    public static String getLastMetaTagContent(WebDriver driver, WebDriverWait wait, String attribute, String value) {
        try {
            WebElement metaTag = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//meta[@" + attribute + "='" + value + "'])[last()]")));
            if (metaTag == null) {
                return null;
            }
            String content = metaTag.getAttribute("content");
            if (content == null || content.isEmpty()) {
                content = metaTag.getAttribute("value");
            }
            return content;
        } catch (Exception e) {
            return null;
        }
    }
}