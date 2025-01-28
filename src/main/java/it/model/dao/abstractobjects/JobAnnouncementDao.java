package it.model.dao.abstractobjects;

import it.model.entity.JobAnnouncement;

public interface JobAnnouncementDao {

    void createJobAnnouncement(JobAnnouncement jobAnnouncement);
    JobAnnouncement getJobAnnouncement(int id);
}
