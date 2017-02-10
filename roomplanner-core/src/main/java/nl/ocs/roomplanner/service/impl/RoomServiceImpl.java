package nl.ocs.roomplanner.service.impl;

import javax.inject.Inject;

import nl.ocs.roomplanner.dao.RoomDao;
import nl.ocs.roomplanner.domain.Room;
import nl.ocs.roomplanner.service.RoomService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocs.dynamo.dao.BaseDao;
import com.ocs.dynamo.service.impl.BaseServiceImpl;

@Transactional
@Service("roomService")
public class RoomServiceImpl extends BaseServiceImpl<Integer, Room> implements RoomService {

	@Inject
	private RoomDao roomDao;

	@Override
	protected BaseDao<Integer, Room> getDao() {
		return roomDao;
	}
	
	
}
