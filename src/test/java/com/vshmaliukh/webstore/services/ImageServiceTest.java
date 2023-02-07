package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Image;
import com.vshmaliukh.webstore.model.ItemImage;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Book;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Comics;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Magazine;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Newspaper;
import com.vshmaliukh.webstore.repositories.ImageRepository;
import com.vshmaliukh.webstore.repositories.ItemImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    // TODO fix test
//    @Test
//    void buildImageFromFile_exception(CapturedOutput output){
//        MultipartFile mockMultipartFile = new MockMultipartFile("some name", (byte[]) null);
//        Optional<Image> optionalImage = imageService.buildImageFromFile(mockMultipartFile);
//
//        assertThrows(IOException.class, () -> new FileWriter("/no/such/place"));
//        assertFalse(optionalImage.isPresent());
//        assertTrue(output.getOut().contains("problem to build image out of the file // IOException"));
//    }

    @Test
    void saveImageTest_success(CapturedOutput output) {
        Optional<Image> optionalImage = imageService.buildImageFromFile(multipartFile1);
        assertTrue(optionalImage.isPresent());
        imageService.saveImage(optionalImage.get());

        assertTrue(output.getOut().contains("saved image"));
        assertFalse(output.getOut().contains("image not saved "));
    }

    @Test
    void saveImageTest_null(CapturedOutput output) {
        imageService.saveImage(null);

        assertFalse(output.getOut().contains("deleted image"));
        assertTrue(output.getOut().contains("image not saved // invalid image"));
    }


    @Test
    void deleteImageTest(CapturedOutput output) {
        Optional<Image> optionalImage = imageService.buildImageFromFile(multipartFile1);
        assertTrue(optionalImage.isPresent());
        Image image = optionalImage.get();
        imageService.deleteImage(image);

        assertTrue(output.getOut().contains("deleted image: '" + image + "'"));
        assertFalse(output.getOut().contains("image not deleted"));
    }

    @Test
    void deleteItemImageTest(CapturedOutput output) {
        Optional<Image> optionalImage = imageService.buildImageFromFile(multipartFile1);
        assertTrue(optionalImage.isPresent());
        Image image = optionalImage.get();
        ItemImage itemImage = new ItemImage(image, new Magazine());
        imageService.deleteImage(itemImage);

        assertTrue(output.getOut().contains("deleted image: '" + itemImage + "'"));
        assertFalse(output.getOut().contains("image not deleted"));
    }

    @Test
    void deleteImageTest_null(CapturedOutput output) {
        imageService.deleteImage(null);

        assertFalse(output.getOut().contains("deleted image"));
        assertTrue(output.getOut().contains("image not deleted // invalid image"));
    }

    private static Stream<Long> providedArgs_findImageByIdTest() {
        return Stream.of(1L, 2L, 3L, 1_000_000L, 1234_5678_9012_3456L);
    }

    @ParameterizedTest
    @MethodSource("providedArgs_findImageByIdTest")
    void findImageByIdTest(Long id) {
        Image image = null;
        Optional<Image> optionalImage = imageService.buildImageFromFile(multipartFile1);
        if (optionalImage.isPresent()) {
            image = optionalImage.get();
            image.setId(id);
        }
        Mockito
                .when(imageRepository.findById(id))
                .thenReturn(Optional.ofNullable(image));

        Optional<Image> optionalImageById = imageService.readImageById(id);
        assertTrue(optionalImageById.isPresent());
        Image imageById = optionalImageById.get();
        assertNotNull(imageById.getId());
        assertNotNull(imageById.getName());
        assertNotNull(imageById.getType());
        assertNotNull(imageById.getImageData());
    }

    private static Stream<Long> providedArgs_findImageByIdTest_invalid() {
        return Stream.of(0L, -1L, -123_456_789_101L, null);
    }

    @ParameterizedTest
    @MethodSource("providedArgs_findImageByIdTest_invalid")
    void findImageByIdTest_invalid(Long id) {
        Optional<Image> optionalImageById = imageService.readImageById(id);

        assertFalse(optionalImageById.isPresent());
    }

    @Test
    void findImageByIdTest_null(CapturedOutput output) {
        imageService.readImageById(null);

        assertTrue(output.getOut().contains("problem to find image by id // id is NULL"));
    }

    @Test
    void findImageByIdTest_zero(CapturedOutput output) {
        imageService.readImageById(0L);

        assertTrue(output.getOut().contains("problem to find image by id // id < 1"));
    }

    private static Stream<Arguments> providedArgs_findImageListByItemTest() {
        return Stream.of(
                Arguments.of(new Book(), Collections.singletonList(new ItemImage(new Image(), new Book()))),
                Arguments.of(new Comics(), Collections.singletonList(new ItemImage(new Image(), new Comics()))),
                Arguments.of(new Magazine(), Collections.singletonList(new ItemImage(new Image(), new Magazine()))),
                Arguments.of(new Newspaper(), Collections.singletonList(new ItemImage(new Image(), new Newspaper()))),
                Arguments.of(new Newspaper(), Arrays.asList(new ItemImage(new Image(), new Newspaper()), new ItemImage(new Image(), new Newspaper()), new ItemImage(new Image(), new Newspaper())))
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_findImageListByItemTest")
    void findImageListByItemTest(Item item, List<ItemImage> imageList) {
        Mockito
                .when(itemImageRepository.findImagesByItem(item))
                .thenReturn(imageList);
        List<ItemImage> imageListByItem = imageService.readImageListByItem(item);

        assertNotNull(imageListByItem);
        assertFalse(imageListByItem.isEmpty());
        assertEquals(imageList.size(), imageListByItem.size());
        imageListByItem.forEach(itemImage -> assertEquals(item, itemImage.getItem()));
    }

    private static Stream<Arguments> providedArgs_findImageListByItemTest_emptyList() {
        return Stream.of(
                Arguments.of(new Magazine()),
                Arguments.of(new Book()),
                Arguments.of(new Comics()),
                Arguments.of(new Newspaper()),
                Arguments.of((Object) null)
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_findImageListByItemTest_emptyList")
    void findImageListByItemTest_emptyList(Item item) {
        Mockito
                .when(itemImageRepository.findImagesByItem(item))
                .thenReturn(Collections.emptyList());
        List<ItemImage> imageListByItem = imageService.readImageListByItem(item);

        assertNotNull(imageListByItem);
        assertTrue(imageListByItem.isEmpty());
    }

    private static Stream<Arguments> providedArgs_formItemImageFromFileTest() {
        return Stream.of(
                Arguments.of(new Book(), multipartFile1),
                Arguments.of(new Comics(), multipartFile1),
                Arguments.of(new Magazine(), multipartFile1),
                Arguments.of(new Newspaper(), multipartFile1),
                Arguments.of(new Newspaper(), multipartFile2),
                Arguments.of(new Newspaper(), multipartFile3),
                Arguments.of(new Newspaper(), multipartFile4),
                Arguments.of(new Newspaper(), multipartFile5),
                Arguments.of(new Newspaper(), multipartFile6)
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_formItemImageFromFileTest")
    void formItemImageFromFileTest(Item item, MultipartFile multipartFile) {
        Optional<ItemImage> optionalItemImage = imageService.formItemImageFromFile(item, multipartFile);

        assertTrue(optionalItemImage.isPresent());
        ItemImage itemImage = optionalItemImage.get();
        assertNotNull(itemImage);
        assertNotNull(itemImage.getName());
        assertNotNull(itemImage.getType());
        assertNotNull(itemImage.getItem());
        assertNotNull(itemImage.getImageData());
        assertEquals(itemImage.getItem(), item);
    }

    private static Stream<Arguments> providedArgs_formItemImageFromFileTest_invalid() {
        return Stream.of(
                Arguments.of(new Book(), emptyMultipartFile),
                Arguments.of(new Magazine(), null),
                Arguments.of(null, multipartFile1),
                Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_formItemImageFromFileTest_invalid")
    void formItemImageFromFileTest_invalid(Item item, MultipartFile multipartFile) {
        Optional<ItemImage> optionalItemImage = imageService.formItemImageFromFile(item, multipartFile);

        assertFalse(optionalItemImage.isPresent());
    }

    @Test
    void formItemImageFromFileTest_itemIsNull(CapturedOutput output) {
        Optional<ItemImage> optionalItemImage = imageService.formItemImageFromFile(null, multipartFile1);

        assertFalse(optionalItemImage.isPresent());
        assertTrue(output.getOut().contains("problem to generate item image // item is NULL"));
    }

    @Test
    void formItemImageFromFileTest_fileIsEmpty(CapturedOutput output) {
        Optional<ItemImage> optionalItemImage = imageService.formItemImageFromFile(new Newspaper(), emptyMultipartFile);

        assertFalse(optionalItemImage.isPresent());
        assertTrue(output.getOut().contains("problem to generate item image // problem to generate image out of file"));
    }

    @Test
    void formItemImageFromFileTest_fileIsNull(CapturedOutput output) {
        Optional<ItemImage> optionalItemImage = imageService.formItemImageFromFile(new Newspaper(), null);

        assertFalse(optionalItemImage.isPresent());
        assertTrue(output.getOut().contains("problem to generate item image // problem to generate image out of file"));
    }

}