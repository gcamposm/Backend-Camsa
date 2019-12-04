package tingeso.backend.SQL.mappers;


import tingeso.backend.SQL.dto.EntryDetailDto;
import tingeso.backend.SQL.models.EntryDetail;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EntryDetailMapper {
    public EntryDetail mapToModel(EntryDetailDto entryDetailDto){

        EntryDetail entryDetail = new EntryDetail();
        entryDetail.setId(entryDetailDto.getId());
        entryDetail.setType(entryDetailDto.getType());
        entryDetail.setQuantity(entryDetailDto.getQuantity());
        entryDetail.setProduct(entryDetailDto.getProduct());
        entryDetail.setEntry(entryDetailDto.getEntry());
        entryDetail.setSumStock(entryDetailDto.getSumStock());
        return entryDetail;
    }

    public List<EntryDetailDto> mapToDtoArrayList(List<EntryDetail> entryDetailArrayList) {
        int i;

        List<EntryDetailDto> entryDetailDtoArrayList = new ArrayList<>();
        for(i=0;i<entryDetailArrayList.size();i++){
            entryDetailDtoArrayList.add(mapToDto(entryDetailArrayList.get(i)));
        }

        return entryDetailDtoArrayList;
    }

    public EntryDetailDto mapToDto (EntryDetail entryDetail){

        EntryDetailDto entryDetailDto = new EntryDetailDto();
        entryDetailDto.setId(entryDetail.getId());
        entryDetailDto.setQuantity(entryDetail.getQuantity());
        entryDetailDto.setProduct(entryDetail.getProduct());
        entryDetailDto.setEntry(entryDetail.getEntry());
        entryDetailDto.setType(entryDetail.getType());
        entryDetailDto.setSumStock(entryDetail.getSumStock());
        return entryDetailDto;
    }

}