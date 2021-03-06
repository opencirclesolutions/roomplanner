package nl.ocs.roomplanner.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocs.dynamo.dao.BaseDao;
import com.ocs.dynamo.filter.Compare;
import com.ocs.dynamo.service.impl.BaseServiceImpl;
import com.ocs.dynamo.utils.DateUtils;

import nl.ocs.roomplanner.dao.MeetingDao;
import nl.ocs.roomplanner.domain.Employee;
import nl.ocs.roomplanner.domain.Location;
import nl.ocs.roomplanner.domain.Meeting;
import nl.ocs.roomplanner.domain.Organisation;
import nl.ocs.roomplanner.domain.PriorityType;
import nl.ocs.roomplanner.service.EmployeeService;
import nl.ocs.roomplanner.service.LocationService;
import nl.ocs.roomplanner.service.MeetingService;

@Service("meetingService")
@Transactional
public class MeetingServiceImpl extends BaseServiceImpl<Integer, Meeting> implements MeetingService {

    @Inject
    private MeetingDao meetingDao;

    @Inject
    private LocationService locationService;

    @Inject
    private EmployeeService employeeService;

    @Override
    protected BaseDao<Integer, Meeting> getDao() {
        return meetingDao;
    }

    @Override
    public void generateRandomMeetings(Organisation organisation, int numberOfMeetings) {

        Random random = new Random();
        List<Location> locations = locationService.find(new Compare.Equal("organisation", organisation));
        List<Employee> employees = employeeService.findAll();

        LocalDate startDate = LocalDate.now().plusDays(7);

        meetingDao.deleteForOrganisation(organisation);

        List<Meeting> meetings = new ArrayList<>();
        for (int i = 0; i < numberOfMeetings; i++) {
            Meeting meeting = new Meeting();
            meeting.setOrganisation(organisation);
            meeting.setDesiredLocation(locations.get(random.nextInt(locations.size())));
            meeting.setMeetingDate(startDate.plusDays(random.nextInt(7)));
            meeting.setPriority(PriorityType.values()[random.nextInt(4)]);

            meeting.setWhiteboard(random.nextBoolean());
            meeting.setVideoConferencing(false);
            meeting.setPhoneConferencing(false);
            meeting.setStartTime(LocalTime.of(12, 12));

            int attendeeCount = 2 + random.nextInt(3);
            for (int j = 0; j < attendeeCount; j++) {
                Employee emp = employees.get(random.nextInt(employees.size()));
                meeting.addAttendee(emp);
            }

            meeting.setDescription("Meeting " + (i + 1) + " (" + meeting.getDesiredLocation().getName() + ", "
                    + DateUtils.formatDate(meeting.getMeetingDate(), "dd-MM-yyyy") + ", "
                    + meeting.getNumberOfAttendees() + " attendees)");

            meetings.add(meeting);
        }
        meetingDao.save(meetings);
    }

    public List<Meeting> fetchByOrganisation(Organisation organisation) {
        return meetingDao.fetchByOrganisation(organisation);
    }

    @Override
    public void confirmMeetings(Organisation organisation) {
        List<Meeting> meetings = meetingDao.fetchUnconfirmedByOrganisation(organisation);
        for (Meeting m : meetings) {
            m.setConfirmed(true);
        }
    }

}
