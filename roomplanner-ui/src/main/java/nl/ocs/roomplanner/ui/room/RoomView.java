package nl.ocs.roomplanner.ui.room;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import nl.ocs.roomplanner.domain.Organisation;
import nl.ocs.roomplanner.domain.Room;
import nl.ocs.roomplanner.service.OrganisationService;
import nl.ocs.roomplanner.service.RoomService;
import nl.ocs.roomplanner.ui.RoomplannerUI;
import nl.ocs.roomplanner.ui.Views;

import com.ocs.dynamo.ui.composite.form.FormOptions;
import com.ocs.dynamo.ui.composite.layout.ServiceBasedDetailLayout;
import com.ocs.dynamo.ui.view.LazyBaseView;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

@UIScope
@SpringView(name = Views.ROOMS_VIEW)
@SuppressWarnings("serial")
public class RoomView extends LazyBaseView {

	@Inject
	private OrganisationService organisationService;

	@Inject
	private RoomService roomService;

	private ServiceBasedDetailLayout<Integer, Room, Integer, Organisation> roomLayout;

	@Override
	public Component build() {

		final Organisation organisation = ((RoomplannerUI) UI.getCurrent()).getSelectedOrganisation();

		Map<String, Filter> fieldFilters = new HashMap<>();
		fieldFilters.put("location", new Compare.Equal("organisation", organisation));

		FormOptions fo = new FormOptions().setShowRemoveButton(true);
		roomLayout = new ServiceBasedDetailLayout<Integer, Room, Integer, Organisation>(roomService, organisation,
		        organisationService, getModelFactory().getModel(Room.class), fo, null) {

			@Override
			protected Filter constructFilter() {
				return new Compare.Equal("organisation", getParentEntity());
			}

			@Override
			protected Room createEntity() {
				Room room = super.createEntity();
				room.setOrganisation(organisation);
				return room;
			}

		};
		roomLayout.setFieldFilters(fieldFilters);
		return roomLayout;
	}

}
