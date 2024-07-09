package pages.Locators;

public class DigitalPageLocators {
    public final String PULSA_VERIFY_ACTIVE="//div[text()='Pulsa & Paket Data']/../following-sibling::div[@class='active-product__line']";
    public final String NOMOR_HP="//label[text()='Nomor HP']/parent::div/following-sibling::input";
    public final String NOMINALS="//div[@class='card pulsa-sku-height form__product']//p[@class='form__product-content__name']";
    public final String CONTINUE_TO_PAY="//button[@id='btn-paynow']";
    public final String NOMINAL_LIST="//div[@class='nominalWrap']";
    public final String SELECTED_NOMINAL="//div[@class='card selected pulsa-sku-height form__product']";
    public final String ValidateNominal="//p[text()='TEXT']";

}
