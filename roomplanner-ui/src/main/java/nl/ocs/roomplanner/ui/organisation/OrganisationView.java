package nl.ocs.roomplanner.ui.organisation;

import nl.ocs.roomplanner.domain.Employee;
import nl.ocs.roomplanner.domain.Organisation;
import nl.ocs.roomplanner.service.EmployeeService;
import nl.ocs.roomplanner.service.LocationService;
import nl.ocs.roomplanner.service.OrganisationService;
import nl.ocs.roomplanner.service.RoomService;
import nl.ocs.roomplanner.ui.Views;

import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.dynamo.dao.query.FetchJoinInformation;
import com.ocs.dynamo.domain.model.EntityModelFactory;
import com.ocs.dynamo.service.MessageService;
import com.ocs.dynamo.ui.component.DefaultVerticalLayout;
import com.ocs.dynamo.ui.composite.form.FormOptions;
import com.ocs.dynamo.ui.composite.layout.SimpleEditLayout;
import com.ocs.dynamo.ui.composite.table.ModelBasedTree;
import com.ocs.dynamo.ui.container.QueryType;
import com.ocs.dynamo.ui.container.ServiceContainer;
import com.ocs.dynamo.ui.view.BaseView;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = Views.ORGANISATION_VIEW)
@UIScope
@SuppressWarnings("serial")
public class OrganisationView extends BaseView {

	@Autowired
	private OrganisationService organisationService;

	@Autowired
	private EntityModelFactory entityModelFactory;

	@Autowired
	private MessageService messageService;

	@Autowired
	private LocationService locationService;

	@Autowired
	private RoomService roomService;

	@Autowired
	private EmployeeService employeeService;

	private Layout mainLayout;

	@Override
	public void enter(ViewChangeEvent event) {
		VerticalLayout main = new DefaultVerticalLayout(true, true);

		mainLayout = new VerticalLayout();
		main.addComponent(mainLayout);

		setCompositionRoot(main);

		FormOptions fo = new FormOptions();
		fo.setShowSearchDialogButton(true);
		fo.setShowRemoveButton(true);

		final Organisation organisation = (Organisation) VaadinSession.getCurrent().getAttribute(
				"organisation");

		SimpleEditLayout<Integer, Organisation> layout = new SimpleEditLayout<Integer, Organisation>(
				organisation, organisationService, entityModelFactory.getModel(Organisation.class),
				new FormOptions());
		mainLayout.addComponent(layout);

		// List<BaseService<?, ?>> services = new ArrayList<>();
		// services.add(organisationService);
		// services.add(locationService);
		// services.add(roomService);

		// ModelBasedHierarchicalContainer<Organisation> container = new
		// ModelBasedHierarchicalContainer<Organisation>(
		// messageService, entityModelFactory.getModel(Organisation.class),
		// services,
		// new HierarchicalFetchJoinInformation[0]);

		// BeanItemContainer<Employee> employeeContainer = new
		// BeanItemContainer<>(Employee.class);
		// employeeContainer.addAll(employeeService.findAll());

		ServiceContainer<Integer, Employee> employeeContainer = new ServiceContainer<Integer, Employee>(
				employeeService, false, 10, QueryType.ID_BASED, new FetchJoinInformation[0]);

		// RecursiveContainer<Integer, Employee> employeeContainer = new
		// RecursiveContainer<Integer, Employee>(
		// entityModelFactory.getModel(Employee.class),
		// employeeService.findAll());
		// List<Employee> employees = employeeService.findAll();

		ModelBasedTree<Integer, Employee> tree = new ModelBasedTree<Integer, Employee>(
				employeeContainer, entityModelFactory.getModel(Employee.class)) {
			@Override
			protected Employee determineParent(Employee child) {
				return child.getParent();
			}
		};

		mainLayout.addComponent(tree);
	}
}
