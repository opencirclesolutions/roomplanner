package nl.ocs.roomplanner.ui.employee;

import nl.ocs.roomplanner.domain.Employee;
import nl.ocs.roomplanner.service.EmployeeService;
import nl.ocs.roomplanner.ui.Views;

import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.dynamo.ui.component.DefaultVerticalLayout;
import com.ocs.dynamo.ui.composite.form.FormOptions;
import com.ocs.dynamo.ui.composite.layout.TabularEditLayout;
import com.ocs.dynamo.ui.view.BaseView;
import com.vaadin.data.sort.SortOrder;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = Views.EMPLOYEE_VIEW)
@UIScope
@SuppressWarnings("serial")
public class EmployeesView extends BaseView {

    private Layout mainLayout;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public void enter(ViewChangeEvent event) {
        VerticalLayout main = new DefaultVerticalLayout(true, true);

        mainLayout = new VerticalLayout();
        main.addComponent(mainLayout);

        setCompositionRoot(main);

        FormOptions options = new FormOptions();
        options.setShowRemoveButton(true);

        TabularEditLayout<Integer, Employee> employeeLayout = new TabularEditLayout<Integer, Employee>(
                employeeService, getModelFactory().getModel(Employee.class), options,
                new SortOrder("lastName", SortDirection.ASCENDING)) {
        };
        employeeLayout.addSortOrder(new SortOrder("firstName", SortDirection.ASCENDING));
        mainLayout.addComponent(employeeLayout);
    }

}
