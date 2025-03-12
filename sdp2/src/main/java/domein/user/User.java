package domein.user;
import jakarta.persistence.*;
import lombok.*;
import utils.Rollen;

import org.mindrot.jbcrypt.BCrypt;
import utils.SoftDeletable;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User implements Serializable, SoftDeletable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Genereert automatisch id
    private Long id;

    @Column(nullable = false, unique = true) // firstName moet uniek en niet leeg zijn
    private String firstName;

    @Column(nullable = false, unique = true) // lastName moet uniek en niet leeg zijn
    private String lastName;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false) //Dit zorgt dat wachtwoord niet leeg kan zijn
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String adres;

    @Column(nullable = true, unique = true)
    private String gsmNummer;

    @Column(nullable = false)
    private Rollen rol;

    @Column(nullable = false)
    private boolean deleted = false;

    public User(String firstName, String lastName, LocalDate birthDate,String email, String password, String adres, String gsmNummer, Rollen rol) {
        beheerGebruiker(firstName, lastName, birthDate, password, email, adres, gsmNummer, rol);
    }

    public User(User u){
        this.firstName = u.firstName;
        this.lastName = u.lastName;
        this.birthDate = u.birthDate;
        this.password = u.password;
        this.email = u.email;
        this.adres = u.adres;
        this.gsmNummer = u.gsmNummer;
        this.rol = u.rol;

    }

    public void setPassword(String password) {
        checkString(password);
        // Als het wachtwoord nog niet gehasht is, dan doen we dat hier
        if (!password.startsWith("$2a$")) {
            this.password = BCrypt.hashpw(password, BCrypt.gensalt());  // Hashen van het wachtwoord
            System.out.println(this.password);
        } else {
            this.password = password; // Als het al gehasht is, slaan we het gewoon op
        }
    }

    public void setLastName(String naam)
        {
        checkString(naam);

        this.lastName = naam;
        }

    public void setFirstName(String voornaam)
        {
        checkString(voornaam);

        this.firstName = voornaam;
        }

    public void setEmail(String email)
        {
        checkString(email);

        this.email = email;
        }

    public void setAdres(String adres)
        {
        checkString(adres);

        this.adres = adres;
        }

    public void setGsmNummer(String gsm)
        {
        if (this.rol == Rollen.TECHNIEKER) {
            checkString(gsm);
        }

        this.gsmNummer = gsm;
        }

    public void setRol(Rollen rol)
        {
        if (rol == null) {
            throw new IllegalArgumentException("Role has to be filled in");
        }

        this.rol = rol;
        }

    public void beheerGebruiker(String firstName, String lastName, LocalDate birthDate,String password,
                                String email, String adres, String gsmNummer, Rollen rol)
        {
        setFirstName(firstName);
        setLastName(lastName);
        setBirthDate(birthDate);
        setPassword(password);
        setEmail(email);
        setAdres(adres);
        setGsmNummer(gsmNummer);
        setRol(rol);
        }

    public boolean checkString(String string)
        {
        if (string == null || string.isEmpty() || string.isBlank()) {
            throw new IllegalArgumentException(String.format("%s has to be filled in", string));
        }
        return true;
        }

    public String getFullName()
        {
        return String.format("%s %s", getFirstName(), getLastName());
        }

    @Override
    public boolean getDeleted()
        {
            return deleted;
        }
}
