package pl.com.warehousefull.warehouse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@PropertySource({"classpath:application.yml"})
@EnableJpaRepositories(
        basePackages = "pl.com.computerplus.murapol.warehousefull.warehouse.repositories.config",
        entityManagerFactoryRef = "warehouseFullEntityManager",
        transactionManagerRef = "warehouseFullTransactionManager"
)
public class PersistenceWarehouseFullAutoConfiguration {

    private final Environment env;

    @Autowired
    public PersistenceWarehouseFullAutoConfiguration(final Environment env) {
        this.env = env;
    }

    @Bean(destroyMethod = "")
    public LocalContainerEntityManagerFactoryBean warehouseFullEntityManager() {
        final LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(warehouseFullDataSource());
        em.setPackagesToScan("pl.com.computerplus.murapol.warehousefull.warehouse.models.config");

        final HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        final HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",
                env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect",
                "org.hibernate.dialect.SQLServer2012Dialect");
        em.setJpaPropertyMap(properties);
        
        return em;
    }

    @Bean(destroyMethod = "")
    @ConfigurationProperties(prefix = "spring.datasource-warehouse-full")
    public DataSource warehouseFullDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(destroyMethod = "")
    public PlatformTransactionManager warehouseFullTransactionManager() {

        final JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                warehouseFullEntityManager().getObject());
        return transactionManager;
    }

}
