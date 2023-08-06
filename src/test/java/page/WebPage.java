package page;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class WebPage {
    private SelenideElement buyButton = $$(".button__text").find(exactText("Купить"));
    private SelenideElement buyCreditButton = $$(".button__text").find(exactText("Купить в кредит"));
    private SelenideElement cardNumberField = $$(".input__inner").findBy(text("Номер карты"))
            .$(".input__control");
    private SelenideElement monthField = $$(".input__inner").findBy(text("Месяц"))
            .$(".input__control");
    private SelenideElement yearField = $$(".input__inner").findBy(text("Год"))
            .$(".input__control");
    private SelenideElement cardOwnerField = $$(".input__inner").findBy(text("Владелец"))
            .$(".input__control");
    private SelenideElement cvcOrCvvField = $$(".input__inner").findBy(text("CVC/CVV"))
            .$(".input__control");

    private SelenideElement payCard = $$(".heading").find(exactText("Оплата по карте"));
    private SelenideElement payCreditByCard = $$(".heading")
            .find(exactText("Кредит по данным карты"));
    private SelenideElement messageSuccessfully = $$(".notification__title").find(exactText("Успешно"));
    private SelenideElement messageError = $$(".notification__content").find(exactText("Ошибка! Банк отказал в проведении операции."));
    private SelenideElement continueButton = $$(".button__content").find(text("Продолжить"));
    private SelenideElement cardExpired = $$("span.input__sub")
            .find(exactText("Истёк срок действия карты"));
    private SelenideElement invalidCardExpirationDate = $$("span.input__sub")
            .find(exactText("Неверно указан срок действия карты"));
    private SelenideElement incorrectFormat = $$("span.input__sub")
            .find(exactText("Неверный формат"));
    private SelenideElement requiredField = $$(".input__inner span.input__sub")
            .find(exactText("Поле обязательно для заполнения"));


    public void buyWithCash() {
        open("http://localhost:8080/");
        buyButton.click();
        payCard.shouldBe(visible);

    }

    public void buyInCredit() {
        open("http://localhost:8080/");
        buyCreditButton.click();
        payCreditByCard.shouldBe(visible);
    }


    public void setCardNumber(String number) {
        cardNumberField.setValue(number);
    }


    public void setCardMonth(String month) {
        monthField.setValue(month);
    }


    public void setCardYear(String year) {
        yearField.setValue(year);
    }


    public void setCardOwner(String owner) {
        cardOwnerField.setValue(owner);
    }


    public void setCardCVV(String cvc) {
        cvcOrCvvField.setValue(cvc);
    }


    public void clickContinueButton() {
        continueButton.click();
    }

    public void messageSuccessfully() {
        messageSuccessfully.shouldBe(visible, Duration.ofSeconds(10));
    }

    public void messageError() {
        messageError.shouldBe(visible, Duration.ofSeconds(10));
    }

    public void messageIncorrectFormat() {
        incorrectFormat.shouldBe(visible);
    }

    public void messageRequiredField() {
        requiredField.shouldBe(visible);
    }

    public void messageInvalidDate() {
        invalidCardExpirationDate.shouldBe(visible);
    }

    public void messageCardExpired() {
        cardExpired.shouldBe(visible);
    }
}