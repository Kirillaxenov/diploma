package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.DataHelperDB;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.WebPage;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestsWeb {
    WebPage webPage = new WebPage();

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void clearDatabaseTables() {
        DataHelperDB.clearTables();
    }


    @Test
    @DisplayName("Оплата тура с валидной картой с статусом APPROVED")
    void testCashValidCardApproved() {
        webPage.buyWithCash();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getYear());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageSuccessfully();
        assertEquals("APPROVED", DataHelperDB.findPayStatus());
    }


    @Test
    @DisplayName("Кредит за тур с валидной картой с статусом APPROVED")
    void testCreditValidCardApproved() {
        webPage.buyInCredit();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getYear());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageSuccessfully();
        assertEquals("APPROVED", DataHelperDB.findCreditStatus());
    }

    @Test
    @DisplayName("Оплата тура с валидной картой с статусом DECLINED")
    void testCashValidCardDeclined() {
        webPage.buyWithCash();
        webPage.setCardNumber(DataHelper.getCardNumberDeclined());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getYear());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageError();
        assertEquals("DECLINED", DataHelperDB.findPayStatus());
    }

    @Test
    @DisplayName("Кредит за тур с валидной картой с статусом DECLINED")
    void testCreditValidCardDeclined() {
        webPage.buyInCredit();
        webPage.setCardNumber(DataHelper.getCardNumberDeclined());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getYear());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageError();
        assertEquals("DECLINED", DataHelperDB.findCreditStatus());
    }

    @Test
    @DisplayName("Оплата тура по несуществующей карте")
    void testCashInvalidCard() {
        webPage.buyWithCash();
        webPage.setCardNumber(DataHelper.getCardNumberNothing());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getYear());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageError();
        assertEquals(0, DataHelperDB.getOrderEntityCount());
    }


    @Test
    @DisplayName("Кредит за тур по несуществующей карте")
    void testCreditInvalidCard() {
        webPage.buyInCredit();
        webPage.setCardNumber(DataHelper.getCardNumberNothing());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getYear());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageError();
        assertEquals(0, DataHelperDB.getOrderEntityCount());

    }


    @Test
    @DisplayName("Оплата тура по не полностью заполненной карте")
    void testCashUnfilledCard() {
        webPage.buyWithCash();
        webPage.setCardNumber(DataHelper.getCardNumberNotFilled());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getYear());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageIncorrectFormat();
        assertEquals(0, DataHelperDB.getOrderEntityCount());

    }


    @Test
    @DisplayName("Кредит за тур по не полностью заполненной карте")
    void testCreditUnfilledCard() {
        webPage.buyInCredit();
        webPage.setCardNumber(DataHelper.getCardNumberNotFilled());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getYear());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageIncorrectFormat();
        assertEquals(0, DataHelperDB.getOrderEntityCount());

    }


    @Test
    @DisplayName("Оплата тура карте с истекшим сроком действия (месяц)")
    void testCashLastMonth() {
        webPage.buyWithCash();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getMonthOneMonthAgo());
        webPage.setCardYear(DataHelper.getCurrentYear());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageInvalidDate();
    }

    @Test
    @DisplayName("Кредит за тур по карте с истекшим сроком действия (месяц)")
    void testCreditLastMonth() {
        webPage.buyInCredit();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getMonthOneMonthAgo());
        webPage.setCardYear(DataHelper.getCurrentYear());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageInvalidDate();
    }

    @Test
    @DisplayName("Оплата тура по карте с невалидным месяцем 00")
    void testCashMonth00() {
        webPage.buyWithCash();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getInvalidMonth());
        webPage.setCardYear(DataHelper.getYear());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageInvalidDate();
    }


    @Test
    @DisplayName("Кредит за тур по карте с невалидным месяцем 00")
    void testCreditMonth00() {
        webPage.buyInCredit();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getInvalidMonth());
        webPage.setCardYear(DataHelper.getYear());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageInvalidDate();
    }

    @Test
    @DisplayName("Оплата тура по карте с незаполненым полем месяц")
    void testCashEmptyMonth() {
        webPage.buyWithCash();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getEmptyMonth());
        webPage.setCardYear(DataHelper.getYear());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageIncorrectFormat();
    }


    @Test
    @DisplayName("Кредит за тур по карте с незаполненым полем месяц ")
    void testCreditEmptyMonth() {
        webPage.buyInCredit();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getEmptyMonth());
        webPage.setCardYear(DataHelper.getYear());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageIncorrectFormat();
    }

    //
    @Test
    @DisplayName("Оплата тура по карте с истекшим сроком действия (год)")
    void testCashLastYear() {
        webPage.buyWithCash();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getPreviousYear());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageCardExpired();
    }

    @Test
    @DisplayName("Кредит за тур по карте с истекшим сроком действия (год)")
    void testCreditLastYear() {
        webPage.buyInCredit();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getPreviousYear());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageCardExpired();
    }

    @Test
    @DisplayName("Оплата тура по карте с годом + 6 лет от текущего ")
    void testCashYearPlus6() {
        webPage.buyWithCash();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getCurrentYearPlus6());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageInvalidDate();
    }

    @Test
    @DisplayName("Кредит за тур по карте с годом + 6 лет от текущего")
    void testCreditYearPlus6() {
        webPage.buyInCredit();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getCurrentYearPlus6());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageInvalidDate();
    }

    @Test
    @DisplayName("Оплата тура по карте с незаполненным полем год")
    void testCashEmptyYear() {
        webPage.buyWithCash();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getEmptyYear());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageIncorrectFormat();
    }


    @Test
    @DisplayName("Кредит за тур по карте с незаполненным полем год")
    void testCreditEmptyYear() {
        webPage.buyInCredit();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getEmptyYear());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageIncorrectFormat();
    }

    @Test
    @DisplayName("Оплата тура с вводом цифр в поле Владелец")
    void testCashNumbersOwner() {
        webPage.buyWithCash();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getYear());
        webPage.setCardOwner(DataHelper.getNumberOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageRequiredField();
    }


    @Test
    @DisplayName("Оплата тура с вводом специальных символов в поле Владелец")
    void testCashSymbolsOwner() {
        webPage.buyWithCash();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getYear());
        webPage.setCardOwner(DataHelper.getSpecialCharactersOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageRequiredField();
    }


    @Test
    @DisplayName("Оплата тура с пустым полем Владелец")
    void testCashEmptyOwner() {
        webPage.buyWithCash();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getYear());
        webPage.setCardOwner(DataHelper.getEmptyOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageRequiredField();
    }


    @Test
    @DisplayName("Кредит за тур с вводом цифр в поле Владелец")
    void testCreditNumbersOwner() {
        webPage.buyInCredit();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getYear());
        webPage.setCardOwner(DataHelper.getNumberOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageRequiredField();
    }

    @Test
    @DisplayName("Кредит за тур с вводом специальных символов поле Владелец")
    void testCreditSymbolsOwner() {
        webPage.buyInCredit();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getYear());
        webPage.setCardOwner(DataHelper.getSpecialCharactersOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageRequiredField();
    }

    @Test
    @DisplayName("Кредит за тур с пустым полем Владелец")
    void testCreditEmptyOwner() {
        webPage.buyInCredit();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getYear());
        webPage.setCardOwner(DataHelper.getEmptyOwner());
        webPage.setCardCVV(DataHelper.getCvc());
        webPage.clickContinueButton();
        webPage.messageRequiredField();
    }

    @Test
    @DisplayName("Оплата тура с вводом одной цифры в поле CVC/CVV")
    void testCash1CVC() {
        webPage.buyWithCash();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getYear());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.get1Cvc());
        webPage.clickContinueButton();
        webPage.messageIncorrectFormat();
    }

    @Test
    @DisplayName("Оплата тура с вводом двух цифр в поле CVC/CVV")
    void testCash2CVC() {
        webPage.buyWithCash();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getYear());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.get2Cvc());
        webPage.clickContinueButton();
        webPage.messageIncorrectFormat();
    }

    @Test
    @DisplayName("Оплата тура с пустым значением в поле CVC/CVV")
    void testCashEmptyCVC() {
        webPage.buyWithCash();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getYear());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.getEmptyCvc());
        webPage.clickContinueButton();
        webPage.messageIncorrectFormat();
    }

    @Test
    @DisplayName("Кредит за тур с вводом одной цифры в поле CVC/CVV")
    void testCredit1CVC() {
        webPage.buyInCredit();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getYear());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.get1Cvc());
        webPage.clickContinueButton();
        webPage.messageIncorrectFormat();
    }

    @Test
    @DisplayName("Кредит за тур с вводом двух цифр в поле CVC/CVV")
    void testCredit2CVC() {
        webPage.buyInCredit();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getYear());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.get2Cvc());
        webPage.clickContinueButton();
        webPage.messageIncorrectFormat();
    }

    @Test
    @DisplayName("Кредит за тур с пустым значением в поле CVC/CVV")
    void testCreditEmptyCVC() {
        webPage.buyInCredit();
        webPage.setCardNumber(DataHelper.getCardNumberApproved());
        webPage.setCardMonth(DataHelper.getMonth());
        webPage.setCardYear(DataHelper.getYear());
        webPage.setCardOwner(DataHelper.getOwner());
        webPage.setCardCVV(DataHelper.getEmptyOwner());
        webPage.clickContinueButton();
        webPage.messageIncorrectFormat();
    }

    @Test
    @DisplayName("Оплата тура с незаполнеными полями")
    void testCashEmptyFields() {
        webPage.buyWithCash();
        webPage.setCardNumber(DataHelper.getCardNumberEmpty());
        webPage.setCardMonth(DataHelper.getEmptyMonth());
        webPage.setCardYear(DataHelper.getEmptyYear());
        webPage.setCardOwner(DataHelper.getEmptyOwner());
        webPage.setCardCVV(DataHelper.getEmptyCvc());
        webPage.clickContinueButton();
        webPage.messageIncorrectFormat();

    }

    @Test
    @DisplayName("Кредит за тур с незаполнеными полями")
    void testCreditEmptyFields() {
        webPage.buyInCredit();
        webPage.setCardNumber(DataHelper.getCardNumberEmpty());
        webPage.setCardMonth(DataHelper.getEmptyMonth());
        webPage.setCardYear(DataHelper.getEmptyYear());
        webPage.setCardOwner(DataHelper.getEmptyOwner());
        webPage.setCardCVV(DataHelper.getEmptyCvc());
        webPage.clickContinueButton();
        webPage.messageIncorrectFormat();
    }

}





