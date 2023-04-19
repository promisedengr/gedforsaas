package ma.project.GedforSaas.service;

import freemarker.core.ParseException;
import freemarker.template.*;
import lombok.AllArgsConstructor;
import ma.project.GedforSaas.configuration.ConstantFileNames;
import ma.project.GedforSaas.exception.EmailCfg;
import ma.project.GedforSaas.model.Company;
import ma.project.GedforSaas.model.MailComponent;
import ma.project.GedforSaas.repository.EmailCfgRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class EmailSenderService {
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailSenderService.class);

    private final Configuration configuration;

    @Autowired
    private GmailService gmailService;
    @Autowired
    private EmailCfgRepository emailCfgRepository;
    @Autowired
    private UserCompanyService userCompanyService;

    @Async
    public void sendUsingTemplate(MailComponent mailComponent, String templateName)
            throws IOException, TemplateException, MessagingException {
        if (
                templateName.equals(ConstantFileNames.CONFIRMATION_TEMPLATE_MAIL) ||
                        templateName.equals(ConstantFileNames.NEW_PASSWORD_TEMPLATE_MAIL) ||
                        templateName.equals(ConstantFileNames.SECRET_TEMPLATE_MAIL)) {
            EmailCfg emailCfg = new EmailCfg();
            emailCfg.setPassword("ugcboehzlbudgnpy");
            emailCfg.setUsername("ahmed.dokumentive@gmail.com");
            emailCfg.setMailServer("smtp.gmail.com");
            emailCfg.setPortNumber(587L);
            emailCfg.setSslRequired(false);
            sentJavaMail(mailComponent, templateName, emailCfg);
        } else {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username;
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }
            System.out.println(username);
            Company company = userCompanyService.getStoredCompany(username);
            if (company != null) {
                System.out.println(company.getName());
                System.out.println(company.getId());
                EmailCfg emailCfg = this.findByCompanyId(company.getId());
                if (emailCfg != null) {
                    // USING SMTP
                    sentJavaMail(mailComponent, templateName, emailCfg);
                }
            } else {
                // USING AUTH2 WITH GOOGLE
                Template template = getTemplateByName(templateName);
                String emailContent = fillMailContent(mailComponent, template);
                this.gmailService.sendEmail(emailContent, mailComponent.getSubject(), mailComponent.getTo());
            }
        }
    }

    private void sentJavaMail(MailComponent mailComponent, String templateName, EmailCfg emailCfg) throws MessagingException, IOException, TemplateException {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailCfg.getMailServer());
        mailSender.setPort(Math.toIntExact(emailCfg.getPortNumber()));
        mailSender.setUsername(emailCfg.getUsername());
        mailSender.setPassword(emailCfg.getPassword());
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.enable", emailCfg.isSslRequired());
        mailSender.setJavaMailProperties(props);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage, "utf-8");
        mailMessage.setFrom(emailCfg.getUsername());
        mailMessage.setTo(mailComponent.getTo());
        mailMessage.setSubject(mailComponent.getSubject());
        Template template = getTemplateByName(templateName);
        String emailContent = fillMailContent(mailComponent, template);
        mailMessage.setText(emailContent, true);
        mailSender.send(mimeMessage);
    }

    @Async
    public void send(MailComponent mailComponent)
            throws IOException, TemplateException, MessagingException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        System.out.println("============================");
        System.out.println(username);
        System.out.println("============================");
        Company company = userCompanyService.getStoredCompany(username);
        EmailCfg emailCfg = this.findByCompanyId(company.getId());
        if (emailCfg != null) {
            // USING SMTP
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(emailCfg.getMailServer());
            mailSender.setPort(Math.toIntExact(emailCfg.getPortNumber()));
            mailSender.setUsername(emailCfg.getUsername());
            mailSender.setPassword(emailCfg.getPassword());
            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "true");
            props.put("mail.smtp.ssl.enable", emailCfg.isSslRequired());
            mailSender.setJavaMailProperties(props);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage, "utf-8");
            mailMessage.setFrom(emailCfg.getUsername());
            mailMessage.setTo(mailComponent.getTo());
            mailMessage.setSubject(mailComponent.getSubject());
            mailMessage.setText(mailComponent.getContent(), true);
            mailSender.send(mimeMessage);
        } else {
            // USING AUTH2 WITH GOOGLE
            this.gmailService.sendEmail(mailComponent.getContent(), mailComponent.getSubject(), mailComponent.getTo());
        }
    }

    public Template getTemplateByName(String templateName)
            throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {

        Template tempalte = configuration.getTemplate(templateName);
        return tempalte;
    }

    public String fillMailContent(MailComponent mailComponent, Template template)
            throws IOException, TemplateException {

        Map<String, Object> model = new HashMap<>();

        model.put("link", mailComponent.getLink());

        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

        StringBuffer content = new StringBuffer(html);

        return content.toString();
    }

    public EmailCfg findByCompanyId(Long id) {
        return emailCfgRepository.findByCompanyId(id);
    }

    public boolean existsByCompanyId(Long id) {
        return emailCfgRepository.existsByCompanyId(id);
    }

    public List<EmailCfg> findAll() {
        return emailCfgRepository.findAll();
    }

    public EmailCfg save(EmailCfg entity) {
        return emailCfgRepository.save(entity);
    }

    public Optional<EmailCfg> findById(Long aLong) {
        return emailCfgRepository.findById(aLong);
    }

    @Transactional
    public void deleteById(Long aLong) {
        emailCfgRepository.deleteById(aLong);
    }


    @Async
    public void testConnection(EmailCfg emailCfg, String to) throws MessagingException {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailCfg.getMailServer());
        mailSender.setPort(Math.toIntExact(emailCfg.getPortNumber()));
        mailSender.setUsername(emailCfg.getUsername());
        mailSender.setPassword(emailCfg.getPassword());
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        mailSender.setJavaMailProperties(props);
        props.put("mail.smtp.ssl.enable", emailCfg.isSslRequired());
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage, "utf-8");
        mailMessage.setFrom(emailCfg.getUsername());
        mailMessage.setTo(to);
        mailMessage.setSubject("Test connection");
        mailMessage.setText("This email is only for test connection", true);
        mailSender.testConnection();
        mailSender.send(mimeMessage);
    }


}
