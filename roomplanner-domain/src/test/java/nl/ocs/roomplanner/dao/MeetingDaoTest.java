package nl.ocs.roomplanner.dao;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

import com.ocs.dynamo.test.BaseIntegrationTest;

import nl.ocs.roomplanner.domain.Meeting;
import nl.ocs.roomplanner.domain.PriorityType;
import nl.ocs.roomplanner.domain.QMeeting;

public class MeetingDaoTest extends BaseIntegrationTest {

    @Inject
    private MeetingDao meetingDao;

    @Test
    public void test() {

        Meeting m = new Meeting();
        m.setPriority(PriorityType.HIGH);
        m.setMeetingDate(LocalDate.now());
        m = meetingDao.save(m);

        List<Meeting> meetings = meetingDao.find(QMeeting.meeting.meetingDate.eq(LocalDate.now()));
        Assert.assertEquals(1, meetings.size());
    }

}
