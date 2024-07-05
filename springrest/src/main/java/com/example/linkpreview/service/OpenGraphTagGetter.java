package com.example.linkpreview.service;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class OpenGraphTagGetter {

    public static String getTitle(Document doc) {
        String title = MetaTagContentGetter.getMetaTagContent(doc, "og:title");
        if (title == null || title.isEmpty()) {
            
            Element titleTag = doc.select("title").first();
            title = titleTag != null ? titleTag.text() : "";
        }
        return title;
    }

    public static String getDescription(Document doc) {
        String description = MetaTagContentGetter.getMetaTagContent(doc, "og:description");
        if (description == null || description.isEmpty()) {
            
            description = MetaTagContentGetter.getMetaTagContent(doc, "description", "name");
        }
        return description;
    }

    public static String getImageUrl(Document doc) {
       
        return MetaTagContentGetter.getMetaTagContent(doc, "og:image");
    }
    
    public static String getImageUrlLast(Document doc) {
        
        return MetaTagContentGetter.getLastMetaTagContent(doc, "og:image");
    }
}
