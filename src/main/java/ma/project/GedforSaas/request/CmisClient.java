package ma.project.GedforSaas.request;

import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.Action;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisUnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ma.project.GedforSaas.utils.CMISSession;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CmisClient {
    private static Map<String, Session> connections = new ConcurrentHashMap<>();
    static Logger LOGGER = LoggerFactory.getLogger(CMISSession.class);

    public CmisClient() {
    }

    public Session getSession(String connectionName, String username, String pwd) throws Exception {
        Session session = connections.get(connectionName);

        if (session == null) {
            System.out.println("Not connected, creating new connection to" +
                    " Alfresco with the connection id (" + connectionName + ")");

            SessionFactory factory = SessionFactoryImpl.newInstance();
            Map<String, String> parameter = new HashMap<String, String>();

            // user credentials
            parameter.put(SessionParameter.USER, username);
            parameter.put(SessionParameter.PASSWORD, pwd);

            // connection settings
            parameter.put(SessionParameter.ATOMPUB_URL, "http://127.0.0.1:8080/alfresco/cmisatom");
//            parameter.put(SessionParameter.ATOMPUB_URL, "http://localhost:8050/alfresco/cmisatom");
            parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
            //create repositories
            List<Repository> repositories = factory.getRepositories(parameter);
            // create session
            session = repositories.get(0).createSession();
            LOGGER.info("******* CMIS Session created successfully *******");
            System.out.println("Repository Name: " + session.getRepositoryInfo().getName());
            System.out.println("Repository ID: " + session.getRepositoryInfo().getId());
            System.out.println("CMIS Version: " + session.getRepositoryInfo().getCmisVersion());
            session.getDefaultContext().setCacheEnabled(false);
        } else {
            System.out.println("Already connected to Alfresco with the " +
                    "connection id (" + connectionName + ")");
        }
        System.out.println(session.getRootFolder());
        return session;
    }

    public List<Folder> listTopFolder(Session session) throws CloneNotSupportedException {
        List<Folder> folderList = new LinkedList<>();
        Folder root = session.getRootFolder();
        int i = 0;
        ItemIterable<CmisObject> contentItems = root.getChildren();
        for (CmisObject contentItem : contentItems) {
            if (contentItem instanceof Document) {
                Document docMetadata = (Document) contentItem;
                ContentStream docContent = docMetadata.getContentStream();

                System.out.println("================DOCS================");
                System.out.println(docMetadata.getName() +
                        " [ size=" + docContent.getLength() + "]," +
                        "[Mimetype=" + docContent.getMimeType() + "]," +
                        "[type=" + docMetadata.getType().getDisplayName() + "]");
                System.out.println("=====================================");
            } else {
                System.out.println("=================FOLDERS===============");
                Folder folder = (Folder) contentItem;
                System.out.println(folder.getName() +
                        " [ ID=" + folder.getId() + "]," +
                        " [ Parent=" + folder.getParentId() + "]," +
                        "[Path=" + folder.getPath() + "],");
                System.out.println("======================================");

                folderList.add(i, folder);
            }
            i++;
        }
        System.out.println(folderList.size());
        return folderList;
    }

    public Folder createFolder(ma.project.GedforSaas.model.Folder folder, Session session) {
        Folder parentFolder = session.getRootFolder();

        // Make sure the user is allowed to create a folder
        // under the root folder
        if (parentFolder.getAllowableActions().getAllowableActions().
                contains(Action.CAN_CREATE_FOLDER) == false) {
            throw new CmisUnauthorizedException(
                    "Current user does not have permission to create a " +
                            "sub-folder in " + parentFolder.getPath());
        }

        // Check if folder already exist, if not create it
        Folder newFolder = (Folder) getObject(session, parentFolder, folder.getName());
        if (newFolder == null) {
            Map<String, Object> newFolderProps = new HashMap<String, Object>();
            newFolderProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
            newFolderProps.put(PropertyIds.NAME, folder.getName());
            newFolderProps.put(PropertyIds.CREATED_BY, folder.getCreatedBy());
            newFolderProps.put(PropertyIds.CREATION_DATE, folder.getDateCreated());
            newFolderProps.put(PropertyIds.LAST_MODIFIED_BY, folder.getLastModifiedBy());
            newFolderProps.put(PropertyIds.LAST_MODIFICATION_DATE, folder.getDateCreated());


            newFolder = parentFolder.createFolder(newFolderProps);
            System.out.println("Created new folder: " + newFolder.getPath() +
                    " [creator=" + newFolder.getCreatedBy() + "][created=" +
                    date2String(newFolder.getCreationDate().getTime()) + "]");
        } else {
            System.out.println("Folder already exist: " + newFolder.getPath());
        }

        return newFolder;
    }

    private CmisObject getObject(Session session, Folder parentFolder, String objectName) {
        CmisObject object = null;
        try {
            String path2Object = parentFolder.getPath();
            if (!path2Object.endsWith("/")) {
                path2Object += "/";
            }
            path2Object += objectName;
            object = session.getObjectByPath(path2Object);
        } catch (CmisObjectNotFoundException nfe0) {
            // Nothing to do, object does not exist
        }

        return object;
    }

    private String date2String(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").format(date);
    }


    public Document createDocument(String documentName, Session session, Folder parentFolder)
            throws IOException {

        // Make sure the user is allowed to create a document
        // in the passed in folder
        if (parentFolder.getAllowableActions().getAllowableActions().
                contains(Action.CAN_CREATE_DOCUMENT) == false) {
            throw new CmisUnauthorizedException("Current user does not " +
                    "have permission to create a document in " +
                    parentFolder.getPath());
        }

        // Check if document already exist, if not create it
        Document newDocument = (Document) getObject(session, parentFolder, documentName);
        if (newDocument == null) {
            // Setup document metadata
            Map<String, Object> newDocumentProps =
                    new HashMap<String, Object>();
            newDocumentProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
            newDocumentProps.put(PropertyIds.NAME, documentName);

            // Setup document content
            String mimetype = "text/plain; charset=UTF-8";
            String documentText = "This is a test document!";
            byte[] bytes = documentText.getBytes("UTF-8");
            ByteArrayInputStream input = new ByteArrayInputStream(bytes);
            ContentStream contentStream = session.getObjectFactory().createContentStream(
                    documentName, bytes.length, mimetype, input);

            // Create versioned document object
            newDocument = parentFolder.createDocument(
                    newDocumentProps, contentStream, VersioningState.MAJOR);

            System.out.println("Created new document: " +
                    getDocumentPath(newDocument) + " [version=" +
                    newDocument.getVersionLabel() + "][creator=" +
                    newDocument.getCreatedBy() + "][created=" +
                    date2String(newDocument.getCreationDate().getTime()) + "]");
        } else {
            System.out.println("Document already exist: " + getDocumentPath(newDocument));
        }

        return newDocument;
    }


    private String getDocumentPath(Document document) {
        String path2Doc = getParentFolderPath(document);
        if (!path2Doc.endsWith("/")) {
            path2Doc += "/";
        }

        path2Doc += document.getName();

        return path2Doc;
    }


    private String getParentFolderPath(Document document) {
        Folder parentFolder = getDocumentParentFolder(document);
        return parentFolder == null ? "Un-filed" : parentFolder.getPath();
    }


    private Folder getDocumentParentFolder(Document document) {
        // Get all the parent folders (could be more than one if multi-filed)
        List<Folder> parentFolders = document.getParents();
        // Grab the first parent folder
        if (parentFolders.size() > 0) {
            if (parentFolders.size() > 1) {
                System.out.println("The " + document.getName() +
                        " has more than one parent folder, it is multi-filed");
            }

            return parentFolders.get(0);
        } else {
            System.out.println("Document " + document.getName() +
                    " is un-filed and does not have a parent folder");

            return null;
        }
    }

}