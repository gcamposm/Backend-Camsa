package tingeso.backend.SQL.services;

import tingeso.backend.SQL.dao.*;
import tingeso.backend.SQL.dao.*;
import tingeso.backend.SQL.dto.SaleDto;
import tingeso.backend.SQL.dto.VoucherDto;
import tingeso.backend.SQL.mappers.SaleMapper;
import tingeso.backend.SQL.models.*;
import tingeso.backend.SQL.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SuppressWarnings("Duplicates")
@Service
public class SaleService {

    @Autowired
    private SaleDao saleDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SaleDetailDao saleDetailDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private DeskDao deskDao;


    @Autowired
    private SaleMapper saleMapper;

    public List<SaleDto> getAllSale(){
        List<Sale> saleList = saleDao.findAll();
        return saleMapper.mapToDtoList(saleList);
    }

    public SaleDto getSaleById(Integer id){
        if(saleDao.findById(id).isPresent()){
            Sale sale = saleDao.findSaleById(id);
            //if the sale is not finalized, the return value is the sale without the promotions
            if(!sale.getFinalized()){
                System.out.println("here");
                List<SaleDetail> recalculatedSDList = new ArrayList<>();
                List<SaleDetail> saleDetailList = sale.getSaleDetailList();
                for (SaleDetail saleDetail: saleDetailList
                     ) {
                    SaleDetail sdRecalculated = recalculateSaleDetail(saleDetail);
                    System.out.println("Recalculated: unitary: " + sdRecalculated.getUnitaryPrice() +
                            " discount: " + sdRecalculated.getDiscount() + " subTotal: " + sdRecalculated.getSubtotal());
                    recalculatedSDList.add(sdRecalculated);
                }
                sale.setSaleDetailList(recalculatedSDList);
                return saleMapper.mapToDto(sale);
            }
            return saleMapper.mapToDto(sale);
        }
        else{
            return  null;
        }
    }
    private SaleDetail recalculateSaleDetail(SaleDetail saleDetail){
        int quantity = saleDetail.getQuantity();
        BigDecimal unitaryPrice = saleDetail.getUnitaryPrice();
        BigDecimal discount = saleDetail.getDiscount();
        //use unitary * quantity is better idea for subtotal
        BigDecimal subTotal = saleDetail.getSubtotal();
        BigDecimal subTotalWithDiscount;

        BigDecimal productDiscount = saleDetail.calculateProductDiscount("Local");
        System.out.println("Discount: " + discount + ", ProductDiscount: " + productDiscount);
        discount = discount.subtract(productDiscount);
        subTotalWithDiscount = subTotal.subtract(discount);
        saleDetail.setDiscount(discount);
        saleDetail.setSubtotal(subTotal);
        saleDetail.setTotalWithDiscount(subTotalWithDiscount);
        return saleDetail;
    }
    public SaleDto updateSale(SaleDto saleDto, Integer id){
        if(saleDao.findById(id).isPresent()){
            Sale saleFinded = saleDao.findSaleById(id);
            saleFinded.setDescription(saleDto.getDescription());
            if(saleFinded.getPositiveBalance().subtract(saleDto.getPositiveBalance()).intValue() < 0)
            {
                saleFinded.setPositiveBalance(saleDto.getPositiveBalance());
                saleFinded.setTotalDiscount(saleDto.getTotalDiscount());
                BigDecimal bigDecimal = new BigDecimal(0);
                bigDecimal = bigDecimal.subtract(saleDto.getTotalDiscount().add(saleDto.getPositiveBalance()));
                System.out.println(bigDecimal);
                saleFinded.setTotalWithDiscount(bigDecimal);
            }
            return saleMapper.mapToDto(saleDao.save(saleFinded));
        }
        return null;
    }

    public void deleteSale(Integer id){
        Sale sale = saleDao.findSaleById(id);
        saleDao.delete(sale);
    }

    public SaleDto createSale(SaleDto saleDto){
        return saleMapper.mapToDto(saleDao.save(saleMapper.mapToModel(saleDto)));
    }

