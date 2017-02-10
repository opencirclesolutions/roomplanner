package nl.ocs.roomplanner.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ocs.dynamo.domain.AbstractTreeEntity;
import com.ocs.dynamo.domain.model.VisibilityType;
import com.ocs.dynamo.domain.model.annotation.Attribute;
import com.ocs.dynamo.domain.model.annotation.AttributeOrder;
import com.ocs.dynamo.domain.model.annotation.Model;

@Model(displayProperty = "fullName", sortOrder = "lastName asc")
@Entity
@Table(name = "employees")
@AttributeOrder(attributeNames = { "firstName", "lastName", "email" })
public class Employee extends AbstractTreeEntity<Integer, Employee> {

    private static final long serialVersionUID = 3366471117830574071L;

    @Id
    @SequenceGenerator(name = "employees_id_seq", sequenceName = "employees_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employees_id_seq")
    private Integer id;

    @Attribute(searchable = true, defaultValue = "Bas")
    @Size(min = 0, max = 50)
    @Column(name = "first_name")
    @NotNull
    private String firstName;

    @Attribute(searchable = true)
    @Size(min = 0, max = 50)
    @Column(name = "last_name")
    @NotNull
    private String lastName;

    @Size(min = 0, max = 100)
    @Column(name = "email")
    @NotNull
    private String email;

    // @ManyToOne
    // @JoinColumn(name = "manager_id")
    // @Attribute(complexEditable = true, selectMode =
    // AttributeSelectMode.LOOKUP)
    // private Employee manager;

    @ManyToMany(mappedBy = "attendees")
    private Set<Meeting> meetings = new HashSet<Meeting>();

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Attribute(readOnly = true, visible = VisibilityType.HIDE)
    public String getFullName() {
        return lastName + ", " + firstName;
    }

    // public Employee getManager() {
    // return manager;
    // }
    //
    // public void setManager(Employee manager) {
    // this.manager = manager;
    // }

    @Override
    @Attribute(displayName = "manager", complexEditable = true)
    public Employee getParent() {
        return super.getParent();
    }

}
