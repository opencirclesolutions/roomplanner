package nl.ocs.roomplanner.ui.room;

import java.util.HashMap;
import java.util.Map;

import nl.ocs.roomplanner.domain.Organisation;
import nl.ocs.roomplanner.domain.Room;
import nl.ocs.roomplanner.service.OrganisationService;
import nl.ocs.roomplanner.service.RoomService;
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
import com.vaadin.ui.VerticalLayout;

@SpringView(name = Views.ROOMS_VIEW)
@UIScope
@SuppressWarnings("serial")
public class RoomView extends BaseView {

    private VerticalLayout mainLayout;

    @Autowired
    private OrganisationService organisationService;

    @Autowired
    private RoomService roomService;

    @Override
    public void enter(ViewChangeEvent event) {
        VerticalLayout main = new DefaultVerticalLayout(true, true);

        mainLayout = new VerticalLayout();
        main.addComponent(mainLayout);

        final Organisation organisation = (Organisation) VaadinSession.getCurrent().getAttribute(
                "organisation");

        Map<String, Filter> fieldFilters = new HashMap<>();
        fieldFilters.put("location", new Compare.Equal("organisation", organisation));

        FormOptions fo = new FormOptions();
        fo.setShowRemoveButton(true);

        ServiceBasedDetailLayout<Integer, Room, Integer, Organisation> roomLayout = new ServiceBasedDetailLayout<Integer, Room, Integer, Organisation>(
                roomService, organisation, organisationService, getModelFactory().getModel(
                        Room.class), fo, null) {

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

        mainLayout.addComponent(roomLayout);

        setCompositionRoot(main);
    }
}
