package gr.ioannidis.crawler.Impl;

import gr.ioannidis.crawler.IPageCrawler;
import gr.ioannidis.crawler.db.model.Product;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Connection;
import gr.ioannidis.crawler.IWebsiteCrawler;
import gr.ioannidis.utils.checks.ArgsCheck;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author yiannis
 */
public class MediaMarktWebsiteCrawler implements IWebsiteCrawler {

    private final String url;
    private final String brandName;
    private final int numberOfPagesToCrawl;
    private Set<String> websiteLinks;
    private final Set<Product> crawledProducts;
    private IPageCrawler crawler;
    private static final Pattern PATTERN = Pattern.compile("(http://www.mediamarkt.gr/).*");

    public MediaMarktWebsiteCrawler(String url, String brandName, int numberOfPagesToCrawl) {
        ArgsCheck.ensureNotNull(url, "url");
        ArgsCheck.ensureNotNull(brandName, "brandName");
        ArgsCheck.ensureNotNegative(numberOfPagesToCrawl, "number of pages");
        this.url = url;
        this.brandName = brandName;
        this.numberOfPagesToCrawl = numberOfPagesToCrawl;
        this.websiteLinks = new HashSet<>();
        this.crawledProducts = new HashSet<>();
    }

    @Override
    public Set<Product> crawlWebsite() throws IOException {
        produceLinksFromUrl(url);
        if (websiteLinks != null) {
            for (String link : websiteLinks) {
                crawler = new MediaMarktCrawler(link, brandName);
                Product product = crawler.crawlPage();
                addProductToSet(product);
            }
        }
        return crawledProducts;
    }

    private void produceLinksFromUrl(String url) {
        // The base case of the recursion
        if (!websiteLinks.contains(url) && websiteLinks.size() < numberOfPagesToCrawl) {
            websiteLinks.add(url);
            Document doc = null;
            try {
                doc = createDocumentFromUrl(url);
            } catch (IOException ex) {
                Logger.getLogger(MediaMarktWebsiteCrawler.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (doc != null) {
                Set<String> linksFromDocument = findLinksFromDocument(doc);
                for (String link : linksFromDocument) {
                    produceLinksFromUrl(link);
                }
            }
        }
    }

    private static Document createDocumentFromUrl(String url) throws IOException {
        Document doc = null;
        Connection conn = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36")
                .ignoreContentType(true).ignoreHttpErrors(true)
                .timeout(5000);
        Connection.Response response = conn.execute();
        if (response.statusCode() == 200) {
            doc = conn.get();
        }
        return doc;
    }

    private static Set<String> findLinksFromDocument(Document doc) {
        Set<String> links = new HashSet<>();
        Elements anchorTags = doc.getElementsByTag("a");
        for (Element anchorTag : anchorTags) {
            String link = anchorTag.attr("abs:href");
            /*
            regex to exclude empty links and take only the mediamarkt links (not to go to youtube, for example) 
            A simpler but slower way would be:
            if (link.matches("^(http://www.mediamarkt.gr/).*")) { 
                links.add(link);
            }
             */
            
            Matcher matcher = PATTERN.matcher(link);
            if (matcher.find()) { //Also this would work: matcher.matches()
                links.add(link);
            }
        }
        return links;
    }

    private void addProductToSet(Product product) {
        if (product != null) {
            crawledProducts.add(product);
        }
    }

}
