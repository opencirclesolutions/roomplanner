package nl.ocs.roomplanner.service.impl;

import javax.inject.Inject;

import nl.ocs.roomplanner.dao.EmployeeDao;
import nl.ocs.roomplanner.domain.Employee;
import nl.ocs.roomplanner.service.EmployeeService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocs.dynamo.dao.BaseDao;
import com.ocs.dynamo.service.impl.BaseServiceImpl;

@Transactional
@Service("employeeService")
public class EmployeeServiceImpl extends BaseServiceImpl<Integer,Employee> implements EmployeeService {

	@Inject
	private EmployeeDao employeeDao;
	
	@Override
	protected BaseDao<Integer, Employee> getDao() {
		return employeeDao;
	}

}
