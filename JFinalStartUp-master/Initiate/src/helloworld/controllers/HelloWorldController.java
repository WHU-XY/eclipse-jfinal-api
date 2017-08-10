package helloworld.controllers;

import com.jfinal.core.Controller;

public class HelloWorldController extends Controller {
	public void sayHello(){
		renderHtml("hello " + this.getPara(0) + "greetings:" + this.getPara(1));
	}
	
	public void sayGoodBye(){
		renderHtml("goodbye " + this.getPara(0) + "goodbye: " + this.getPara(1));
	}
}
