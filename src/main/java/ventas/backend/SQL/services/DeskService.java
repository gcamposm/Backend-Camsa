package ventas.backend.SQL.services;

import ventas.backend.SQL.dao.DeskDao;
import ventas.backend.SQL.dao.PhysicalDeskDao;
import ventas.backend.SQL.dao.UserDao;
import ventas.backend.SQL.dto.DeskDto;
import ventas.backend.SQL.mappers.DeskMapper;
import tingeso.backend.SQL.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ventas.backend.SQL.models.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("Duplicates")
@Service
public class DeskService {
    @Autowired
    private DeskDao deskDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PhysicalDeskDao physicalDeskDao;

    @Autowired
    private DeskMapper deskMapper;

    public List<DeskDto> getAllDesk(){
        List<Desk> deskList = deskDao.findAll();
        return deskMapper.mapToDtoArrayList(deskList);
    }

    public DeskDto getDeskById(Integer id){
        if(deskDao.findById(id).isPresent()){
            return deskMapper.mapToDto(deskDao.findDeskById(id));
        }
        else{
            return  null;
        }
    }
    public void updateDesk(DeskDto deskDto, Integer id){
        if(deskDao.findById(id).isPresent()){
            Desk deskFinded = deskDao.findDeskById(id);
            deskFinded.setDateOpen(deskDto.getDateOpen());
            deskFinded.setUserventas(deskDto.getUserventas());
            deskFinded.setDateClose(deskDto.getDateClose());
            deskFinded.setIsOpen(deskDto.getIsOpen());
            deskFinded.setTotal(deskDto.getTotal());
            deskFinded.setSaleList(deskDto.getSaleList());
            deskFinded.setPhysicalDesk(deskDto.getPhysicalDesk());
            deskFinded.setInitialMoney(deskDto.getInitialMoney());
            deskDao.save(deskFinded);
        }
    }

    public void deleteDesk(Integer id){
        Desk desk = deskDao.findDeskById(id);
        deskDao.delete(desk);

    }

    public DeskDto createDesk(DeskDto deskDto){
        return deskMapper.mapToDto(deskDao.save(deskMapper.mapToModel(deskDto)));
    }


    //Verificia si hay alguna caja abierta asociada al usuario o caja fisica
    public String isDeskOpen(int userId, int physicalDeskId){
        User user = userDao.findById(userId);
        PhysicalDesk physicalDesk = physicalDeskDao.findPhysicalDeskById(physicalDeskId);
        //obtain the current open desk by user
        Desk deskOpenByUser = deskDao.findDeskByIsOpenAndUserventas(true, user);
        //obtain the current open desk by physicalDesk
        Desk physicalDeskOpen = deskDao.findDeskByIsOpenAndPhysicalDesk(true, physicalDesk);
        //obtain the last desk by physical desk
        Desk lastDesk = getLastDeskByPhysicalDesk(physicalDeskId);
        if(deskOpenByUser != null || physicalDeskOpen != null){
            return "opened";
        }
        else if(lastDesk == null){
            return "nonDeskCreated";
        }
        else{
            return "closed";
        }
    }
    private Desk getLastDeskByPhysicalDesk(int physicalDeskId) throws NullPointerException
    {
        PhysicalDesk physicalDesk = physicalDeskDao.findPhysicalDeskById(physicalDeskId);
        try {
            return deskDao.findDeskByPhysicalDeskOrderByIdDesc(physicalDesk).get(0);
        }catch (Exception ex){
            return null;
        }
    }
    public DeskDto createNewDesk(int userId, int iniMoney, int physicalDeskId) {
        String isOpen = isDeskOpen(userId, physicalDeskId);
        Desk lastDeskOpenedByPhysicalDesk = getLastDeskByPhysicalDesk(physicalDeskId);
        if(isOpen.equals("closed") && lastDeskOpenedByPhysicalDesk != null){
            int initialMoney = calculateInitialMoney(lastDeskOpenedByPhysicalDesk);
            return getDeskDto(initialMoney + iniMoney, userId, physicalDeskId);
        }
        else if(isOpen.equals("nonDeskCreated")){
            System.out.println("noncreated");
            return getDeskDto(iniMoney, userId, physicalDeskId);
        }
        else if(isOpen.equals("opened")){
            return null;
        }
        System.out.println("here");
        return null;
    }

