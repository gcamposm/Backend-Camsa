package tingeso.backend.SQL.mappers;

import tingeso.backend.SQL.dto.DeskDto;
import tingeso.backend.SQL.models.Desk;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
@Component
public class DeskMapper {

    public Desk mapToModel(DeskDto deskDto){
        Desk desk = new Desk();
        desk.setId(deskDto.getId());
        desk.setTotal(deskDto.getTotal());
        desk.setDateOpen(deskDto.getDateOpen());
        desk.setDateClose(deskDto.getDateClose());
        desk.setIsOpen(deskDto.getIsOpen());
        desk.setUserventas(deskDto.getUserventas());
        desk.setSaleList(deskDto.getSaleList());
        desk.setPhysicalDesk(deskDto.getPhysicalDesk());
        desk.setInitialMoney(deskDto.getInitialMoney());
        desk.setWithdraw(deskDto.getWithdraw());
        return desk;
    }

    public List<DeskDto> mapToDtoArrayList(List<Desk> deskList) {
        int i;
        List<DeskDto> deskDtoArrayList = new ArrayList<>();
        for(i=0;i<deskList.size();i++){
            deskDtoArrayList.add(mapToDto(deskList.get(i)));
        }

        return deskDtoArrayList;
    }

    public DeskDto mapToDto (Desk desk){
        DeskDto deskDto = new DeskDto();
        deskDto.setId(desk.getId());
        deskDto.setTotal(desk.getTotal());
        deskDto.setDateOpen(desk.getDateOpen());
        deskDto.setDateClose(desk.getDateClose());
        deskDto.setIsOpen(desk.getIsOpen());
        deskDto.setUserventas(desk.getUserventas());
        deskDto.setSaleList(desk.getSaleList());
        deskDto.setPhysicalDesk(desk.getPhysicalDesk());
        deskDto.setInitialMoney(desk.getInitialMoney());
        deskDto.setWithdraw(desk.getWithdraw());
        return deskDto;
    }
}