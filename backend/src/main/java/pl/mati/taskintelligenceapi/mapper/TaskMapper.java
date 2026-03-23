package pl.mati.taskintelligenceapi.mapper;

import org.mapstruct.Mapper;
import pl.mati.taskintelligenceapi.dto.taskDto.TaskRequestDTO;
import pl.mati.taskintelligenceapi.dto.taskDto.TaskResponseDTO;
import pl.mati.taskintelligenceapi.entity.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskResponseDTO toDto(pl.mati.taskintelligenceapi.entity.Task entity);

    Task toTask(TaskRequestDTO taskRequestDTO);

}
