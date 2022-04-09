package com.rw.testapp.domain.articles;


import com.rw.testapp.domain.products.ProductArticleDetailsEntity;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Entity
@Table(name = "ARTICLES")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ArticleEntity {
    @Id
    private Long id;
    private String name;
    private Integer stock;

    @OneToMany(mappedBy = "article")
    @ToString.Exclude
    private Set<ProductArticleDetailsEntity> productArticlesDetailsEntities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ArticleEntity that = (ArticleEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
