package com.vshmaliukh.webstore.login;

import com.vshmaliukh.webstore.model.PersistentToken;
import com.vshmaliukh.webstore.repositories.MyPersistentTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class CustomPersistentTokenHandler implements org.springframework.security.web.authentication.rememberme.PersistentTokenRepository {

    private final MyPersistentTokenRepository myPersistentTokenRepository;

    public CustomPersistentTokenHandler(MyPersistentTokenRepository myPersistentTokenRepository) {
        this.myPersistentTokenRepository = myPersistentTokenRepository;
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        String series = token.getSeries();
        PersistentToken persistentToken = myPersistentTokenRepository.findBySeries(series);
        if (persistentToken == null) {
            persistentToken = new PersistentToken(token.getUsername(), token.getSeries(), token.getTokenValue(), new Date());
            myPersistentTokenRepository.save(persistentToken);
        } else {
            log.error("problem to create new persistent login token // token with '{}' series already exists", series);
        }
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        PersistentToken persistentToken = myPersistentTokenRepository.findBySeries(series);
        if (persistentToken != null) {
            persistentToken.setToken(tokenValue);
            persistentToken.setLastUsed(new Date());
            myPersistentTokenRepository.save(persistentToken);
        }
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        Optional<PersistentToken> optionalPersistentLogin = myPersistentTokenRepository.findById(seriesId);
        if (optionalPersistentLogin.isPresent()) {
            PersistentToken persistentToken = optionalPersistentLogin.get();
            new PersistentRememberMeToken(
                    persistentToken.getUsername(),
                    persistentToken.getSeries(),
                    persistentToken.getToken(),
                    persistentToken.getLastUsed()
            );
        }
        return null;
    }

    @Override
    public void removeUserTokens(String username) {
        List<PersistentToken> persistentTokenList = myPersistentTokenRepository.findAllByUsernameIgnoreCase(username);
        myPersistentTokenRepository.deleteAll(persistentTokenList);
    }

}
