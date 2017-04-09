package nl.ocs.roomplanner.ui.meeting;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.dynamo.ui.composite.form.FormOptions;
import com.ocs.dynamo.ui.view.LazyBaseView;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.sort.SortOrder;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

import nl.ocs.roomplanner.domain.Meeting;
import nl.ocs.roomplanner.domain.Organisation;
import nl.ocs.roomplanner.service.MeetingService;
import nl.ocs.roomplanner.service.OrganisationService;
import nl.ocs.roomplanner.ui.RoomplannerUI;
import nl.ocs.roomplanner.ui.Views;

@UIScope
@SpringView(name = Views.MEETINGS_VIEW)
@SuppressWarnings("serial")
public class MeetingView extends LazyBaseView {

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private OrganisationService organisationService;

    private MeetingLayout meetingLayout;

    @Override
    public Component build() {

        FormOptions fo = new FormOptions().setShowRemoveButton(true).setEditAllowed(true).setOpenInViewMode(true);
        final Organisation organisation = ((RoomplannerUI) UI.getCurrent()).getSelectedOrganisation();

        Map<String, Filter> fieldFilters = new HashMap<>();
        fieldFilters.put("desiredLocation", new Compare.Equal("organisation", organisation));

        meetingLayout = new MeetingLayout(meetingService, organisation, organisationService,
                getModelFactory().getModel(Meeting.class), fo, null);
        meetingLayout.setFieldFilters(fieldFilters);
        meetingLayout.addSortOrder(new SortOrder("meetingDate", SortDirection.ASCENDING));
        meetingLayout.addSortOrder(new SortOrder("whiteboard", SortDirection.ASCENDING));
        meetingLayout.setSortEnabled(false);

        return meetingLayout;
    }
}
