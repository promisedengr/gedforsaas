package ma.project.GedforSaas.service;


import org.springframework.content.commons.property.PropertyPath;
import org.springframework.content.commons.repository.ContentStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.Serializable;

@Service
public class MyCmisServiceImpl implements ContentStore {


    @Override
    public Object setContent(Object o, InputStream inputStream) {
        System.out.println("===================TEST===================");
        return null;
    }

    @Override
    public Object setContent(Object o, PropertyPath propertyPath, InputStream inputStream) {
        System.out.println("===================TEST===================");

        return null;
    }

    @Override
    public Object setContent(Object o, Resource resource) {
        System.out.println("===================TEST===================");

        return null;
    }

    @Override
    public Object setContent(Object o, PropertyPath propertyPath, Resource resource) {
        System.out.println("===================TEST===================");

        return null;
    }

    @Override
    public Object unsetContent(Object o) {
        System.out.println("===================TEST===================");

        return null;
    }

    @Override
    public Object unsetContent(Object o, PropertyPath propertyPath) {        System.out.println("===================TEST===================");

        return null;
    }

    @Override
    public InputStream getContent(Object o) {
        System.out.println("===================TEST===================");

        return null;
    }

    @Override
    public InputStream getContent(Object o, PropertyPath propertyPath) {
        System.out.println("===================TEST===================");

        return null;
    }

    @Override
    public Resource getResource(Object o) {
        System.out.println("===================TEST===================");

        return null;
    }

    @Override
    public Resource getResource(Object o, PropertyPath propertyPath) {
        System.out.println("===================TEST===================");

        return null;
    }

    @Override
    public void associate(Object o, Serializable serializable) {
        System.out.println("===================TEST===================");


    }

    @Override
    public void associate(Object o, PropertyPath propertyPath, Serializable serializable) {
        System.out.println("===================TEST===================");


    }

    @Override
    public void unassociate(Object o) {
        System.out.println("===================TEST===================");


    }

    @Override
    public void unassociate(Object o, PropertyPath propertyPath) {
        System.out.println("===================TEST===================");


    }

    @Override
    public Resource getResource(Serializable serializable) {
        System.out.println("===================TEST===================");

        return null;
    }
}
