package org.example.togetjob.testing;

import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.exceptions.UsernameTakeException;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.boundary.RegisterBoundary;

public class Testing {

    private final RegisterBoundary registerBoundary = new RegisterBoundary();

    public void testUsername(){

       RegisterUserBean userBean = new RegisterUserBean();
       userBean.setUsername("alreadyExisting");
       userBean.setEmail("newEmail@gmail.com");
       userBean.setName("Mario");
       userBean.setSurname("Rossi");
       userBean.setRole("Recruiter");
       userBean.setPassword("password");

       try {
           boolean registrationSuccess = registerBoundary.registerUser(userBean, userBean);
       } catch (UsernameTakeException e) {
           Printer.print("test successful");
       }
   }

}
