package ventas.backend.SQL.services;

import ventas.backend.SQL.dao.ImageDao;
import ventas.backend.SQL.dao.ProductDao;
import ventas.backend.SQL.dto.ImageDto;
import ventas.backend.SQL.mappers.ImageMapper;
import ventas.backend.SQL.models.Image;
import ventas.backend.SQL.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {
    @Autowired
    private ImageDao imageDao;

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private ProductDao productDao;

    public List<ImageDto> getAllImages(){
        List<Image> imageList = imageDao.findAll();
        return imageMapper.mapToDtoArrayList(imageList);
    }

    public ImageDto getImageById(Integer id){
        if(imageDao.findById(id).isPresent()){
            return imageMapper.mapToDto(imageDao.findImageById(id));
        }
        else{
            return  null;
        }
    }
    public void updateImage(ImageDto imageDto, Integer id){
        if(imageDao.findById(id).isPresent()){
            Image imageFinded = imageDao.findImageById(id);
            imageFinded.setExtension(imageDto.getExtension());
            imageFinded.setName(imageDto.getName());
            imageFinded.setPrincipal(imageDto.getPrincipal());
            imageFinded.setProduct(imageDto.getProduct());
            imageDao.save(imageFinded);

        }

    }
    public String deleteImage(String name){
        Image image = imageDao.findImageByName(name);
        if (image.getPrincipal())
        {
            return "No se puede eliminar la imagen principal";
        }
        imageDao.delete(image);
        return "Imagen eliminada con éxito";
    }

    public ImageDto createImage(ImageDto imageDto){
        return imageMapper.mapToDto(imageDao.save(imageMapper.mapToModel(imageDto)));
    }

    public String[] uploadMultipleImages(String[] imageNames) {
        for (String name: imageNames
             ) {
            String code[] = name.split("_");
            String imageName[] = name.split(".");
              Product product = productDao.findProductByCode(code[0]);
            if(product != null){
                Image image = new Image();
                image.setName(name);
                image.setPrincipal(true);
                image.setProduct(product);
                image.setExtension(".jpg");
                imageDao.save(image);
            }

        }
        return imageNames;
    }
}
