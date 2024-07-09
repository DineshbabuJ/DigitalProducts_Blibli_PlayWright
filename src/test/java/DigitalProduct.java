import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import jdk.jfr.Description;
import org.junit.Assert;
import org.testng.annotations.Test;
import pages.*;
import pages.Locators.CheckOutPageLocators;
import pages.Locators.DigitalPageLocators;
import pages.Locators.HomePageLocators;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


public class DigitalProduct {
    HomePage homePage;
    DigitalPage digitalPage;
    LogInPage loginPage;
    CheckOutPage checkOutPage;
    HomePageLocators homePageLocators=new HomePageLocators();
    DigitalPageLocators digitalPageLocators= new DigitalPageLocators();
    CheckOutPageLocators checkOutPageLocators= new CheckOutPageLocators();

    Page initializeDriver(){
        Playwright play=Playwright.create();
        Browser browser=play.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        return browser.newPage();
    }

    @Test(description = "Digital section in Blibli")
    @Description("purchae a digital nominal and cancel that order")
    public void purchase() throws InterruptedException, IOException {
        FileReader reader=new FileReader("application.properties");
        Properties properties= new Properties();
        properties.load(reader);

        Page page=initializeDriver();
        page.navigate(properties.getProperty("url"));
        homePage=new HomePage(page,page.context());
        Assert.assertTrue("tagihan section is not enabled",homePage.isEnabled(homePageLocators.TAGIHAN));
        homePage.clickTagihan();
        Assert.assertTrue("product lainnya is not able to click",homePage.isEnabled(homePageLocators.PRODUK_LAINNYA));
        homePage.clickProduckLainya();
        Assert.assertTrue("Pulsa is not displayed in product list",homePage.isEnabled(homePageLocators.PULSA));
        homePage.choosePulsa();

        digitalPage=new DigitalPage(homePage.getPage(),homePage.getContext());

        page.waitForSelector(digitalPageLocators.PULSA_VERIFY_ACTIVE);
        Assert.assertTrue("not directed to digital pulsa and packet data section",digitalPage.verifyLocator(digitalPageLocators.PULSA_VERIFY_ACTIVE));
        digitalPage.enterNomorHP(properties.getProperty("NomorHp"));
        Assert.assertEquals(properties.getProperty("NomorHp"),digitalPage.getInputValue(digitalPageLocators.NOMOR_HP));
        page.waitForSelector(digitalPageLocators.NOMINAL_LIST);
        Assert.assertTrue("Nominals not listed",digitalPage.verifyLocator(digitalPageLocators.NOMINAL_LIST));
        digitalPage.validateResponse();
        String nominal_name=digitalPage.chooseNominal();
        Assert.assertTrue("Nominal not selected",digitalPage.verifyNominalSelected());
        Assert.assertTrue("Continue button is not enabled",digitalPage.verifyLocator(digitalPageLocators.CONTINUE_TO_PAY));
        digitalPage.continueToPay();


        loginPage=new LogInPage(page,page.context());
        Assert.assertTrue("Not navigated to login page",loginPage.isEnable(loginPage.EMAIL));
        loginPage.enterEmail(properties.getProperty("email"));
        Assert.assertEquals("Email field does not reflect the input entered",properties.getProperty("email"),loginPage.getInputValue());
        Assert.assertTrue("Not navigated to login page",loginPage.isEnable(loginPage.PASSWORD));
        loginPage.enterPassword(properties.getProperty("password"));
        Assert.assertTrue("Login Button not enabled",loginPage.isEnable(loginPage.LOGIN_BTN));
        loginPage.clickLogin();
        loginPage.sendOtp();

        checkOutPage=new CheckOutPage(page, page.context());
        page.waitForSelector(checkOutPageLocators.PAYMENT_PAGE);
        page.waitForSelector(checkOutPageLocators.VIRTUAL_ACCOUNT);
        Assert.assertTrue("not navigated to Payment page",checkOutPage.verifyPaymentPage());
        Assert.assertTrue("no Virtual Account option for Payment",checkOutPage.getPage().isVisible(checkOutPageLocators.VIRTUAL_ACCOUNT));

        checkOutPage.selectBank();
        Assert.assertTrue("no Bank Bca is not selected",checkOutPage.verifyBankDropDown());
        Assert.assertTrue("nominal MisMatch",checkOutPage.verifyNominal(nominal_name));
        Assert.assertTrue("Pay button not enabled",checkOutPage.getPage().isEnabled(checkOutPageLocators.PAY_BTN));
        checkOutPage.clickPayNowBtn();

        Page newPage = checkOutPage.getContext().waitForPage(() -> {
            checkOutPage.goToHome();
        });
        newPage.waitForLoadState();
        Page newpage = checkOutPage.getContext().pages().get(1);


        homePage=new HomePage(newpage,newpage.context());
        homePage.hoverAccount();
        homePage.gotoOrderHistory();
        Assert.assertTrue("nominal MisMatch", homePage.verifyNominal(nominal_name));
        homePage.clickViewDetails();
        homePage.cancelOrder();
        homePage.confirmCancel();
        Assert.assertTrue("order not present in canceled list",homePage.verifyCancelledOrders(nominal_name));

        Thread.sleep(10000);


    }
}
