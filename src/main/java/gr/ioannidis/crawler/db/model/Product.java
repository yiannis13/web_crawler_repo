package gr.ioannidis.crawler.db.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author yiannis persistence.xml created because of that
 */
@Entity
@Table(name = "product")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p"),
    @NamedQuery(name = "Product.findByProductId", query = "SELECT p FROM Product p WHERE p.productId = :productId"),
    @NamedQuery(name = "Product.findByBrandName", query = "SELECT p FROM Product p WHERE p.brandName = :brandName"),
    @NamedQuery(name = "Product.findByDescription", query = "SELECT p FROM Product p WHERE p.description = :description"),
    @NamedQuery(name = "Product.findByModel", query = "SELECT p FROM Product p WHERE p.model = :model"),
    @NamedQuery(name = "Product.findByPrice", query = "SELECT p FROM Product p WHERE p.price = :price"),
    @NamedQuery(name = "Product.findByWhen", query = "SELECT p FROM Product p WHERE p.crawlDate = :crawlDate")})
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Basic(optional = false)
    @Column(name = "brand_name", nullable = false, length = 256)
    private String brandName;

    @Basic(optional = false)
    @Column(name = "description", length = 256)
    private String description;

    @Column(name = "model", length = 256, unique = true)
    private String model;

    @Basic(optional = false)
    @Column(name = "price", nullable = false)
    private double price;
    
    @Column(name = "availability", length = 45)
    private String availability;

    @Basic(optional = false)
    @Column(name = "crawl_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date crawlDate;

    
    public Product() {
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getCrawlDate() {
        return crawlDate;
    }

    public void setCrawlDate(Date crawlDate) {
        this.crawlDate = crawlDate;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }


    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.model.equals(other.model)) || (this.productId == other.productId)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.brandName != null ? this.brandName.hashCode() : 0);
        hash = 37 * hash + (this.model != null ? this.model.hashCode() : 0);
        return hash;
    }
   

    @Override
    public String toString() {
        return "product with id: " + productId + ", is a " + description + ", " + brandName + ",  Model: " + model + ", Price :" + price + ".Availability:  " + availability;
    }

}
