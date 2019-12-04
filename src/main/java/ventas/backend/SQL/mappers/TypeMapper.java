package ventas.backend.SQL.mappers;

import ventas.backend.SQL.dto.TypeDto;
import ventas.backend.SQL.models.Type;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
@Component
public class TypeMapper {
    public Type mapToModel(TypeDto typeDto){
        Type type = new Type();
        type.setId(typeDto.getId());
        type.setProductList(typeDto.getProductList());
        type.setType(typeDto.getType());
        return type;
    }

    public List<TypeDto> mapToDtoArrayList(List<Type> typeArrayList) {
        int i;

        List<TypeDto> typeDtoArrayList = new ArrayList<>();
        for(i=0;i<typeArrayList.size();i++){
            typeDtoArrayList.add(mapToDto(typeArrayList.get(i)));
        }

        return typeDtoArrayList;
    }

    public TypeDto mapToDto (Type type){

        TypeDto typeDto = new TypeDto();
        typeDto.setId(type.getId());
        typeDto.setProductList(type.getProductList());
        typeDto.setType(type.getType());
        return typeDto;
    }

}
