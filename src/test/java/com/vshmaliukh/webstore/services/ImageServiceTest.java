package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Image;
import com.vshmaliukh.webstore.repositories.ImageRepository;
import com.vshmaliukh.webstore.repositories.ItemImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class ImageServiceTest {

    @MockBean
    ImageRepository imageRepository;

    @MockBean
    ItemImageRepository itemImageRepository;

    @Autowired
    ImageService imageService;

    static final String IMAGE_1_NAME = "image-1.png";
    static final String IMAGE_2_NAME = "image-2.jpg";
    static final String IMAGE_3_NAME = "image-3.webp";
    static final String IMAGE_4_NAME = "image-4.jpg";
    static final String IMAGE_5_NAME = "image-5.webp";
    static final String IMAGE_6_NAME = "image-6.jpg";
    static final String SRC_TEST_RESOURCES_PATH_STR = "src/test/resources/images";

    static final MultipartFile multipartFile1;
    static final MultipartFile multipartFile2;
    static final MultipartFile multipartFile3;
    static final MultipartFile multipartFile4;
    static final MultipartFile multipartFile5;
    static final MultipartFile multipartFile6;
    static final MultipartFile emptyMultipartFile;

    static {
        try {
            multipartFile1 = new MockMultipartFile(IMAGE_1_NAME, IMAGE_1_NAME, MediaType.IMAGE_PNG_VALUE, Files.newInputStream(Paths.get(SRC_TEST_RESOURCES_PATH_STR, IMAGE_1_NAME)));
            multipartFile2 = new MockMultipartFile(IMAGE_2_NAME, IMAGE_2_NAME, MediaType.IMAGE_JPEG_VALUE, Files.newInputStream(Paths.get(SRC_TEST_RESOURCES_PATH_STR, IMAGE_2_NAME)));
            multipartFile3 = new MockMultipartFile(IMAGE_3_NAME, IMAGE_3_NAME, MediaType.IMAGE_JPEG_VALUE, Files.newInputStream(Paths.get(SRC_TEST_RESOURCES_PATH_STR, IMAGE_3_NAME)));
            multipartFile4 = new MockMultipartFile(IMAGE_4_NAME, IMAGE_4_NAME, MediaType.IMAGE_JPEG_VALUE, Files.newInputStream(Paths.get(SRC_TEST_RESOURCES_PATH_STR, IMAGE_4_NAME)));
            multipartFile5 = new MockMultipartFile(IMAGE_5_NAME, IMAGE_5_NAME, MediaType.IMAGE_JPEG_VALUE, Files.newInputStream(Paths.get(SRC_TEST_RESOURCES_PATH_STR, IMAGE_5_NAME)));
            multipartFile6 = new MockMultipartFile(IMAGE_6_NAME, IMAGE_6_NAME, MediaType.IMAGE_JPEG_VALUE, Files.newInputStream(Paths.get(SRC_TEST_RESOURCES_PATH_STR, IMAGE_6_NAME)));
            emptyMultipartFile = new MockMultipartFile("empty file", new byte[]{});
        } catch (IOException ioe) {
            log.error(ioe.getMessage(), ioe);
            throw new RuntimeException(ioe);
        }
    }


    private static Stream<Arguments> providedArgs_buildImageFromFileTest() {
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

    @Test
    void buildImageFromFileTest_null(CapturedOutput output) {
        Optional<Image> optionalImage = imageService.buildImageFromFile(null);

        assertTrue(output.getOut().contains("problem to build image out of the file // file is NULL"));
        assertFalse(optionalImage.isPresent());
    }

    @Test
    void buildImageFromFileTest_emptyFile(CapturedOutput output) {
        Optional<Image> optionalImage = imageService.buildImageFromFile(emptyMultipartFile);

        assertTrue(output.getOut().contains("problem to build image out of the file"));
        assertFalse(optionalImage.isPresent());
    }

    @Test
    void saveImageTest_success(CapturedOutput output) {
        Optional<Image> optionalImage = imageService.buildImageFromFile(multipartFile1);
        optionalImage.ifPresent(image -> imageService.saveImage(image));

        assertTrue(output.getOut().contains("saved image"));
    }

    @Test
    void saveImageTest_null(CapturedOutput output) {
        imageService.saveImage(null);

        assertTrue(output.getOut().contains("image not saved // invalid image"));
    }


}