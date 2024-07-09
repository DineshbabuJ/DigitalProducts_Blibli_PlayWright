package pages;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import pages.Locators.HomePageLocators;

public class HomePage {
    HomePageLocators homePageLocators= new HomePageLocators();

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
    public HomePage(Page page, BrowserContext context) {
        this.page=page;
        this.context=context;
    }
    @Step("Click Tagihan and isi-Ulang and clear popups")
    public void clickTagihan() throws InterruptedException {
        page.click(homePageLocators.TAGIHAN);
        page.click(homePageLocators.popUpButton);
        page.click(homePageLocators.FLASH_SALE_POPUP);
    }

    @Step("Click on Produk Lainnya and change view to list")
    public void clickProduckLainya() {
        page.click(homePageLocators.PRODUK_LAINNYA);
        page.click(homePageLocators.LIST_VIEW);
    }

    @Step("Choose pulsa")
    public void choosePulsa(){
        page.click(homePageLocators.PULSA);
    }


    public void hoverAccount() {
        page.hover(homePageLocators.ACCOUNT_HOVER);

    }

    public void gotoOrderHistory(){
        page.click(homePageLocators.DAFTAR_PESANAN);
        page.click(homePageLocators.TAGIHAN_ORDERS);
    }

    public void clickViewDetails() {
        page.click(homePageLocators.VIEW_ORDER);
    }
    public void cancelOrder(){
        page.click(homePageLocators.CANCEL_ORDER);
    }
    public void confirmCancel(){
        page.click(homePageLocators.CONFIRM_CANCEL);
    }

    public boolean isEnabled(String tagihan) {
        return page.locator(tagihan).isVisible();
    }

    public boolean verifyNominal(String nominalName) {
        return nominalName.contains(page.locator(homePageLocators.PRODUCT_IN_ORDERS).textContent());
    }

    public boolean verifyCancelledOrders(String nominalName) {
        System.out.println(homePageLocators.PRODUCT_IN_CANCELLED.replace("PRODUCT",nominalName));
        return page.locator(homePageLocators.PRODUCT_IN_CANCELLED.replace("PRODUCT",nominalName)).isVisible();
    }
}
