package org.shibaty.SeleniumBrowserTest.example.pageobject;

import org.openqa.selenium.WebDriver;
import org.shibaty.SeleniumBrowserTest.base.PageObjectBase;

/**
 * @author Yasutaka
 *
 */
public class POGoogleResult extends PageObjectBase {

    /**
     * コンストラクタ.<br>
     *
     * @param driver
     */
    public POGoogleResult(WebDriver driver) {
        super(driver);
    }

    /**
     * {@inheritDoc}
     * @see org.shibaty.SeleniumBrowserTest.base.PageObjectBase#back()
     */
    @Override
    public <T extends PageObjectBase> T back() {
        super.back();
        return new POGoogle(_driver).initElements();
    }


}
