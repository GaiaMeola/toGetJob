package org.example.togetjob.bean;

import org.example.togetjob.exceptions.InvalidCompanyListException;
import java.util.ArrayList;
import java.util.List;

public class RecruiterInfoBean {

    private List<String> companies;

    public RecruiterInfoBean() {
        /* builder */
    }

    public void setCompanies(List<String> companiesList) throws InvalidCompanyListException {
        if (companiesList == null || companiesList.isEmpty()) {
            throw new InvalidCompanyListException("You must enter at least one company.");
        }

        List<String> mutableList = new ArrayList<>(companiesList);
        mutableList.replaceAll(String::trim);

        if (mutableList.stream().allMatch(String::isEmpty)) {
            throw new InvalidCompanyListException("You must enter at least one company.");
        }

        this.companies = mutableList;
    }

    public List<String> getCompanies() {
        return companies;
    }
}
