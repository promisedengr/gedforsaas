package ma.project.GedforSaas.service;

import org.springframework.stereotype.Service;

import static ma.project.GedforSaas.utils.i18n.Translator.TO_LOCALE;

import java.util.Locale;

@Service
public class TranslationService {

    public String translate(String message,Locale locale) {
        return TO_LOCALE(message, locale);
    }
}
