package domein.site;

import domein.machine.Machine;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.SoftDeletable;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sites")
@NoArgsConstructor
@Getter
@Setter
public class Site implements SoftDeletable, Serializable, Cloneable {

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(nullable = false)
    private String address;

    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false)
    private int Id;

    @Column(nullable = false)
    private String verantwoordelijke;

    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Machine> machines;

    public Site(String name, String address, String verantwoordelijke) {
        this.name = name;
        this.address = address;
        this.verantwoordelijke = verantwoordelijke;
        this.machines = new HashSet<>();
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean getDeleted() {
        return this.deleted;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
