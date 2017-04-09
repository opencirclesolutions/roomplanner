package nl.ocs.roomplanner.dao;

import java.util.List;

import nl.ocs.roomplanner.domain.Meeting;
import nl.ocs.roomplanner.domain.Organisation;

import com.ocs.dynamo.dao.BaseDao;

public interface MeetingDao extends BaseDao<Integer, Meeting> {

	public void deleteForOrganisation(Organisation organisation);

	public List<Meeting> fetchByOrganisation(Organisation organisation);

	public List<Meeting> fetchUnconfirmedByOrganisation(Organisation organisation);
}
