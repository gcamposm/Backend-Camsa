package ventas.backend.SQL.services;

import tingeso.backend.SQL.dao.*;
import ventas.backend.SQL.dao.*;
import ventas.backend.SQL.dto.EntryDto;
import ventas.backend.SQL.mappers.EntryMapper;
import tingeso.backend.SQL.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ventas.backend.SQL.models.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EntryService {

    @Autowired
    private EntryDao entryDao;

    @Autowired
    private EntryDetailDao entryDetailDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private SaleDao saleDao;

    @Autowired
    private UserDao userDao;


    @Autowired
    private EntryMapper entryMapper;

    public List<EntryDto> getAllEntry(){
        List<Entry> entryList = entryDao.findAll();
        return entryMapper.mapToDtoArrayList(entryList);
    }

    public EntryDto getEntryById(Integer id){
        if(entryDao.findById(id).isPresent()){
            return entryMapper.mapToDto(entryDao.findEntryById(id));
        }
        else{
            return  null;
        }
    }
    public void updateEntry(EntryDto entryDto, Integer id){
        if(entryDao.findById(id).isPresent()){
            Entry entryFinded = entryDao.findEntryById(id);
            entryFinded.setDate(entryDto.getDate());
            entryFinded.setType(entryDto.getType());
            entryFinded.setEntryDetailList(entryDto.getEntryDetailList());
            entryFinded.setUserventas(entryDto.getUserventas());
            entryDao.save(entryFinded);

        }

    }
    public void deleteEntry(Integer id){
        Entry entry = entryDao.findEntryById(id);
        entryDao.delete(entry);

    }

    public EntryDto createEntry(EntryDto entryDto){
        return entryMapper.mapToDto(entryDao.save(entryMapper.mapToModel(entryDto)));
    }

    public EntryDto createNewDevolution(Integer saleId, Integer userId) {
        User user = userDao.findById(userId);
        Sale sale = saleDao.findSaleById(saleId);
        LocalDateTime localDateTime = LocalDateTime.now();
        Entry entry = new Entry();
        entry.setTicketNumber(sale.getTicketNumber());
        entry.setType("devolution");
        entry.setDate(localDateTime);
        entry.setUserventas(user);
        entry.setTotal(BigDecimal.valueOf(0));
        entry.setEntryDetailList(new ArrayList<>());
        return entryMapper.mapToDto(entryDao.save(entry));
    }

    public EntryDto createNewEntry(Integer userId, String type) {
        User user = userDao.findById(userId);
        LocalDateTime localDateTime = LocalDateTime.now();
        Entry entry = new Entry();
        entry.setDate(localDateTime);
        entry.setUserventas(user);
        entry.setEntryDetailList(new ArrayList<>());
        entry.setType(type);
        return entryMapper.mapToDto(entryDao.save(entry));
    }

    public EntryDto createDevolution(Sale sale, Integer userId) {
        Entry entry = entryMapper.mapToModel(createNewEntry(userId, "devolution"));
        for (SaleDetail saleDetail: sale.getSaleDetailList()
             ) {
            EntryDetail entryDetail = new EntryDetail();
            entryDetail.setEntry(entry);
            entryDetail.setProduct(saleDetail.getProduct());
            //entryDetail.setType();
            entryDetail.setQuantity(saleDetail.getQuantity());
            entryDetail.getProduct().setStock(entryDetail.getProduct().getStock() + entryDetail.getQuantity());
            productDao.save(entryDetail.getProduct());
            entry.getEntryDetailList().add(entryDetail);
            entryDetailDao.save(entryDetail);
        }
        return entryMapper.mapToDto(entryDao.save(entry));
    }
    public EntryDto confirmAndSumStock(Integer entryId) {
        Entry entry = entryDao.findEntryById(entryId);
        entry.getEntryDetailList().forEach(entryDetail -> {
            if (entryDetail.getSumStock())
                entryDetail.getProduct().setStock(
                        entryDetail.getProduct().getStock() + entryDetail.getQuantity());
            productDao.save(entryDetail.getProduct());
        });
        return entryMapper.mapToDto(entry);
    }
}