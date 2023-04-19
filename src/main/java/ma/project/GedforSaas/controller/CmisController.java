package ma.project.GedforSaas.controller;

import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ma.project.GedforSaas.request.CmisClient;

import java.io.IOException;
import java.util.List;

@RestController
public class CmisController {

    @GetMapping("/get")
    public List<Folder> getObject() throws Exception {
        CmisClient cmisClient = new CmisClient();
        Session session = cmisClient.getSession("Elmoudene", "admin", "admin");
        return cmisClient.listTopFolder(session);
    }


    @RequestMapping(value = "/browser/", method = RequestMethod.POST)
    public ResponseEntity<?> setContent(@PathVariable("fileId") Long id, @RequestParam("objectId") String file)
            throws IOException {
        System.out.println("------------CREATE-------------------");
        System.out.println(file);
        return null;
    }

    @RequestMapping(value = "/files/{fileId}", method = RequestMethod.GET)
    public ResponseEntity<?> getContent(@PathVariable("fileId") Long id) {
        System.out.println("------------CREATE-------------------");
        System.out.println(id);
        return null;
    }
}
//http://localhost:8050/browser/
//1
//        /root?
//        objectId=7
//        &
//        cmisselector=versions
//        &filter=cmis%3
//        AbaseTypeId%2Ccmis%3
//        AcontentStreamFileName%2
//        Ccmis%3AcontentStreamLength%2Ccmis%3AcontentStreamMimeType%2Ccmis%3Aname%2Ccmis%3AobjectId%2Ccmis%3AobjectTypeId&includeAllowableActions=false&succinct=true