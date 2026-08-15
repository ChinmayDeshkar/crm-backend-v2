package com.deshkar.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;

@Configuration
public class PdfConfig {

    private final TemplateEngine templateEngine;

    public PdfConfig(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Bean
    public CommandLineRunner invoiceTemplateInit() {
        return args -> InvoicePdfGenerator.setTemplateEngine(templateEngine);
    }
}
