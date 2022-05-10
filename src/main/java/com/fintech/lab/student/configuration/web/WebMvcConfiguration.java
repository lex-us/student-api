package com.fintech.lab.student.configuration.web;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.Locale;

@Configuration
public class WebMvcConfiguration {

    private static final Locale RUSSIAN_LOCALE = new Locale("ru");
    private static final Locale UKRAINIAN_LOCALE = new Locale("uk");

    private final MessageSource messageSource;

    public WebMvcConfiguration(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(UKRAINIAN_LOCALE);
        localeResolver.setSupportedLocales(Arrays.asList(
                UKRAINIAN_LOCALE, RUSSIAN_LOCALE
        ));
        return localeResolver;
    }

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setValidationMessageSource(messageSource);
        return validatorFactoryBean;
    }

    @Bean
    public static OffsetDateTimeConverter offsetDateTimeConverter() {
        return new OffsetDateTimeConverter();
    }

}
