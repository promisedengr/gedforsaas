package ma.project.GedforSaas.abstractSupperClasses;

import org.thymeleaf.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @author ELmoudene_Youssef
 */
public class AbstractService {

    public static Date convert(String date) {
        if(StringUtils.isEmpty(date)) return null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.parse(date);
        } catch (ParseException ex) {
            return null;
        }
    }

    public static java.sql.Date converte(java.util.Date date) {
        if (date != null) {
            return new java.sql.Date(date.getTime());
        } else {
            return null;
        }
    }

    public static String addConstraint(String beanAbrev, String atributeName, Object value , String operator, String operatorConstraint) {
        boolean condition = value != null;
        if (value != null && value.getClass().getSimpleName().equals("String")) {
            condition = condition && !value.equals("");
        }
        if (condition && operator.equals("LIKE")) {
            return " " + operatorConstraint + " LOWER(" + beanAbrev + "." + (atributeName) + ") " + operator + " '%" + value + "%'";
        } else if (condition) {
            if (value instanceof Boolean) {
                return " " + operatorConstraint+ " "+ beanAbrev + "." + atributeName + " " + operator + " '" + (value.equals(true)?1:0) + "'";
            } else {
                return " " + operatorConstraint + " " + beanAbrev + "." + atributeName + " " + operator + " '" + value + "'";
            }
        }
        return "";
    }



    public static String addConstraintMinMax(String beanAbrev, String atributeName, Object valueMin, Object valueMax) {
        String requette = "";
        if (valueMin != null) {
            requette += " AND " + beanAbrev + "." + atributeName + " >= '" + valueMin + "'";
        }
        if (valueMax != null) {
            requette += " AND " + beanAbrev + "." + atributeName + " <= '" + valueMax + "'";
        }
        return requette;
    }


    public static String init(Long  companyId, String entity) {
        String requette = "";
        if (companyId != null) {
            requette = "SELECT o FROM " +  entity + " o where o.company.id=" + companyId + " AND ";
        }

        return requette;
    }

    public static String addConstraintDate(String beanAbrev, String atributeName, Date value , String operator, String operatorConstraint) {
        return addConstraint(beanAbrev, atributeName,converte(value), operator , operatorConstraint);
    }

    public static String addConstraintDateWithStringValue(String beanAbrev, String atributeName, String operator, String value , String operatorConstraint) {
        return addConstraintDate(beanAbrev, atributeName, convert(value), operator, operatorConstraint);
    }

    public static String addConstraintMinMaxDate(String beanAbrev, String atributeName, Date valueMin, Date valueMax) {
        return addConstraintMinMax(beanAbrev, atributeName, converte(valueMin), converte(valueMax));
    }

    public static String addConstraintMinMaxDate(String beanAbrev, String atributeName, String valueMin, String valueMax) {
        return addConstraintMinMaxDate(beanAbrev, atributeName, convert(valueMin), convert(valueMax));
    }

}