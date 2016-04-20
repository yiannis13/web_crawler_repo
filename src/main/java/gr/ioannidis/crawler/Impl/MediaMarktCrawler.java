package gr.ioannidis.crawler.Impl;

import gr.ioannidis.crawler.IPageCrawler;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import gr.ioannidis.crawler.db.model.Product;
import gr.ioannidis.utils.checks.ArgsCheck;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Use this class by passing 1 specific URL in its constructor. If this link has
 * the specified brandName, it creates a new Product object and returns it.
 *
 * @author yiannis
 *
 */
public class MediaMarktCrawler implements IPageCrawler {

    private String url;
    private final String brandName;
    private Document doc;
    private Product product;

    public MediaMarktCrawler(String url, String brandName) {
        ArgsCheck.ensureNotNull(url, "url");
        ArgsCheck.ensureNotNull(brandName, "brandName");
        this.url = url;
        this.brandName = brandName;
    }

    @Override
    public Product crawlPage() {
        try {
            doc = Jsoup.connect(url).ignoreContentType(true).get();

            String pageProductBrandName = findProductBrandName(doc);
            if ((pageProductBrandName != null) && (pageProductBrandName.equalsIgnoreCase(brandName))) {
                product = new Product();

                String description = findProductDescription(doc);
                String model = findProductModel(doc);
                double price = findProductPrice(doc);
                String availability = findProductAvailability(doc);

                product.setBrandName(brandName);
                product.setDescription(description);
                product.setModel(model);
                product.setPrice(price);
                product.setAvailability(availability);
                product.setCrawlDate(new Timestamp(new Date().getTime()));
                return product;
            }
        } catch (IOException ex) {
            Logger.getLogger(MediaMarktCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static String findProductBrandName(Document doc) {
        String brandName;
        Elements metaElements = doc.getElementsByTag("meta");
        for (Element e : metaElements) {
            if (e.hasAttr("property") && (e.attr("property").equals("product:brand"))) {
                brandName = e.attr("content");
                return brandName;
            }
        }
        return null;
    }

    private static String findProductDescription(Document doc) {
        String description = null;
        Elements dlElememts = doc.getElementsByTag("dl");
        for (Element e : dlElememts) {
            if (e.hasAttr("class") && (e.attr("class").equals("product-details"))) {
                description = e.getElementsByTag("dd").first().text();
                return description;
            }
        }
        return description;
    }

    private static String findProductModel(Document doc) {
        String model;
        Elements metaElements = doc.getElementsByTag("meta");
        for (Element e : metaElements) {
            if ((e.hasAttr("property")) && (e.attr("property").equals("og:title"))) {
                model = e.attr("content");
                return model;
            }
        }
        return null;
    }

    private static Double findProductPrice(Document doc) {
        double price;
        Elements metaElements = doc.getElementsByTag("meta");
        for (Element e : metaElements) {
            if (e.hasAttr("property") && (e.attr("property").equals("product:price:amount"))) {
                String priceInStringFormat = e.attr("content");
                price = Double.parseDouble(priceInStringFormat);
                return price;
            }
        }
        return null;
    }

    private static String findProductAvailability(Document doc) {
        String availability;
        Elements metaElements = doc.getElementsByTag("meta");
        for (Element e : metaElements) {
            if (e.hasAttr("property") && (e.attr("property").equals("og:availability"))) {
                availability = e.attr("content");
                return availability;
            }
        }
        return null;
    }

}
