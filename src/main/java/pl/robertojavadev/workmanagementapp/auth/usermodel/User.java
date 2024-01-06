package pl.robertojavadev.workmanagementapp.auth.usermodel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import pl.robertojavadev.workmanagementapp.model.Project;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    @Size(min = 1, max = 20)
    private String userName;

    @NotBlank
    @Size(min = 6, max = 100)
    @Pattern(regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;

    @NotBlank
    private String password;

    @Column(name = "creation_date")
    @DateTimeFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    private Instant creationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", columnDefinition = "ENUM('VERIFIED', 'UNVERIFIED', 'BLOCKED', 'BANNED')")
    private UserStatus userStatus;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Project> projects = new ArrayList<>();
}
