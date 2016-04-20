package gr.ioannidis.crawler;

/**
 *
 * @author yiannis
 * @param <T> the entity to be crawled
 */
public interface ICrawler<T> {
    T crawlPage();
}
