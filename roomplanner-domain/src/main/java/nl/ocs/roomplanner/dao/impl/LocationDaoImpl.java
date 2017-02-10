package nl.ocs.roomplanner.dao.impl;

import nl.ocs.roomplanner.dao.LocationDao;
import nl.ocs.roomplanner.domain.Location;
import nl.ocs.roomplanner.domain.QLocation;

import org.springframework.stereotype.Repository;

import com.mysema.query.types.path.EntityPathBase;
import com.ocs.dynamo.dao.impl.BaseDaoImpl;
import com.ocs.dynamo.dao.query.FetchJoinInformation;

@Repository("locationDao")
public class LocationDaoImpl extends BaseDaoImpl<Integer, Location> implements LocationDao {

	@Override
	public Class<Location> getEntityClass() {
		return Location.class;
	}

	@Override
	protected EntityPathBase<Location> getDslRoot() {
		return QLocation.location;
	}

	@Override
	protected FetchJoinInformation[] getFetchJoins() {
		return new FetchJoinInformation[] { new FetchJoinInformation("organisation") };
	}
}
