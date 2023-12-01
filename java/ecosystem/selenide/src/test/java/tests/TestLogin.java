package tests;


import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;



public class TestLogin {

    @Test
    public void userCanLoginByUsername() {
      open("http://the-internet.herokuapp.com/login");
      $(By.id("username")).setValue("tomsmith");
      $(By.id("password")).setValue("SuperSecretPassword!");
      $("button").click();
      $(".flash.success").should(appear); 
    }
    
}