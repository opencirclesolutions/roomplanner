package nl.ocs.roomplanner.service;

import java.util.List;

import nl.ocs.roomplanner.domain.Meeting;
import nl.ocs.roomplanner.domain.Organisation;

import com.ocs.dynamo.service.BaseService;

public interface MeetingService extends BaseService<Integer, Meeting> {

	public void generateRandomMeetings(Organisation organisation, int number);

	public List<Meeting> fetchByOrganisation(Organisation organisation);

	public void confirmMeetings(Organisation organisation);
}
