package pages;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import org.json.JSONObject;
import org.json.JSONArray;
import pages.Locators.DigitalPageLocators;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class DigitalPage {
    DigitalPageLocators digitalPageLocators=new DigitalPageLocators();
    private Page page;
    List<Locator> nominals;
    public Page getPage() {
        return page;
    }

    public BrowserContext getContext() {
        return context;
    }

    public void setPage(Page page) {
        this.page = page;
    }
    private Playwright playwright;
    private APIRequestContext request;
    APIResponse response;
    void createPlaywright(){
        playwright=Playwright.create();
    }
    void createApiRequestContext(String baseUrl){
        HashMap<String,String> map=new HashMap<>();
        map.put("accept","application/json");
        request=playwright.request()
                .newContext(new APIRequest.NewContextOptions().setBaseURL(baseUrl).setExtraHTTPHeaders(map));
    }
    private BrowserContext context;
    public DigitalPage(Page page, BrowserContext context) {
        this.page=page;
        this.context=context;
    }

    public void enterNomorHP(String number) {
        page.fill(digitalPageLocators.NOMOR_HP,number);
    }

    public void validateResponse() throws IOException {
        createPlaywright();
        createApiRequestContext("https://wwwpreprod.gdn-app.com/");
        response=request.get("backend/digital-product/products", RequestOptions.create().setQueryParam("productType","PHONE_CREDIT").setQueryParam("customerNumber","085765160652"));

        if (response.status() == 200) {
            String responseBody = response.text();
            JSONObject jsonResponse = new JSONObject(responseBody);
            validateApiResponse(jsonResponse);

        } else {
            System.out.println("Request failed with status: " + response.status());
        }
    }

    private void validateApiResponse(JSONObject jsonResponse) throws IOException {
        JSONArray flashSaleSkus = jsonResponse.getJSONObject("data").getJSONArray("flashSaleSkus");
        for (int i = 0; i < flashSaleSkus.length(); i++) {
            JSONObject flashSaleSku = flashSaleSkus.getJSONObject(i);
            System.out.println("Flash Sale SKU: " + flashSaleSku.toString(2));
        }
        JSONArray products = jsonResponse.getJSONObject("data").getJSONArray("products");
        for (int i = 0; i < products.length(); i++) {
            JSONObject product = products.getJSONObject(i);
            System.out.println("Product: " + product.toString(2));
            ObjectMapper objectMapper = new ObjectMapper();
            pojo.Response prod=objectMapper.readValue(product.toString(),pojo.Response.class);

            if (!page.locator(digitalPageLocators.ValidateNominal.replace("TEXT", prod.getName())).isVisible())
                throw new AssertionError("Nominal not listed in UI");

        }

        System.out.println("API response validated successfully!");


    }


    public String chooseNominal() {
        nominals=page.locator(digitalPageLocators.NOMINALS).all();
        if(!nominals.isEmpty()){
            String nominal=nominals.get(6).textContent();
            nominals.get(6).click();
            return nominal;

        }
        else{
            System.out.println("no nominals are present");
            return "";
        }

    }
    public boolean verifyNominalSelected() {
        return page.locator(digitalPageLocators.SELECTED_NOMINAL).isEnabled();
    }
    public void continueToPay() {
        page.click(digitalPageLocators.CONTINUE_TO_PAY);
    }

    public boolean verifyLocator(String locator) {
        return page.locator(locator).isEnabled();
    }

    public String getInputValue(String locator) {
        return page.locator(locator).inputValue();
    }


}
