package ma.project.GedforSaas.configuration;

// import ma.project.GedforSaas.model.Company;
import ma.project.GedforSaas.model.Company;

import ma.project.GedforSaas.model.Document;
import ma.project.GedforSaas.model.Folder;
import ma.project.GedforSaas.repository.DocumentRepository;
import ma.project.GedforSaas.repository.FolderRepository;
import ma.project.GedforSaas.service.UserCompanyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.content.cmis.CmisNavigationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CmisNavigationConfig {
    @Autowired
    private UserCompanyService userCompanyService;

    @Bean
    public CmisNavigationService cmisNavigationService(FolderRepository folders, DocumentRepository docs) {

        return new CmisNavigationService<Folder>() {
            @Override
            public List getChildren(Folder parent) {
                List<Folder> folderChildern = new ArrayList<>();
                List<Document> documentChildren = new ArrayList<>();

                List<Object> children = new ArrayList<>();
                if (parent == null) {
                    System.out.println("******************************** ROOT *************************************");
                    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    String username;
                    if (principal instanceof UserDetails) {
                        username = ((UserDetails) principal).getUsername();
                    } else {
                        username = principal.toString();
                    }
                    System.out.println("USERNAME ::: " + username);
                    List<Company> companies = userCompanyService.getCompanyOfUser(username);
                    System.out.println("******************************** ROOT *************************************");
                    for (Company p : companies
                    ) {
                        folderChildern.addAll(folders.findByCompanyIdAndTrashAndParentFolderId(p.getId(), false, null));
                        documentChildren.addAll(docs.findByCompanyId(p.getId()));
                    }
                } else {
                    System.out.println("FOLDER  NAME :" + parent.getName());
                    System.out.println("FOLDER  ID :" + parent.getId());
                    System.out.println(parent);
                    folderChildern = folders.findByFolderId(parent.getId());
                    documentChildren = docs.findByFolderId(parent.getId());
                }

                children.addAll(folderChildern);
                children.addAll(documentChildren);
                return children;
            }
        };
    }


}
