package org.example.togetjob.bean;

import java.util.List;

public class RecruiterInfoBean {

    private List<String> companies;

    public RecruiterInfoBean() {
        /* builder */
    }

    public void setCompanies(List<String> companies){
        this.companies = companies;
    }

    public List<String> getCompanies() {
        return companies;
    }

}
