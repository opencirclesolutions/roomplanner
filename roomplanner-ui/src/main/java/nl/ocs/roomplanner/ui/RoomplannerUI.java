package nl.ocs.roomplanner.ui;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

import nl.ocs.roomplanner.domain.MeetingDTO;
import nl.ocs.roomplanner.domain.Organisation;
import nl.ocs.roomplanner.service.OrganisationService;

import org.drools.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoaderListener;

import com.ocs.dynamo.service.MessageService;
import com.ocs.dynamo.ui.Observer;
import com.ocs.dynamo.ui.Subject;
import com.ocs.dynamo.ui.component.DefaultHorizontalLayout;
import com.ocs.dynamo.ui.component.DefaultVerticalLayout;
import com.ocs.dynamo.ui.component.ErrorView;
import com.ocs.dynamo.ui.menu.MenuService;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.EnableVaadin;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Main class
 * 
 * @author bas.rutten
 * 
 */
@SpringUI()
@Theme("light")
@SuppressWarnings("serial")
@UIScope
// @Widgetset("nl.ocs.MyAppWidgetset")
public class RoomplannerUI extends UI implements Subject<MeetingDTO> {

	private boolean loggedIn = false;

	// the version number - retrieved from pom file via application.properties
	@Autowired
	@Qualifier("versionNumber")
	private String versionNumber;

	@Autowired
	private SpringViewProvider viewProvider;

	@Autowired
	private MessageService messageService;

	@Autowired
	private MenuService menuService;

	@Autowired
	private OrganisationService organisationService;

	private Navigator navigator;

	private Panel viewPanel;

	private MenuBar menu;

	// the main layout
	private VerticalLayout main;

	// the currently displayed layout
	private Layout selectedLayout;

	private List<Observer<MeetingDTO>> observers = new ArrayList<>();

