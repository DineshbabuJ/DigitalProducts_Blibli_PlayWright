package pages;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import pages.Locators.CheckOutPageLocators;

public class CheckOutPage {
    CheckOutPageLocators checkOutPageLocators=new CheckOutPageLocators();
    private Page page;
    public Page getPage() {
        return page;
    }

    public BrowserContext getContext() {
        return context;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    private BrowserContext context;
    public CheckOutPage(Page page, BrowserContext context) {
        this.page=page;
        this.context=context;
    }


    public void selectBank() {
        page.click(checkOutPageLocators.ACCOUNT_DROPDOWN);
        page.locator(checkOutPageLocators.BANK_BCA).click();
    }
    public void clickPayNowBtn(){
        try {
            if(page.locator(checkOutPageLocators.REMOVE_COUPON).isVisible()){
                page.click(checkOutPageLocators.REMOVE_COUPON);
            }
        }
        finally {
            page.click(checkOutPageLocators.PAY_BTN);
        }
    }

    public void goToHome(){
        page.click(checkOutPageLocators.BACK_TO_HOME);
    }


    public boolean verifyPaymentPage() {
        return page.locator(checkOutPageLocators.PAYMENT_PAGE).isVisible();
    }

    public boolean verifyBankDropDown() {
        return page.locator(checkOutPageLocators.DROP_DOWN_VALUE).textContent().contains("BCA");
    }

    public boolean verifyNominal(String nominalName) {
        return nominalName.contains(page.locator(checkOutPageLocators.NOMINAL_NAME).textContent());
    }
}
