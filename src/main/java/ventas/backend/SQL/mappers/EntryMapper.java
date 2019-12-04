package ventas.backend.SQL.mappers;

import ventas.backend.SQL.dto.EntryDto;
import ventas.backend.SQL.models.Entry;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("Duplicates")
@Component
public class EntryMapper {
    public Entry mapToModel(EntryDto entryDto){

        Entry entry = new Entry();
        entry.setId(entryDto.getId());
        entry.setDate(entryDto.getDate());
        entry.setType(entryDto.getType());
        entry.setEntryDetailList(entryDto.getEntryDetailList());
        entry.setUserventas(entryDto.getUserventas());
        entry.setTicketNumber(entryDto.getTicketNumber());
        entry.setTotal(entryDto.getTotal());
        return entry;
    }

    public List<EntryDto> mapToDtoArrayList(List<Entry> entryArrayList) {
        int i;

        List<EntryDto> entryDtoArrayList = new ArrayList<>();
        for(i=0;i<entryArrayList.size();i++){
            entryDtoArrayList.add(mapToDto(entryArrayList.get(i)));
        }

        return entryDtoArrayList;
    }

    public EntryDto mapToDto (Entry entry){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime localDateTime = entry.getDate();
        String formatDateTime = localDateTime.format(formatter);
        EntryDto entryDto = new EntryDto();
        entryDto.setFormattedDate(formatDateTime);
        entryDto.setId(entry.getId());
        entryDto.setDate(entry.getDate());
        entryDto.setType(entry.getType());
        entryDto.setEntryDetailList(entry.getEntryDetailList());
        entryDto.setUserventas(entry.getUserventas());
        entryDto.setTicketNumber(entry.getTicketNumber());
        entryDto.setTotal(entry.getTotal());
        return entryDto;
    }
}