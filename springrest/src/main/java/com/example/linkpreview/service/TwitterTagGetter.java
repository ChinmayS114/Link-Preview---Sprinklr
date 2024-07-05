package com.example.linkpreview.service;

import org.jsoup.nodes.Document;

public class TwitterTagGetter {

    public static String getTitle(Document doc) {
        return MetaTagContentGetter.getMetaTagContent(doc, "twitter:title");
    }

    public static String getDescription(Document doc) {
        return MetaTagContentGetter.getMetaTagContent(doc, "twitter:description");
    }

    public static String getImageUrl(Document doc) {
        String imageUrl = MetaTagContentGetter.getMetaTagContent(doc, "twitter:image");
        if (imageUrl.isEmpty()) {
            imageUrl = MetaTagContentGetter.getMetaTagContent(doc, "twitter:image:src");
        }
        return imageUrl;
    }

    public static String getCard(Document doc) {
        return MetaTagContentGetter.getMetaTagContent(doc, "twitter:card");
    }
}
