package gr.ioannidis.crawler;

import gr.ioannidis.crawler.db.model.Product;

/**
 *
 * @author yiannis
 */
public interface IPageCrawler extends ICrawler<Product> {
    @Override
    Product crawlPage();
    
}
