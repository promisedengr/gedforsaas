package ma.project.GedforSaas.controller;

import freemarker.template.TemplateException;
import ma.project.GedforSaas.exception.EmailCfg;
import ma.project.GedforSaas.model.MailComponent;
import ma.project.GedforSaas.service.EmailSenderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("admin/api/v1/emailParams")
public class EmailCfgAdminController {
    @Autowired
    private EmailSenderService emailSenderService;


    @PostMapping("/send")
    public void send(@RequestParam("to") String to,
                     @RequestParam("subject") String subject,
                     @RequestParam("content") String content) throws IOException, TemplateException, MessagingException {
        MailComponent mailComponent =new MailComponent();
        mailComponent.setTo(to);
        mailComponent.setSubject(subject);
        mailComponent.setContent(content);
        emailSenderService.send(mailComponent);
    }

    @GetMapping("/company/id/{id}")
    public EmailCfg findByCompanyId(@PathVariable Long id) {
        return emailSenderService.findByCompanyId(id);
    }


    @GetMapping("/")
    public List<EmailCfg> findAll() {
        return emailSenderService.findAll();
    }

    @PostMapping("/")
    public EmailCfg save(@RequestBody EmailCfg entity) {
        return emailSenderService.save(entity);
    }



    @PostMapping("/test/send/{to}")
    public void testEmail(@RequestBody EmailCfg entity,@PathVariable String to) throws MessagingException {
          emailSenderService.testConnection(entity, to);
    }

    @DeleteMapping("/id/{id}")
    public void deleteById(@PathVariable Long id) {
        emailSenderService.deleteById(id);
    }

    @GetMapping("/exist/company/{id}")
    public boolean existsByCompanyId(@PathVariable Long id) {
        return emailSenderService.existsByCompanyId(id);
    }
}
