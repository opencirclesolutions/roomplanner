package nl.ocs.roomplanner.ui.meeting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nl.ocs.roomplanner.domain.Employee;
import nl.ocs.roomplanner.domain.Meeting;
import nl.ocs.roomplanner.domain.Organisation;
import nl.ocs.roomplanner.service.EmployeeService;
import nl.ocs.roomplanner.service.MeetingService;

import com.ocs.dynamo.domain.model.AttributeModel;
import com.ocs.dynamo.domain.model.EntityModel;
import com.ocs.dynamo.service.BaseService;
import com.ocs.dynamo.ui.ServiceLocator;
import com.ocs.dynamo.ui.composite.form.DetailsEditTable;
import com.ocs.dynamo.ui.composite.form.FormOptions;
import com.ocs.dynamo.ui.composite.layout.ServiceBasedDetailLayout;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.sort.SortOrder;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Field;
import com.vaadin.ui.Layout;

public class MeetingLayout extends
        ServiceBasedDetailLayout<Integer, Meeting, Integer, Organisation> {

    private static final long serialVersionUID = 4983552565372330040L;

    public MeetingLayout(BaseService<Integer, Meeting> service, Organisation parentEntity,
            BaseService<Integer, Organisation> organisationService,
            EntityModel<Meeting> entityModel, FormOptions formOptions, SortOrder sortOrder) {
        super(service, parentEntity, organisationService, entityModel, formOptions, sortOrder);
    }

    @Override
    protected Filter constructFilter() {
        return new Compare.Equal("organisation", getParentEntity());
    }

    @Override
    protected Meeting createEntity() {
        Meeting meeting = super.createEntity();
        meeting.setOrganisation(getParentEntity());
        return meeting;
    }

    /**
     * 
     * @param viewMode
     * @return
     */
    @SuppressWarnings("serial")
    private DetailsEditTable<Integer, Employee> createAttendeeTable(boolean viewMode) {
        List<Employee> ds = new ArrayList<>();
        ds.addAll(getSelectedItem().getAttendees());

        FormOptions fo = new FormOptions();
        fo.setShowRemoveButton(true);
        fo.setHideAddButton(true);
        fo.setShowSearchDialogButton(true);

        DetailsEditTable<Integer, Employee> table = new DetailsEditTable<Integer, Employee>(ds,
                getEntityModelFactory().getModel(Employee.class), viewMode, fo) {

            @Override
            protected Employee createEntity() {
                // not needed
                return null;
            }

            @Override
            protected void removeEntity(Employee toRemove) {
                MeetingLayout.this.getSelectedItem().removeAttendee(toRemove);
            }

            @Override
            public void afterItemsSelected(Collection<Employee> selectedItems) {
                if (selectedItems != null) {
                    for (Employee e : selectedItems) {
                        MeetingLayout.this.getSelectedItem().addAttendee(e);
                    }
                }
            }
        };
        // table cannot be directly edited
        table.setTableReadOnly(true);
        table.setService(ServiceLocator.getService(EmployeeService.class));
        table.setSearchDialogSortOrder(new SortOrder("lastName", SortDirection.ASCENDING));
        return table;
    }

    @Override
    protected Field<?> constructCustomField(EntityModel<Meeting> entityModel,
            AttributeModel attributeModel, boolean viewMode, boolean searchMode) {
        if ("attendees".equals(attributeModel.getName())) {
            return createAttendeeTable(viewMode);
        }
        return null;
    }

    @Override
    protected void postProcessButtonBar(Layout buttonBar) {

        Button randomButton = new Button("Generate random meetings");
        randomButton.addClickListener(new Button.ClickListener() {

            private static final long serialVersionUID = -8368258847189827995L;

            @Override
            public void buttonClick(ClickEvent event) {
                MeetingService ms = ServiceLocator.getService(MeetingService.class);
                ms.generateRandomMeetings(getParentEntity());
                reload();
            }
        });
        buttonBar.addComponent(randomButton);

    }
}
