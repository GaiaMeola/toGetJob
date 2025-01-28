package it.model.dao.concreteobjects;

import it.model.dao.abstractobjects.JobAnnouncementDao;
import it.model.entity.JobAnnouncement;

public class InMemoryJobAnnouncementDao implements JobAnnouncementDao {
    @Override
    public void createJobAnnouncement(JobAnnouncement jobAnnouncement) {

    }

    @Override
    public JobAnnouncement getJobAnnouncement(int id) {
        return null;
    }
}
