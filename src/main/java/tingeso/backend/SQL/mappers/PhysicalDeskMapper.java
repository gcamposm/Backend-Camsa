package tingeso.backend.SQL.mappers;

import tingeso.backend.SQL.dto.PhysicalDeskDto;
import tingeso.backend.SQL.models.PhysicalDesk;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
@Component
public class PhysicalDeskMapper {

    public PhysicalDesk mapToModel(PhysicalDeskDto physicalDeskDto){

        PhysicalDesk physicalDesk = new PhysicalDesk();
        physicalDesk.setId(physicalDeskDto.getId());
        physicalDesk.setDescription(physicalDeskDto.getDescription());
        physicalDesk.setOffice(physicalDeskDto.getOffice());
        physicalDesk.setDeskList(physicalDeskDto.getDeskList());
        return physicalDesk;
    }

    public List<PhysicalDeskDto> mapToDtoArrayList(List<PhysicalDesk> physicalDesks) {
        int i;

        List<PhysicalDeskDto> physicalDeskDtos = new ArrayList<>();
        for(i=0;i<physicalDesks.size();i++){
            physicalDeskDtos.add(mapToDto(physicalDesks.get(i)));
        }

        return physicalDeskDtos;
    }

    public PhysicalDeskDto mapToDto (PhysicalDesk physicalDesk){

        PhysicalDeskDto physicalDeskDto = new PhysicalDeskDto();
        physicalDeskDto.setId(physicalDesk.getId());
        physicalDeskDto.setDescription(physicalDesk.getDescription());
        physicalDeskDto.setOffice(physicalDesk.getOffice());
        physicalDeskDto.setDeskList(physicalDesk.getDeskList());
        return physicalDeskDto;
    }
}