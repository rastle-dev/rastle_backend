package rastle.dev.rastle_backend.global.component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import rastle.dev.rastle_backend.domain.admin.exception.InvalidImageUrlException;
import rastle.dev.rastle_backend.domain.product.model.ProductImage;
import rastle.dev.rastle_backend.global.error.exception.S3ImageUploadException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class S3Component {

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private static final String IMAGE_PREFIX = "https://rastle-dev.s3.ap-northeast-2.amazonaws.com/";

    public void deleteImageByUrl(String imageUrl) {
        if (imageUrl != null) {
            log.info("delete file name " + getFileName(imageUrl));
            amazonS3.deleteObject(bucket, getFileName(imageUrl));
        }

    }

    private String getFileName(String url) {
        if (!url.contains(IMAGE_PREFIX)) {
            throw new InvalidImageUrlException();
        }
        return url.substring(IMAGE_PREFIX.length());
    }

    public ProductImage uploadAndGetImageUrlList(String type, List<MultipartFile> images) {
        List<String> toReturn = new ArrayList<>();
        for (MultipartFile mainImageFile : images) {
            toReturn.add(uploadSingleImageToS3(type, mainImageFile));
        }
        return new ProductImage(toReturn);
    }

    public String uploadImagesAndGetString(String type, List<MultipartFile> images) {
        StringBuilder sb = new StringBuilder();
        for (MultipartFile image : images) {
            sb.append(uploadSingleImageToS3(type, image)).append(",");
        }
        return sb.toString();
    }


    public String uploadSingleImageToS3(String type, MultipartFile file) {
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
