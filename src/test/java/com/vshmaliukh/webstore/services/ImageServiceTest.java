package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Image;
import com.vshmaliukh.webstore.repositories.ImageRepository;
import com.vshmaliukh.webstore.repositories.ItemImageRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ImageServiceTest {

    @MockBean
    ImageRepository imageRepository;

    @MockBean
    ItemImageRepository itemImageRepository;

    @Autowired
    ImageService imageService;

    public static final String IMAGE_1_NAME = "image-1.png";
    public static final String IMAGE_2_NAME = "image-2.jpg";
    public static final String IMAGE_3_NAME = "image-3.webp";
    public static final String IMAGE_4_NAME = "image-4.jpg";
    public static final String IMAGE_5_NAME = "image-5.webp";
    public static final String IMAGE_6_NAME = "image-6.jpg";
    public static final String SRC_TEST_RESOURCES_PATH_STR = "src/test/resources/images";


    private static Stream<Arguments> providedArgs_buildImageFromFileTest() throws IOException {
        MultipartFile multipartFile1 = new MockMultipartFile(IMAGE_1_NAME, IMAGE_1_NAME, MediaType.IMAGE_PNG_VALUE, Files.newInputStream(Paths.get(SRC_TEST_RESOURCES_PATH_STR, IMAGE_1_NAME)));
        MultipartFile multipartFile2 = new MockMultipartFile(IMAGE_2_NAME, IMAGE_2_NAME, MediaType.IMAGE_JPEG_VALUE, Files.newInputStream(Paths.get(SRC_TEST_RESOURCES_PATH_STR, IMAGE_2_NAME)));
        MultipartFile multipartFile3 = new MockMultipartFile(IMAGE_3_NAME, IMAGE_3_NAME, MediaType.IMAGE_JPEG_VALUE, Files.newInputStream(Paths.get(SRC_TEST_RESOURCES_PATH_STR, IMAGE_3_NAME)));
        MultipartFile multipartFile4 = new MockMultipartFile(IMAGE_4_NAME, IMAGE_4_NAME, MediaType.IMAGE_JPEG_VALUE, Files.newInputStream(Paths.get(SRC_TEST_RESOURCES_PATH_STR, IMAGE_4_NAME)));
        MultipartFile multipartFile5 = new MockMultipartFile(IMAGE_5_NAME, IMAGE_5_NAME, MediaType.IMAGE_JPEG_VALUE, Files.newInputStream(Paths.get(SRC_TEST_RESOURCES_PATH_STR, IMAGE_5_NAME)));
        MultipartFile multipartFile6 = new MockMultipartFile(IMAGE_6_NAME, IMAGE_6_NAME, MediaType.IMAGE_JPEG_VALUE, Files.newInputStream(Paths.get(SRC_TEST_RESOURCES_PATH_STR, IMAGE_6_NAME)));
        return Stream.of(
                Arguments.of(multipartFile1),
                Arguments.of(multipartFile2),
                Arguments.of(multipartFile3),
                Arguments.of(multipartFile4),
                Arguments.of(multipartFile5),
                Arguments.of(multipartFile6)
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_buildImageFromFileTest")
    void buildImageFromFileTest(MultipartFile multipartFile) {
        Optional<Image> optionalImage = imageService.buildImageFromFile(multipartFile);

        assertTrue(optionalImage.isPresent());
        Image image = optionalImage.get();
        assertNotNull(image);
        assertTrue(StringUtils.isNotBlank(image.getName()));
        assertTrue(StringUtils.isNotBlank(image.getType()));
        assertNotNull(image.getImageData());
    }

    private static Stream<Arguments> providedArgs_saveImageTest() {
        return Stream.of(
//                image-1.png
//                image-2.jpg
//                image-3.webp
//                image-4.jpg
//                image-5.webp
//                image-6.jpg
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_saveImageTest")
    void saveImageTest(Image image) {

        imageService.saveImage(image);
    }

}