    private int calculateInitialMoney(Desk desk){
        Map<String, Integer> deskTotals = totalByDeskId(desk.getId());
        return (deskTotals.get("efectivo") + desk.getInitialMoney() - desk.getWithdraw());
    }

    private DeskDto getDeskDto(int initialMoney, int userId, int physicalDeskId) {
        Desk desk = new Desk();
        User user = userDao.findById(userId);
        PhysicalDesk physicalDesk = physicalDeskDao.findPhysicalDeskById(physicalDeskId);
        LocalDateTime localDateTime = LocalDateTime.now();
        desk.setInitialMoney(initialMoney);
        desk.setDateOpen(localDateTime);
        desk.setDateClose(null);
        desk.setIsOpen(true);
        desk.setTotal(0);
        desk.setWithdraw(0);
        desk.setUserventas(user);
        desk.setPhysicalDesk(physicalDesk);
        return deskMapper.mapToDto(deskDao.save(desk));
    }
    public DeskDto actualDesk(Integer userId, Integer physicalDeskId) {
        Desk desk = deskDao.findDeskByIsOpenAndUserventasAndPhysicalDesk(true,
                userDao.findById(userId),
                physicalDeskDao.findPhysicalDeskById(physicalDeskId));
        if(desk == null)
        {
            return null;
        }
        return deskMapper.mapToDto(deskDao.save(desk));
    }
    public DeskDto getLastDesk(Integer physicalDeskId){
        PhysicalDesk physicalDesk = physicalDeskDao.findPhysicalDeskById(physicalDeskId);
        List<Desk> deskList = physicalDesk.getDeskList();
        if(deskList.size() == 0) {
            return null;
        }else{
            return deskMapper.mapToDto(deskList.get(deskList.size() - 1));
        }
    }
    public DeskDto addMoney(Integer userId, Integer moneyToAdd, Integer physicalDeskId) {
        Desk desk = deskDao.findDeskByIsOpenAndUserventasAndPhysicalDesk(true, userDao.findById(userId),
                physicalDeskDao.findPhysicalDeskById(physicalDeskId));
        if(desk == null)
        {
            return null;
        } else{
            desk.setInitialMoney(desk.getInitialMoney() + moneyToAdd);
            return deskMapper.mapToDto(deskDao.save(desk));
        }
    }
    public DeskDto closeDesk(Integer userId, Integer withdraw, Integer physicalDeskId) {
        User user = userDao.findById(userId);
        //Desk deskOpenByUser = deskDao.findDeskByIsOpenAndUserventas(true, user);
        Desk deskOpenByUser = deskDao.findDeskByIsOpenAndUserventasAndPhysicalDesk(true,
                user, physicalDeskDao.findPhysicalDeskById(physicalDeskId));
        if (deskOpenByUser == null) {
            return null;
        }else{
            deskOpenByUser.calculateTotal();
            deskOpenByUser.setWithdraw(withdraw);
            LocalDateTime localDateTime = LocalDateTime.now();
            deskOpenByUser.setIsOpen(false);
            deskOpenByUser.setDateClose(localDateTime);
            return deskMapper.mapToDto(deskDao.save(deskOpenByUser));
        }
    }
    public DeskDto actualizeTotal(Integer userId, Integer physicalDeskId){
        User user = userDao.findById(userId);
        Desk deskOpenByUser = deskDao.findDeskByIsOpenAndUserventasAndPhysicalDesk(true,
                user, physicalDeskDao.findPhysicalDeskById(physicalDeskId));
        deskOpenByUser.calculateTotal();
        return deskMapper.mapToDto(deskDao.save(deskOpenByUser));
    }
    public DeskDto createNewDesk1(int userId, int iniMoney, int physicalDeskId) {
        User user = userDao.findById(userId);
        Desk lastDesk = deskDao.findTopByOrderByIdDesc();
        //Desk lastDesk = deskDao.findDeskByPhysicalDeskByOrderByIdDesc(physicalDesk);
        Desk deskOpenByUser = deskDao.findDeskByIsOpenAndUserventas(true, user);
        if(lastDesk != null) {
            if (deskOpenByUser == null) {
                int initialMoney = lastDesk.getTotal() - lastDesk.getWithdraw();
                return getDeskDto(initialMoney, userId, physicalDeskId);
            }else{
                return deskMapper.mapToDto(deskOpenByUser);
            }
        }
        else {
            return getDeskDto(iniMoney, userId, physicalDeskId);
        }

    }
    public DeskDto closeDesk1( long userId, int withdraw, Integer deskId){
        Desk desk = deskDao.findDeskById(deskId);
        if(desk != null){
            boolean isOpen = desk.getIsOpen();
            if(isOpen && (userId == desk.getUserventas().getId())){
                LocalDateTime localDateTime = LocalDateTime.now();
                desk.setDateClose(localDateTime);
                desk.calculateTotal();
                desk.setWithdraw(withdraw);
                desk.setIsOpen(false);
                return deskMapper.mapToDto(deskDao.save(desk));
            }
            return null;
        }
        return null;
    }
    public Integer getTotalByIsOpenedAndPhysicalDeskId(int physicalDeskId){
        PhysicalDesk physicalDesk = physicalDeskDao.findPhysicalDeskById(physicalDeskId);
        if(physicalDesk != null) {
            Desk desk = deskDao.findFirstByIsOpenAndPhysicalDeskOrderByIdDesc(false, physicalDesk);
            if(desk != null) {
                Map <String, Integer> result = totalByDeskId(desk.getId());
                int efectivoTotal = result.get("efectivoTotal");
                result.replace("efectivoTotal", efectivoTotal - desk.getWithdraw());
                return result.get("efectivoTotal");
            }
        }
        return null;
    }
    public Map<String,Integer> totalByDeskId(int deskId) {
        Desk desk = deskDao.findDeskById(deskId);
        if(desk == null)
        {
            return null;
        }
        Integer credito = 0;
        Integer debito = 0;
        Integer efectivo = 0;
        Integer cheques = 0;
        Integer transfer = 0;
        for (Sale sale: desk.getSaleList()
             ) {
            if(sale.getFinalized() == true) {
                for (SalePaymentMethod salePaymentMethod : sale.getSalePaymentMethodList()
                ) {
                    PaymentMethod paymentMethod = salePaymentMethod.getPaymentMethod();
                    switch (paymentMethod.getType()) {
                        case "Crédito":
                            credito = credito + salePaymentMethod.getAmount();
                            break;
                        case "Débito":
                            debito = debito + salePaymentMethod.getAmount();
                            break;
                        case "Efectivo":
                            efectivo = efectivo + salePaymentMethod.getAmount() - salePaymentMethod.getExchange();
                            break;
                        case "Cheques":
                            cheques = cheques + salePaymentMethod.getAmount();
                            break;
                        case "Transferencia bancaria":
                            transfer = transfer + salePaymentMethod.getAmount();
                            break;
                    }
                }
            }
        }
        Map<String, Integer> model = new HashMap<>();
        model.put("credito", credito);
        model.put("debito", debito);
        model.put("cheques", cheques);
        model.put("transfer", transfer);
        model.put("efectivo", efectivo);
        model.put("efectivoCaja", desk.getInitialMoney());
        model.put("efectivoTotal", efectivo + desk.getInitialMoney());
        return model;
    }
}