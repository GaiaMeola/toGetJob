package org.example.togetjob.boundary;

import org.example.togetjob.bean.RecruiterInfoBean;
import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.bean.StudentInfoBean;
import org.example.togetjob.controller.registration.AbstractRegisterController;
import org.example.togetjob.controller.registration.RegisterRecruiterController;
import org.example.togetjob.controller.registration.RegisterStudentController;

public class RegisterBoundary {

    public boolean registerUser(RegisterUserBean userBean, Object infoBean){
        AbstractRegisterController registerController;

        if ("student".equals(userBean.getRole().toLowerCase())) {
            registerController = new RegisterStudentController((StudentInfoBean) infoBean);
        } else if ("recruiter".equals(userBean.getRole().toLowerCase())) {
            registerController = new RegisterRecruiterController((RecruiterInfoBean) infoBean);
        } else {
            return false;
        }

        boolean success = registerController.registerUser(userBean);

        if(success) {
            return true;
        } else {
            return false;
        }

    }

}
