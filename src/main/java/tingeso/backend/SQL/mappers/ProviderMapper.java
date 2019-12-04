package tingeso.backend.SQL.mappers;

import tingeso.backend.SQL.dto.ProviderDto;
import tingeso.backend.SQL.models.Provider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
@Component
public class ProviderMapper {
    public Provider mapToModel(ProviderDto providerDto){
        Provider provider = new Provider();
        provider.setId(providerDto.getId());
        provider.setName(providerDto.getName());
        provider.setDeleted(providerDto.getDeleted());
        return provider;
    }
    public ProviderDto mapToDto(Provider provider){
        ProviderDto providerDto = new ProviderDto();
        providerDto.setId(provider.getId());
        providerDto.setName(provider.getName());
        providerDto.setDeleted(provider.getDeleted());
        return  providerDto;
    }
    public List<ProviderDto> mapToDtoList(List<Provider> providerList){
        List<ProviderDto> providerDtoList = new ArrayList<>();
        for (Provider provider: providerList
             ) {
            ProviderDto providerDto = new ProviderDto();
            providerDto = mapToDto(provider);
            providerDtoList.add(providerDto);
        }
        return  providerDtoList;
    }
}
