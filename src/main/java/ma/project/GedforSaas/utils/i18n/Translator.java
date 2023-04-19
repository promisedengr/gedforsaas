package ma.project.GedforSaas.utils.i18n;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class Translator {

    private static ResourceBundleMessageSource messageSource;

    public Translator(@Qualifier("textsResourceBundleMessageSource") ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public static String TO_LOCALE(String code , Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }
}
