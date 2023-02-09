package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.UnauthorizedUser;
import com.vshmaliukh.webstore.repositories.UnauthorizedUserRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UnauthorizedUserServiceTest  {

    @MockBean
    UnauthorizedUserRepository unauthorizedUserRepository;

    @Autowired
    UnauthorizedUserService unauthorizedUserService;

    static final UnauthorizedUser firstUser = new UnauthorizedUser(1L);
    static final UnauthorizedUser secondUser = new UnauthorizedUser(2L);

    private static Stream<Arguments> provideIdAndUsersForGettingUsersById() {
        return Stream.of(
                Arguments.of(1L, firstUser),
                Arguments.of(2L, secondUser),
                Arguments.of(null,null),
                Arguments.of(4L, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideIdAndUsersForGettingUsersById")
    void getUnauthorizedUserByIdTest(Long id, UnauthorizedUser expectedUnauthorizedUser) {
        Mockito.when(unauthorizedUserRepository.getReferenceById(id)).thenReturn(expectedUnauthorizedUser);
        assertEquals(expectedUnauthorizedUser,unauthorizedUserService.getUserById(id));

    }

    private static Stream<Arguments> provideUnauthorizedUsersForCreation() {
        return Stream.of(
                Arguments.of(firstUser),
                Arguments.of(secondUser)
        );
    }

//    @ParameterizedTest //todo check null in result
//    @MethodSource("provideUnauthorizedUsersForCreation")
//    void createUnauthorizedUserTest(UnauthorizedUser expectedUnauthorizedUser) {
//        Mockito.when(unauthorizedUserRepository.save(new UnauthorizedUser())).thenReturn(expectedUnauthorizedUser);
//        UnauthorizedUser unauthorizedUser = unauthorizedUserService.createUnauthorizedUser();
//        assertEquals(expectedUnauthorizedUser,unauthorizedUser);
//    }

    private static Stream<Arguments> provideUnauthorizedUsersForExistenceChecking() {
        return Stream.of(
                Arguments.of(1L,true),
                Arguments.of(0L, false),
                Arguments.of(null, false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideUnauthorizedUsersForExistenceChecking")
    void existsByIdTest(Long id, boolean exists) {
        Mockito.when(unauthorizedUserRepository.existsById(id)).thenReturn(exists);
        assertEquals(exists,unauthorizedUserService.existsById(id));
    }

}
