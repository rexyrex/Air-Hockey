package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class Menu extends AbstractAppState implements ScreenController{
    Nifty nifty;
    SimpleApplication rootApp;
    
    public void initialize(AppStateManager stateManager, Application app){
        super.initialize(stateManager, app);
        rootApp = (SimpleApplication) app;
        
        NiftyJmeDisplay ourNiftyScreen = new NiftyJmeDisplay(
                app.getAssetManager(), app.getInputManager(), app.getAudioRenderer(),
                app.getGuiViewPort());
        
        nifty = ourNiftyScreen.getNifty();
        
        nifty.fromXml("Interface/mainMenu.xml", "start", this);
        
        app.getGuiViewPort().addProcessor(ourNiftyScreen);
        
        
        
        
    }
    
    public void cleanup(){
        nifty.exit();
    }
    
    public void onStartScreen(){
        
    }
    
    public void onEndScreen(){
        
    }

    public void bind(Nifty nifty, Screen screen) {
        
    }
    
    public void startGame(){
        this.rootApp.getStateManager().detach(this);
    }
    
    public void exitGame(){
        rootApp.stop();
    }
}
