package ventas.backend.SQL.services;

import ventas.backend.SQL.dao.PhysicalDeskDao;
import ventas.backend.SQL.dto.PhysicalDeskDto;
import ventas.backend.SQL.mappers.PhysicalDeskMapper;
import ventas.backend.SQL.models.PhysicalDesk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PhysicalDeskService {
    @Autowired
    private PhysicalDeskDao physicalDeskDao;

    @Autowired
    private PhysicalDeskMapper physicalDeskMapper;

    public List<PhysicalDeskDto> getAllPhysicalDesk(){
        List<PhysicalDesk> physicalDeskDaoAll = physicalDeskDao.findAll();
        return physicalDeskMapper.mapToDtoArrayList(physicalDeskDaoAll);
    }

    public PhysicalDeskDto getPhysicalDeskById(Integer id){
        if(physicalDeskDao.findById(id).isPresent()){
            return physicalDeskMapper.mapToDto(physicalDeskDao.findPhysicalDeskById(id));
        }
        else{
            return  null;
        }
    }
    public void updatePhysicalDesk(PhysicalDeskDto physicalDeskDto, Integer id){
        if(physicalDeskDao.findById(id).isPresent()){
            PhysicalDesk physicalDeskFinded = physicalDeskDao.findPhysicalDeskById(id);
            physicalDeskFinded.setDescription(physicalDeskDto.getDescription());
            physicalDeskFinded.setOffice(physicalDeskDto.getOffice());
            physicalDeskFinded.setDeskList(physicalDeskDto.getDeskList());
            physicalDeskDao.save(physicalDeskFinded);
        }

    }
    public void deletePhysicalDesk(Integer id){
        PhysicalDesk physicalDesk = physicalDeskDao.findPhysicalDeskById(id);
        physicalDeskDao.delete(physicalDesk);
    }

    public PhysicalDeskDto createPhysicalDesk(PhysicalDeskDto paymentMethodDto){
        return physicalDeskMapper.mapToDto(physicalDeskDao.save(physicalDeskMapper.mapToModel(paymentMethodDto)));
    }

    public PhysicalDeskDto createNewPhysicalDesk(String office, String description) {
        PhysicalDesk physicalDesk = new PhysicalDesk();
        physicalDesk.setOffice(office);
        physicalDesk.setDeskList(new ArrayList<>());
        physicalDesk.setDescription(description);
        return physicalDeskMapper.mapToDto(physicalDeskDao.save(physicalDesk));
    }
}