package ru.practicum.ewm.compilation.mapper;

import org.mapstruct.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationRequest;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.model.Compilation;

@Mapper(componentModel = "spring")
public interface CompilationMapper{
    @Mapping(target = "events", ignore = true)
    Compilation toCompilation(CompilationRequest request);

    CompilationDto toCompilationDto(Compilation compilation);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "events", ignore = true)
    Compilation updateCompilation(UpdateCompilationRequest request, @MappingTarget Compilation compilation);
}
