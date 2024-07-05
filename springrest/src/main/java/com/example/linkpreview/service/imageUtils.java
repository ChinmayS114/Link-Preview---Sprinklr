package com.example.linkpreview.service;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class imageUtils {

    public static boolean isImageTooSmall(String imageUrl, int minW, int minH) {
        try {
            URL url = new URL(imageUrl);
            BufferedImage image = ImageIO.read(url);
            return image.getWidth() < minW || image.getHeight() < minH;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


public static boolean isImageTooSmall(WebDriver driver, String imageUrl, int minW, int minH) {
   try {
       driver.get(imageUrl);
       WebElement imgElement = driver.findElement(By.tagName("img"));
       int width = imgElement.getSize().getWidth();
       int height = imgElement.getSize().getHeight();
       return width < minW || height < minH;
   } catch (Exception e) {
       e.printStackTrace();
       return true;
   }
}

public static String findFirstLargeImageFallback(WebDriver driver, int minWidth, int minHeight) {
    try {
        for (WebElement img : driver.findElements(By.tagName("img"))) {
            String src = img.getAttribute("src");
             System.out.println(src);
            if (src != null && !src.isEmpty()) {
                try {
                    URL imgUrl = new URL(src);
                    BufferedImage image = ImageIO.read(imgUrl);
                    if (image != null && image.getWidth() >= minWidth && image.getHeight() >= minHeight) {
                         System.out.println("large image: " + src);
                        return src;
                    }
                } catch (Exception e) {
                     System.out.println("Error image: " + src);
                     //return src;
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
  
   

   
public static String findFirstLargeImage(WebDriver driver, int minWidth, int minHeight) {
	   try {
	       List<WebElement> images = driver.findElements(By.tagName("img"));
	       for (WebElement img : images) {
	           String src = img.getAttribute("src");
	           System.out.println(src);
	       }
	       
	       JavascriptExecutor js = (JavascriptExecutor) driver;

	       for (WebElement img : images) {
	           String src = img.getAttribute("src");
	           if (src != null && !src.isEmpty()) {
	               try {
	                  
	                   boolean isVisible = img.isDisplayed();
	                   if (isVisible) {
	                      
	                       js.executeAsyncScript(
	                           "var img = arguments[0];" +
	                           "var callback = arguments[arguments.length - 1];" +
	                           "var timeout = setTimeout(function() {" +
	                           "    callback(null);" +
	                           "}, 2000);" +
	                           "img.onload = function() {" +
	                           "    clearTimeout(timeout);" +
	                           "    callback({ width: img.naturalWidth, height: img.naturalHeight });" +
	                           "};" +
	                           "if (img.complete) {" +
	                           "    clearTimeout(timeout);" +
	                           "    callback({ width: img.naturalWidth, height: img.naturalHeight });" +
	                           "}"
	                       , img);

	                     
	                       Object result = js.executeAsyncScript(
	                           "var img = arguments[0];" +
	                           "var callback = arguments[arguments.length - 1];" +
	                           "if (img.complete) {" +
	                           "    callback({ width: img.naturalWidth, height: img.naturalHeight });" +
	                           "} else {" +
	                           "    img.onload = function() {" +
	                           "        callback({ width: img.naturalWidth, height: img.naturalHeight });" +
	                           "    };" +
	                           "}"
	                       , img);

	                       if (result != null) {
	                           Map<String, Long> dimensions = (Map<String, Long>) result;
	                           int width = dimensions.get("width").intValue();
	                           int height = dimensions.get("height").intValue();
	                           System.out.println(src + "   " + height + "  " + width);
	                           if (width >= minWidth && height >= minHeight) {
	                               return src;
	                           }
	                       } else {
	                           System.out.println("Image not loaded within 2 seconds: " + src);
	                       }
	                   } else {
	                       System.out.println("Image not displayed: " + src);
	                   }
	               } catch (Exception e) {
	                   System.out.println("Failed to retrieve image dimensions for: " + src + ", skipping...");
	                   e.printStackTrace();
	               }
	           }
	       }
	   } catch (Exception e) {
	       e.printStackTrace();
	   }
	   return null;
	}

}