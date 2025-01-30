package it.bean;

import java.util.List;

public class RecruiterInfoBean {

    private List<String> companies;

    public RecruiterInfoBean(List<String> companies) {
        this.companies = companies;
    }

    public List<String> getCompanies() {
        return companies;
    }

    public void setCompanies(List<String> companies) {
        this.companies = companies;
    }
}
