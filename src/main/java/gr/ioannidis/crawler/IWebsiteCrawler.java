package gr.ioannidis.crawler;

import gr.ioannidis.crawler.db.model.Product;
import java.io.IOException;
import java.util.Set;

/**
 *
 * @author yiannis
 */
public interface IWebsiteCrawler {
    Set<Product> crawlWebsite() throws IOException;
}
