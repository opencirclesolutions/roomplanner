package nl.ocs.roomplanner.dao.impl;

import nl.ocs.roomplanner.dao.OrganisationDao;
import nl.ocs.roomplanner.domain.Organisation;
import nl.ocs.roomplanner.domain.QOrganisation;

import org.springframework.stereotype.Repository;

import com.mysema.query.types.path.EntityPathBase;
import com.ocs.dynamo.dao.impl.BaseDaoImpl;

@Repository("organisationDao")
public class OrganisationDaoImpl extends BaseDaoImpl<Integer, Organisation> implements
		OrganisationDao {

	private static final QOrganisation qOrganisation = QOrganisation.organisation;

	@Override
	public Class<Organisation> getEntityClass() {
		return Organisation.class;
	}

	@Override
	protected EntityPathBase<Organisation> getDslRoot() {
		return qOrganisation;
	}
}
