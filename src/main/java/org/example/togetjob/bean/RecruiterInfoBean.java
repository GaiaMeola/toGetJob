package org.example.togetjob.bean;

import java.util.List;

public class RecruiterInfoBean {

    private final List<String> companies;

    public RecruiterInfoBean(List<String> companies) {
        this.companies = companies;
    }

    public List<String> getCompanies() {
        return companies;
    }

}
