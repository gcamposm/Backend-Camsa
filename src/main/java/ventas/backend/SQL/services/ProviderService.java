package ventas.backend.SQL.services;

import ventas.backend.SQL.dao.ProviderDao;
import ventas.backend.SQL.dto.ProviderDto;
import ventas.backend.SQL.mappers.ProviderMapper;
import ventas.backend.SQL.models.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderService {

    @Autowired
    private ProviderDao providerDao;

    @Autowired
    private ProviderMapper providerMapper;

    public List<ProviderDto> getAllProvider(){
        List<Provider> providerList = providerDao.findProviderByDeletedFalse();
        return providerMapper.mapToDtoList(providerList);
    }

    public ProviderDto getProviderByID(Integer id){
        if(providerDao.findById(id).isPresent()){
            return providerMapper.mapToDto(providerDao.findProviderById(id));
        } else{
            return  null;
        }
    }

    public ProviderDto updateProvider(ProviderDto providerDto, Integer id){
        if(providerDao.findById(id).isPresent()){
            Provider providerFinded = providerDao.findProviderById(id);
            providerFinded.setName(providerDto.getName());
            providerFinded.setDeleted(providerDto.getDeleted());
            return providerMapper.mapToDto(providerDao.save(providerFinded));
        }
        return null;
    }

    public ProviderDto deleteProvider(Integer id){
        Provider provider = providerDao.findProviderById(id);
        provider.setDeleted(true);
        providerDao.save(provider);
        return providerMapper.mapToDto(provider);
    }

    public ProviderDto createProvider(ProviderDto providerDto){
        return providerMapper.mapToDto(providerDao.save(providerMapper.mapToModel(providerDto)));
    }
}