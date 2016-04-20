package gr.ioannidis.crawler.db.controller;


import java.util.List;

/**
 *
 * @author yiannis
 */
public interface IDbService<T> {

    void create(T entiy);

    void remove(Integer id);

    void edit(T entity);

    T find(Integer id);

    List<T> findAll();

    List<T> findRange(int maxResults, int range);

    int count();
    
}
