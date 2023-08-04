package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.DataHelperDB;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.WebPage;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestsWeb {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void clearDatabaseTables() throws SQLException {
        DataHelperDB.clearTables();
    }

    @Test
    @DisplayName("Оплата тура с валидной картой с статусом APPROVED")
    void testCashValidCardApproved() throws SQLException {
        WebPage.buyWithCash();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear(DataHelper.getYear());
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageSuccessfully();
        assertEquals("APPROVED", DataHelperDB.findPayStatus());

    }

    @Test
    @DisplayName("Кредит за тур с валидной картой с статусом APPROVED")
    void testCreditValidCardApproved() throws SQLException {
        WebPage.buyInCredit();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear(DataHelper.getYear());
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageSuccessfully();
        assertEquals("APPROVED", DataHelperDB.findCreditStatus());
    }

    @Test
    @DisplayName("Оплата тура с валидной картой с статусом DECLINED")
    void testCashValidCardDeclined() throws SQLException {
        WebPage.buyWithCash();
        WebPage.setCardNumber(DataHelper.getCardNumberDeclined());
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear(DataHelper.getYear());
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageError();
        assertEquals("DECLINED", DataHelperDB.findPayStatus());
    }

    @Test
    @DisplayName("Кредит за тур с валидной картой с статусом DECLINED")
    void testCreditValidCardDeclined() throws SQLException {
        WebPage.buyInCredit();
        WebPage.setCardNumber(DataHelper.getCardNumberDeclined());
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear(DataHelper.getYear());
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageError();
        assertEquals("DECLINED", DataHelperDB.findCreditStatus());
    }

    @Test
    @DisplayName("Оплата тура по несуществующей карте")
    void testCashInvalidCard() throws SQLException {
        WebPage.buyWithCash();
        WebPage.setCardNumber("4444 4444 4444 4443");
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear(DataHelper.getYear());
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageError();
        assertEquals(0, DataHelperDB.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур по несуществующей карте")
    void testCreditInvalidCard() throws SQLException {
        WebPage.buyInCredit();
        WebPage.setCardNumber("4444444444444444");
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear(DataHelper.getYear());
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageError();
        assertEquals(0, DataHelperDB.getOrderEntityCount());

    }

    @Test
    @DisplayName("Оплата тура по не полностью заполненной карте")
    void testCashUnfilledCard() throws SQLException {
        WebPage.buyWithCash();
        WebPage.setCardNumber("444444444444444");
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear(DataHelper.getYear());
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageIncorrectFormat();
        assertEquals(0, DataHelperDB.getOrderEntityCount());

    }

    @Test
    @DisplayName("Кредит за тур по не полностью заполненной карте")
    void testCreditUnfilledCard() throws SQLException {
        WebPage.buyInCredit();
        WebPage.setCardNumber("444444444444444");
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear(DataHelper.getYear());
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageIncorrectFormat();
        assertEquals(0, DataHelperDB.getOrderEntityCount());

    }

    @Test
    @DisplayName("Оплата тура карте с истекшим сроком действия (месяц)")
    void testCashLastMonth() {
        WebPage.buyWithCash();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth(DataHelper.getMonthOneMonthAgo());
        WebPage.setCardYear(DataHelper.getCurrentYear());
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageInvalidDate();
    }

    @Test
    @DisplayName("Кредит за тур по карте с истекшим сроком действия (месяц)")
    void testCreditLastMonth() {
        WebPage.buyInCredit();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth(DataHelper.getMonthOneMonthAgo());
        WebPage.setCardYear(DataHelper.getCurrentYear());
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageInvalidDate();
    }

    @Test
    @DisplayName("Оплата тура по карте с невалидным месяцем 00")
    void testCashMonth00() {
        WebPage.buyWithCash();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth("00");
        WebPage.setCardYear(DataHelper.getYear());
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageInvalidDate();
    }

    @Test
    @DisplayName("Кредит за тур по карте с невалидным месяцем 00")
    void testCreditMonth00() {
        WebPage.buyInCredit();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth("00");
        WebPage.setCardYear(DataHelper.getYear());
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageInvalidDate();
    }

    @Test
    @DisplayName("Оплата тура по карте с незаполненым полем месяц")
    void testCashEmptyMonth() {
        WebPage.buyWithCash();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth("");
        WebPage.setCardYear(DataHelper.getYear());
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageIncorrectFormat();
    }

    @Test
    @DisplayName("Кредит за тур по карте с незаполненым полем месяц ")
    void testCreditEmptyMonth() {
        WebPage.buyInCredit();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth("");
        WebPage.setCardYear(DataHelper.getYear());
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageIncorrectFormat();
    }

    @Test
    @DisplayName("Оплата тура по карте с истекшим сроком действия (год)")
    void testCashLastYear() {
        WebPage.buyWithCash();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear(DataHelper.getPreviousYear());
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageCardExpired();
    }

    @Test
    @DisplayName("Кредит за тур по карте с истекшим сроком действия (год)")
    void testCreditLastYear() {
        WebPage.buyInCredit();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear(DataHelper.getPreviousYear());
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageCardExpired();
    }

    @Test
    @DisplayName("Оплата тура по карте с годом + 6 лет от текущего ")
    void testCashYearPlus6() {
        WebPage.buyWithCash();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear(DataHelper.getCurrentYearPlus6());
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageInvalidDate();
    }

    @Test
    @DisplayName("Кредит за тур по карте с годом + 6 лет от текущего")
    void testCreditYearPlus6() {
        WebPage.buyInCredit();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear(DataHelper.getCurrentYearPlus6());
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageInvalidDate();
    }

    @Test
    @DisplayName("Оплата тура по карте с незаполненным полем год")
    void testCashEmptyYear() {
        WebPage.buyWithCash();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear("");
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageIncorrectFormat();
    }

    @Test
    @DisplayName("Кредит за тур по карте с незаполненным полем год")
    void testCreditEmptyYear() {
        WebPage.buyInCredit();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear("");
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageIncorrectFormat();
    }

    @Test
    @DisplayName("Оплата тура с вводом цифр в поле Владелец")
    void testCashNumbersOwner() {
        WebPage.buyWithCash();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear(DataHelper.getYear());
        WebPage.setCardOwner("1111");
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageRequiredField();
    }

    @Test
    @DisplayName("Оплата тура с вводом специальных символов в поле Владелец")
    void testCashSymbolsOwner() {
        WebPage.buyWithCash();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear(DataHelper.getYear());
        WebPage.setCardOwner("!@#$%^&*()-+_");
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageRequiredField();
    }

    @Test
    @DisplayName("Оплата тура с пустым полем Владелец")
    void testCashEmptyOwner() {
        WebPage.buyWithCash();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear(DataHelper.getYear());
        WebPage.setCardOwner("");
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageRequiredField();
    }

    @Test
    @DisplayName("Кредит за тур с вводом цифр в поле Владелец")
    void testCreditNumbersOwner() {
        WebPage.buyInCredit();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear(DataHelper.getYear());
        WebPage.setCardOwner("1111");
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageRequiredField();
    }

    @Test
    @DisplayName("Кредит за тур с вводом специальных символов поле Владелец")
    void testCreditSymbolsOwner() {
        WebPage.buyInCredit();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear(DataHelper.getYear());
        WebPage.setCardOwner("!@#$%^&*()-+_");
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageRequiredField();
    }

    @Test
    @DisplayName("Кредит за тур с пустым полем Владелец")
    void testCreditEmptyOwner() {
        WebPage.buyInCredit();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear(DataHelper.getYear());
        WebPage.setCardOwner("");
        WebPage.setCardCVV(DataHelper.getCvc());
        WebPage.clickContinueButton();
        WebPage.messageRequiredField();
    }

    @Test
    @DisplayName("Оплата тура с вводом одной цифры в поле CVC/CVV")
    void testCash1CVC() {
        WebPage.buyWithCash();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear(DataHelper.getYear());
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV("7");
        WebPage.clickContinueButton();
        WebPage.messageIncorrectFormat();
    }

    @Test
    @DisplayName("Оплата тура с вводом двух цифр в поле CVC/CVV")
    void testCash2CVC() {
        WebPage.buyWithCash();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear(DataHelper.getYear());
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV("73");
        WebPage.clickContinueButton();
        WebPage.messageIncorrectFormat();
    }

    @Test
    @DisplayName("Оплата тура с пустым значением в поле CVC/CVV")
    void testCashEmptyCVC() {
        WebPage.buyWithCash();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear(DataHelper.getYear());
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV("");
        WebPage.clickContinueButton();
        WebPage.messageIncorrectFormat();
    }

    @Test
    @DisplayName("Кредит за тур с вводом одной цифры в поле CVC/CVV")
    void testCredit1CVC() {
        WebPage.buyInCredit();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear(DataHelper.getYear());
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV("7");
        WebPage.clickContinueButton();
        WebPage.messageIncorrectFormat();
    }

    @Test
    @DisplayName("Кредит за тур с вводом двух цифр в поле CVC/CVV")
    void testCredit2CVC() {
        WebPage.buyInCredit();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear(DataHelper.getYear());
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV("73");
        WebPage.clickContinueButton();
        WebPage.messageIncorrectFormat();
    }

    @Test
    @DisplayName("Кредит за тур с пустым значением в поле CVC/CVV")
    void testCreditEmptyCVC() {
        WebPage.buyInCredit();
        WebPage.setCardNumber(DataHelper.getCardNumberApproved());
        WebPage.setCardMonth(DataHelper.getMonth());
        WebPage.setCardYear(DataHelper.getYear());
        WebPage.setCardOwner(DataHelper.getOwner());
        WebPage.setCardCVV("");
        WebPage.clickContinueButton();
        WebPage.messageIncorrectFormat();
    }

    @Test
    @DisplayName("Оплата тура с незаполнеными полями")
    void testCashEmptyFields() {
        WebPage.buyWithCash();
        WebPage.setCardNumber("");
        WebPage.setCardMonth("");
        WebPage.setCardYear("");
        WebPage.setCardOwner("");
        WebPage.setCardCVV("");
        WebPage.clickContinueButton();
        WebPage.messageIncorrectFormat();

    }

    @Test
    @DisplayName("Кредит за тур с незаполнеными полями")
    void testCreditEmptyFields() {
        WebPage.buyInCredit();
        WebPage.setCardNumber("");
        WebPage.setCardMonth("");
        WebPage.setCardYear("");
        WebPage.setCardOwner("");
        WebPage.setCardCVV("");
        WebPage.clickContinueButton();
        WebPage.messageIncorrectFormat();
    }
}





