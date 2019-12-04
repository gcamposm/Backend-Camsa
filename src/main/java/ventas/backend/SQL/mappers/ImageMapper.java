package ventas.backend.SQL.mappers;

import ventas.backend.SQL.dto.ImageDto;
import ventas.backend.SQL.models.Image;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
@Component
public class ImageMapper {
    public Image mapToModel(ImageDto imageDto){

        Image image = new Image();
        image.setId(imageDto.getId());
        image.setExtension(imageDto.getExtension());
        image.setName(imageDto.getName());
        image.setPrincipal(imageDto.getPrincipal());
        image.setProduct(imageDto.getProduct());
        return image;
    }

    public List<ImageDto> mapToDtoArrayList(List<Image> imageArrayList) {
        int i;

        List<ImageDto> imageDtoArrayList = new ArrayList<>();
        for(i=0;i<imageArrayList.size();i++){
            imageDtoArrayList.add(mapToDto(imageArrayList.get(i)));
        }

        return imageDtoArrayList;
    }

    public ImageDto mapToDto (Image image){

        ImageDto imageDto = new ImageDto();
        imageDto.setId(image.getId());
        imageDto.setExtension(image.getExtension());
        imageDto.setName(image.getName());
        imageDto.setPrincipal(image.getPrincipal());
        imageDto.setProduct(image.getProduct());
        return imageDto;
    }
}
