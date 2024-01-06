package pl.robertojavadev.workmanagementapp.auth.usermodel.dto;

import pl.robertojavadev.workmanagementapp.auth.usermodel.UserStatus;
import pl.robertojavadev.workmanagementapp.model.Project;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UserDto(UUID id,
                      String userName,
                      String email,
                      String password,
                      Instant creationDate,
                      UserStatus userStatus,
                      List<Project> projects) {
}
