package propensi.proyek.properly.Dto.semester;

import org.mapstruct.Mapper;

import propensi.proyek.properly.model.Semester;

@Mapper(componentModel = "spring")
public interface SemesterMapper {
    Semester createSemesterDTOToSemester(CreateSemesterDTO createSemesterDTO);

    UpdateSemesterDTO semesterToUpdateSemesterDTO(Semester semester);

    Semester updateSemesterDTOToSemester(UpdateSemesterDTO updateSemesterDTO);
}
