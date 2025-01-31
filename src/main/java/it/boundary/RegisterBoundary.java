package it.boundary;

import it.bean.RecruiterInfoBean;
import it.bean.RegisterUserBean;
import it.bean.StudentInfoBean;
import it.controller.registration.AbstractRegisterController;
import it.controller.registration.RegisterRecruiterController;
import it.controller.registration.RegisterStudentController;

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
