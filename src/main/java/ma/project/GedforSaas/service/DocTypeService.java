package ma.project.GedforSaas.service;


import com.google.gson.Gson;

import ma.project.GedforSaas.model.DocType;
import ma.project.GedforSaas.repository.DocTypeRepository;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class DocTypeService {

    public List<DocType> findAll() {
        return docTypeRepository.findAll();
    }

    public DocType save(DocType entity) {
        return docTypeRepository.save(entity);
    }

    public Optional<DocType> findById(Long id) {
        return docTypeRepository.findById(id);
    }


    public DocType findDoctypeById(Long id) throws Exception {
        Optional<DocType> docType = docTypeRepository.findById(id);
        if (docType.isPresent()){
            return docType.get();
        } else {
            throw new Exception("Not found !");
        }
    }

    @Transactional
    public void deleteById(Long id) {
        docTypeRepository.deleteById(id);
    }

    public List<DocType> findByCompanyId(Long id) {
        return docTypeRepository.findByCompanyId(id);
    }

    @Autowired
    private DocTypeRepository docTypeRepository;

    // TODO : get all prorietés of a document
    public Object getProprietesService(String company, Long id) throws Exception {
        HashMap<String , Object> proprietes = new HashMap<>();
        DocType docTypes = docTypeRepository.findProprietes(id);
        Object content = new Gson().fromJson(docTypes.getContentJson(),Object.class);
        if(docTypes.getCompany().getName().equals(company)){
            proprietes.put("proprietes", content);
        }else {
            proprietes.put("proprietes", "company is not exist !!!");
        }
        //return proprietes.get("proprietes");
        //ArrayList<Object> myContent = new ArrayList<>();
        //myContent.add(contentGson);
        //String label = "";
        //for(Object item : myContent){
            //for(Object attribut : item){
                //label = attribut.toString();
            //}
        //}

        return  proprietes.get("proprietes");
    }

    // TODO : get all types of a document
    public  List<Object> getTypesService(){
        return  docTypeRepository.getTypes();
    }
}
