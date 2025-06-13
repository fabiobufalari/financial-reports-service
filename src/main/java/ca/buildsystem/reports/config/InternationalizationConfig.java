package ca.buildsystem.reports.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Configuration for internationalization support.
 * Configures locale resolution, message sources, and locale change interceptors.
 */
@Configuration
public class InternationalizationConfig implements WebMvcConfigurer {

    private static final List<Locale> SUPPORTED_LOCALES = Arrays.asList(
            new Locale("en", "CA"), // Canadian English (default)
            new Locale("en", "US"), // American English
            new Locale("fr", "CA"), // Canadian French
            new Locale("zh", "CN"), // Simplified Chinese
            new Locale("hi", "IN")  // Hindi
    );
    
    /**
     * Configures the locale resolver based on Accept-Language header.
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setSupportedLocales(SUPPORTED_LOCALES);
        resolver.setDefaultLocale(new Locale("en", "CA")); // Default: Canadian English
        return resolver;
    }
    
    /**
     * Configures the interceptor for language change via parameter.
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang"); // Parameter to change language
        return interceptor;
    }
    
    /**
     * Registers the language change interceptor.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
    
    /**
     * Configures the message source for internationalization.
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }
}
