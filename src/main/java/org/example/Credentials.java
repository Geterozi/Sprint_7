package org.example;

import org.apache.commons.lang3.RandomStringUtils;

public class Credentials {
    public final String login;
    public final String password;

    public Credentials() {
        this.login = RandomStringUtils.randomAlphabetic(11);
        this.password = RandomStringUtils.randomAlphabetic(10);
    }

    public Credentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

}
