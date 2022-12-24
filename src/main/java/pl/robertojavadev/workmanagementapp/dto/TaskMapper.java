package pl.robertojavadev.workmanagementapp.dto;

import org.mapstruct.Mapper;
import pl.robertojavadev.workmanagementapp.model.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDto mapTaskEntityToTaskDto(Task task);
}
