package nl.ocs.roomplanner.ui;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoaderListener;

import com.ocs.dynamo.ui.BaseUI;
import com.ocs.dynamo.ui.Observer;
import com.ocs.dynamo.ui.Subject;
import com.ocs.dynamo.ui.component.DefaultHorizontalLayout;
import com.ocs.dynamo.ui.component.DefaultVerticalLayout;
import com.ocs.dynamo.ui.component.ErrorView;
import com.ocs.dynamo.ui.menu.MenuService;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.EnableVaadin;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import nl.ocs.roomplanner.domain.MeetingDTO;
import nl.ocs.roomplanner.domain.Organisation;
import nl.ocs.roomplanner.service.OrganisationService;

/**
 * Main class
 * 
 * @author bas.rutten
 * 
 */
@UIScope
@SpringUI()
@Theme("light")
@Widgetset(value = "com.ocs.dynamo.DynamoWidgetSet")
public class RoomplannerUI extends BaseUI implements Subject<MeetingDTO> {

    private static final long serialVersionUID = 7627794125037816693L;

    @Autowired
    @Qualifier("versionNumber")
    private String versionNumber;

    @Inject
    private SpringViewProvider viewProvider;

    @Inject
    private MenuService menuService;

    @Inject
    private OrganisationService organisationService;

    private Panel viewPanel;

    private MenuBar menu;

    // the main layout
    private VerticalLayout main;

    // the currently displayed layout
    private Layout selectedLayout;

    private Organisation selectedOrganisation;

    private List<Observer<MeetingDTO>> observers = new ArrayList<>();

    @WebListener
    public static class MyContextLoaderListener extends ContextLoaderListener {
        // needed to get the Spring integration going
    }

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = RoomplannerUI.class)
    public static class Servlet extends SpringVaadinServlet {

        private static final long serialVersionUID = -6324147788108224249L;
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

        VerticalLayout center = new DefaultVerticalLayout(true, false);
        hCenter.addComponent(center);
        hCenter.setComponentAlignment(center, Alignment.MIDDLE_LEFT);

        // Label titleLabel = new Label("OCS Room Planner Prototype");
        // center.addComponent(titleLabel);

        List<Organisation> allOrgs = organisationService.findAll();
        if (!allOrgs.isEmpty()) {
            setSelectedOrganisation(allOrgs.get(3));
        }

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

        initNavigation(viewProvider, viewPanel, Views.LOCATIONS_VIEW, true);
        getNavigator().setErrorView(new ErrorView());

        // build the menu
        menu = menuService.constructMenu("roomplanner.menu", getNavigator());
        result.addComponent(menu);
        result.addComponent(viewPanel);

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
        observers.stream().forEach(o -> o.notify(entity));
    }

    public Organisation getSelectedOrganisation() {
        return selectedOrganisation;
    }

    public void setSelectedOrganisation(Organisation selectedOrganisation) {
        this.selectedOrganisation = selectedOrganisation;
    }

}
