package nl.ocs.roomplanner.dao.impl;

import nl.ocs.roomplanner.dao.RoomDao;
import nl.ocs.roomplanner.domain.QRoom;
import nl.ocs.roomplanner.domain.Room;

import org.springframework.stereotype.Repository;

import com.mysema.query.types.path.EntityPathBase;
import com.ocs.dynamo.dao.impl.BaseDaoImpl;
import com.ocs.dynamo.dao.query.FetchJoinInformation;

@Repository("roomDao")
public class RoomDaoImpl extends BaseDaoImpl<Integer, Room> implements RoomDao {

	private static final QRoom qRoom = QRoom.room;

	@Override
	public Class<Room> getEntityClass() {
		return Room.class;
	}

	@Override
	protected EntityPathBase<Room> getDslRoot() {
		return qRoom;
	}

	@Override
	protected FetchJoinInformation[] getFetchJoins() {
		return new FetchJoinInformation[] { new FetchJoinInformation("location"),
				new FetchJoinInformation("organisation") };
	}
}
