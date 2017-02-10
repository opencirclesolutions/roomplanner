package nl.ocs.roomplanner.dao.impl;

import java.util.List;

import nl.ocs.roomplanner.dao.MeetingDao;
import nl.ocs.roomplanner.domain.Meeting;
import nl.ocs.roomplanner.domain.Organisation;
import nl.ocs.roomplanner.domain.QMeeting;
import nl.ocs.roomplanner.domain.QRoom;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.path.EntityPathBase;
import com.ocs.dynamo.dao.impl.BaseDaoImpl;
import com.ocs.dynamo.dao.query.FetchJoinInformation;

@Repository("meetingDao")
public class MeetingDaoImpl extends BaseDaoImpl<Integer, Meeting> implements MeetingDao {

	private static QMeeting qMeeting = QMeeting.meeting;

	@Override
	public Class<Meeting> getEntityClass() {
		return Meeting.class;
	}

	@Override
	protected EntityPathBase<Meeting> getDslRoot() {
		return qMeeting;
	}

	@Override
	protected FetchJoinInformation[] getFetchJoins() {
		return new FetchJoinInformation[] { new FetchJoinInformation("attendees"),
				new FetchJoinInformation("desiredLocation"),
				new FetchJoinInformation("assignedLocation"), new FetchJoinInformation("room"),
				new FetchJoinInformation("organisation") };
	}

	@Override
	public void deleteForOrganisation(Organisation organisation) {
		JPADeleteClause clause = new JPADeleteClause(getEntityManager(), qMeeting);
		clause.where(qMeeting.organisation.eq(organisation));
		clause.execute();

	}

	@Override
	public List<Meeting> fetchByOrganisation(Organisation organisation) {
		JPAQuery query = createQuery();
		QRoom qRoom = QRoom.room;

		query.leftJoin(qMeeting.room, qRoom).fetch().leftJoin(qRoom.location).fetch()
				.leftJoin(qMeeting.desiredLocation).fetch().leftJoin(qMeeting.attendees).fetch();

		query.where(qMeeting.organisation.eq(organisation));
		return query.distinct().list(qMeeting);
	}

	@Override
	public List<Meeting> fetchUnconfirmedByOrganisation(Organisation organisation) {
		JPAQuery query = createQuery();
		query.join(qMeeting.room).fetch().join(qMeeting.desiredLocation).fetch()
				.join(qMeeting.attendees).fetch();

		query.where(qMeeting.organisation.eq(organisation), qMeeting.confirmed.isFalse());
		return query.distinct().list(qMeeting);
	}
}
