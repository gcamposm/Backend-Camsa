package ventas.backend.SQL.services;

import ventas.backend.SQL.dao.EntryDao;
import ventas.backend.SQL.dao.EntryDetailDao;
import ventas.backend.SQL.dao.ProductDao;
import ventas.backend.SQL.dao.SaleDetailDao;
import ventas.backend.SQL.dto.EntryDetailDto;
import ventas.backend.SQL.dto.ProductInterfaceDto;
import ventas.backend.SQL.mappers.EntryDetailMapper;
import ventas.backend.SQL.mappers.EntryMapper;
import ventas.backend.SQL.mappers.ProductInterfaceMapper;
import ventas.backend.SQL.models.Entry;
import ventas.backend.SQL.models.EntryDetail;
import ventas.backend.SQL.models.Product;
import ventas.backend.SQL.models.SaleDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EntryDetailService {

    @Autowired
    private EntryDetailDao entryDetailDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private EntryDao entryDao;

    @Autowired
    private EntryMapper entryMapper;

    @Autowired
    private SaleDetailDao saleDetailDao;

    @Autowired
    private ProductInterfaceMapper productMapper;

    @Autowired
    private EntryDetailMapper entryDetailMapper;

    public List<EntryDetailDto> getAllEntryDetails(){
        List<EntryDetail> entryDetailList = entryDetailDao.findAll();
        return entryDetailMapper.mapToDtoArrayList(entryDetailList);
    }

    public EntryDetailDto getEntryDetailById(Integer id){
        if(entryDetailDao.findById(id).isPresent()){
            return entryDetailMapper.mapToDto(entryDetailDao.findEntryDetailById(id));
        }
        else{
            return  null;
        }
    }
    public void updateEntryDetail(EntryDetailDto entryDetailDto, Integer id){
        if(entryDetailDao.findById(id).isPresent()){
            EntryDetail entryDetailFinded = entryDetailDao.findEntryDetailById(id);
            entryDetailFinded.setQuantity(entryDetailDto.getQuantity());
            entryDetailFinded.setProduct(entryDetailDto.getProduct());
            entryDetailFinded.setEntry(entryDetailDto.getEntry());
            entryDetailFinded.setSumStock(entryDetailDto.getSumStock());
            entryDetailDao.save(entryDetailFinded);

        }

    }
    public void deleteEntryDetail(Integer id){
        EntryDetail entryDetail = entryDetailDao.findEntryDetailById(id);
        Entry entry = entryDetail.getEntry();
        entryDetailDao.delete(entryDetail);
        entry.setTotal(entryDetailDao.sumAllSubTotals(entry));
        entryDao.save(entry);
    }

    public EntryDetailDto createEntryDetail(EntryDetailDto entryDetailDto){
        return entryDetailMapper.mapToDto(entryDetailDao.save(entryDetailMapper.mapToModel(entryDetailDto)));
    }

    public EntryDetailDto createEntryDetailWithProduct(ProductInterfaceDto productDto, Entry entry) {
        EntryDetail entryDetail = findEntryDetail(entry.getEntryDetailList(), productDto);
        if(entryDetail == null)
        {
            entryDetail = new EntryDetail();
            entryDetail.setQuantity(1);
            entryDetail.setEntry(entry);
            entryDetail.setType("stock");
            entryDetail.setProduct(productMapper.mapToModel(productDto, "oneProductDto"));
        }
        else{
            entryDetail.setQuantity(entryDetail.getQuantity() + 1);
        }
        productDto.setStock(productDto.getStock() + 1);
        productDao.save(productMapper.mapToModel(productDto, "oneProductDto"));
        entryDetailDao.save(entryDetail);
        entry.getEntryDetailList().add(entryDetail);
        entryDao.save(entry);
        return entryDetailMapper.mapToDto(entryDetailDao.save(entryDetail));
    }

    public EntryDetailDto quitProduct(ProductInterfaceDto productDto, Entry entry) {
        EntryDetail entryDetail = findEntryDetail(entry.getEntryDetailList(), productDto);
        if(entryDetail == null)
        {
            return null;
        }
        else{
            productDto.setStock(productDto.getStock() - entryDetail.getQuantity());
            entryDetail.setQuantity(0);
            productDao.save(productMapper.mapToModel(productDto, "oneProductDto"));
            entryDao.save(entry);
            return entryDetailMapper.mapToDto(entryDetailDao.save(entryDetail));
        }
    }

    private EntryDetail findEntryDetail(List<EntryDetail> entryDetailList, ProductInterfaceDto product){
        for (EntryDetail entryDetail: entryDetailList
        ) {
            if(entryDetail.getProduct().getId().equals(product.getId())){
                return entryDetail;
            }
        }
        return null;
    }

    public Object editQuantity(ProductInterfaceDto productDto, Entry entry, Integer quantity) {
        EntryDetail entryDetail = findEntryDetail(entry.getEntryDetailList(), productDto);
        if(entryDetail == null)
        {
            entryDetail = new EntryDetail();
        }
        productDto.setStock(productDto.getStock() - entryDetail.getQuantity());
        entryDetail.setQuantity(quantity);
        productDto.setStock(productDto.getStock() + quantity);
        productDao.save(productMapper.mapToModel(productDto, "oneProductDto"));
        entryDetail.setProduct(productMapper.mapToModel(productDto, "oneProductDto"));
        entryDetail.setEntry(entry);
        entryDetailDao.save(entryDetail);
        entry.getEntryDetailList().add(entryDetail);
        return entryDetailMapper.mapToDto(entryDetail);
    }

    public List<EntryDetailDto> getEntryDetails(Entry entry) {
        return entryDetailMapper.mapToDtoArrayList(entryDetailDao.findEntryDetailsByEntry(entry));
    }
    private Integer getTotalQuantity(EntryDetail entryDetailValue, EntryDetail entryDetailChange){
        int entryDetailValueQuantity = 0;
        int entryDetailChangeQuantity = 0;
        if(entryDetailValue != null){
            entryDetailValueQuantity = entryDetailValue.getQuantity();
        }
        if(entryDetailChange != null){
            entryDetailChangeQuantity = entryDetailChange.getQuantity();
        }
        return entryDetailChangeQuantity + entryDetailValueQuantity;
    }
    public EntryDetailDto appendEntryDetailForDevolution(Integer entryId,
                                                         Integer saleDetailId,
                                                         Integer quantity,
                                                         String type,
                                                         Boolean sumStock){
        Entry entry = entryDao.findEntryById(entryId);
        SaleDetail saleDetail = saleDetailDao.findSaleDetailById(saleDetailId);
        ProductInterfaceDto productDto = productMapper.mapToDto(saleDetail.getProduct(),"oneProductDto" );
        EntryDetail entryDetailValue = findEntryDetailByType(entry.getEntryDetailList(), productDto, "value");
        EntryDetail entryDetailChange = findEntryDetailByType(entry.getEntryDetailList(), productDto, "change");
        //calculo de cantidad en base a lo que está en la venta
        Integer quantityInSaleDetail = saleDetail.getQuantity();
        Integer actualTotalQuantityInEntries = getTotalQuantity(entryDetailValue,entryDetailChange);
        //arreglar en un futuro
        Integer realStock = quantityInSaleDetail;
        Integer realQuantity = 0;
        if(quantity >= realStock){
            realQuantity = realStock;
        }else{
            realQuantity = quantity;
        }
        if(entryDetailValue != null && type.equals("value")){
                entryDetailValue.setQuantity(realQuantity);
                entryDetailValue.setSubTotal(saleDetail.getUnitaryPrice().multiply(BigDecimal.valueOf(realQuantity)));
                entryDetailValue.setSumStock(sumStock);
                entryDetailDao.save(entryDetailValue);
                entry.setTotal(entryDetailDao.sumAllSubTotals(entry));
                entryDao.save(entry);
                return entryDetailMapper.mapToDto(entryDetailValue);
        }
        else if(entryDetailChange != null && type.equals("change")){
            entryDetailChange.setQuantity(realQuantity);
            entryDetailChange.setSumStock(sumStock);
            entryDetailDao.save(entryDetailChange);
            return entryDetailMapper.mapToDto(entryDetailChange);
        }else{
            EntryDetail entryDetail = createEntryDetail(realQuantity, entry, type,
                    productMapper.mapToModel(productDto, "oneProductDto"));
            if(type.equals("value")){
                entryDetail.setSubTotal(saleDetail.getUnitaryPrice().multiply(BigDecimal.valueOf(realQuantity)));
                entryDetail.setSumStock(sumStock);
                entryDetailDao.save(entryDetail);
                entry.setTotal(entryDetailDao.sumAllSubTotals(entry));
                entryDao.save(entry);
            }
            entryDetailDao.save(entryDetail);
            return entryDetailMapper.mapToDto(entryDetail);
        }
    }

    private EntryDetail createEntryDetail(Integer quantity, Entry entry, String type, Product product){
        EntryDetail entryDetail = new EntryDetail();
        entryDetail.setQuantity(quantity);
        entryDetail.setEntry(entry);
        entryDetail.setType(type);
        entryDetail.setProduct(product);
        return entryDetailDao.save(entryDetail);
    }

    private EntryDetail findEntryDetailByType(List<EntryDetail> entryDetailList, ProductInterfaceDto product, String type){
        for (EntryDetail entryDetail: entryDetailList
        ) {
            if(entryDetail.getProduct().getId().equals(product.getId()) && entryDetail.getType().equals(type)){
                return entryDetail;
            }
        }
        return null;
    }
}