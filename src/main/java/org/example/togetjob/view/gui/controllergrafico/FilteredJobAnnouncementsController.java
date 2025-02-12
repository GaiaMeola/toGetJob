package org.example.togetjob.view.gui.controllergrafico;

import org.example.togetjob.bean.JobAnnouncementSearchBean;
import org.example.togetjob.view.gui.GUIContext;

public class FilteredJobAnnouncementsController {

    private GUIContext context;
    private JobAnnouncementSearchBean jobAnnouncementSearchBean;

    public void setContext(GUIContext context){
        this.context = context;
    }

    public void setJobAnnouncementSearchBean(JobAnnouncementSearchBean jobAnnouncementSearchBean){
        this.jobAnnouncementSearchBean = jobAnnouncementSearchBean;
    }


}
