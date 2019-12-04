package ventas.backend.SQL.services;


import ventas.backend.SQL.dao.TypeDao;
import ventas.backend.SQL.dto.TypeDto;
import ventas.backend.SQL.mappers.TypeMapper;
import ventas.backend.SQL.models.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeService {
    @Autowired
    private TypeMapper typeMapper;

    @Autowired
    private TypeDao typeDao;

    public List<TypeDto> getAllType(){
        List<Type> typeList = typeDao.findAll();
        return typeMapper.mapToDtoArrayList(typeList);
    }

    public TypeDto getTypeByID(Integer id){
        if(typeDao.findById(id).isPresent()){
            return typeMapper.mapToDto(typeDao.findTypeById(id));
        }
        else{
            return  null;
        }
    }
    public void updateType(TypeDto typeDto, Integer id){
        if(typeDao.findById(id).isPresent()){
            Type typeFinded = typeDao.findTypeById(id);
            typeFinded.setType(typeDto.getType());
            typeFinded.setProductList(typeDto.getProductList());
            typeDao.save(typeFinded);

        }

    }
    public void deleteType(Integer id){
        Type type = typeDao.findTypeById(id);
        typeDao.delete(type);

    }

    public TypeDto createType(TypeDto typeDto){
        return typeMapper.mapToDto(typeDao.save(typeMapper.mapToModel(typeDto)));
    }
}
