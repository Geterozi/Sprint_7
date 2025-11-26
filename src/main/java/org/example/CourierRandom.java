package org.example;

import org.apache.commons.lang3.RandomStringUtils;

 public class CourierRandom {
    private String login;
    private String password;
    private String firstName;

    // Добавляем конструктор без параметров
    public CourierRandom() {
        this.login = RandomStringUtils.randomAlphabetic(11);
        this.password = RandomStringUtils.randomAlphabetic(10);
        this.firstName = "Artem";
    }

    // Сохраняем существующий конструктор для совместимости
    public CourierRandom(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }
}