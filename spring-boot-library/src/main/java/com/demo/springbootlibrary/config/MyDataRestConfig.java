package com.demo.springbootlibrary.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.demo.springbootlibrary.entity.Book;
import com.demo.springbootlibrary.entity.Message;
import com.demo.springbootlibrary.entity.Review;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

	private String allowedOrigins = "https://localhost:3000"; // React
//	private String allowedOrigins1 = "https://192.168.1.4:3000"; 
	
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
		HttpMethod[] unsupportedActions = { HttpMethod.POST, HttpMethod.PATCH, HttpMethod.PUT, HttpMethod.DELETE };

		config.exposeIdsFor(Book.class);
		disableHttpMethod(Book.class, config, unsupportedActions);
		
		config.exposeIdsFor(Review.class);
		disableHttpMethod(Review.class, config, unsupportedActions);
		
		config.exposeIdsFor(Message.class);
		disableHttpMethod(Message.class, config, unsupportedActions);
		
		/* Configure CORS */
		cors.addMapping(config.getBasePath() + "/**").allowedOrigins(allowedOrigins);

	}

	private void disableHttpMethod(Class classs, RepositoryRestConfiguration config, HttpMethod[] unsupportedActions) {
		config.getExposureConfiguration().forDomainType(classs)
				.withItemExposure((metadata, httpMethod) -> httpMethod.disable(unsupportedActions))
				.withCollectionExposure((metadata, httpMethod) -> httpMethod.disable(unsupportedActions));

	}

}
