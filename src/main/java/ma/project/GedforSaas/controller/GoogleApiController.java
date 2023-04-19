package ma.project.GedforSaas.controller;

import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;

import ma.project.GedforSaas.service.CalendarService;
import ma.project.GedforSaas.service.GmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import org.springframework.web.bind.annotation.*;


@RestController
public class GoogleApiController {

    @Autowired
    private GmailService gmailService;
    @Autowired
    private CalendarService calendarService;

    @GetMapping("/list")
    public List<Message> getMessages() throws IOException {
        return gmailService.getMessages();
    }

    @GetMapping("/addSchedule")
    public void addSchedule() throws IOException {
        calendarService.addSchedule();
    }

//    @GetMapping("/send/{fromEmailAddress}/{toEmailAddress}")
//    public Message sendEmail(@PathVariable String fromEmailAddress, @PathVariable String toEmailAddress) throws MessagingException, IOException {
//        return gmailService.sendEmail(fromEmailAddress, toEmailAddress);
//    }

    @GetMapping("/googlesignin")
    public String googleSignIn(HttpServletResponse response) throws Exception {
        this.generateFlow();
        GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
        String redirectUrl = url.setRedirectUri(CALLBACK_URL).setAccessType("offline").build();
        response.sendRedirect(redirectUrl);
        System.out.println(url.getUserInfo());
        return url.getUserInfo();
    }

    @PostMapping("/oauth")
    public void saveAuthorizationCode(@RequestParam("code") String code) throws Exception {
        System.out.println(code);
        if (code != null) {
            saveToken(code);
        } else {
            throw new Exception("Code is null, try again");
        }
    }

    public void generateFlow() throws Exception {
        GoogleClientSecrets secrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(gdSecretKeys.getInputStream()));
        flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, secrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(credentialsFolder.getFile())).build();
    }

    private void saveToken(String code) throws Exception {
        GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(CALLBACK_URL).execute();
        flow.createAndStoreCredential(response, USER_IDENTIFIER_KEY);
    }


    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = new ArrayList<>(
            Arrays.asList(GmailScopes.MAIL_GOOGLE_COM,
                    GmailScopes.GMAIL_ADDONS_CURRENT_MESSAGE_ACTION,
                    GmailScopes.GMAIL_INSERT,
                    GmailScopes.GMAIL_COMPOSE,
                    GmailScopes.GMAIL_SEND
            ));

    private static final String USER_IDENTIFIER_KEY = "218177790525-ip7s8qgf4i3dm9e5u4neu2ttmvl6ndk5.apps.googleusercontent.com";
    private GoogleAuthorizationCodeFlow flow;


    @Value("${google.oauth.callback.uri}")
    private String CALLBACK_URL;
    @Value("${google.secret.key.path}")
    private Resource gdSecretKeys;
    @Value("${google.credentials.folder.path}")
    private Resource credentialsFolder;
}
