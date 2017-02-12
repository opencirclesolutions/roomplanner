package nl.ocs.roomplanner.ui.employee;

import nl.ocs.roomplanner.domain.Employee;
import nl.ocs.roomplanner.service.EmployeeService;
import nl.ocs.roomplanner.ui.Views;

import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.dynamo.ui.composite.form.FormOptions;
import com.ocs.dynamo.ui.composite.layout.TabularEditLayout;
import com.ocs.dynamo.ui.view.LazyBaseView;
import com.vaadin.data.sort.SortOrder;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;

@UIScope
@SpringView(name = Views.EMPLOYEE_VIEW)
@SuppressWarnings("serial")
public class EmployeesView extends LazyBaseView {

	@Autowired
	private EmployeeService employeeService;

	private TabularEditLayout<Integer, Employee> employeeLayout;

	@Override
	public Component build() {

		FormOptions options = new FormOptions().setShowRemoveButton(true);

		TabularEditLayout<Integer, Employee> employeeLayout = new TabularEditLayout<Integer, Employee>(employeeService,
		        getModelFactory().getModel(Employee.class), options, new SortOrder("lastName", SortDirection.ASCENDING)) {
		};
		employeeLayout.addSortOrder(new SortOrder("firstName", SortDirection.ASCENDING));
		return employeeLayout;
	}

	@Override
	protected void refresh() {
		employeeLayout.reload();
	}
}
