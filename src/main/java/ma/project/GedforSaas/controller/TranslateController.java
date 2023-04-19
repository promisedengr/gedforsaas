package ma.project.GedforSaas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.project.GedforSaas.utils.i18n.TranslatorCode;

import static ma.project.GedforSaas.utils.i18n.Translator.TO_LOCALE;

import java.util.Locale;

@RestController
@RequestMapping("/")
public class TranslateController {
    public static Locale LOCALE;

    @GetMapping("translate/{locale}")
    public ResponseEntity<String> getTranslation(@PathVariable Locale locale) {
        TranslateController.LOCALE = locale;
        String translation = TO_LOCALE(TranslatorCode.WELCOME_MESSAGE, locale);
        return ResponseEntity.ok(translation);
    }
}
