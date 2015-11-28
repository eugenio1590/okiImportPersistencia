package com.okiimport.persistencia;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.velocity.app.VelocityEngine;
import org.hibernate.cache.HashtableCacheProvider;
import org.hibernate.dialect.PostgreSQLDialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.okiimport.app.service.mail.MailService;
import com.okiimport.app.service.mail.impl.MailServiceImpl;
import com.okiimport.persistencia.mail.ConfigMail;

//@EnableTransactionManagement
//@EnableJpaRepositories("com.okiimport.app.dao")
public class AbstractJpaConfiguration {

//	@Value("#{dataSource}")
//	private javax.sql.DataSource dataSource;
	
	private ConfigMail configMail;
	
	public AbstractJpaConfiguration(String user, String password){
		this.configMail = new ConfigMail(user, password);
	}
	
	@Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/requerimientos");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");
//        dataSource.setTestOnBorrow(Boolean.TRUE);
//        dataSource.setTestOnReturn(Boolean.TRUE);
//        dataSource.setTestWhileIdle(Boolean.TRUE);
//        dataSource.setTimeBetweenEvictionRunsMillis(1800000);
//        dataSource.setNumTestsPerEvictionRun(3);
//        dataSource.setMinEvictableIdleTimeMillis(1800000);
//        dataSource.setMaxActive(5);
//        dataSource.setLogAbandoned(Boolean.TRUE);
//        dataSource.setRemoveAbandoned(Boolean.TRUE);
//        dataSource.setRemoveAbandonedTimeout(10);
        return dataSource;
    }

	@Bean
	public Map<String, Object> jpaProperties() {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("hibernate.dialect", PostgreSQLDialect.class.getName());
		props.put("hibernate.cache.provider_class", HashtableCacheProvider.class.getName());
		return props;
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setShowSql(true);
		hibernateJpaVendorAdapter.setGenerateDdl(true);
		hibernateJpaVendorAdapter.setDatabase(Database.POSTGRESQL);
		return hibernateJpaVendorAdapter;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		//return new JpaTransactionManager( entityManagerFactory().getObject() ); 4000
		JpaTransactionManager transactionManager = new JpaTransactionManager( entityManagerFactory() );
		transactionManager.setDefaultTimeout(4000);
		return transactionManager;
	}

	@Bean(name = "entityManagerFactory")
	//public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
	public EntityManagerFactory entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
		lef.setDataSource(dataSource());
		lef.setJpaPropertyMap(this.jpaProperties());
		lef.setJpaVendorAdapter(this.jpaVendorAdapter());
		lef.setPersistenceUnitName("okiImportPersistencia");
		lef.afterPropertiesSet();
		return lef.getObject();
	}

	/**SUPPORT*/
	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator(){
		return new HibernateExceptionTranslator();
	}
	
	@Bean
	public PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor(){
		return new PersistenceAnnotationBeanPostProcessor();
	}
	
	@Bean
	public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor(){
		return new PersistenceExceptionTranslationPostProcessor();
	}
	
	/**MAIL*/
	@Bean(name="mailService")
	public MailService mailService(){
		MailService mailService = new MailServiceImpl();
		mailService.setMailSender(mailSender());
		mailService.setVelocityEngine(velocityEngine());
		return mailService;
	}
	
	@Bean(name="mailSender")
	public JavaMailSender mailSender(){
		return this.configMail.getMailSender();
	}
	
	@Bean(name="velocityEngine")
	public VelocityEngine velocityEngine(){
		return this.configMail.getVelocityEngine();
	}
}
