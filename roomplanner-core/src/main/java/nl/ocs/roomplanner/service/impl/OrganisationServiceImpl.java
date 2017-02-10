package nl.ocs.roomplanner.service.impl;

import javax.inject.Inject;

import nl.ocs.roomplanner.dao.OrganisationDao;
import nl.ocs.roomplanner.domain.Organisation;
import nl.ocs.roomplanner.service.OrganisationService;

import org.springframework.stereotype.Service;

import com.ocs.dynamo.dao.BaseDao;
import com.ocs.dynamo.service.impl.BaseServiceImpl;

@Service("organisationService")
public class OrganisationServiceImpl extends BaseServiceImpl<Integer, Organisation> implements
		OrganisationService {

	@Inject
	private OrganisationDao organisationDao;

	@Override
	protected BaseDao<Integer, Organisation> getDao() {
		return organisationDao;
	}

}
