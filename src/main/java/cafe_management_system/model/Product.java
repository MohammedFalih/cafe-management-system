package cafe_management_system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.DynamicInsert;

@NamedQuery(name = "Product.getAllProduct", query = "select new cafe_management_system.wrapper.ProductWrapper(p.id, p.name, p.description, p.price, p.status, p.category.id, p.category.name) from Product p")

@NamedQuery(name = "Product.updateProductStatus", query = "update Product p set p.status=:status where p.id=:id")

@NamedQuery(name = "Product.getProductsByCategory", query = "select new cafe_management_system.wrapper.ProductWrapper(p.id, p.name) from Product p where p.category.id=:id and p.status='true'")

@NamedQuery(name = "Product.findProductById", query = "select new cafe_management_system.wrapper.ProductWrapper(p.id, p.name, p.description, p.price) from Product p where p.id=:id")

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "product")
public class Product implements Serializable{

    private static final long serialVersionUID = 1234L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_fk", nullable = false)
    private Category category;

}
