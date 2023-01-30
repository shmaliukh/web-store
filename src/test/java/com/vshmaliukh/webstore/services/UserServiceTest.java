package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.login.LogInProvider;
import com.vshmaliukh.webstore.login.UserRole;
import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.repositories.UserRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    UserService userService;

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
    void isAdminUserTest(User user, boolean isAdminExpectedResult){
        boolean isAdminResult = userService.isAdminUser(user);

        assertEquals(isAdminResult, isAdminExpectedResult);
    }



}