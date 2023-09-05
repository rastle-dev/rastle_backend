package rastle.dev.rastle_backend.global.component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import rastle.dev.rastle_backend.domain.Product.model.Image;
import rastle.dev.rastle_backend.domain.Product.model.ProductImage;
import rastle.dev.rastle_backend.global.error.exception.S3ImageUploadException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Component
@RequiredArgsConstructor
public class S3Component {

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<Image> uploadAndGetImageList(List<MultipartFile> images, ProductImage image) {
        List<Image> toReturn = new ArrayList<>();
        for (MultipartFile mainImageFile : images) {
            Image newImage = new Image(uploadSingleImageToS3(mainImageFile), image);
            toReturn.add(newImage);
        }
        return toReturn;
    }

    public String uploadImagesAndGetString(List<MultipartFile> images) {
        StringBuilder sb = new StringBuilder();
        for (MultipartFile image : images) {
            sb.append(uploadSingleImageToS3(image)).append(",");
        }
        return sb.toString();
    }


    public String uploadSingleImageToS3(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        try {

            amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);
        } catch (IOException e) {
            throw new S3ImageUploadException();
        }
        return amazonS3.getUrl(bucket, fileName).toString();
    }
}