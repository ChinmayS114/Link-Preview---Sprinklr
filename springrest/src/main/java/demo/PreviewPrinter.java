package demo;

import java.net.MalformedURLException;
import java.net.URL;

public class PreviewPrinter {
    public static void printPreview(String title, String description, String imageUrl, String urlString) {
        try {
            URL url = new URL(urlString);
            String domain = url.getHost();
            System.out.println("Domain: " + domain);
            System.out.println("Title: " + (title != null ? title : "Not available"));
            System.out.println("Description: " + (description != null ? description : "Not available"));
            System.out.println("Image URL: " + (imageUrl != null ? imageUrl : "Not available"));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}