package com.splitwise.configuration;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.splitwise.dto.Category;

@Configuration
public class SplitwiseConfig {

	@Bean
	public List<Category> categories(ObjectMapper mapper) throws StreamReadException, DatabindException, IOException {
		List<Category> categories = (List<Category>) mapper.readValue(
				new ClassPathResource("Categories.json").getInputStream(), new TypeReference<List<Category>>() {
				});

		categories.stream().forEach(category -> {
			if (category.getSplitwiseId() == null) {
				category.setSplitwiseId(category.getId());
			}
			category.setPatterndescriptions(category.getDescriptions().stream()
					.map(desc -> Pattern.compile(desc, Pattern.CASE_INSENSITIVE)).collect(Collectors.toList()));
		});
		return categories;
	}

	@Bean
	public RestTemplate restTemplate(@Value("${app.splitwise.api-key}") String apiKey) {
		return new RestTemplateBuilder()
				.defaultHeader("Authorization", "Bearer "+apiKey).build();

	}
}
