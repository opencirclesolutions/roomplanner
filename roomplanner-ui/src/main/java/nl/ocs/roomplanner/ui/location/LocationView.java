package nl.ocs.roomplanner.ui.location;

import nl.ocs.roomplanner.domain.Location;
import nl.ocs.roomplanner.domain.Organisation;
import nl.ocs.roomplanner.service.LocationService;
import nl.ocs.roomplanner.service.OrganisationService;
import nl.ocs.roomplanner.ui.Views;

import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.dynamo.ui.component.DefaultVerticalLayout;
import com.ocs.dynamo.ui.composite.form.FormOptions;
import com.ocs.dynamo.ui.composite.layout.ServiceBasedDetailLayout;
import com.ocs.dynamo.ui.view.BaseView;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = Views.LOCATIONS_VIEW)
@UIScope
@SuppressWarnings("serial")
public class LocationView extends BaseView {

	private Layout mainLayout;

	@Autowired
	private OrganisationService organisationService;

	@Autowired
	private LocationService locationService;

	@Override
	public void enter(ViewChangeEvent event) {
		VerticalLayout main = new DefaultVerticalLayout(true, true);

		mainLayout = new VerticalLayout();
		main.addComponent(mainLayout);

		setCompositionRoot(main);

		final Organisation organisation = (Organisation) VaadinSession.getCurrent().getAttribute(
				"organisation");

		ServiceBasedDetailLayout<Integer, Location, Integer, Organisation> locationLayout = new ServiceBasedDetailLayout<Integer, Location, Integer, Organisation>(
				locationService, (Organisation) VaadinSession.getCurrent().getAttribute(
						"organisation"), organisationService, getModelFactory().getModel(
						Location.class), new FormOptions(), null) {

			@Override
			protected Filter constructFilter() {
				return new Compare.Equal("organisation", organisation);
			}

			@Override
			protected Location createEntity() {
				Location location = super.createEntity();
				location.setOrganisation(getParentEntity());
				return location;
			}
		};
		mainLayout.addComponent(locationLayout);
	}

}