    public VoucherDto getVoucher(Integer sale_id){
        //Var
        Sale sale = saleDao.findSaleById(sale_id);
        if(sale != null){
            User user = sale.getUserventas();
            //List<ReservedRoom> reservedRoomList = reservation.getReservedRoomList();
            //Make voucher
            VoucherDto voucherDto = new VoucherDto();
            voucherDto.setGuestName(user.getFirstName() + " " + user.getLastName());
            BigDecimal total =  new java.math.BigDecimal("0");
            //VOUCHER reservedRoomLIST
            //List<VoucherPerRoomDto> voucherPerRoomDtoList = new ArrayList<>();
            //RESERVED ROOM
            /*if(reservedRoomList !=  null) {
                for (ReservedRoom reservedRoom : reservedRoomList
                ) {
                    List<RoomSaleDetail> roomSaleDetailList = reservedRoom.getRoomSaleDetailList();
                    VoucherPerRoomDto voucherPerRoomDto = new VoucherPerRoomDto();
                    voucherPerRoomDto.setRoom_number(reservedRoom.getRoom().getNumber());
                    voucherPerRoomDto.setDateIni(reservedRoom.getDateIni());
                    voucherPerRoomDto.setDateOut(reservedRoom.getDateOut());
                    //ADD COST PER DAY
                    voucherPerRoomDto = this.addDaysCost(voucherPerRoomDto, reservedRoom);

                    voucherPerRoomDto = this.addServicesCostList(voucherPerRoomDto, roomSaleDetailList);
                    voucherPerRoomDtoList.add(voucherPerRoomDto);
                    total = total.add(voucherPerRoomDto.getSub_total());
                }
            }*/
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            voucherDto.setTimeStamp(now);
            voucherDto.setDateTimeStamp(dtf.format(now));

            //voucherDto.setVoucherPerRoomDtoList(voucherPerRoomDtoList);
            voucherDto.setTotal(total);
            return voucherDto;
        }
        return null;
    }

    public SaleDto createSaleNewSale(Integer userId, Integer deskId) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Desk desk = deskDao.findDeskById(deskId);
        User user = userDao.findById(userId);
        Sale sale = new Sale();
        Sale lastSale = saleDao.findTopByOrderByIdDesc();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formatDateTime = localDateTime.format(formatter);
        BigDecimal zero = new BigDecimal("0");
        if(lastSale != null){
            Integer ticketNumber = lastSale.getTicketNumber();
            sale.setTicketNumber(ticketNumber + 1);
        }
        else{
            sale.setTicketNumber(1);
        }
        sale.setDesk(desk);
        sale.setFormatDate(formatDateTime);
        sale.setDate(localDateTime);
        sale.setTotal(zero);
        sale.setFinalized(false);
        sale.setTotalDiscount(zero);
        sale.setTotalQuantity(0);
        sale.setTotalWithDiscount(zero);
        sale.setUserventas(user);
        sale.setPositiveBalance(zero);
        return saleMapper.mapToDto(saleDao.save(sale));
    }

    //CANCELING SALE AND RETURNING STOCK TO PRODUCTS
    public SaleDto cancelSale(Integer id) {
        Sale sale = saleDao.findSaleById(id);
        List<SaleDetail> saleDetailList = sale.getSaleDetailList();
        //Return stock
        for (SaleDetail saleDetail: saleDetailList
        ) {
            Product product = saleDetail.getProduct();
            Integer stock = product.getStock();
            stock = stock + saleDetail.getQuantity();
            product.setStock(stock);
            productDao.save(product);
        }
        saleDao.delete(sale);
        return saleMapper.mapToDto(sale);
    }

    public SaleDto finalizeSale(Integer id){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formatDateTime = localDateTime.format(formatter);
        Sale sale = saleDao.findSaleById(id);
        sale.setFinalized(true);
        sale.setDate(localDateTime);
        sale.setFormatDate(formatDateTime);
        saleDao.save(sale);
        return saleMapper.mapToDto(sale);
    }

    public List<SaleDto> getCurrentsSales() {
        List<Sale> saleList;
        saleList = saleDao.findAllByFinalized(false);
        return saleMapper.mapToDtoList(saleList);
    }

    public SaleDto cleanSaleDetails(Integer id){
        Sale sale = saleDao.findSaleById(id);
        List<SaleDetail> saleDetailList = sale.getSaleDetailList();
        for (SaleDetail saleDetail: saleDetailList
        ) {
            if(saleDetail.getQuantity() == 0){
                System.out.println("Se va a borrar");
                saleDetailDao.deleteSaleDetail(saleDetail.getId());
                //sale.getSaleDetailList().remove(saleDetail);
            }
        }
        return saleMapper.mapToDto(saleDao.save(sale));
    }
    public SaleDto getSaleByTicketNumber(Integer ticketNumber){
        Sale sale = saleDao.findSaleByTicketNumber(ticketNumber);
        return saleMapper.mapToDto(sale);
    }

    public List<SaleDto> getFinalizedSales() {
        List<Sale> saleList;
        saleList = saleDao.findAllByFinalized(true);
        return saleMapper.mapToDtoList(saleList);
    }






}