package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.login.LogInProvider;
import com.vshmaliukh.webstore.login.UserRole;
import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;
import java.util.stream.Stream;

import static com.vshmaliukh.webstore.services.UserService.DEFAULT_PASSWORD;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class UserServiceTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    UserService userService;

    // TODO implement validation check
    Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private static Stream<Long> providedArgs_readUserByIdTest() {
        return Stream.of(1L, 2L, 3L, 1_000_000L, 1234_5678_9012_3456L);
    }

    @ParameterizedTest
    @MethodSource("providedArgs_readUserByIdTest")
    void readUserByIdTest(Long id) {
        Optional<User> optionalUserToReturn = Optional.of(
                new User(id, "some username", "some@mail.com", LogInProvider.LOCAL, UserRole.ADMIN, "1234", true));
        Mockito
                .when(userRepository.findById(id))
                .thenReturn(optionalUserToReturn);
        Optional<User> optionalUser = userService.readUserById(id);

        assertTrue(optionalUser.isPresent());
        User actualUser = optionalUser.get();
        User expercteduser = optionalUserToReturn.get();
        assertEquals(expercteduser.getId(), actualUser.getId());
        assertEquals(optionalUserToReturn, optionalUser);
        assertEquals(expercteduser, actualUser);
    }

    private static Stream<Long> providedArgs_readUserByIdTest_failToFind() {
        return Stream.of(0L, -1L, -1234_5678_9012_3456L, null);
    }

    @ParameterizedTest
    @MethodSource("providedArgs_readUserByIdTest_failToFind")
    void readUserByIdTest_failToFind(Long id) {
        Mockito
                .when(userRepository.findById(id))
                .thenReturn(Optional.empty());
        Optional<User> optionalUser = userService.readUserById(id);

        assertFalse(optionalUser.isPresent());
    }

    private static Stream<Arguments> providedArgs_isAdminUserTest() {
        User user1 = new User(null, "some username1", "some1@mail.com", LogInProvider.LOCAL, UserRole.ADMIN, "1234", true);
        User user2 = new User(null, "some username2", "some2@mail.com", LogInProvider.LOCAL, UserRole.CUSTOMER, "1234", true);
        User user3 = new User(null, "some username2", "some2@mail.com", LogInProvider.LOCAL, null, "1234", true);
        return Stream.of(
                Arguments.of(user1, true),
                Arguments.of(user2, false),
                Arguments.of(user3, false),
                Arguments.of(null, false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_isAdminUserTest")
    void isAdminUserTest(User user, boolean isAdminExpectedResult) {
        boolean isAdminResult = userService.isAdminUser(user);

        assertEquals(isAdminResult, isAdminExpectedResult);
    }

    @Test
    void saveTest_null(CapturedOutput output) {
        userService.save(null);

        assertTrue(output.getOut().contains("user not saved // invalid user"));
    }


    @Test
    void saveTest(CapturedOutput output) {
        User user = new User(1L, "some username", "some@mail.com", LogInProvider.LOCAL, UserRole.ADMIN, "1234", true);
        userService.save(user);

        assertTrue(output.getOut().contains("saved user"));
    }


    private static Stream<Arguments> providedArgs_isUserSavedTest() {
        User user1 = new User(1L, "some username1", "some1@mail.com", LogInProvider.LOCAL, UserRole.ADMIN, "1234", true);
        User user2 = new User(2L, "some username2", "some2@mail.com", LogInProvider.LOCAL, UserRole.CUSTOMER, "1234", true);
        User user3 = new User(3L, "some username3", "some3@mail.com", LogInProvider.LOCAL, UserRole.ADMIN, "1234", true);
        User user4 = new User(null, "some username4", "some4@mail.com", LogInProvider.LOCAL, UserRole.ADMIN, "1234", false);
        User user5 = new User(null, null, null, null, null, null, false);
        User user6 = new User(-1L, null, null, null, null, null, false);
        User user7 = new User(0L, "some username4", "some4@mail.com", LogInProvider.LOCAL, UserRole.ADMIN, "1234", false);
        return Stream.of(
                Arguments.of(user1, true),
                Arguments.of(user2, true),
                Arguments.of(user3, true),
                Arguments.of(user4, false),
                Arguments.of(user5, false),
                Arguments.of(user6, false),
                Arguments.of(user7, false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_isUserSavedTest")
    void isUserSavedTest(User user, boolean expectedIsSaved) {
        Mockito
                .when(userRepository.existsById(user.getId()))
                .thenReturn(expectedIsSaved);

        assertEquals(userService.isUserSaved(user), expectedIsSaved);
    }

    @Test
    void isUserSavedTest_null() {
        assertFalse(userService.isUserSaved(null));
    }

    private static Stream<Arguments> providedArgs_isValidEntityTest() {
        User user1 = new User(1L, "some username1", "some1@mail.com", LogInProvider.LOCAL, UserRole.ADMIN, "1234", true);
        User user2 = new User(2L, "some username2", "some2@mail.com", LogInProvider.LOCAL, UserRole.CUSTOMER, "1234", true);
        User user3 = new User(3L, "some username3", "some3@mail.com", LogInProvider.LOCAL, UserRole.ADMIN, "1234", true);
        User user4 = new User(null, "some username4", "some4@mail.com", LogInProvider.LOCAL, UserRole.ADMIN, "1234", false);
        User user5 = new User(null, null, null, null, null, null, false);
        User user6 = new User(-1L, null, null, null, null, null, false);
        User user7 = new User(0L, "some username4", "some4@mail.com", LogInProvider.LOCAL, UserRole.CUSTOMER, "1234", false);
        return Stream.of(
                Arguments.of(user1, true),
                Arguments.of(user2, true),
                Arguments.of(user3, true),
                Arguments.of(user4, true),
                Arguments.of(user5, true), // ???
                Arguments.of(user6, false),
                Arguments.of(user7, false),
                Arguments.of(null, false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_isValidEntityTest")
    void isValidEntityTest(User user, boolean expectedIsValid) {
        assertEquals(userService.isValidEntity(user), expectedIsValid);

//        TODO solve problem to read and check output from ParameterizedTest
//        if(!expectedIsValid){
//            assertTrue(output.getOut().contains("invalid user"));
//        }
//        Set<ConstraintViolation<User>> validation = validator.validate(user);
//        assertEquals(1, validation.size(), "should be 1 exception");
    }

    private static Stream<Arguments> providedArgs_createBaseUserTest() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            stringBuilder.append("a");
        }
        String maxLengthNameStr = stringBuilder.toString();
        return Stream.of(
                Arguments.of(maxLengthNameStr, "some1@mail.com", UserRole.CUSTOMER, true),
                Arguments.of("username1", "some1@mail.com", UserRole.CUSTOMER, true),
                Arguments.of("username2", "some2@mail.com", UserRole.CUSTOMER, false),
                Arguments.of("username3", "some2@mail.com", UserRole.ADMIN, false),
                Arguments.of("username4", "a@b.c", UserRole.ADMIN, false),
                Arguments.of("abc", "a@b.c", UserRole.ADMIN, true),
                Arguments.of("ac", "a@b.c", UserRole.ADMIN, false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_createBaseUserTest")
    void createBaseUserTest(String username, String email, UserRole role, boolean enabled) {
        User baseUser = userService.createBaseUser(username, email, role, enabled);

        assertNotNull(baseUser);
        assertNull(baseUser.getId());
        assertNotNull(baseUser.getRole());
        assertEquals(baseUser.getRole(), role);
        assertNotNull(baseUser.getUsername());
        assertEquals(baseUser.getUsername(), username);
        assertNotNull(baseUser.getEmail());
        assertEquals(baseUser.getEmail(), email);
        assertTrue(baseUser.getEmail().length() >= 5);
        assertEquals(baseUser.isEnabled(), enabled);
        assertEquals(baseUser.getPassword(), DEFAULT_PASSWORD);
        assertEquals(baseUser.getLogInProvider(), LogInProvider.LOCAL);
    }

    @Test
    void getUserRepositoryTest() {
        assertNotNull(userService.getUserRepository());
    }

    private static Stream<Arguments> providedArgs_readAllUserListTest() {
        User user1 = new User(1L, "some username1", "some1@mail.com", LogInProvider.LOCAL, UserRole.ADMIN, "1234", true);
        User user2 = new User(2L, "some username2", "some2@mail.com", LogInProvider.LOCAL, UserRole.CUSTOMER, "1234", true);
        User user3 = new User(3L, "some username3", "some3@mail.com", LogInProvider.LOCAL, UserRole.ADMIN, "1234", true);
        return Stream.of(
                Arguments.of(Collections.emptyList()),
                Arguments.of(Collections.singletonList(user1)),
                Arguments.of(Arrays.asList(user1, user2, user3))
        );
    }

    @ParameterizedTest
    @MethodSource("providedArgs_readAllUserListTest")
    void readAllUserListTest(List<User> repositoryUserList) {
        Mockito
                .when(userRepository.findAll())
                .thenReturn(repositoryUserList);
        List<User> userList = userService.readAllUserList();

        assertNotNull(userList);
        assertEquals(userList, repositoryUserList);
        assertTrue(Collections.unmodifiableList(userList).getClass().isInstance(Collections.unmodifiableList(new ArrayList<>())));
    }

    // TODO add test for 'createBaseUser' with invalid data

}