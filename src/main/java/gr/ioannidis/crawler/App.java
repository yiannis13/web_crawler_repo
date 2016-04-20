package gr.ioannidis.crawler;

import gr.ioannidis.crawler.Impl.MediaMarktWebsiteCrawler;
import gr.ioannidis.crawler.db.controller.ProductJpaController;
import gr.ioannidis.crawler.db.model.Product;
import java.io.IOException;
import java.util.Set;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author yiannis
 *
 */
public class App {

    public static void main(String[] args) throws IOException {

        //String baseUrl = "http://www.mediamarkt.gr/";
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("crawler_pu");
        ProductJpaController controller = new ProductJpaController(emf);

        String baseUrl = "http://www.mediamarkt.gr/el/product/_sony-kdl-50w807c-1161286.html";

        MediaMarktWebsiteCrawler websiteCrawler = new MediaMarktWebsiteCrawler(baseUrl, "sony", 50);

        Set<Product> crawlWebsite = websiteCrawler.crawlWebsite();
        for (Product p : crawlWebsite) {
            controller.create(p);
        }

    }// end of main

}// end of class
