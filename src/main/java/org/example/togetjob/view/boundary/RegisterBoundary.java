package org.example.togetjob.view.boundary;

import org.example.togetjob.bean.RecruiterInfoBean;
import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.bean.StudentInfoBean;
import org.example.togetjob.controller.registration.AbstractRegisterController;
import org.example.togetjob.controller.registration.RegisterRecruiterController;
import org.example.togetjob.controller.registration.RegisterStudentController;

public class RegisterBoundary {
    //polymorphism
    AbstractRegisterController registerController;

    public boolean registerUser(RegisterUserBean userBean, Object infoBean){

        if ("student".equalsIgnoreCase(userBean.getRole())) {
            registerController = new RegisterStudentController((StudentInfoBean) infoBean);
        } else if ("recruiter".equalsIgnoreCase(userBean.getRole())) {
            registerController = new RegisterRecruiterController((RecruiterInfoBean) infoBean);
        } else {
            return false;
        }

       return registerController.registerUser(userBean);

    }

}
