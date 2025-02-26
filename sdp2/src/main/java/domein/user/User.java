package domein.user;
import jakarta.persistence.*;
import lombok.*;
import utils.Rollen;

import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@ToString
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Genereert automatisch id
    private Long id;

    @Column(nullable = false, unique = true) // firstName moet uniek en niet leeg zijn
    private String firstName;

    @Column(nullable = false, unique = true) // lastName moet uniek en niet leeg zijn
    private String lastName;

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

    public User(String firstName, String lastName, String email, String password, String adres, String gsmNummer, Rollen rol)
    {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPassword(password);
        setAdres(adres);
        setGsmNummer(gsmNummer);
        setRol(rol);
    }

    public void setPassword(String password)
    {
    	checkString(password);
    	
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean checkPassword(String inputPw)
    {
        return BCrypt.checkpw(inputPw, this.password);
    }
    
    public void setLastName(String naam) {
    	checkString(naam);
    	
    	this.lastName = naam;
    }
    
    public void setFirstName(String voornaam) {
    	checkString(voornaam);
    	
    	this.firstName = voornaam;
    }
    
    public void setEmail(String email) {
    	checkString(email);
    	
    	this.email = email;
    }
    
    public void setAdres(String adres) {
    	checkString(adres);
    	
    	this.adres = adres;
    }
    
    public void setGsmNummer(String gsm) {
    	if(this.rol == Rollen.TECHNIEKER) {
    		checkString(gsm);
    	}
    	
    	this.gsmNummer = gsm;
    }
    
    public void setRol(Rollen rol) {
    	if(rol == null) {
    		throw new IllegalArgumentException("Role has to be filled in");
    	}
    	
    	this.rol = rol;
    }
    
    public void beheerGebruiker(String firstName, String lastName, String password, 
    		String email, String adres, String gsmNummer, Rollen rol) {
    	setFirstName(firstName);
    	setLastName(lastName);
    	setPassword(password);
    	setEmail(email);
    	setAdres(adres);
    	setGsmNummer(gsmNummer);
    	setRol(rol);
    }
    
    public boolean checkString(String string) {
    	if(string == null || string.isEmpty() || string.isBlank()) {
    		throw new IllegalArgumentException(String.format("%s has to be filled in", string));
    	}
    	return true;
    }
    

}
