package page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class WebPage {
    private static SelenideElement buyButton = $$(".button__text").find(exactText("Купить"));
    private static SelenideElement buyCreditButton = $$(".button__text").find(exactText("Купить в кредит"));
    private static SelenideElement cardNumberField = $$(".input__inner").findBy(text("Номер карты"))
            .$(".input__control");
    private static SelenideElement monthField = $$(".input__inner").findBy(text("Месяц"))
            .$(".input__control");
    private static SelenideElement yearField = $$(".input__inner").findBy(text("Год"))
            .$(".input__control");
    private static SelenideElement cardOwnerField = $$(".input__inner").findBy(text("Владелец"))
            .$(".input__control");
    private static SelenideElement cvcOrCvvField = $$(".input__inner").findBy(text("CVC/CVV"))
            .$(".input__control");

    private static SelenideElement payCard = $$(".heading").find(exactText("Оплата по карте"));
    private static SelenideElement payCreditByCard = $$(".heading")
            .find(exactText("Кредит по данным карты"));
    private static SelenideElement messageSuccessfully = $$(".notification__title").find(exactText("Успешно"));
    private static SelenideElement messageError = $$(".notification__content").find(exactText("Ошибка! Банк отказал в проведении операции."));
    private static SelenideElement continueButton = $$(".button__content").find(text("Продолжить"));
    private static SelenideElement cardExpired = $$("span.input__sub")
            .find(exactText("Истёк срок действия карты"));
    private static SelenideElement invalidCardExpirationDate = $$("span.input__sub")
            .find(exactText("Неверно указан срок действия карты"));
    private static SelenideElement incorrectFormat = $$("span.input__sub")
            .find(exactText("Неверный формат"));
    private static SelenideElement requiredField = $$(".input__inner span.input__sub")
            .find(exactText("Поле обязательно для заполнения"));


    public static void buyWithCash() {
        open("http://localhost:8080/");
        buyButton.click();
        payCard.shouldBe(visible);

    }

    public static void buyInCredit() {
        open("http://localhost:8080/");
        buyCreditButton.click();
        payCreditByCard.shouldBe(visible);
    }


    public static void setCardNumber(String number) {
        cardNumberField.setValue(number);
    }


    public static void setCardMonth(String month) {
        monthField.setValue(month);
    }


    public static void setCardYear(String year) {
        yearField.setValue(year);
    }


    public static void setCardOwner(String owner) {
        cardOwnerField.setValue(owner);
    }


    public static void setCardCVV(String cvc) {
        cvcOrCvvField.setValue(cvc);
    }


    public static void clickContinueButton() {
        continueButton.click();
    }

    public static void messageSuccessfully() {
        messageSuccessfully.waitUntil(visible, 10000);
    }

    public static void messageError() {
        messageError.waitUntil(visible, 10000);
    }

    public static void messageIncorrectFormat() {
        incorrectFormat.shouldBe(visible);
    }

    public static void messageRequiredField() {
        requiredField.shouldBe(visible);
    }

    public static void messageInvalidDate() {
        invalidCardExpirationDate.shouldBe(visible);
    }

    public static void messageCardExpired() {
        cardExpired.shouldBe(visible);
    }
}