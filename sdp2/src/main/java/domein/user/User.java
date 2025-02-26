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
@Setter
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

    public User(String firstName, String lastName, String email, String password)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public void setPassword(String password)
    {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean checkPassword(String inputPw)
    {
        return BCrypt.checkpw(inputPw, this.password);
    }
    
    

}