	@WebListener
	public static class MyContextLoaderListener extends ContextLoaderListener {
		// needed to get the Spring integration going
	}

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = RoomplannerUI.class)
	public static class Servlet extends SpringVaadinServlet {
		// do nothing
	}

	@EnableVaadin
	@Configuration
	public static class MyConfiguration {
		// this is needed to kick off the Spring integration
	}

	/**
	 * Main method - sets up the application
	 */
	@Override
	protected void init(VaadinRequest request) {

		main = new VerticalLayout();
		setContent(main);

		// if (!loggedIn) {
		// Layout layout = buildLoginLayout();
		// main.replaceComponent(selectedLayout, layout);
		// selectedLayout = layout;
		// } else {
		Layout layout = buildLoggedInLayout();
		main.replaceComponent(selectedLayout, layout);
		selectedLayout = layout;
		// }
	}

	private Layout buildLoginLayout() {
		Layout result = new DefaultVerticalLayout(false, false);
		result.setHeight(1000, Unit.PERCENTAGE);

		result.setStyleName("login");

		GridLayout gridLayout = new GridLayout(9, 9);
		gridLayout.setSizeFull();
		result.addComponent(gridLayout);

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (!(j == 4 && i == 4)) {
					Label label = new Label("");
					label.setHeight(100, Unit.PIXELS);
					gridLayout.addComponent(label, i, j);
				}
			}
		}

		FormLayout loginForm = new FormLayout();
		loginForm.setMargin(true);
		loginForm.setSpacing(true);
		gridLayout.addComponent(loginForm, 4, 4);

		Label loginLabel = new Label("Log in");
		loginForm.addComponent(loginLabel);
		loginForm.setComponentAlignment(loginLabel, Alignment.MIDDLE_CENTER);
		loginForm.setId("loginForm");

		final TextField userName = new TextField("User name");
		loginForm.addComponent(userName);

		final PasswordField password = new PasswordField("Password");
		loginForm.addComponent(password);

		HorizontalLayout buttonBar = new DefaultHorizontalLayout();
		loginForm.addComponent(buttonBar);

		Button loginButton = new Button("Log in");
		loginButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				login(userName.getValue(), password.getValue());
			}
		});
		buttonBar.addComponent(loginButton);

		return result;
	}

	private void login(String userName, String password) {

		if (StringUtils.isEmpty(userName)) {
			Notification.show("User name is required", Notification.Type.ERROR_MESSAGE);
			return;
		}

		if (StringUtils.isEmpty(password)) {
			Notification.show("Password is required", Notification.Type.ERROR_MESSAGE);
			return;
		}

		if (!userName.equals(password)) {
			Notification.show("Your credentials are not correct", Notification.Type.ERROR_MESSAGE);
			return;
		}

		Layout layout = buildLoggedInLayout();
		main.replaceComponent(selectedLayout, layout);
		selectedLayout = layout;
	}

	private Layout buildLoggedInLayout() {
		Layout result = new DefaultVerticalLayout(false, false);

		HorizontalLayout banner = new DefaultHorizontalLayout(true, false, false);
		banner.setId("banner");
		banner.setSizeFull();
		result.addComponent(banner);

		Image image = new Image(null, new ThemeResource("../../images/ocs.png"));
		banner.addComponent(image);
		banner.setComponentAlignment(image, Alignment.MIDDLE_LEFT);

		Image horseHead = new Image(null, new ThemeResource("../../images/horsehead.jpg"));
		banner.addComponent(horseHead);
		banner.setComponentAlignment(horseHead, Alignment.MIDDLE_LEFT);

		// the center block
		HorizontalLayout hCenter = new HorizontalLayout();
		banner.addComponent(hCenter);

		// FormLayout form = new FormLayout();
		// form.setSizeUndefined();
		//
		// EntityModel<Organisation> orgModel =
		// ServiceLocator.getEntityModelFactory().getModel(
		// Organisation.class);
		// DefaultEntityComboBox<Integer, Organisation> orgs = new
		// DefaultEntityComboBox<>(orgModel,
		// null, organisationService);
		// orgs.setPrimaryStyleName("");
		// orgs.setWidth(500, Unit.PIXELS);
		// form.addComponent(orgs);
		//
		// orgs.addValueChangeListener(new ValueChangeListener() {
		//
		// @Override
		// public void valueChange(ValueChangeEvent event) {
		// VaadinSession.getCurrent().setAttribute("organisation",
		// event.getProperty().getValue());
		// navigator.navigateTo(Views.LOCATIONS_VIEW);
		// }
		// });

		// HorizontalLayout buttonBar = new DefaultHorizontalLayout();
		// form.addComponent(buttonBar);
		//

		VerticalLayout center = new DefaultVerticalLayout(true, false);
		hCenter.addComponent(center);
		hCenter.setComponentAlignment(center, Alignment.MIDDLE_LEFT);

		// Label titleLabel = new Label("OCS Room Planner Prototype");
		// center.addComponent(titleLabel);

		// navigator part
		VerticalLayout viewLayout = new VerticalLayout();
		viewPanel = new Panel();
		viewPanel.setImmediate(Boolean.TRUE);
		viewPanel.setContent(viewLayout);

		// create a state manager and set its default view
		// this is done to circumvent a bug with the view being created twice if
		// navigator.navigateTo is called directly
		Navigator.UriFragmentManager stateManager = new com.vaadin.navigator.Navigator.UriFragmentManager(
		        this.getPage());
		stateManager.setState(Views.LOCATIONS_VIEW);

		// create the navigator
		navigator = new Navigator(this, stateManager, new Navigator.SingleComponentContainerViewDisplay(viewPanel));
		UI.getCurrent().setNavigator(navigator);
		navigator.addProvider(viewProvider);
		navigator.setErrorView(new ErrorView());

		// build the menu
		menu = menuService.constructMenu("roomplanner.menu", navigator);
		result.addComponent(menu);
		result.addComponent(viewPanel);

		List<Organisation> allOrgs = organisationService.findAll();
		if (!allOrgs.isEmpty()) {
			// orgs.setValue(allOrgs.get(3));
			VaadinSession.getCurrent().setAttribute("organisation", allOrgs.get(3));
			navigator.navigateTo(Views.LOCATIONS_VIEW);
		}
		return result;
	}

	@Override
	public void register(Observer<MeetingDTO> observer) {
		observers.add(observer);
	}

	@Override
	public void unregister(Observer<MeetingDTO> observer) {
		observers.remove(observer);
	}

	@Override
	public void unregisterAll() {
		observers.clear();
	}

	@Override
	public void notifyObservers(MeetingDTO entity) {
		for (Observer<MeetingDTO> o : observers) {
			o.notify(entity);
		}
	}
}
