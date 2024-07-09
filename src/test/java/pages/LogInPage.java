package pages;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

public class LogInPage {
    public String EMAIL="//label[text()='Email/nomor HP']/preceding-sibling::input";
    public String PASSWORD="//label[text()='Kata sandi']/preceding-sibling::input";
    public String LOGIN_BTN="//div[@class='login__button']";
    public String SEND_OTP_BTN="//div[text()='  Kirim kode OTP  ']";
    public String OTP_FIELD="//input[@class='otp__textField space item active']";
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
    public LogInPage(Page page, BrowserContext context) {
        this.page=page;
        this.context=context;
    }


    public void enterEmail(String email) {
        page.fill(EMAIL,email);
    }
    public void enterPassword(String password) {
        page.fill(PASSWORD,password);
    }

    public void clickLogin() {
        page.click(LOGIN_BTN);
    }

    public void sendOtp() {
        page.click(SEND_OTP_BTN);
        for(int field=1;field<=4;field++){
            page.fill(OTP_FIELD,"3");
        }
    }

    public boolean isEnable(String locator) {
        return page.locator(locator).isEnabled();
    }

    public String getInputValue() {
        return page.locator(EMAIL).inputValue();
    }
}
