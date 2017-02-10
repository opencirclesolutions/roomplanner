package nl.ocs.roomplanner.dao.impl;


import nl.ocs.roomplanner.dao.EmployeeDao;
import nl.ocs.roomplanner.domain.Employee;
import nl.ocs.roomplanner.domain.QEmployee;

import org.springframework.stereotype.Repository;

import com.mysema.query.types.path.EntityPathBase;
import com.ocs.dynamo.dao.impl.BaseDaoImpl;
import com.ocs.dynamo.dao.query.FetchJoinInformation;

@Repository("employeeDao")
public class EmployeeDaoImpl extends BaseDaoImpl<Integer, Employee> implements EmployeeDao {

	private static QEmployee qEmployee = QEmployee.employee;

	@Override
	public Class<Employee> getEntityClass() {
		return Employee.class;
	}

	@Override
	protected EntityPathBase<Employee> getDslRoot() {
		return qEmployee;
	}

	@Override
	protected FetchJoinInformation[] getFetchJoins() {
		return new FetchJoinInformation[] {new FetchJoinInformation("children")};
	}

}
