package nl.ocs.roomplanner.service.impl;

import nl.ocs.roomplanner.dao.LocationDao;
import nl.ocs.roomplanner.domain.Location;
import nl.ocs.roomplanner.service.LocationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocs.dynamo.dao.BaseDao;
import com.ocs.dynamo.service.impl.BaseServiceImpl;

@Service("locationService")
@Transactional
public class LocationServiceImpl extends BaseServiceImpl<Integer, Location> implements
		LocationService {

	@Autowired
	private LocationDao locationDao;

	@Override
	protected BaseDao<Integer, Location> getDao() {
		return locationDao;
	}

}
