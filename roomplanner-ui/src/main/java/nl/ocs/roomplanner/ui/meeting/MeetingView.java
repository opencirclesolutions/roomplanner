package nl.ocs.roomplanner.ui.meeting;

import java.util.HashMap;
import java.util.Map;

import nl.ocs.roomplanner.domain.Meeting;
import nl.ocs.roomplanner.domain.Organisation;
import nl.ocs.roomplanner.service.MeetingService;
import nl.ocs.roomplanner.service.OrganisationService;
import nl.ocs.roomplanner.ui.Views;

import org.springframework.beans.factory.annotation.Autowired;

import com.ocs.dynamo.ui.component.DefaultVerticalLayout;
import com.ocs.dynamo.ui.composite.form.FormOptions;
import com.ocs.dynamo.ui.view.BaseView;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.sort.SortOrder;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = Views.MEETINGS_VIEW)
@UIScope
@SuppressWarnings("serial")
public class MeetingView extends BaseView {

    private Layout mainLayout;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private OrganisationService organisationService;

    @Override
    public void enter(ViewChangeEvent event) {
        VerticalLayout main = new DefaultVerticalLayout(true, true);

        mainLayout = new VerticalLayout();
        main.addComponent(mainLayout);

        setCompositionRoot(main);

        FormOptions fo = new FormOptions();
        fo.setShowSearchDialogButton(true);
        fo.setShowRemoveButton(true);
        fo.setShowEditButton(true);
        fo.setOpenInViewMode(false);

        final Organisation organisation = (Organisation) VaadinSession.getCurrent().getAttribute(
                "organisation");

        Map<String, Filter> fieldFilters = new HashMap<>();
        fieldFilters.put("desiredLocation", new Compare.Equal("organisation", organisation));

        MeetingLayout meetingLayout = new MeetingLayout(meetingService, organisation,
                organisationService, getModelFactory().getModel(Meeting.class), fo, null);
        meetingLayout.setFieldFilters(fieldFilters);
        meetingLayout.addSortOrder(new SortOrder("meetingDate", SortDirection.ASCENDING));
        meetingLayout.addSortOrder(new SortOrder("whiteboard", SortDirection.ASCENDING));
        meetingLayout.setSortEnabled(false);
        
        mainLayout.addComponent(meetingLayout);
    }
}
