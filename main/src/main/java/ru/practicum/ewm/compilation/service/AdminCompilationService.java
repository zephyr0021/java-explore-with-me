package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationRequest;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;

    @Transactional
    public CompilationDto createCompilation(CompilationRequest request) {
        List<Event> events = eventRepository.findAllByIdIn(request.getEvents());
        Compilation compilation = compilationMapper.toCompilation(request);

        compilation.setEvents(events);
        compilation = compilationRepository.save(compilation);

        return compilationMapper.toCompilationDto(compilation);
    }

    @Transactional
    public void deleteCompilation(Long id) {
        int rows = compilationRepository.deleteCompilatiom(id);

        if (rows == 0) {
            throw new NotFoundException("Compilation with id=" + id + " was not found");
        }
    }

    public CompilationDto updateCompilation(Long id, CompilationRequest request) {
        List<Event> events = eventRepository.findAllByIdIn(request.getEvents());
        Compilation newCompilation = compilationRepository.findById(id)
                .map(compilation -> compilationMapper.updateCompilation(request, compilation))
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + id + " was not found"));

        newCompilation.setEvents(events);
        newCompilation = compilationRepository.save(newCompilation);

        return compilationMapper.toCompilationDto(newCompilation);
    }
}
